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

