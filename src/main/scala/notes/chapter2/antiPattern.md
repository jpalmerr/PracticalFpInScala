# Anti-Patterns

## Seq: a base trait for sequences

```
Thou shall not use Seq in your interface
```

Seq is a generic representation of collection-like data structures, defined in the standard library.
It is so generic that List, Vector, and Stream share it as a parent interface.
This is problematic, since these types are completely different.

```scala
trait Items[F[_]] {
  def getAll: F[Seq[Item]]
}
```

Users of this interface might use it to calculate the total price of all the items.

```scala
class Program[F[_]](items: Items[F[_]]) {
def calcTotalPrice: F[BigDecimal] = items.getAll.map { seq =>
      seq.toList
        .map(_.price)
        .foldLeft(0)((acc, p) => acc + p)
}}
```

How do we know it is safe to call `toList`?

What if the Items interpreter uses a Stream (or LazyList since Scala 2.13.0) representing possibly infinite items?
It would still be compatible with our interface, yet, it will have different semantics.

## About monad transformers

```
Thou shalt not use Monad Transformers in your interface
```

```scala
trait Users[F[_]] {
def findUser(id: UUID): OptionT[F, User]
}
```

```scala
trait Users[F[_]] {
def findUser(id: UUID): F[Option[User]]
}
```

This is a common API design, sometimes taken for granted.

**Committing to a specific Monad Transformer kills compositionality for the API users.**

Say one needed an `OptionT` and another `EitherT`, we would have to call `.value`, an unnecessary wrapping.

Alternatively, **let typeclass constraints dictate what F is** capable of in your programs.
