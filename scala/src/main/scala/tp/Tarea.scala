package tp
import scala.util.Try

trait Tarea {
  def facilidad(heroe: Heroe): Option[Int]
  def intentar(heroe: Heroe): Try[Heroe]
}
