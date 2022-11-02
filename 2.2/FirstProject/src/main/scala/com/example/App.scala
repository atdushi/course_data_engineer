package com.example

import scala.io.StdIn.readLine

object App {
  val salaries: List[Int] = List(100, 150, 200, 80, 120, 75)

  def main(args: Array[String]): Unit = {
    task_a()
    task_b()
    task_c()
    task_d()
    task_e()
    task_f()
    task_g()
    task_h()
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
  def task_b(): Unit = {
    println("Введите годовой доход:")
    val salaryGross = readLine.toInt
    println("Размер премии:")
    val bonus = readLine.toFloat
    println("Компенсация питания:")
    val eatBonus = readLine.toInt

    println(s"ежемесячный оклад сотрудника после вычета налогов: ${Utils.computeSalary(salaryGross, bonus, eatBonus)}")
  }

  /*
    c. Напишите программу, которая рассчитывает для каждого сотрудника отклонение(в процентах)
    от среднего значения оклада на уровень всего отдела. В итоговом значении должно учитываться
    в большую или меньшую сторону отклоняется размер оклада. На вход вышей программе подаются все значения,
    аналогичные предыдущей программе, а также список со значениями окладов сотрудников отдела 100, 150, 200, 80, 120, 75.
   */
  def task_c(): Unit = {
    println("Введите годовой доход:")
    val salaryGross = readLine.toInt

    val deviation = Utils.computeDeviation(salaryGross, salaries)

    println(s"отклонение(в процентах) от среднего значения оклада на уровень всего отдела: $deviation")
  }

  /*
    d. Попробуйте рассчитать новую зарплату сотрудника, добавив (или отняв, если сотрудник плохо себя вел)
    необходимую сумму с учетом результатов прошлого задания.
    Добавьте его зарплату в список и вычислите значение самой высокой зарплаты и самой низкой.
   */
  def task_d(): Unit = {
    val prevBonus = List(5, 5, 5, -5, 5, -5)

    var result = List[Int]()

    for ((elem, i) <- salaries.zipWithIndex) {
      result = result :+ (elem + prevBonus.apply(i))
    }

    println(s"новая зарплата сотрудника: $result")
    println(s"минимальная зарплата: ${result.min}")
    println(s"максимальная зарплата: ${result.max}")
  }

  /*
    e. Также в вашу команду пришли два специалиста с окладами 350 и 90 тысяч рублей.
    Попробуйте отсортировать список сотрудников по уровню оклада от меньшего к большему.
   */
  def task_e(): Unit = {
    var result = salaries
    result = salaries :+ 350
    result = salaries :+ 90
    result = result.sorted

    println(s"список сотрудников по уровню оклада от меньшего к большему: ${result.mkString(", ")}")
  }

  /*
    f.  Кажется, вы взяли в вашу команду еще одного сотрудника и предложили ему оклад 130 тысяч.
    Вычислите самостоятельно номер сотрудника в списке так, чтобы сортировка не нарушилась и добавьте его на это место.
   */
  def task_f(): Unit = {
    var j = 0
    val salary = 130

    for ((elem, i) <- salaries.zipWithIndex) {
      if (elem < salary) {
        j = i
      }
    }

    val result = salaries.take(j + 1) ++ List(salary) ++ salaries.drop(j + 1)

    println(s"с новым сотрудник с зарплатой 130 тысяч: ${result.mkString(", ")}")
  }

  /*
    g. Попробуйте вывести номера сотрудников из полученного списка, которые попадают под категорию middle.
    На входе программе подается «вилка» зарплаты специалистов уровня middle.
   */
  def task_g(): Unit = {
    println("Введите верхнюю границу для зарплаты middle:")
    val min = readLine().toInt
    println("Введите нижнюю границу для зарплаты middle:")
    val max = readLine().toInt

    println(s"специалисты уровня middle: ${Utils.computeFork(salaries, min, max).mkString(", ")}")
  }

  /*
    h. Однако наступил кризис и ваши сотрудники требуют повысить зарплату.
    Вам необходимо проиндексировать зарплату каждого сотрудника на уровень инфляции – 7%
   */
  def task_h(): Unit = {
    println(s"старая зарплата: ${salaries.mkString(", ")}")

    val inflation = 0.07f

    println(s"проиндексированная зарплата: ${Utils.indexSalary(salaries, inflation).mkString(", ")}")
  }
}
