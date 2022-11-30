from datetime import datetime
import csv
import requests
import os
from airflow.models import Variable
from airflow import DAG
from airflow.operators.bash import BashOperator
from airflow.operators.python import PythonOperator


def download_addresses():
    url = "https://people.sc.fsu.edu/~jburkardt/data/csv/addresses.csv"
    response = requests.get(url)
    open("addresses.csv", "wb").write(response.content)


def read_addresses():
    print(f'current directory {os.path.abspath("~/")}')

    with open(r"addresses.csv", 'r') as fp:
        for count, line in enumerate(fp):
            pass

    Variable.set("addresses.csv", os.path.abspath("addresses.csv"))

    return count + 1


def write_addresses(**kwargs):
    ti = kwargs['ti']
    lines_count = ti.xcom_pull(task_ids='python_read_addresses_task')
    print('Lines count: {}'.format(lines_count))

    rows = []

    with open('addresses.csv', newline='') as csvfile:
        reader = csv.reader(csvfile, delimiter=',')
        for row in reader:
            print(', '.join(row))
            row.insert(0, lines_count)
            rows.append(row)
            lines_count -= 1

    with open('addresses1.csv', 'w') as f:
        write = csv.writer(f)
        write.writerows(rows)

    ti.xcom_push(value=os.path.abspath("addresses1.csv"), key='file_path')


with DAG(dag_id="first_dag", start_date=datetime(2022, 1, 1), schedule="0 0 * * *") as dag:
    python_download_addresses_task = PythonOperator(task_id="python_download_addresses_task",
                                                    python_callable=download_addresses)
    python_read_addresses_task = PythonOperator(task_id="python_read_addresses_task", python_callable=read_addresses)

    python_write_addresses_task = PythonOperator(task_id="python_write_addresses_task", python_callable=write_addresses)

    copy_bash_task = BashOperator(task_id="copy_bash_task",
                                  bash_command='cp {{ti.xcom_pull(task_ids="python_write_addresses_task", key="file_path")}} ~/')

    success_bash_task = BashOperator(task_id="success_bash_task", bash_command="echo Success")

    python_download_addresses_task >> python_read_addresses_task >> python_write_addresses_task >> copy_bash_task >> success_bash_task
