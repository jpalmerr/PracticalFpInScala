package shoppingCart.domain

import cats.effect._
import cats.effect.implicits._
import cats.syntax.all._

import scala.concurrent.duration.FiniteDuration

/**
 * effect which lets us schedule tasks to run in the background sometime in the future
 * this is essentially what Concurrent and Timer do
 */

trait Background[F[_]] {
  def schedule[A](
                   fa: F[A],
                   duration: FiniteDuration
                 ): F[Unit]
}

object Background {
  def apply[F[_]](implicit ev: Background[F]): Background[F] = ev

  implicit def concurrentBackground[F[_]: Concurrent: Timer]: Background[F] =
    new Background[F] {

      def schedule[A](
                       fa: F[A],
                       duration: FiniteDuration
                     ): F[Unit] =
        (Timer[F].sleep(duration) *> fa).start.void

    }

}
