package tp
import types._

case class Taberna(misiones: Seq[Mision]) {
  def entrenar(equipo: Equipo): Equipo = ???
  def elegirMisionPara(equipo: Equipo, predicadoEquipoMision: PredicadoEquipoMision): Mision = ???
}
