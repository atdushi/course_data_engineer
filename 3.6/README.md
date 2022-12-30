## 3.6 Практика Airflow

**first_dag.py** - light версия прошлой домашней работы. Airflow устанавливался локально через pip install https://airflow.apache.org/docs/apache-airflow/stable/start.html

**second_dag.py** - не совсем полная pro версия новой домашней работы. Здесь Airflow устанавливается через *docker-compose.yml*

Для second_dag.py нужно создать
1. конекшн для postgres с любым именем (например, **my_postgres**)
2. переменную с именем **conn_id** и присвоить ей значение имени из пункта 1
3. конекшн для файлового сенсора с именем **my_conn**
