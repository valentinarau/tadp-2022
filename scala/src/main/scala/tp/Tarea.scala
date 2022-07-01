package tp
import scala.util.{Failure, Success, Try}

case class MisionFallidaException(tarea: Tarea) extends RuntimeException

trait Tarea {
  def facilidad(equipo: Equipo, heroe: Heroe): Option[Int]
  def intentar(heroe: Heroe): Try[Heroe]
}

case class PelearContraMonstruo(danio: Int) extends Tarea {
  override def facilidad(equipo: Equipo, heroe: Heroe): Option[Int] = equipo.trabajoDelLider match {
    case Some(Guerrero) => Some(20)
    case _ => Some(10)
  }

  override def intentar(heroe: Heroe): Try[Heroe] = {
    val heroeDaniado = if(Fuerza(heroe.stats()) < 20) heroe.modificarStats(Stats(hp = -danio)) else heroe
    heroe match {
      case _ if HP(heroeDaniado.statsBase) > 0 => Success(heroeDaniado)
      case _ => Failure(MisionFallidaException(this))
    }
  }
}

case object ForzarPuerta extends Tarea {
  override def facilidad(equipo: Equipo, heroe: Heroe): Option[Int] = Some(
    Inteligencia(heroe.stats()) + 10 * equipo.heroes.count(h => h.es(Ladron))
  )

  override def intentar(heroe: Heroe): Try[Heroe] = heroe.trabajo match {
    case Some(Ladron) | Some(Mago) => Success(heroe)
    case _ => Success(heroe.modificarStats(Stats(hp = -5, fuerza = 1)))
  }
}

case class RobarTalisman(talisman: Talisman) extends Tarea {
  override def facilidad(equipo: Equipo, heroe: Heroe): Option[Int] = equipo.trabajoDelLider match {
    case Some(Ladron) => Some(Velocidad(heroe.stats()))
    // TODO otra clase de Hero NO PUEDE realizar la tarea
  }

  override def intentar(heroe: Heroe): Try[Heroe] = Success(heroe.equipar(talisman))
}
