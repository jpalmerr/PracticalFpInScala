package shoppingCart

import cats.effect.Timer
import cats.implicits._
import io.chrisdavenport.log4cats.Logger
import retry.RetryDetails._
import retry._
import shoppingCart.domain.checkout._
import shoppingCart.domain._
import shoppingCart.effects.MonadThrow
import squants.market.Money

import scala.concurrent.duration._

final class CheckoutProgram[F[_]: Background: Logger: MonadThrow: Timer](
                                                                          paymentClient: PaymentClient[F],
                                                                          shoppingCart: ShoppingCart[F],
                                                                          orders: Orders[F],
                                                                          retryPolicy: RetryPolicy[F]
                                                                        ) {

  // common function to log errors for all cases
  private def logError(action: String)(e: Throwable, details: RetryDetails): F[Unit] =
    details match {
      case r: WillDelayAndRetry =>
        Logger[F].error(
          s"Failed to process $action with ${e.getMessage}. So far we have retried ${r.retriesSoFar} times."
        )
      case g: GivingUp =>
        Logger[F].error(s"Giving up on $action after ${g.totalRetries} retries.")
    }

  // helper function that retries payments
  private def processPayment(payment: Payment): F[PaymentId] = {
    val action = retryingOnAllErrors[PaymentId](
      policy = retryPolicy,
      onError = logError("Payments")
    )(paymentClient.process(payment))

    action.adaptError {
      case e =>
        PaymentError(Option(e.getMessage).getOrElse("Unknown"))
    }
  }

  private def createOrder(userId: UserId, paymentId: PaymentId, items: List[CartItem], total: Money): F[OrderId] = {
    val action = retryingOnAllErrors[OrderId](
      policy = retryPolicy,
      onError = logError("Order")
    )(orders.create(userId, paymentId, items, total))

    def bgAction(fa: F[OrderId]): F[OrderId] =
      fa.adaptError {
        case e => OrderError(e.getMessage)
      }
        .onError {
          case _ =>
            Logger[F].error(s"Failed to create order for Payment: ${paymentId}. Rescheduling as a background action") *>
              Background[F].schedule(bgAction(fa), 1.hour)
        }

    bgAction(action)
  }

  def checkout(userId: UserId, card: Card): F[OrderId] =
    shoppingCart
      .get(userId)
      .ensure(EmptyCartError)(_.items.nonEmpty)
      .flatMap {
        case CartTotal(items, total) =>
          for {
            pid <- processPayment(domain.Payment(userId, total, card))
            order <- createOrder(userId, pid, items, total)
            _ <- shoppingCart.delete(userId).attempt.void
          } yield order
      }

}
