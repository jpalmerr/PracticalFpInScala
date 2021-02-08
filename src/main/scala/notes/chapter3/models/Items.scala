package notes.chapter3.models

trait Items[F[_]] {
  def getAll: F[List[Item]]
  def add(item: Item): F[Unit]
}

case class Item()
