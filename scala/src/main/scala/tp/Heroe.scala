package tp

case class Heroe(trabajo: Trabajo, statsBase: Stats, inventario: Inventario) {
  def stats(): Stats = inventario.modificarStats(trabajo.modificaciones(statsBase))
  def equipar(item: Item): Heroe = copy(inventario = inventario.agregarItem(item, this))
}
