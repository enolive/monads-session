import arrow.core.Either
import arrow.core.flatMap
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.right

sealed interface Expression
data class Const(val num: Int) : Expression
data class Div(val a: Expression, val b: Expression) : Expression

data object DivByZeroError

val answer = Div(Div(Const(1932), Const(23)), Const(2))
val err = Div(Const(1), Const(0))
val complexErr = Div(answer, err)

// monadic comprehensions with the power of Arrow-Kt
fun eval(e: Expression): Either<DivByZeroError, Int> = when (e) {
    is Const -> e.num.unit()
    is Div ->
        either {
            val x = eval(e.a).bind()
            val y = eval(e.b).bind()
            (x safeDiv y).bind()
        }
    // monadic bind -- need to remove the suspend keyword to make it work!
    //  eval(e.a).bind { x ->
    //  eval(e.b).bind { y ->
    //  x safeDiv y
    //  }
    //  }
}

// a monad is a structure that implements both unit and bind!
// unfortunately, there is no way in Kotlin's type system to define a generic monad because it lacks the
// syntax to enforce type constructors such as unit and higher kinded types
// (something like a generic type with another generic type embedded, such as M<T>)
// the only 3 languages I am aware of where something like this would be possible are Scala, C++ Templates and Haskell

// unit :: (T) -> Monad<T>
fun <T> T.unit() = right()

// bind :: (Monad<T>, (T) -> Monad<U>) -> Monad<U>
fun <T, U, E> Either<E, T>.bind(f: (T) -> Either<E, U>) = flatMap(f)

infix fun Int.safeDiv(that: Int) = either {
    ensure(that != 0) { DivByZeroError }
    this@safeDiv / that
}

fun main() {
    listOf(
        answer,
        err,
        complexErr,
    ).map { eval(it) }.forEach { println(it) }
}
