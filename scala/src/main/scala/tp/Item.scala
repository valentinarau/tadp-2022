package tp
import types._

case class Item(restricciones: List[Restriccion] = List(), modificaciones: Heroe => Stats = (heroe) => heroe.statsBase) {
  def puedeEquiparse(heroe: Heroe): Boolean = restricciones.forall(restriccion => restriccion(heroe))
}

object Restricciones {
  def statMinimo(stat: Stat, valor: Int): Restriccion = (heroe: Heroe) => stat(heroe.statsBase) > valor
  def trabajosValidos(trabajos: List[Trabajo]): Restriccion = (heroe: Heroe) => trabajos.contains(heroe.trabajo.get)
  def sinTrabajo: Restriccion = (heroe: Heroe) => heroe.trabajo.isEmpty
}

trait Mano
case class UnaMano(derecha: Option[Item], izquierda: Option[Item]) extends Mano
case class DosManos(item: Item) extends Mano
case class Cabeza(item: Item)
case class Torso(item: Item)
case class Talisman(item: Item)

//object PalitoMagico extends UnaMano(List(heroe => heroe.trabajo.get == Guerrero || (heroe.trabajo.get == Guerrero)),
//  (stats) => stats.copy(fuerza = stats.fuerza + 15))

//  Casco Vikingo: +10 hp, sólo lo pueden usar héroes con fuerza base > 30. Va en la cabeza.
object cascoVikingo extends Cabeza(item = Item(restricciones = List(Restricciones.statMinimo(Fuerza, 30)),
                                        modificaciones = (hereo) => hereo.stats().sumar(stats = Stats(hp = 10))))

//  Palito mágico: +20 inteligencia, sólo lo pueden usar magos (o ladrones con más de 30 de inteligencia base). Una mano.
//object palitoMagico extends UnaMano(item = Item(restricciones = List(Restricciones), modificaciones = (stats) => stats.copy(hp = stats.hp + 10) ))

//  Armadura Elegante-Sport: +30 velocidad, -30 hp. Armadura.
object armaduraEleganteSport extends Torso(item = Item(modificaciones = (hereo) => hereo.stats().sumar(Stats(velocidad = 30, hp = -30))))

//  Arco Viejo: +2 fuerza. Ocupa las dos manos.
object arcoViejo extends DosManos(item = Item(modificaciones = (heroe) => heroe.stats().sumar(Stats(fuerza = 2))))

//  Escudo Anti-Robo: +20 hp. No pueden equiparlo los ladrones ni nadie con menos de 20 de fuerza base. Una mano.
//  Talismán de Dedicación: Todos los stats se incrementan 10% del valor del stat principal del trabajo.
//  Talismán del Minimalismo: +50 hp. -10 hp por cada otro ítem equipado.
object talismanDelMinimalismo extends Talisman(item =
  Item(modificaciones = (heroe) => heroe.stats()
  .sumar(Stats(hp = 50))
  .sumar(Stats(hp = - (10 * heroe.inventario.cantidadItems)))
  ))

//  Vincha del búfalo de agua: Si el héroe tiene más fuerza que inteligencia, +30 a la inteligencia; de lo contrario +10 a todos los stats menos la inteligencia. Sólo lo pueden equipar los héroes sin trabajo. Sombrero.
object vinchaDelBufalo extends Cabeza(item = Item( restricciones =
List(Restricciones.sinTrabajo)
))

//  Talismán maldito: Todos los stats son 1.
object talismanMaldito extends Talisman(item = Item(modificaciones = (heroe) => heroe.stats().reset()))

//  Espada de la Vida: Hace que la fuerza del héroe sea igual a su hp.