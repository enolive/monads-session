private sealed interface Expression
private data class Const(val num: Int) : Expression
private data class Div(val a: Expression, val b: Expression) : Expression

private val answer = Div(Div(Const(1932), Const(23)), Const(2))
private val err = Div(Const(1), Const(0))
private val complexErr = Div(answer, err)

// this type signature is a lie as it promises that any expression can be converted to an int, which is
// obviously not true due to division by zero errors
private fun eval(e: Expression): Int = when (e) {
  is Const -> e.num
  is Div -> {
    val x = eval(e.a)
    val y = eval(e.b)
    x / y
  }
}

fun main() {
  listOf(
    answer,
    err,
    complexErr,
  ).map { eval(it) }.forEach { println(it) }
}
