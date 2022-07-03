package tp

import types._

import scala.util.{Failure, Success, Try}

case class Equipo(nombre: String, pozo: Int = 0, heroes: List[Heroe] = List()) {
  lazy val trabajoDelLider: Option[Trabajo] = for {
    lider <- lider()
    trabajo <- lider.trabajo
  } yield trabajo

  def lider(): Option[Heroe] = {
    val ordenados = (for {
      heroe <- heroes
      valorStatPrincipal <- heroe.valorStatPrincipal()
    } yield (heroe, valorStatPrincipal)).sortBy(_._2).reverse

    ordenados match {
      case (h1, v1) :: (_, v2) :: _ => if (v1 > v2) Some(h1) else None
      case (h1, _) :: _ => Some(h1)
      case _ => None
    }
  }

  def incorporar(heroe: Heroe): Equipo = copy(heroes = heroe :: heroes)

  def reemplazar(heroeAReemplazar: Heroe, heroeNuevo: Heroe): Equipo = {
    if (heroes.isEmpty) return this
    val equipoNuevo = copy(heroes = heroes.filterNot(_ == heroeAReemplazar))
    equipoNuevo.incorporar(heroeNuevo)
  }

  def obtenerItem(item: Item): Equipo = {
    val hereosTupla = heroes.map(h => (h,  h.valorStatPrincipal(), h.equipar(item).valorStatPrincipal()))

    val diferencias = for {
      tupla <- hereosTupla
      valorEquipado <- tupla._3
      valorInicial <- tupla._2
    } yield (tupla._1, valorEquipado - valorInicial)

    val tuplaDiferencia = diferencias.maxBy(_._2)
    if(tuplaDiferencia._2 > 0)
      reemplazar(heroeAReemplazar = tuplaDiferencia._1, heroeNuevo = tuplaDiferencia._1.equipar(item))
    else
      aumentarPozoEn(item.precio)

//    (for {
//      h <- heroes
//      trabajo <- h.trabajo
//    } yield (h, trabajo.statPrincipal))
//      .sortBy(par => par._2(item.modificador(Stats(), par._1)))
//      .headOption match {
//      case Some((h: Heroe, s: Stat)) => if (s(item.modificador(Stats(), h)) > 0) reemplazar(h, h.equipar(item)) else copy(pozo = pozo + item.precio)
//      case _ => copy(pozo = pozo + item.precio)
//    }
  }

  def aumentarPozoEn(cantidad: Int): Equipo = copy(pozo = pozo + cantidad)

  def mejorSegun(predicado: PredicadoMejorHeroe): Option[Heroe] = heroes.maxByOption(predicado)

  def mejorHeroePorFacilidad(tarea: Tarea): Option[Heroe] = heroes.maxByOption(h => tarea.facilidad(this, h).toOption)

  def intentar(tarea: Tarea): Try[Equipo] = {
    val heroe = mejorHeroePorFacilidad(tarea)

    val heroeAfectado = for {
      heroe <- heroe
      heroeAfectado <- tarea.intentar(heroe).toOption
    } yield heroeAfectado

    (heroe, heroeAfectado) match {
      case (Some(h: Heroe), Some(ha: Heroe)) => Success(reemplazar(h, ha))
      case _ => Failure(NingunHereoDispuestoPara(tarea))
    }
  }
}
