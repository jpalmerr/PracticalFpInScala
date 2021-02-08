# Intepreters

We would normally have two interpreters per algebra:
- one for testing
- one for doing real things.

For instance, we could have two different implementations of our Counter.

_(this is a pattern I tend to use at work)_ :-) 

```scala
@newtype case class RedisKey(value: String)

class LiveCounter[F[_]: Functor](
                                  key: RedisKey, 
                                  cmd: RedisCommands[F, String, Int]
                                ) extends Counter[F] {

  def incr: F[Unit] = cmd.incr(key).void
  def get: F[Int] = cmd.get(key).map(_.getOrElse(0))
}


class TestCounter[F[_]] (
                          ref: Ref[F, Int]
                        ) extends Counter[F] {
  
  def incr: F[Unit] = ref.update(_ + 1)
  def get: F[Int] = ref.get
}
```

- helps us encapsulate state
- allow seperation of concerns 
- intepreters can also use a concrete data type (like `IO` in test suite) or remain polymorphic 


