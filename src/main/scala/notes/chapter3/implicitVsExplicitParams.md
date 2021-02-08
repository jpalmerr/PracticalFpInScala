# Implicit vs explicit parameters

**Consider** 

```scala
def program[
F[_]: Cache: Console: Users: Monad
      : Parallel: Items: EventsManager
      : HttpClient: KafkaClient: EventsPublisher
]: F[Unit] = ???
```
**an anti-pattern**


```
Business logic algebras should always be passed explicitly
```

```scala
def program[
F[_]: Console: Monad: Parallel](
   users: Users[F],
   items: Items[F],
   eventsManager: EventsManager[F],
   eventsPublisher: EventsPublisher[F],
   cache: Cache[F],
   kafkaClient: KafkaClient[F],
   httpClient: HttpClient[F]
): F[Unit] = ???
```

Much better. However now we have ran into another problem: too many dependencies.

```scala
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
```
