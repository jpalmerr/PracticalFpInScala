package notes.chapter3.clients

trait KafkaClient[F] {}

object KafkaClient {
  def make[F]: KafkaClient[F] = ???
}
