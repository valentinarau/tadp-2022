package tp

import scala.util.Try

case class Mision(tareas: Seq[Tarea], recompensa: Recompensa) {
  def intentar(equipo: Equipo): Try[Equipo] = ???
}
