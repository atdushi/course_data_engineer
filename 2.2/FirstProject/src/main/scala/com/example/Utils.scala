package com.example

import net.liftweb.json
import net.liftweb.json.DefaultFormats
import scalaj.http._

import scala.language.implicitConversions

object Utils {
  /*
    Зарплата после вычета налога
   */
  def computeSalaryNet(salaryGross: Int, bonus: Float, eatBonus: Int): Double = {
    // 13 % налог
    (salaryGross + salaryGross * bonus + eatBonus) * 0.87 / 12
  }

  /*
    Процент отклонения
   */
  def computeDeviationPercent(salaryGross: Int, salaries: List[Int]): Double = {
    val middleSalary = salaries.sum / salaries.length
    val deviation: Double = 100 - (100 * salaryGross / middleSalary)
    -deviation / 100
  }

  /*
    Отклонение зарплаты от средней
   */
  def computeDeviation(salaryGross: Int, salaries: List[Int]): Int = {
    val middleSalary = salaries.sum / salaries.length
    middleSalary - salaryGross
  }

  /*
    Зарплаты из вилки
   */
  def computeFork(salaries: List[Int], min: Int, max: Int): List[Int] = {
    var result = List[Int]()

    for ((elem, _) <- salaries.zipWithIndex) {
      if (elem < max && elem > min) {
        result = result :+ elem
      }
    }

    result
  }

  /*
    Индексация зарплат на multiplier
   */
  def indexSalary(salaries: List[Int], multiplier: Float): List[Int] = {
    var result = List[Int]()
    salaries.foreach(c => {
      // округлим для красоты
      result = result :+ (c + c * multiplier).toInt
    })
    result
  }

  /*
    Разница между средней зарплатой и числом avgSalaryByMarket
   */
  def computeDiff(avgSalaryByMarket: Int, salaries: List[Int]): Float = {
    val avgMiddleSalary = salaries.sum / salaries.size
    val avgMiddleSalaryDiff = 1 - avgMiddleSalary / avgSalaryByMarket
    avgMiddleSalaryDiff
  }

  def getHhAvgSalary(level: String): Float = {
    object Currencies extends Enumeration {
      val RUR, USD, EUR = Value
    }
    import Currencies._

    val exchangeRates = Map(
      RUR -> 1,
      USD -> 62,
      EUR -> 62
    )

    case class salary(from: String, to: String, currency: String, gross: Boolean)
    case class vacancy(name: String, salary: salary)

    val response: HttpResponse[String] = Http("https://api.hh.ru/vacancies")
      .param("text", s"name:\"$level data engineer\"")
      .param("only_with_salary", "true")
      .param("area", "1") // 1 - мск, 2 - спб, 113 - россия
      .asString

    implicit val formats: DefaultFormats.type = DefaultFormats

    val jValue = json.parse(response.body)

    val vacancies = (jValue \ "items").extract[List[vacancy]]

    var result: List[Int] = Nil

    implicit def stringToCurrency(currency: String): Value = Currencies.values.find(_.toString == currency).get

    for (vac <- vacancies) {
      var sal: Int = if (vac.salary.from != null) vac.salary.from.toInt else vac.salary.to.toInt
      sal = sal * exchangeRates(vac.salary.currency)
      result = result :+ sal
    }

    val middleSalary = result.sum / result.length
    middleSalary
  }
}
