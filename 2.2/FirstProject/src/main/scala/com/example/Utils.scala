package com.example

object Utils {
  def compute_salary(salaryGross: Int, bonus: Float, eatBonus: Int): Double = {
    // 13 % налог
    (salaryGross + salaryGross * bonus + eatBonus) * 0.87 / 12
  }

  def compute_deviation(salaryNet: Double, salaries: List[Int] ): Double = {
    val middleSalary = salaries.sum / salaries.length
    100 - (100 * salaryNet / middleSalary)
  }
}
