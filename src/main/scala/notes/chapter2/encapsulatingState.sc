/**
Tip:   Our interface should know nothing about state
       (know this, hence why write HKT clients etc)
 */

trait Counter[F[_]] {
  def incr: F[Unit]
  def get: F[Int]
}

// Next, we need to define an interpreter, in this case using a Ref

import cats.effect.concurrent.Ref

class LiveCounter[F[_]] private (
                                  ref: Ref[F, Int]
                                ) extends Counter[F] {
  def incr: F[Unit] = ref.update(_ + 1) def get: F[Int] = ref.get
}

/*
Notice how we made the interpreter’s constructor private.
This has a fundamental reason:

  state shall not leak.

A best practice is to make it private and provide a smart constructor.
 */

import cats.effect.Sync
import cats.implicits._
import cats.effect.concurrent.Ref

object LiveCounter {
  def make[F[_]: Sync]: F[Counter[F]] =
    Ref.of[F, Int](0).map(new LiveCounter(_))
}

/*
It is also a good practice to return our new interface wrapped in F since its creation is effectful
 */

object LiveCounter {
  def make[F[_]: Sync]: F[Counter[F]] =
    Ref.of[F, Int](0).map { ref => new Counter[F] {
      def incr: F[Unit] = ref.update(_ + 1)
      def get: F[Int] = ref.get }
    }
}

/*
The only issue with this approach is that we can neither define local variables (unless private)
inside the implementation nor override defs as vals
  - the Scala compiler won’t allow it.

Because of this limitation, we will be defining all of our interpreters using classes.
You can always come back to it and simplify it, if possible.
 */
