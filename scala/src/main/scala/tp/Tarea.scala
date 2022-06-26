package tp
import scala.util.Try

trait Tarea {
  def facilidad(heroe: Heroe): Option[Heroe]
  def intentar(heroe: Heroe): Try[Heroe]
}
