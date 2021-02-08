package notes.chapter3.clients

trait HttpClient[F] {}

object HttpClient {
  def make[F]: HttpClient[F] = ???
}