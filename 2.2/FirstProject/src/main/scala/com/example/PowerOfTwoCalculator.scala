package com.example

import scala.annotation.tailrec

trait PowerOfTwoCalculator {
  def calculate(power: Int): Int
}

class RecursionCalculator extends PowerOfTwoCalculator {
  override def calculate(power: Int): Int = {
    if (power == 0) 1
    else 2 * calculate(power - 1)
  }

  override def toString: String = {
    "Рекурсия"
  }
}

class TailRecursionCalculator extends PowerOfTwoCalculator {
  override def calculate(power: Int): Int = {
    @tailrec
    def loop(power: Int, acc: Int = 1): Int = {
      if (power == 0) acc
      else loop(power - 1, 2 * acc)
    }
    loop(power)
  }

  override def toString: String = {
    "Хвостовая рекурсия"
  }
}
