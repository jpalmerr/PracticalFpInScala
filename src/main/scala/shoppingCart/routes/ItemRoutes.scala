package shoppingCart.routes

import cats.{Defer, Monad}
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import shoppingCart.codecs._
import shoppingCart.domain.Items
import shoppingCart.domain.brand.BrandParam
import shoppingCart.params._

final class ItemRoutes[F[_]: Defer: Monad](
                                            items: Items[F]
                                          ) extends Http4sDsl[F] {

  private[routes] val prefixPath = "/items"

  object BrandQueryParam extends OptionalQueryParamDecoderMatcher[BrandParam]("brand")

  private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {

    case GET -> Root :? BrandQueryParam(brand) =>
      Ok(brand.fold(items.findAll)(b => items.findBy(b.toDomain)))

  }

  val routes: HttpRoutes[F] = Router(
    prefixPath -> httpRoutes
  )

}
