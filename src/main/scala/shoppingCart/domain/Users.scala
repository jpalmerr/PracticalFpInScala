package shoppingCart.domain

import eu.timepit.refined.string.Uuid

/**
 * POST /auth/users
 * POST /auth/login
 * POST /auth/logout
 */

trait Users[F[_]] {
  def find(username: UserName, password: Password): F[Option[User]]
  def create(username: UserName, password: Password): F[UserId]
}

trait Auth[F[_]] {
  def findUser(token: JwtToken): F[Option[User]]
  def newUser(username: UserName, password: Password): F[JwtToken]
  def login(username: UserName, password: Password): F[JwtToken]
  def logout(token: JwtToken, username: UserName): F[Unit]
}

case class UserId(value: Uuid)
case class UserName(value: String)
case class Password(value: String)
case class JwtToken(value: String)
case class User(id: UserId, name: UserName)