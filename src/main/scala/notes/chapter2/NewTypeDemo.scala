package notes.chapter2

import cats.implicits.{catsSyntaxOptionId, none}
import eu.timepit.refined.types.string.NonEmptyString
import io.estatico.newtype.macros.newtype
import eu.timepit.refined.auto._

object NewTypeDemo extends App {

  @newtype case class Username(value: String)

  @newtype case class Email(value: String)

  def mkUsername(value: String): Option[Username] = if (value.nonEmpty) Username(value).some else none[Username]

  def mkEmail(value: String): Option[Email] = if (value.contains("@")) Email(value).some else none[Email]

  println(mkUsername("jamesp123"))
  println(mkEmail("james"))
  println(mkEmail("james@gmail.com"))

  val email = Email("foo") // but notice we can still get round it
  println(email)

  // so lets combine with refined

  @newtype case class Brand(value: NonEmptyString)

  @newtype case class Category(value: NonEmptyString)

  val brand: Brand = Brand("foo")
  //  val fails = Brand("") // fails
  println(brand)
}
