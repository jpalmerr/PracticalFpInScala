package shoppingCart.domain

case class User(userId: UserId, username: String, password: String) // todo

case class UserId(uuid: String)