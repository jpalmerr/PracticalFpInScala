package shoppingCart.domain

import squants.Money

case class Order(orderId: OrderId, paymentId: PaymentId, items: Map[Item, Int], total: Money)

case class OrderId(uuid: String)
case class PaymentId(uuid: String)
