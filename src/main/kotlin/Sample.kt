
import arrow.core.*
import arrow.data.Kleisli
import arrow.data.fix
import arrow.data.monad
import arrow.typeclasses.binding

object Sample {

  @JvmStatic
  fun main(args: Array<String>) {
    println("a".safeToInt())
    println("1".safeToInt())
    println(optionIntKleisli.run("1").fix())
    println(optionIntKleisli.local { optStr: Option<String> -> optStr.getOrElse { "0" } }.run(None))
    println(optionIntKleisli.map (Option.functor(), {output:Int -> output + 1 }).fix().run("1"))
    println(optionIntKleisli.ap(Option.applicative(), optionIntDoubleKleisli).fix().run("1"))
    println(optionIntKleisli.flatMap(Option.monad(), { optionDoubleKleisli }).fix().run("1"))
    println(optionIntKleisli.andThen(Option.monad(), optionFromOptionKleisli).fix().run("1"))
    println(optionIntKleisli.andThen(Option.monad(), { number: Int -> Some(number + 1) }).fix().run("1"))
    println(optionIntKleisli.andThen(Option.monad(), Some(0)).fix().run("1"))
    println(configKleisli.run(Config(1, 2.0)))
    println(askKleisli.run(Config(1, 2.0)))
  }

  val k1 = Kleisli { intNumber: Int -> Some(intNumber.toString()) }
  val k2 = Kleisli { doubleNumber: Double -> Some(doubleNumber.toString()) }

  data class Config(val n: Int, val d: Double)

  val configKleisli: Kleisli<ForOption, Config, String> =
      Kleisli.monad<ForOption, Config>(Option.monad()).binding {
        val a = k1.local<Config> { it.n }.bind()
        val b = k2.local<Config> { it.d }.bind()
        a + b
      }.fix()

  val askKleisli = Kleisli.monad<ForOption, Config>(Option.monad()).binding {
    val (n, d) = Kleisli.ask<ForOption, Config>(Option.monad()).bind()
    n + d
  }.fix()

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
    optionIntKleisli.run(str).fix().fold(
        { Left(str) },
        { number -> Right(number) })
  }


  fun String.safeToInt(): Either<String, Int> {
    return eitherFromOptionKleisli.run(this).fix()
  }
}
