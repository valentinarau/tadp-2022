package tp

case class Heroe(trabajo: Option[Trabajo] = None, statsBase: Stats, inventario: Inventario = Inventario()) {

  def stats(): Stats = trabajo
    .foldLeft(inventario
      .modificar(this)) { (s, t) => t.modificar(s)}
    .refinar()

  def modificarStat(stat: Stats): Stats = statsBase + stat

  def equipar(item: Item): Heroe = copy(inventario = inventario.agregarItem(item, this))

  def especializarse(trabajo: Trabajo): Heroe = copy(trabajo= Some(trabajo))
}
