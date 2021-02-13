package shoppingCart

import eu.timepit.refined.api.{Refined, Validate}
import eu.timepit.refined.refineV
import org.http4s.{ParseFailure, QueryParamDecoder}
import cats.implicits._
import io.estatico.newtype.Coercible
import io.estatico.newtype.ops._

package object params {

  implicit def coercibleQueryParamDecoder[A: Coercible[B, *], B: QueryParamDecoder]: QueryParamDecoder[A] =
    QueryParamDecoder[B].map(_.coerce[A])

  implicit def refinedQueryParamDecoder[T: QueryParamDecoder, P](
                                                                  implicit ev: Validate[T, P]
                                                                ): QueryParamDecoder[T Refined P] =
    QueryParamDecoder[T].emap(refineV[P](_).leftMap(m => ParseFailure(m, m)))
}
