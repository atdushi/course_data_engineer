from datetime import datetime
from random import randint
import psycopg2
import os
from airflow.models import Variable
from airflow import DAG
from airflow.operators.bash import BashOperator
from airflow.operators.python import PythonOperator
from airflow.sensors.filesystem import FileSensor
from airflow.hooks.base import BaseHook
from airflow.operators.python import BranchPythonOperator
from airflow.utils.trigger_rule import TriggerRule

# pg_hostname = 'host.docker.internal'
# pg_port = '5432'
# pg_username = 'postgres'
# pg_pass = 'myPassword'
# pg_db = 'test'
filename = 'file.txt'


def hello():
    print("Airflow")


def write_random_numbers():
    if os.path.exists(filename):
        # удаляем последнюю строку
        os.system('sed -i "$ d" {0}'.format(os.path.abspath(filename)))

    # пишем рандомные числа
    with open(filename, 'a') as f:
        f.write(f'{randint(1, 100)} {randint(1, 100)}\n')


def write_sum():
    value_1, value_2 = 0, 0

    with open(filename) as f:
        for line in f:
            contents = line.split('\n')[0]
            value_1 += int(contents.split(' ')[0])
            value_2 += int(contents.split(' ')[1])

    with open(filename, 'a') as f:
        f.write(f'{value_1} {value_2}\n')


def read_random_numbers():
    with open(filename) as f:
        contents = f.read()
        print(contents)


def get_conn_credentials(conn_id) -> BaseHook.get_connection:
    conn_to_airflow = BaseHook.get_connection(conn_id)
    return conn_to_airflow


def create_table():
    conn_id = Variable.get("conn_id")
    conn_to_airflow = get_conn_credentials(conn_id)

    pg_hostname, pg_port, pg_username, pg_pass, pg_db = conn_to_airflow.host, conn_to_airflow.port, \
        conn_to_airflow.login, conn_to_airflow.password, \
        conn_to_airflow.schema

    conn = psycopg2.connect(host=pg_hostname, port=pg_port, user=pg_username, password=pg_pass, database=pg_db)
    cursor = conn.cursor()

    cursor.execute("CREATE TABLE IF NOT EXISTS test_table (value_1 integer, value_2 integer);")

    with open(filename) as f:
        for line in f.readlines()[:-1]:
            contents = line.split('\n')[0]
            value_1 = int(contents.split(' ')[0])
            value_2 = int(contents.split(' ')[1])
            cursor.execute(f"INSERT INTO public.test_table (value_1, value_2) VALUES({value_1}, {value_2});")

    conn.commit()

    cursor.close()
    conn.close()


def python_branch():
    # тут просто проверяется наличие файла без изысков
    if os.path.exists(filename):
        return "accurate"
    else:
        return "inaccurate"


def add_column():
    conn_id = Variable.get("conn_id")
    conn_to_airflow = get_conn_credentials(conn_id)

    pg_hostname, pg_port, pg_username, pg_pass, pg_db = conn_to_airflow.host, conn_to_airflow.port, \
        conn_to_airflow.login, conn_to_airflow.password, \
        conn_to_airflow.schema

    conn = psycopg2.connect(host=pg_hostname, port=pg_port, user=pg_username, password=pg_pass, database=pg_db)
    cursor = conn.cursor()

    cursor.execute("ALTER TABLE test_table ADD COLUMN IF NOT EXISTS coef integer;")
    cursor.execute("""WITH aux AS
                    (
                    SELECT
                        *,
                        ROW_NUMBER() OVER() rn,
                        SUM(value_1) OVER(ROWS UNBOUNDED PRECEDING) AS sum_1,
                        SUM(value_2) OVER(ROWS UNBOUNDED PRECEDING) AS sum_2
                    FROM
                        test_table
                    )
                    UPDATE
                        test_table tt
                    SET
                        coef = sum_1 - sum_2
                    FROM
                        aux
                    WHERE
                        tt.value_1 = aux.value_1
                        AND tt.value_2 = aux.value_2;""")

    conn.commit()

    cursor.close()
    conn.close()


#0 0 * * *
with DAG(dag_id="second_dag", start_date=datetime(2022, 1, 1), schedule="* * * * *", max_active_runs=5, catchup=False) as dag:
    bash_task = BashOperator(task_id="hello", bash_command="echo hello", do_xcom_push=False)
    python_task = PythonOperator(task_id="world", python_callable=hello, do_xcom_push=False)
    get_two_random_numbers_task = PythonOperator(task_id="get_two_random_numbers", python_callable=write_random_numbers,
                                                 do_xcom_push=False)
    # read_random_numbers_task = PythonOperator(task_id="read_random_numbers", python_callable=read_random_numbers,
    #                                           do_xcom_push=False)
    write_sum_task = PythonOperator(task_id="write_sum", python_callable=write_sum, do_xcom_push=False)

    # тут нужно создать File connection с именем my_conn
    sensor_task = FileSensor(task_id="file_sensor_task", fs_conn_id='my_conn', filepath="file.txt", poke_interval=30)

    accurate = PythonOperator(task_id='accurate', python_callable=create_table, do_xcom_push=False)
    inaccurate = BashOperator(task_id='inaccurate', bash_command="echo Error! File does not exists", do_xcom_push=False)

    choose_best_model = BranchPythonOperator(task_id='branch_oper', python_callable=python_branch, do_xcom_push=False)

    add_column_task = PythonOperator(task_id='add_column', python_callable=add_column, do_xcom_push=False,
                                     trigger_rule=TriggerRule.ONE_SUCCESS)

    bash_task >> python_task >> get_two_random_numbers_task >> \
    write_sum_task >> sensor_task >> choose_best_model >> [accurate, inaccurate] >> add_column_task
