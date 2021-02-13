package shoppingCart.routes

import cats.{Defer, Monad}
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import shoppingCart.domain.Brands
import shoppingCart.codecs._


final class BrandRoutes[F[_]: Defer: Monad]( brands: Brands[F]
                                           ) extends Http4sDsl[F] {
  private[routes] val prefixPath = "/brands"
  private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root =>
    Ok(brands.findAll)
  }
  val routes: HttpRoutes[F] = Router(prefixPath -> httpRoutes)
}

/**
 * some constraints on F[_]
 *  - Defer is required by HttpRoutes.of[F]
 *  - Monad is needed to create a response
 *      - and it also gives us Applicative, also needed for HttpRoutes.of[F]
 *
 * Brands[F] algebra as an argument to our class
 *
 * Extending Http4sDsl gets us syntax
 *
 *
 */