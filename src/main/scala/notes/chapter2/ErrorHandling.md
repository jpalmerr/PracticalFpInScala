# Error Handling

## MonadError and ApplicativeError
(personal favourite!)

```scala
import cats.MonadError
import cats.implicits._
import scala.util.Random

trait Categories[F[_]] {
def findAll: F[List[Category]]
}

sealed trait BusinessError extends NoStackTrace
case object RandomError extends BusinessError

class LiveCategories[
  F[_]: MonadError[*[_], Throwable]: Random
] extends Categories[F] {
  def findAll: F[List[Category]] = Random[F].bool.ifM(
    List.empty[Category].pure[F],
    RandomError.raiseError[F, List[Category]]
  )
}
```

"We can handle the errors that are relevant to the business and forget about the rest."


## Either Monad

In some other cases, it is perfectly valid to use F[Either[E, A]].
Say we have a Program using Categories[F] and depending on whether it gets BusinessError or a List[Category]
the business logic changes.

(think case at work like user not found => sign up offer etc)

This is a fair case and you can see how we can, at the same time, eliminate F[Either[E, A]] and go back to F[A]:

```scala
class Program[F[_]: Functor]( categories: Categories[F]) {
  def findAll: F[List[Category]] = category.maybeFindAll.map { 
    case Right(c) => c 
    case Left(RandomError) => List.empty[Category]
  }
}
```

Big trade off is having to lift all our operations (`EitherT`)

```
when E <: Throwable, we are better off using F[A] and relying on MonadError,
which has better ergonomics at the cost of losing the error type.
```

... do we really care about error type?