package tp

import types._

import scala.util.Try

case class Equipo(nombre: String, pozo: Int = 0, heroes: List[Heroe] = List()) {
  val trabajoDelLider: Option[Trabajo] = for {
    lider <- lider()
    trabajo <- lider.trabajo
  } yield trabajo

//  def lider(): Option[Heroe] = {
//    val lead = heroes.maxByOption(h => {
//      for {
//        trabajo <- h.trabajo
//      } yield trabajo.statPrincipal(h.stats())
//    })
//
//    val count = heroes.map(h => {
//      for {
//        trabajo <- h.trabajo
//      } yield trabajo.statPrincipal(h.stats())
//    }).count(v => v.contains(lead.trabajo.get.statPrincipal(lead.stats())))
//
//    if (count== 1) {
//      Some(lead)
//    } else {
//      None
//    }
//  }

  def lider(): Option[Heroe] = {
    //val ordenados = heroes.map( h => (h, h.valorStatPrincipal())).filter( p => p._2.isDefined).sortBy(p => p._2.get)
    val ordenados = (for {
      heroe <- heroes
      valorStatPrincipal <- heroe.valorStatPrincipal()
    } yield (heroe, valorStatPrincipal)).sortBy(_._2)

    ordenados match {
      case (h1, v1)::(_, v2)::_ => if(v1 > v2) Some(h1) else None
      case (h1, _)::_ => Some(h1)
      case _ => None
    }
//        for {
////          statPrincipales <- heroes.map(_.valorStatPrincipal())
//          lider <- heroes.maxByOption(h => for { statPrincipal <- h.valorStatPrincipal() } yield statPrincipal)
//          statPrincipalLider <- lider.valorStatPrincipal()
//        } yield lider
//
//    val heroesConTrabajo = heroes.filter(_.trabajo.isDefined)
//    val statsPrincipales = heroesConTrabajo.map(_.trabajo.get.statPrincipal)
//    if (statsPrincipales.size != statsPrincipales.distinct.size) {
//      None
//    } else {
//      heroesConTrabajo.maxByOption(h => h.valorStatPrincipal())
//    }
  }

  def incorporar(heroe: Heroe): Equipo = copy(heroes = heroe :: heroes)

  def reemplazar(heroeAReemplazar: Heroe, heroeNuevo: Heroe): Equipo = {
    if (heroes.isEmpty) return this
    val equipoNuevo = copy(heroes = heroes.filterNot(_ == heroeAReemplazar))
    equipoNuevo.incorporar(heroeNuevo)
  }

  def obtenerItem(item: Item): Equipo = ??? // se puede usar mejorSegun(criterio) ??

  def mejorSegun(predicado: PredicadoMejorHeroe): Option[Heroe] = heroes.maxByOption(predicado)

  def intentar(tarea: Tarea): Try[Equipo] = {
    val heroe = heroes.minBy( h => tarea.facilidad(this, h).get) // FIXME este get esta mal

    for {
      heroeAfectado <- tarea.intentar(heroe)
    } yield reemplazar(heroe, heroeAfectado)
  }
}
