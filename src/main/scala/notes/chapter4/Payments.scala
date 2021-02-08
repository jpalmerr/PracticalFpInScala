package notes.chapter4

import notes.chapter4.checkout.Card
import squants.market.Money

//A good practice is to also define a tagless algebra for remote clients.

trait PaymentClient[F[_]] {
  def process(payment: Payment): F[PaymentId]
}

case class Payment(id: UserId, total: Money, card: Card)
