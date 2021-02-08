# Shared State

To understand shared state, we need to talk about regions of sharing. These regions are denoted by a simple flatMap call.

## Regions of sharing

Say that we need two different programs to concurrently acquire a permit and perform some expensive task.
We will use a Semaphore (another concurrent data structure provided by Cats Effect) of one permit.

-- see `SharedState.md`

## Leaky State

```scala
import cats.effect.Console.io.putStrLn
import cats.effect.concurrent.Semaphore
import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._

import scala.concurrent.duration.DurationInt

/*

To illustrate this better, let’s look at what this program would look like if our shared state,
the Semaphore, wasn’t wrapped in IO (or any other abstract effect).
 */

object LeakySharedState extends IOApp {
  // global access
  val sem: Semaphore[IO] = Semaphore[IO](1).unsafeRunSync()

  def someExpensiveTask: IO[Unit] = IO.sleep(1.second) >>
  putStrLn("expensive task") >>
  someExpensiveTask


  new LaunchMissiles(sem).run // Unit
  val p1: IO[Unit] = sem.withPermit(someExpensiveTask) >> p1
  val p2: IO[Unit] = sem.withPermit(someExpensiveTask) >> p2

  def run(args: List[String]): IO[ExitCode] = p1.start.void *> p2.start.void *>
    IO.never.as(ExitCode.Success)
}
```

We have now lost our flatMap-denoted region of sharing,
and we no longer control where our data structure is being shared.
We don’t know what LaunchMissiles does internally.
