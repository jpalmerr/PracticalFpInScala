package shoppingCart.domain
import eu.timepit.refined.string.Uuid
import eu.timepit.refined.types.string.NonEmptyString
import io.estatico.newtype.macros.newtype
import shoppingCart.domain.brand._

/**
 * GET /brands
 * POST /brands
 */

trait Brands[F[_]] {
  def findAll: F[List[Brand]]
  def create(name: BrandName): F[Unit]
}

object brand {
  case class BrandId(value: Uuid)

  case class BrandName(value: String) {
    def toBrand(brandId: BrandId): Brand =
      Brand(brandId, this)
  }
  case class Brand(uuid: BrandId, name: BrandName)

  @newtype case class BrandParam(value: NonEmptyString) {
    def toDomain: BrandName = BrandName(value.value.toLowerCase.capitalize)
  }
}