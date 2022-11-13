package com.example

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
    var deviation: Double = 100 - (100 * salaryGross / middleSalary)
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
    val avgMiddleSalaryDiff = 1 - avgMiddleSalary / avgSalaryByMarket.toFloat
    avgMiddleSalaryDiff
  }
}
