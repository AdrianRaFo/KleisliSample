import arrow.core.*
import arrow.data.Kleisli

object Sample {

  @JvmStatic
  fun main(args: Array<String>) {
    println(safeToInt(None))
    println(safeToInt(Some("a")))
    println(safeToInt(Some("1")))
  }

  fun safeToInt(value: Option<String>): Either<RuntimeException, Int> {
    val optionKleisli = Kleisli { str: String ->
      if (str.toCharArray().all { it.isDigit() }) Some(str.toInt()) else None
    }

    val eitherFromOptionKleisli = Kleisli { optString: Option<String> ->
      optString.fold({ Left(NullPointerException("Any String here")) }, { str ->
        optionKleisli.run(str).ev().fold(
            { Left(IllegalArgumentException("It's not an Integer")) },
            { number -> Right(number) })
      })
    }
    return eitherFromOptionKleisli.run(value).ev()
  }
}
