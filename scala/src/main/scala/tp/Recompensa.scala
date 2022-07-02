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
  override def otorgar(equipo: Equipo): Equipo = equipo.obtenerItem(item)
//  {
//    equipo.heroes.foreach(h => {
//      for {
//        trabajo <- h.trabajo
//        valor = trabajo.statPrincipal(item.modificador(Stats(), h))
//      } yield trabajo
//    })
//
//    equipo.heroes.maxByOption( h =>
//      for {
//        trabajo <- h.trabajo
//        increm = trabajo.statPrincipal(item.modificador(Stats(), h))
//      } yield increm) match {
//      case h: Heroe => h.equipar(item)
//      case _ => equipo.
//    }
//  }

}
