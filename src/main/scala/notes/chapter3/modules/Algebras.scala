package notes.chapter3.modules

import notes.chapter3.clients._
import notes.chapter3.events._
import notes.chapter3.models._

trait Algebras[F[_]] {
  def users: Users[F]
  def items: Items[F]
}

trait Events[F[_]] {
  def manager: EventsManager[F]
  def publisher: EventsPublisher[F]
}
trait Clients[F[_]] {
  def kafka: KafkaClient[F]
  def http: HttpClient[F]
}

// To build our modules, we can use a smart constructor in the interface’s companion object
