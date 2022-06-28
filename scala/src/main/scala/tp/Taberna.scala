package tp
import types._

case class Taberna(misiones: List[Mision]) {
  def entrenar(equipo: Equipo): Equipo = ???
  def elegirMisionPara(equipo: Equipo, predicadoEquipoMision: PredicadoEquipoMision): Mision = ???
}
