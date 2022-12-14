package tp

case class Heroe(trabajo: Option[Trabajo] = None, statsBase: Stats, inventario: Inventario = Inventario()) {

  def stats(): Stats = statsSinRefinar().refinar()

  def statsSinRefinar(): Stats = trabajo.foldLeft(inventario
                                        .modificar(this)) { (s, t) => t.modificar(s) }

  def modificarStats(stat: Stats): Heroe = copy(statsBase = statsBase + stat)

  def equipar(item: Item): Heroe = copy(inventario = inventario.agregarItem(item, this))

  def especializarse(trabajo: Trabajo): Heroe = copy(trabajo = Some(trabajo))

  def es(t: Trabajo): Boolean = trabajo.contains(t)

  def valorStatPrincipal(): Option[Int] = for {trabajo <- trabajo} yield trabajo.statPrincipal(stats())
}
