package com.example

import scala.io.StdIn.readLine

object App {
  case class Employee(salaryGross: Int, bonus: Float, eatBonus: Int)

  def main(args: Array[String]): Unit = {

    // Версия Lite

    taskA()

    var salaries: List[Int] = List(100, 150, 200, 80, 120, 75)

    val employee = taskB()

    taskC(employee, salaries)

    salaries = taskD(employee.salaryGross, salaries)

    salaries = taskE(salaries)

    salaries = taskF(salaries)

    taskG(salaries)

    salaries = taskH(salaries)
  }

  /*
    a. Напишите программу, которая:
     i.   выводит фразу «Hello, Scala!» справа налево
     ii.  переводит всю фразу в нижний регистр
     iii. удаляет символ!
     iv.  добавляет в конец фразы «and goodbye python!»
   */
  def taskA(): Unit = {
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
  def taskB(): Employee = {
    println("Введите годовой доход:")
    val salaryGross = readLine.toInt
    println("Размер премии в процентах (типа 0.2):")
    val bonus = readLine.toFloat
    println("Компенсация питания:")
    val eatBonus = readLine.toInt

    println(s"Ежемесячный оклад сотрудника после вычета налогов: ${Utils.computeSalaryNet(salaryGross, bonus, eatBonus)}")

    val employee = Employee(salaryGross, bonus, eatBonus)
    employee
  }

  /*
    c. Напишите программу, которая рассчитывает для каждого сотрудника отклонение(в процентах)
    от среднего значения оклада на уровень всего отдела. В итоговом значении должно учитываться
    в большую или меньшую сторону отклоняется размер оклада. На вход вышей программе подаются все значения,
    аналогичные предыдущей программе, а также список со значениями окладов сотрудников отдела 100, 150, 200, 80, 120, 75.
   */
  def taskC(employee: Employee, salaries: List[Int]): Double = {
    val deviation = Utils.computeDeviationPercent(employee.salaryGross, salaries)

    println(s"Отклонение (в процентах) от среднего значения оклада на уровень всего отдела: $deviation")

    deviation
  }

  /*
    d. Попробуйте рассчитать новую зарплату сотрудника, добавив (или отняв, если сотрудник плохо себя вел)
    необходимую сумму с учетом результатов прошлого задания.
    Добавьте его зарплату в список и вычислите значение самой высокой зарплаты и самой низкой.
   */
  def taskD(salaryGross: Int, salaries: List[Int]): List[Int] = {
    val salary = salaryGross + Utils.computeDeviation(salaryGross,salaries)
    val result = salaries :+ salary

    println(s"Новые зарплаты сотрудников: ${result.mkString(", ")}")
    println(s"Минимальная зарплата: ${result.min}")
    println(s"Максимальная зарплата: ${result.max}")

    result
  }

  /*
    e. Также в вашу команду пришли два специалиста с окладами 350 и 90 тысяч рублей.
    Попробуйте отсортировать список сотрудников по уровню оклада от меньшего к большему.
   */
  def taskE(salaries: List[Int]): List[Int] = {
    var result = salaries
    result = salaries :+ 350
    result = salaries :+ 90
    result = result.sorted

    println(s"Список сотрудников по уровню оклада от меньшего к большему: ${result.mkString(", ")}")

    salaries
  }

  /*
    f.  Кажется, вы взяли в вашу команду еще одного сотрудника и предложили ему оклад 130 тысяч.
    Вычислите самостоятельно номер сотрудника в списке так, чтобы сортировка не нарушилась и добавьте его на это место.
   */
  def taskF(salaries: List[Int]): List[Int] = {
    var j = 0
    val salary = 130

    for ((elem, i) <- salaries.zipWithIndex) {
      if (elem < salary) {
        j = i
      }
    }

    val result = salaries.take(j + 1) ++ List(salary) ++ salaries.drop(j + 1)

    println(s"С новым сотрудником с зарплатой 130 тысяч: ${result.mkString(", ")}")

    result
  }

  /*
    g. Попробуйте вывести номера сотрудников из полученного списка, которые попадают под категорию middle.
    На входе программе подается «вилка» зарплаты специалистов уровня middle.
   */
  def taskG(salaries: List[Int]): Unit = {
    println("Введите верхнюю границу для зарплаты middle:")
    val min = readLine().toInt
    println("Введите нижнюю границу для зарплаты middle:")
    val max = readLine().toInt

    println(s"Специалисты уровня middle: ${Utils.computeFork(salaries, min, max).mkString(", ")}")
  }

  /*
    h. Однако наступил кризис и ваши сотрудники требуют повысить зарплату.
    Вам необходимо проиндексировать зарплату каждого сотрудника на уровень инфляции – 7%
   */
  def taskH(salaries: List[Int]): List[Int] = {
    println(s"Старая зарплата: ${salaries.mkString(", ")}")

    val inflation = 0.07f
    var result: List[Int] = Nil

    // округлим для красоты
    for (elem <- Utils.indexSalary(salaries, inflation)) {
      result = result :+ elem.round.toInt
    }

    println(s"Проиндексированная зарплата: ${result.mkString(", ")}")

    result
  }
}
