package tp

case class Equipo(nombre: String, pozo: Int, heroes: List[Heroe]) {
  val trabajoDelLider: Option[Trabajo] = for {
    lider <- lider()
    trabajo <- lider.trabajo
  } yield trabajo

  def lider(): Option[Heroe] = heroes.findLast(h => 1==1) // FIXME
  def incorporar(heroe: Heroe): Equipo = copy(heroes = heroe :: heroes)
  def obtenerItem(item: Item): Equipo = ???
  def mejorSegun(): Option[Heroe] = ??? // Falta parámetro de cuantificador
}
