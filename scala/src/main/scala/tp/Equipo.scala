package tp

import types._

case class Equipo(nombre: String, pozo: Int = 0, heroes: List[Heroe] = List()) {
  val trabajoDelLider: Option[Trabajo] = for {
    lider <- lider()
    trabajo <- lider.trabajo
  } yield trabajo

  def lider(): Option[Heroe] = {
    //    for {
    //      statPrincipales <- heroes.map(_.valorStatPrincipal())
    //      lider <- heroes.maxByOption(h => for { statPrincipal <- h.valorStatPrincipal() } yield statPrincipal)
    //      statPrincipalLider <- lider.valorStatPrincipal()
    //    } yield if(statPrincipales.count(statPrincipalLider == _) > 1) lider else None

    val heroesConTrabajo = heroes.filter(_.trabajo.isDefined)
    val statsPrincipales = heroesConTrabajo.map(_.trabajo.get.statPrincipal)
    if (statsPrincipales.size != statsPrincipales.distinct.size) {
      None
    } else {
      heroesConTrabajo.maxByOption(h => h.valorStatPrincipal())
    }
  }

  def incorporar(heroe: Heroe): Equipo = copy(heroes = heroe :: heroes)

  def reemplazar(heroeAReemplazar: Heroe, heroeNuevo: Heroe): Equipo = {
    if (heroes.isEmpty) return this
    val equipoNuevo = copy(heroes = heroes.filterNot(_ == heroeAReemplazar))
    equipoNuevo.incorporar(heroeNuevo)
  }

  def obtenerItem(item: Item): Equipo = ???

  def mejorSegun(predicado: PredicadoMejorHeroe): Option[Heroe] = heroes.maxByOption(predicado)
}
