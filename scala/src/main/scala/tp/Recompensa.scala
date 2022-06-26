package tp

trait Recompensa {
 def otorgar(equipo: Equipo): Equipo
}

case class NuevoHeroe(heroe: Heroe) extends Recompensa {
  override def otorgar(equipo: Equipo): Equipo = ???
}

case class Oro(monto: Int) extends Recompensa {
  override def otorgar(equipo: Equipo): Equipo = ???
}

case class NuevoItem(item: Item) extends Recompensa {
  override def otorgar(equipo: Equipo): Equipo = ???
}
