# Algebras

An algebra describes a new language (DSL) within a host language, in this case, Scala.

- tagless final encoded algebras are not a new concept
- we already saw them, referring to them as _interface with a higher kinded type_

```scala
trait Counter[F[_]] {
  def incr: F[Unit]
  def get: F[Int]
}
```

This is a _tagless final encoded algebra_ or _tagless algebra_ for short:
- a simple interface that abstracts over the effect type using a type constructor F[_]

The difference is that typeclasses should have coherent instances,
whereas tagless algebras could have many implementations,
or more commonly called interpreters.

This makes them a perfect fit for encoding business concepts.

```scala
trait Items[F[_]] {
def getAll: F[List[Item]] def add(item: Item): F[Unit]
}
```

```
A tagless final encoded algebra is merely an
interface that abstracts over the effect type.
```

If you find yourself needing to add a typeclass constraint, such as Monad, to your algebra,
what you truly need is a program.

