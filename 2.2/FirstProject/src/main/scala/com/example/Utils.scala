package com.example

object Utils {
  def computeSalaryNet(salaryGross: Int, bonus: Float, eatBonus: Int): Double = {
    // 13 % налог
    (salaryGross + salaryGross * bonus + eatBonus) * 0.87 / 12
  }

  def computeDeviationPercent(salaryGross: Int, salaries: List[Int]): Double = {
    val middleSalary = salaries.sum / salaries.length
    var deviation: Double = 100 - (100 * salaryGross / middleSalary)
    -deviation / 100
  }

  def computeDeviation(salaryGross: Int, salaries: List[Int]): Int = {
    val middleSalary = salaries.sum / salaries.length
    middleSalary - salaryGross
  }

  def computeFork(salaries: List[Int], min: Int, max: Int): List[Int] = {
    var result = List[Int]()

    for ((elem, _) <- salaries.zipWithIndex) {
      if (elem < max && elem > min) {
        result = result :+ elem
      }
    }

    result
  }

  def indexSalary(salaries: List[Int], inflation: Float): List[Double] = {
    var result = List[Double]()
    salaries.foreach(c => {
      result = result :+ (c + c * inflation)
    })
    result
  }
}
