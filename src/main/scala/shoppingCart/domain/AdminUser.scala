package shoppingCart.domain

case class AdminUser(adminUserId: AdminUserId, username: String)

case class AdminUserId(uuid: String)
