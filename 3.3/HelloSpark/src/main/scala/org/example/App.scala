package org.example

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{col, count, current_date, datediff, from_unixtime, max, to_date, udf, year}
import org.apache.spark.sql.expressions.Window

import java.util.Properties

object App {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .master("local[1]")
      .appName("SparkByExample")
      .getOrCreate();

    spark.sparkContext.setLogLevel("ERROR")

    val url = "jdbc:postgresql://localhost:5432/postgres"

    //    ·id - уникальный идентификатор посетителя сайта
    //    ·timestamp – дата и время события в формате unix timestamp
    //    ·type – тип события, значение из списка(факт посещения (visit),
    //      клик по визуальному элементу страницы(click),
    //      скролл(scroll),
    //      перед на другую страницу (move)).
    //    ·page_id – id текущей страницы.Тип - последовательность чисел фиксированной длины
    //    ·tag – каждая страница с новостью размечается редакцией специальными тегами,
    //      которые отражают тематику конкретной новости со страницы.
    //      Возможный список тематик: политика, спорт, медицина и т.д.
    //    ·sign – наличие у пользователя личного кабинета.Значения –True / False
    var df_web = spark.read
      .format("jdbc")
      .option("url", url)
      .option("dbtable", "de_sprint.actions")
      .option("user", "postgres")
      .option("password", "myPassword")
      .load()

    df_web = df_web
      .withColumn("event_time", from_unixtime(col("timestamp")))
      .drop("timestamp")

    // всего посещений
    df_web = df_web.withColumn("total_visit_count",
      count(col("id")).over(Window.partitionBy("id")))

    // дата последнего посещения
    df_web = df_web.withColumn("max_event_time",
      max(col("event_time")).over(Window.partitionBy("id")))

    // посещения страниц
    df_web = df_web.withColumn("page_visit_count",
      count(col("id")).over(Window.partitionBy("id", "page_id")))

    // посещения тем
    df_web = df_web.withColumn("tag_visit_count",
      count(col("id")).over(Window.partitionBy("id", "tag")))
      .withColumn("max_tag_visit_count",
        max(col("tag_visit_count")).over(Window.partitionBy("id")))

//    df_web.show()

    val connectionProperties = new Properties()
    connectionProperties.put("user", "postgres")
    connectionProperties.put("password", "myPassword")

    //    1. Id –уникальный идентификатор личного кабинета
    //    2. User_id –уникальный идентификатор посетителя
    //    3. ФИО посетителя
    //    4. Дату рождения посетителя
    //    5. Дата создания ЛК
    var df_lk = spark.read.jdbc(url, "de_sprint.profiles", connectionProperties)

    val genderUDF = udf((fullName: String) => Utils.getGender(fullName))

    df_lk = df_lk.withColumn("gender", genderUDF(col("full_name")))

    df_lk = df_lk.withColumn("age", year(current_date()) - year(col("birthdate")))

    //    Создайте витрину данных в Postgres со следующим содержанием
    //    1. Id посетителя
    //    2. Возраст посетителя
    //    3. Пол посетителя(постарайтесь описать логику вычисления пола в отдельной пользовательской функции)
    //    4. Любимая тематика новостей
    //    5. Любимый временной диапазон посещений
    //    6. Id личного кабинета
    //    7. Разница в днях между созданием ЛК и датой последнего посещения.(-1 если ЛК нет)
    //    8. Общее кол - во посещений сайта
    //    9. Средняя длина сессии(сессией считаем временной промежуток, который охватывает последовательность событий,
    //    которые происходили подряд с разницей не более 5 минут).
    //    10. Среднее кол - во активностей в рамках одной сессии

    var df_all = df_web.alias("web").join(
      df_lk.alias("lk"),
      col("lk.user_id") === col("web.id"),
      "outer")

    df_all = df_all.withColumn("raznica", datediff(col("max_event_time"), col("lk.created_at")))

    df_all.select(
      "web.id",
      "lk.age",

      "lk.gender",

      "lk.id",
      "raznica",
      "web.total_visit_count"


    ).distinct().show()
  }

  def test(): Unit = {
    val spark = SparkSession.builder()
      .master("local[1]")
      .appName("SparkByExample")
      .getOrCreate();

    println("First SparkContext:")
    println("APP Name :" + spark.sparkContext.appName);
    println("Deploy Mode :" + spark.sparkContext.deployMode);
    println("Master :" + spark.sparkContext.master);

    val sparkSession2 = SparkSession.builder()
      .master("local[1]")
      .appName("SparkByExample-test")
      .getOrCreate();

    println("Second SparkContext:")
    println("APP Name :" + sparkSession2.sparkContext.appName);
    println("Deploy Mode :" + sparkSession2.sparkContext.deployMode);
    println("Master :" + sparkSession2.sparkContext.master);
  }
}
