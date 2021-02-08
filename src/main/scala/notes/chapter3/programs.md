# Programs

Use these algebras to describe business logic

```scala
import cats.Apply

class ItemsProgram[F[_]: Apply](
                               counter: Counter[F],
                               items: Items[F]
                               ) {
  
  def addItem(item: Item): F[Unit] =
    items.add(item) *>
    counter.incr
}
```


- pure business logic
- holds no state (which, if there were, would be encapsulated in the interpreters)

It would work with other typeclasses e.g. Monad or Applicative; remember rule of thumb is to limit ourselves 
to adopt the least powerful typeclass that gets the job done.

Other types of proframs might be directly encoded as functions; [personal example of this style](https://github.com/jpalmerr/FootballData/blob/2ce5ddd241ae6f8b0f5848d3056eb6bbd75558ee/footballdata/src/main/scala/footballdata/MasterControlProgram.scala#L27-L35)

```scala
def program[F[_]: Console: Monad]: F[Unit] = for {
    _ <- Console[F].putStrLn("Enter your name: ")
    n <- Console[F].readLn
    _ <- Console[F].putStrLn(s"Hello $n!")
} yield ()
```

Programs composed of other programs: 
```scala
class BiggerProgram[F[_]: Console: Monad](
                                           items: ItemsProgram[F], 
                                           counter: Counter[F]
                                         ) {
def logic(item: Item): F[Unit] = for {
      _ <- items.addItem(item)
      c <- counter.get
      _ <- Console[F].putStrLn(s"Number of items: $c")
} yield () }
```

