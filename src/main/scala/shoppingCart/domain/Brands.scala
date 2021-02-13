package shoppingCart.domain

import eu.timepit.refined.string.Uuid

/**
 * GET /brands
 * POST /brands
 */

trait Brands[F[_]] {
  def findAll: F[List[Brand]]
  def create(name: BrandName): F[Unit]
}

case class BrandId(value: Uuid)
case class BrandName(value: String)
case class Brand(uuid: BrandId, name: BrandName)