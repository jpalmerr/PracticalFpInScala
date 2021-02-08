package notes.chapter4

import eu.timepit.refined.string.Uuid

/**
 * GET /categories
 * POST /categories
 */

trait Categories[F[_]] {
  def findAll: F[List[Category]]
  def create(name: CategoryName): F[Unit]
}

case class CategoryId(value: Uuid)
case class CategoryName(value: String)
case class Category(uuid: CategoryId, name: CategoryName)
