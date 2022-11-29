package org.example

object Utils {
  def main(args: Array[String]): Unit = {
    println(getGender("Логинов Алексей Алексеевич") == "m")
    println(getGender("Калугин Максим Львович") == "m")
    println(getGender("Кудрявцева Варвара Денисовна") == "w")
    println(getGender("Титова Василиса Сергеевна") == "w")
    println(getGender("Смирнова Екатерина Львовна") == "w")
  }

  def getGender(fullName: String): String = {
    val lastName = fullName.split(" ").apply(0)
    val ending = lastName takeRight 2
    if (ending == "ов" || ending == "ев" || ending == "ин" || ending == "ич") {
      "m"
    } else {
      "w"
    }
  }

}
