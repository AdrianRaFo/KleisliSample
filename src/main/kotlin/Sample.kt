
import arrow.core.*
import arrow.data.Kleisli

object Sample {

  @JvmStatic
  fun main(args: Array<String>) {
    val optionKleisli = Kleisli { str :String->
      if (str.toCharArray().all { it.isDigit() }) Some(str.toInt()) else None }

    val eitherFromOptionKleisli = Kleisli{ optString : Option<String> ->
      optString.fold<Either<Int,Int>>({Left(0)},{ str -> Right(optionKleisli.run(str).ev().getOrElse { 0 })})
    }

    println(eitherFromOptionKleisli.run(Some("1")).ev().getOrElse { "" })
  }

}
