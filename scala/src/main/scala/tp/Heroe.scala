package tp

case class Heroe(trabajo: Option[Trabajo], statsBase: Stats, inventario: Inventario) {
//  def stats(): Stats = inventario.modificarStats(trabajo.modificaciones(statsBase))
  def stats(): Stats = ???
  def equipar(item: Item): Heroe = copy(inventario = inventario.agregarItem(item, this))
}
