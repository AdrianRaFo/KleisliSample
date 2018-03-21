import arrow.core.*
import arrow.data.Kleisli

object Sample {

  @JvmStatic
  fun main(args: Array<String>) {
    println("a".safeToInt())
    println("1".safeToInt())
  }

  val optionKleisli = Kleisli { str: String ->
    if (str.toCharArray().all { it.isDigit() }) Some(str.toInt()) else None
  }

  fun String.safeToInt(): Either<String, Int> {

    val eitherFromOptionKleisli = Kleisli { str: String ->
      optionKleisli.run(str).ev().fold(
          { Left(str) },
          { number -> Right(number) })
    }

    return eitherFromOptionKleisli.run(this).ev()
  }
}
