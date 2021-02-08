package notes.chapter4

import eu.timepit.refined.string.Uuid
import squants.market.Money

import scala.util.control.NoStackTrace

/**
 * GET /orders
 * GET /orders/{orderId}
 */

trait Orders[F[_]] {
  def get(userId: UserId, orderId: OrderId): F[Option[Order]]
  def findBy(userId: UserId): F[List[Order]]
  def create(userId: UserId, paymentId: PaymentId, items: List[CartItem], total: Money): F[OrderId]
}

case class OrderId(uuid: Uuid)
case class PaymentId(uuid: Uuid)
case class Order(id: OrderId, pid: PaymentId, items: Map[ItemId, Quantity], total: Money)


case object EmptyCartError extends NoStackTrace
case class OrderError(cause: String) extends NoStackTrace
case class PaymentError(cause: String) extends NoStackTrace