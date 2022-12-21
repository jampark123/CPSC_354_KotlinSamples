fun main(args: Array<String>){
    print("Enter the first number: ")
    val num1 = readLine()!!.toDouble()

    print("Enter the second number: ")
    val num2 = readLine()!!.toDouble()

    print("Enter the operation (+, -, *, /): ")
    val operator = readLine()!!

    val result: Double = when (operator){
        "+" -> num1 + num2
        "-" -> num1 + num2
        "*" -> num1 * num2
        "/" -> num1 / num2
        else -> {
            println("Invalid operator")
            0.0
        }
    }

    println("Result: $result")
}