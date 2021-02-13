## A server is a function

A simple HTTP server can be represented with the following function:

`Request => Response`

However, we commonly need to perform an effectful operation, so we need something more

`Request => F[Response]`

In order to compose routes,
we need to model the possibility that not every single request
will have a matching route,
so we can iterate over the list of routes and try to match the next one.

`Request => F[Option[Response]]`

monad transformer...

`Request => OptionT[F, Response]`

Finally, `Kleisli`
- or also known as `ReaderT`
- is a monad transformer for functions
  
so we can replace the => with it.

`Kleisli[OptionT[F, *], Request[F], Response[F]]`

This is aliased, (the commonly used...)

```scala
HttpRoutes[F]
```

There are some cases where we need to guarantee that given a request, we can return a
response (even if it is a default one).

In such cases, we need to remove the optionality.

```scala
Kleisli[F, Request[F], Response[F]]
// aliased
HttpApp[F]
```
