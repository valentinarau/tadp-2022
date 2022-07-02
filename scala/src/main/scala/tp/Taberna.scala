package tp
import types._

import scala.util.{Failure, Success}

case class Taberna(misiones: List[Mision]) {
  def entrenar(equipo: Equipo): Equipo = ???

  def elegirMisionPara(equipo: Equipo, predicadoEquipoMision: PredicadoEquipoMision): Option[Mision] = {
    misiones.sortWith((m1, m2) =>  {
      (m1.intentar(equipo), m2.intentar(equipo)) match {
        case (Success(e1), Success(e2)) => predicadoEquipoMision(e1,e2)
        case (Success(_), Failure(_)) => true
        case (Failure(_), Success(_)) => false
        case _ => false
      }
    }).headOption.filter(_.intentar(equipo).isSuccess)
  }
}
