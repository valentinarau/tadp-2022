package tp

import scala.util.{Failure, Success, Try}

case class Mision(tareas: List[Tarea], recompensa: Equipo => Equipo) {
  def intentar(equipo: Equipo): Try[Equipo] = {
    tareas.foldLeft(Try(equipo)) {
      case (Success(eq), t: Tarea) => eq.intentar(t)
      case (failure@Failure(_), _) => failure
    } match {
      case Success(equipo) => Success(recompensa(equipo))
      case failure @ Failure(_) => failure
    }
  }
}
