package shoppingCart.domain

import squants.Money

import java.util.Currency._
//import eu.timepit.refined.string.Uuid

case class Item(itemId: ItemId, model: Model, brand: Brand, description: String, price: Money)

case class ItemId(uuid: String)
