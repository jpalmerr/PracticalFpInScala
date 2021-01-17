package notes.chapter2

import cats.effect.Console.io.putStrLn
import cats.effect._
import cats.effect.concurrent.Semaphore
import cats.effect.implicits._
import cats.implicits._

import scala.concurrent.duration._



object SharedState extends IOApp {
  def someExpensiveTask: IO[Unit] = {
    IO.sleep(1.second) >>
      putStrLn("expensive task")>>
      someExpensiveTask
  }

  def p1(sem: Semaphore[IO]): IO[Unit] = sem.withPermit(someExpensiveTask)>> p1(sem)

  def p2(sem: Semaphore[IO]): IO[Unit] = sem.withPermit(someExpensiveTask) >> p2(sem)

  def run(args: List[String]): IO[ExitCode] = Semaphore[IO](1).flatMap { sem =>
    p1(sem).start.void *>
      p2(sem).start.void
  } *> IO.never.as(ExitCode.Success)
}

/*

Notice how both programs use the same Semaphore to control the execution of the expensive tasks.

The enclosing flatMap block is what denotes our region of sharing.
We are in control of how we share such data structure within this block.

This is one of the main reasons why all the concurrent data structures are wrapped in F when we create a new one.
(to get the monad)
 */