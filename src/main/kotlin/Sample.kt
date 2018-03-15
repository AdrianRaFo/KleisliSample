import arrow.core.*
import arrow.data.Kleisli

object Sample {

  @JvmStatic
  fun main(args: Array<String>) {
    println(safeToInt("a"))
    println(safeToInt("1"))
  }

  fun safeToInt(value: String): Either<String, Int> {
    val optionKleisli = Kleisli { str: String ->
      if (str.toCharArray().all { it.isDigit() }) Some(str.toInt()) else None
    }

    val eitherFromOptionKleisli = Kleisli { str: String ->
        optionKleisli.run(str).ev().fold(
            { Left(str) },
            { number -> Right(number) })
    }
    return eitherFromOptionKleisli.run(value).ev()
  }
}
