import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right

private sealed interface Expression
private data class Const(val num: Int) : Expression
private data class Div(val a: Expression, val b: Expression) : Expression

private object DivByZeroError {
  override fun toString() = "/ by zero"
}

private val answer = Div(Div(Const(1932), Const(23)), Const(2))
private val err = Div(Const(1), Const(0))
private val complexErr = Div(answer, err)

private fun eval(e: Expression): Either<DivByZeroError, Int> = when (e) {
  is Const -> e.num.unit()
  is Div ->
    eval(e.a).bind { x ->
      eval(e.b).bind { y ->
        x safeDiv y
      }
    }
}

private infix fun Int.safeDiv(that: Int) = Either.catch({ DivByZeroError }) { this / that }

// a monad is a structure that implements both unit and bind!
// unfortunately, there is no way in Kotlin's type system to define a generic monad because it lacks the
// syntax to enforce type constructors such as unit and higher kinded types
// (something like a generic type with another generic type embedded, such as M<T>)
// the only 3 languages I am aware of where something like this would be possible are Scala, C++ Templates and Haskell

// unit :: (T) -> Monad<T>
private fun <T> T.unit() = right()

// bind :: (Monad<T>, (T) -> Monad<U>) -> Monad<U>
private fun <T, U, E> Either<E, T>.bind(f: (T) -> Either<E, U>) = flatMap(f)

fun main() {
  listOf(
    answer,
    err,
    complexErr,
  ).map { eval(it) }.forEach { println(it) }
}
