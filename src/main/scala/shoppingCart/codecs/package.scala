package shoppingCart

import cats.Applicative
import io.circe.{Encoder, Json}
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf
import eu.timepit.refined.string.Uuid
import io.circe.derivation.deriveEncoder
import shoppingCart.domain.brand._
import shoppingCart.domain.{Category, CategoryId, CategoryName, Item, ItemDescription, ItemId, ItemName}
import squants.market.Money

package object codecs {
  implicit def deriveEntityEncoder[F[_]: Applicative, A: Encoder]: EntityEncoder[F, A] = jsonEncoderOf[F, A]

  implicit final lazy val encodeUUID: Encoder[Uuid] = new Encoder[Uuid] {
    final def apply(a: Uuid): Json = Json.fromString(a.toString)
  }

  // brand

  implicit val brandEncoder: Encoder[Brand]          = deriveEncoder[Brand]
  implicit val brandNameEncoder: Encoder[BrandName]  = deriveEncoder[BrandName]
  implicit val brandIdEncoder: Encoder[BrandId]      = deriveEncoder[BrandId]

  // category

  implicit val categoryEncoder: Encoder[Category] = deriveEncoder[Category]
  implicit val categoryIdEncoder: Encoder[CategoryId]     = deriveEncoder[CategoryId]
  implicit val categoryNameEncoder: Encoder[CategoryName] = deriveEncoder[CategoryName]

  // item
  implicit val itemEncoder: Encoder[Item] = deriveEncoder[Item]
  implicit val itemIdEncoder: Encoder[ItemId] = deriveEncoder[ItemId]
  implicit val itemNameEncoder: Encoder[ItemName] = deriveEncoder[ItemName]
  implicit val itemDescriptionEncoder: Encoder[ItemDescription] = deriveEncoder[ItemDescription]


  // money
  implicit val moneyEncoder: Encoder[Money] =
    Encoder[BigDecimal].contramap(_.amount)



}
