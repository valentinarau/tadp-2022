package tp
import types._

import scala.util.{Failure, Success, Try}

case class Taberna(misiones: List[Mision]) {
  def entrenar(equipo: Equipo): Try[Equipo] = misiones.foldLeft(Try(equipo)) {
    case (Success(eq), mision: Mision) => mision.intentar(eq)
    case (failure @ Failure(_), _) => failure
  }

  def elegirMisionPara(equipo: Equipo, predicadoEquipoMision: PredicadoEquipoMision): Option[Mision] = {
    val misionFinal = misiones match {
      case Nil => None
      case mision :: Nil => Some(mision)
      case head :: tail =>
        (head::tail).sortWith((m1, m2) => {
          (m1.intentar(equipo), m2.intentar(equipo)) match {
            case (Success(e1), Success(e2)) => predicadoEquipoMision(e1, e2)
            case (Success(_), Failure(_)) => true
            case (Failure(_), Success(_)) => false
            case _ => false
          }
        }).headOption
    }

    misionFinal.filter(_.intentar(equipo).isSuccess)
  }
}
