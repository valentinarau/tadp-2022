package tp

import types._

import scala.util.Try

case class Equipo(nombre: String, pozo: Int = 0, heroes: List[Heroe] = List()) {
  val trabajoDelLider: Option[Trabajo] = for {
    lider <- lider()
    trabajo <- lider.trabajo
  } yield trabajo

  def lider(): Option[Heroe] = {
    val ordenados = (for {
      heroe <- heroes
      valorStatPrincipal <- heroe.valorStatPrincipal()
    } yield (heroe, valorStatPrincipal)).sortBy(_._2)

    ordenados match {
      case (h1, v1)::(_, v2)::_ => if(v1 > v2) Some(h1) else None
      case (h1, _)::_ => Some(h1)
      case _ => None
    }
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
