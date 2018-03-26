import arrow.core.*
import arrow.data.Kleisli
import arrow.data.ev
import arrow.syntax.functor.map

object Sample {

  @JvmStatic
  fun main(args: Array<String>) {
    println("a".safeToInt())
    println("1".safeToInt())
    println(optionIntKleisli.run("1").ev())
    println(optionIntKleisli.local { optStr: Option<String> -> optStr.getOrElse { "0" } }.run(None))
    println(optionIntKleisli.map { output -> output + 1 }.ev().run("1"))
    println(optionIntKleisli.ap(optionIntDoubleKleisli, Option.applicative()).ev().run("1"))
    println(optionIntKleisli.flatMap({ optionDoubleKleisli }, Option.monad()).ev().run("1"))
    println(optionIntKleisli.andThen(optionFromOptionKleisli, Option.monad()).ev().run("1"))
    println(optionIntKleisli.andThen({ number: Int -> Some(number + 1) }, Option.monad()).ev().run("1"))
    println(optionIntKleisli.andThen(Some(0), Option.monad()).ev().run("1"))
  }

  val optionIntKleisli = Kleisli { str: String ->
    if (str.toCharArray().all { it.isDigit() }) Some(str.toInt()) else None
  }

  val intToDouble = { number: Int -> number.toDouble() }

  val optionIntDoubleKleisli = Kleisli { str: String ->
    if (str.toCharArray().all { it.isDigit() }) Some(intToDouble) else None
  }

  val optionDoubleKleisli = Kleisli { str: String ->
    if (str.toCharArray().all { it.isDigit() }) Some(str.toDouble()) else None
  }

  val optionFromOptionKleisli = Kleisli { number: Int ->
    Some(number + 1)
  }


  val eitherFromOptionKleisli = Kleisli { str: String ->
    optionIntKleisli.run(str).ev().fold(
        { Left(str) },
        { number -> Right(number) })
  }


  fun String.safeToInt(): Either<String, Int> {
    return eitherFromOptionKleisli.run(this).ev()
  }
}
