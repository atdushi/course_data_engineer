package com.example

object Utils {
  def compute_salary(salaryGross: Int, bonus: Float, eatBonus: Int): Double = {
    val month_total = salaryGross / 12 + (salaryGross * bonus) / 12 + eatBonus / 12
    month_total * 0.87 // 13 % налог
  }

  def compute_deviation(salaryNet: Double, salaries: List[Int] ): Double = {
    val middleSalary = salaries.sum / salaries.length
    100 - (100 * salaryNet / middleSalary)
  }
}
