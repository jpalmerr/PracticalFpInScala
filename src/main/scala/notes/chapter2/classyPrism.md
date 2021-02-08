classy optics, using the Meow MTL9 library.
It gives us back typed errors and exhausting pattern matching,
without polluting our interfaces with `F[Either[E, A]]` nor using monad transformers.

```scala
import scala.util.control.NoStackTrace

sealed trait UserError extends NoStackTrace
final case class UserAlreadyExists(username: String) extends UserError
final case class UserNotFound(username: String) extends UserError
final case class InvalidUserAge(age: Int) extends UserError
```
We then need to define a generic error handler for any error
that is a subtype of Throwable to be compatible with Cats Effect IO.

In this case, we are going to define an HttpErrorHandler for http4s, but it could be other kinds of error handler.

```scala
trait HttpErrorHandler[F[_], E <: Throwable] {
  def handle(routes: HttpRoutes[F]): HttpRoutes[F]
}
```

error handler to avoid repeating ourselves
```scala
abstract class RoutesHttpErrorHandler[F[_], E <: Throwable] extends HttpErrorHandler[F, E]
  with Http4sDsl[F] {
  def A: ApplicativeError[F, E]
  def handler: E => F[Response[F]]
  def handle(routes: HttpRoutes[F]): HttpRoutes[F] =
    Kleisli { req =>
      OptionT {
        A.handleErrorWith(      // handleErrorWith: Handle any error, potentially recovering from it, by mapping it to an F[A] value.
          routes.run(req).value
        )(e =>
          A.map(handler(e))(Option(_))
        ) }
    }
}
```

Finally, we define a specific error handler for UserError, making use of our generic handler.

```scala
object UserHttpErrorHandler {
  def apply[F[_]: MonadError[* , UserError]] : HttpErrorHandler[F, UserError] =
    new RoutesHttpErrorHandler[F, UserError] {
      implicit val entityEncoder: EntityEncoder[F, Json] = ???
      val A: ApplicativeError[F, UserError] = implicitly
      val handler: UserError => F[Response[F]] = { case InvalidUserAge(age) =>
        BadRequest(s"Invalid age $age".asJson) case UserAlreadyExists(username) =>
        Conflict(username.asJson) case UserNotFound(username) =>
        NotFound(username.asJson)
      }
    }
}
```

All we need to do is to require a `MonadError[F, UserError]` and define the handler function.
With all this machinery in place, we can proceed to use our error handler in our HTTP routes

```scala
val users: Users[F] = ???

val httpRoutes: HttpRoutes[F] = HttpRoutes.of { 
  case GET -> Root => Ok(users.findAll)
}

def routes(ev: HttpErrorHandler[F, UserError]): HttpRoutes[F] = ev.handle(httpRoutes)
```

Any other errors should be seen as unexpected failures that should be handled correctly by our HTTP framework.

So, `classy prisms` ...

```scala
import oleg.meow.mtl.hierarchy._

class MyRoutes[F[_]: MonadError[*[_], MyError]] { ... }
def foo[F[_]: Sync] = new MyRoutes[F] {}
```

if we have `MonadError[F, Throwable]` in scope,
the Meow MTL library can derive any other MonadError instances for us

- as long as our error type is a subtype of Throwable
