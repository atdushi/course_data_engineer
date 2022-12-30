package com.example

import scala.io.StdIn.readLine

object App {

  case class Employee(salaryGross: Int, bonus: Option[Float], eatBonus: Option[Int])

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

    // Версия Pro

    taskI(salaries)

    taskJ()

    val deanonymized = taskK()

    taskL(deanonymized)

    taskM(deanonymized)

    val power = 4

    taskOi(power)

    taskOii(power)
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

    val employee = Employee(
      salaryGross = salaryGross,
      bonus = Option(bonus),
      eatBonus = Option(eatBonus)
    )
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
    val salary = salaryGross + Utils.computeDeviation(salaryGross, salaries)
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
    val result: List[Int] = Utils.indexSalary(salaries, inflation)

    println(s"Проиндексированная зарплата: ${result.mkString(", ")}")

    result
  }

  /*
   i. *Ваши сотрудники остались недовольны и просят индексацию на уровень рынка.
   Попробуйте повторить ту же операцию, как и в предыдущем задании,
   но теперь вам нужно проиндексировать зарплаты на процент отклонения
   от среднего по рынку с учетом уровня специалиста.
   На вход вашей программе подается 3 значения – среднее значение зарплаты на рынке
   для каждого уровня специалистов(junior, middle и senior)
   */
  def taskI(salaries: List[Int]): Unit = {

    case class MarketSalary(average: Int, min: Int, max: Int, level: String)

    val marketSalaries: List[MarketSalary] = List(
      MarketSalary(150, 49, 100, "junior"),
      MarketSalary(175, 99, 200, "middle"),
      MarketSalary(220, 180, 250, "senior"))

    for (marketSalary <- marketSalaries) {
      val fork: List[Int] = Utils.computeFork(salaries, marketSalary.min, marketSalary.max)
      val avgSalaryDiff = Utils.computeDiff(marketSalary.average, fork)
      val newSalaries: List[Int] = Utils.indexSalary(fork, avgSalaryDiff)
      println(s"Проиндексированные на уровень рынка зарплаты ${marketSalary.level}: ${newSalaries.mkString(", ")}")
    }
  }

  /*
   j. ****(для тех, кто любит хардкор) Попробуйте самостоятельно вычислить средние значения
   уровня зарплат для data engineer’ов каждого уровня с помощью, например,  https://dev.hh.ru/.
   */
  def taskJ(): Unit = {
    val list = List("junior", "middle", "senior", "lead")

    for (item <- list) {
      val middleSalary = Utils.getHhAvgSalary(item).toInt

      println(s"api.hh.ru средняя зарплата для $item: $middleSalary")
    }
  }

  /*
     k. *Попробуйте деанонимизировать ваших сотрудников –
     составьте структуру, которая позволит иметь знания о том,
     сколько зарабатывает каждый сотрудник(Фамилия и имя).
  */
  def taskK(): Map[String, Int] = {
    val workerSalaryMap = Map(
      "Сафонова Алина" -> 110,
      "Антонов Евгений" -> 50,
      "Седов Антон" -> 80
    )

    workerSalaryMap
  }

  /*
    l. *Выведите фамилию и имя сотрудников с самой высокой
    и самой низкой зарплатой(только не рассказывайте им об этом факте).
   */
  def taskL(workerSalaryMap: Map[String, Int]): Unit = {
    val sorted = workerSalaryMap.toSeq.sortBy(_._2)
    println(s"Минимальная зарплата у ${sorted.head._1}")
    println(s"Максимальная зарплата у ${sorted.last._1}")
  }

  /*
    m. *Попробуйте запутать тех, кто может случайно наткнуться на эти данные –
    удалите для каждого сотрудника имя, переведите строку в нижний регистр,
    удалите гласные и разверните оставшиеся символы справа налево(abc -> cb).
   */
  def taskM(workerSalaryMap: Map[String, Int]): Unit = {
    var result: List[String] = Nil

    workerSalaryMap.foreach(e => {
      val lastName = e._1.split(" ").apply(0).toLowerCase().replaceAll("[аиеёоуыэюя]", "").reverse
      result = result :+ lastName
    })

    println(s"Запутанные сотрудники: ${result.mkString(", ")}")
  }

  /*
    o. *Попробуйте написать функцию, которая вычисляет значение степени двойки:
          i. С помощью обычной рекурсии
   */
  def taskOi(power: Int): Unit = {
    taskO(new RecursionCalculator, power)
  }

  /*
    o. *Попробуйте написать функцию, которая вычисляет значение степени двойки:
           ii. **С помощью хвостовой рекурсии
   */
  def taskOii(power: Int): Unit = {
    taskO(new TailRecursionCalculator, power)
  }

  def taskO(calculator: PowerOfTwoCalculator, power: Int): Unit = {
    println(s"${calculator.toString}: 2^$power = ${calculator.calculate(power)}")
  }
}
