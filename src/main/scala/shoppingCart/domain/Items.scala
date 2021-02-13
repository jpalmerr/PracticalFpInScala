package shoppingCart.domain

import eu.timepit.refined.string.Uuid
import shoppingCart.domain.brand._
import squants.market.Money

/**
 * GET /items
 * GET /items?brand=gibson
 * POST /items
 * PUT /items
 */

trait Items[F[_]] {
  def findAll: F[List[Item]]
  def findBy(brand: BrandName): F[List[Item]]
  def findById(itemId: ItemId): F[Option[Item]]
  def create(item: CreateItem): F[Unit]
  def update(item: UpdateItem): F[Unit]
}

case class ItemId(value: Uuid)
case class ItemName(value: String)
case class ItemDescription(value: String)

case class Item(
  uuid: ItemId,
  name: ItemName,
  description: ItemDescription,
  price: Money,
  brand: Brand,
  category: Category
)
case class CreateItem(
  name: ItemName,
  description: ItemDescription,
  price: Money,
  brandId: BrandId,
  categoryId: CategoryId
)

case class UpdateItem(
  id: ItemId,
  price: Money
)