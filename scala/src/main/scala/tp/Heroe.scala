package tp

case class Heroe(trabajo: Option[Trabajo] = None, statsBase: Stats, inventario: Inventario) {
//  def stats(): Stats = inventario.modificarStats(trabajo.modificaciones(statsBase))

  def stats(): Stats = inventario.modificar(this).sumar(trabajo.get.modificaciones)

  def modificarStat(stat: Stats): Stats = statsBase.sumar(stat)

  def equipar(item: Item): Heroe = copy(inventario = inventario.agregarItem(item, this))
}
