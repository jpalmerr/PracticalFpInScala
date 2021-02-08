package notes.chapter4

import eu.timepit.refined.string.Uuid
import squants.market.Money

/**
 *  GET /cart
 *  POST /cart
 *  PUT /cart
 *  DELETE /cart/{itemId}
 */

trait ShoppingCart[F[_]] {
  def add(userId: UserId, itemId: ItemId, quantity: Quantity): F[Unit]
  def delete(userId: UserId): F[Unit]
  def get(userId: UserId): F[CartTotal]
  def removeItem(userId: UserId, itemId: ItemId): F[Unit]
  def update(userId: UserId, cart: Cart): F[Unit]
}

case class Quantity(value: Int)
case class Cart(items: Map[ItemId, Quantity])
case class CartId(value: Uuid)
case class CartItem(item: Item, quantity: Quantity)
case class CartTotal(items: List[CartItem], total: Money)