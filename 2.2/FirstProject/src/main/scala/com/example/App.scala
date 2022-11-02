package com.example

import scala.io.StdIn.readLine

object App {
  var salaries: List[Int] = List(100, 150, 200, 80, 120, 75)

  def main(args: Array[String]): Unit = {

    task_a

    task_b

    task_c

    // d
    val tuple = task_d(salaries, List(5, 5, 5, -5, 5, -5))
    println(s"новая зарплата сотрудника: ${tuple._1.mkString(", ")}")
    println(s"минимальная зарплата: ${tuple._2}")
    println(s"максимальная зарплата: ${tuple._3}")

    // e
    salaries = task_e(salaries)
    println(s"список сотрудников по уровню оклада от меньшего к большему: ${salaries.mkString(", ")}")

    // f
    salaries = task_f(salaries)
    println(s"с новым сотрудник с зарплатой 130 тысяч: ${salaries.mkString(", ")}")

    // g
    println(s"специалисты уровня middle: ${task_g(salaries, 50, 110).mkString(", ")}")

    // h
    println(s"старая зарплата: ${salaries.mkString(", ")}")
    println(s"проиндексированная зарплата: ${task_h(salaries).mkString(", ")}")
  }

  /*
    a. Напишите программу, которая:
     i.   выводит фразу «Hello, Scala!» справа налево
     ii.  переводит всю фразу в нижний регистр
     iii. удаляет символ!
     iv.  добавляет в конец фразы «and goodbye python!»
   */
  def task_a(): Unit = {
    val hello = "Hello World!"
    println(hello.reverse)
    println(hello.toLowerCase())
    println(hello.replace("!", ""))
    println(hello + "and goodbye python!")
  }

  /*
    b. Напишите программу, которая вычисляет ежемесячный оклад сотрудника после вычета налогов.
    На вход вашей программе подается значение годового дохода до вычета налогов,
    размер премии – в процентах от годового дохода и компенсация питания.
   */
  def task_b() = {
    println("Введите годовой доход:")
    val salaryGross = readLine.toInt
    println("Размер премии:")
    val bonus = readLine.toFloat
    println("Компенсация питания:")
    val nutritional_compensation = readLine.toInt

    println(s"ежемесячный оклад сотрудника после вычета налогов: ${Utils.compute_salary(salaryGross, bonus, nutritional_compensation)}")
  }

  /*
    c. Напишите программу, которая рассчитывает для каждого сотрудника отклонение(в процентах)
    от среднего значения оклада на уровень всего отдела. В итоговом значении должно учитываться
    в большую или меньшую сторону отклоняется размер оклада. На вход вышей программе подаются все значения,
    аналогичные предыдущей программе, а также список со значениями окладов сотрудников отдела 100, 150, 200, 80, 120, 75.
   */
  def task_c() = {

    println("Введите годовой доход:")
    val salaryGross = readLine.toInt
    println("Размер премии:")
    val bonus = readLine.toFloat
    println("Компенсация питания:")
    val nutritional_compensation = readLine.toInt

    val salaryNet = Utils.compute_salary(salaryGross, bonus, nutritional_compensation)
    val deviation = Utils.compute_deviation(salaryNet, salaries)

    println(s"отклонение(в процентах) от среднего значения оклада на уровень всего отдела: $deviation")
  }

  /*
    d. Попробуйте рассчитать новую зарплату сотрудника, добавив(или отняв, если сотрудник плохо себя вел)
    необходимую сумму с учетом результатов прошлого задания. Добавьте его зарплату в список и вычислите значение самой высокой зарплаты и самой низкой.
   */
  def task_d(salaries: List[Int], bonus: List[Int]): (List[Int], Int, Int) = {
    var result = List[Int]()
    for ((elem, i) <- salaries.zipWithIndex) {
      result = result :+ elem + bonus.apply(i)
    }
    val min = result.min
    val max = result.max

    (result, min, max)
  }

  /*
    e. Также в вашу команду пришли два специалиста с окладами 350 и 90 тысяч рублей.
    Попробуйте отсортировать список сотрудников по уровню оклада от меньшего к большему.
   */
  def task_e(salaries: List[Int]): List[Int] = {
    var result = salaries
    result = salaries :+ 350
    result = salaries :+ 90
    result.sorted
  }

  /*
    f.  Кажется, вы взяли в вашу команду еще одного сотрудника и предложили ему оклад 130 тысяч.
    Вычислите самостоятельно номер сотрудника в списке так, чтобы сортировка не нарушилась и добавьте его на это место.
   */
  def task_f(salaries: List[Int]): List[Int] = {
    var j = 0
    val salary = 130

    for ((elem, i) <- salaries.zipWithIndex) {
      //      println(elem, j)
      if (elem < salary) {
        j = i
      }
    }

    salaries.take(j + 1) ++ List(salary) ++ salaries.drop(j + 1)
  }

  /*
    g. Попробуйте вывести номера сотрудников из полученного списка, которые попадают под категорию middle.
    На входе программе подается «вилка» зарплаты специалистов уровня middle.
   */
  def task_g(salaries: List[Int], min: Int, max: Int): List[Int] = {
    var result = List[Int]()
    for ((elem, i) <- salaries.zipWithIndex) {
      //      println(elem, i)
      if (elem < max & elem > min) {
        result = result :+ i
      }
    }
    result
  }

  /*
    h. Однако наступил кризис и ваши сотрудники требуют повысить зарплату.
    Вам необходимо проиндексировать зарплату каждого сотрудника на уровень инфляции – 7%
   */
  def task_h(salaries: List[Int]): List[Int] = {
    var result = List[Int]()
    salaries.foreach(c => {
      result = result :+ (c + c * 0.07).round.toInt
    })
    result
  }

}
