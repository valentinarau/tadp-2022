package tp

case class Heroe(trabajo: Option[Trabajo] = None, statsBase: Stats, inventario: Inventario) {
//  def stats(): Stats = inventario.modificarStats(trabajo.modificaciones(statsBase))

  def stats(): Stats = inventario.modificar(this).sumar(trabajo.get.modificaciones)

  def fuerza = stats.fuerza
  def inteligencia = stats.inteligencia
  def hp = stats.hp
  def velocidad = stats.velocidad

  def fuerzaBase = statsBase.fuerza
  def inteligenciaBase = statsBase.inteligencia
  def hpBase = statsBase.hp
  def velocidadBase = statsBase.velocidad

  def modificarStat(stat: Stats) = statsBase.sumar(stat)

  def equipar(item: Item): Heroe = copy(inventario = inventario.agregarItem(item, this))
}
