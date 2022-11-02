package com.example

object Utils {
  def computeSalary(salaryGross: Int, bonus: Float, eatBonus: Int): Double = {
    // 13 % налог
    (salaryGross + salaryGross * bonus + eatBonus) * 0.87 / 12
  }

  def computeDeviation(salaryGross: Int, salaries: List[Int]): Double = {
    val middleSalary = salaries.sum / salaries.length
    100 - (100 * salaryGross / middleSalary)
  }

  def computeFork(salaries: List[Int], min: Int, max: Int): List[Int] = {
    var result = List[Int]()

    for ((elem, i) <- salaries.zipWithIndex) {
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
