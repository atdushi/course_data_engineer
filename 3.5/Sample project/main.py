import psycopg2

conn = psycopg2.connect(
    database="airflow",
    user="airflow",
    password="airflow",
    host="0.0.0.0",
    port="65432"
)

cur = conn.cursor()

cur.execute("SELECT * FROM db_test")
rows = cur.fetchall()
for row in rows:
    print(row)

cur.close()
conn.close()