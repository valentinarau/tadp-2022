package tp

case class Equipo(nombre: String, pozo: Int, heroe: Seq[Heroe]) {
  def lider(): Option[Heroe] = ???
  def incorporar(heroe: Heroe): Equipo = ???
  def obtenerItem(item: Item): Equipo = ???
  def mejorSegun(): Option[Heroe] = ??? // Falta parámetro de cuantificador
}
