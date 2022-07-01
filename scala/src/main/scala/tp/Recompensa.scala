package tp

trait Recompensa {
 def otorgar(equipo: Equipo): Equipo
}

case class NuevoHeroe(heroe: Heroe) extends Recompensa {
  override def otorgar(equipo: Equipo): Equipo = equipo.incorporar(heroe)
}

case class Oro(monto: Int) extends Recompensa {
  override def otorgar(equipo: Equipo): Equipo = equipo.copy(pozo = equipo.pozo + monto)
}

case class NuevoItem(item: Item) extends Recompensa {
  override def otorgar(equipo: Equipo): Equipo = ???
}
