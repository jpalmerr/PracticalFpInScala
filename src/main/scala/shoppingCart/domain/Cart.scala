package shoppingCart.domain

case class Cart(cartId: CartId, items: Map[Item, Int])

case class CartId(uuid: String)