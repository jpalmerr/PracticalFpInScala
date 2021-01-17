package shoppingCart.domain

sealed trait Model {
  def model: String

  override def toString: String = model
}

case object Guitar extends Model {
  override def model: String = "guitar"
}