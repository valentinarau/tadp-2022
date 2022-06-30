package tp
import types._

/*case class Item(restricciones: List[Restriccion] = List(), val modificaciones: Heroe => Stats = (heroe) => heroe.statsBase) {
  def puedeEquiparse(heroe: Heroe): Boolean = restricciones.forall(restriccion => restriccion(heroe))
}*/

abstract class Item(_restricciones: List[Restriccion] = List(),_modificaciones: Heroe => Stats = (heroe) => heroe.statsBase){
  def puedeEquiparse(heroe: Heroe) = restricciones.forall(restriccion => restriccion(heroe))
  def modificaciones = _modificaciones
  def restricciones = _restricciones
}

trait Mano extends Item

case class UnaMano(_restricciones: List[Restriccion] = List(),_modificaciones: Heroe => Stats) extends Mano
case class DosManos(_restricciones: List[Restriccion]= List(),_modificaciones: Heroe => Stats) extends Item
case class Cabeza(_restricciones: List[Restriccion]= List(),_modificaciones: Heroe => Stats) extends Item
case class Torso(_restricciones: List[Restriccion]= List(),_modificaciones: Heroe => Stats) extends Item
case class Talisman(_restricciones: List[Restriccion]= List(),_modificaciones: Heroe => Stats) extends Item

object Restricciones {
  def statMinimo(stat: Stat, valor: Int): Restriccion = (heroe: Heroe) => stat(heroe.stats()) > valor
  def trabajosValidos(trabajos: List[Trabajo]): Restriccion = (heroe: Heroe) => trabajos.contains(heroe.trabajo.get)
  def sinTrabajo: Restriccion = (heroe: Heroe) => heroe.trabajo.isEmpty
}
//case class Inventario(cabeza: Option[ItemCabeza], torso: Option[ItemTorso], manoDerecha: Option[ItemMano], manoIzquierda: Option[ItemMano], dosManos: Option[DosManos]) {

//case class UnaMano(derecha: Option[Item], izquierda: Option[Item]) extends Mano
/*case class UnaMano(item: Item) extends Mano
case class DosManos(item: Item) extends Mano
case class Cabeza(item: Item)
case class Torso(item: Item)
case class Talisman(item: Item)*/



//object PalitoMagico extends UnaMano(Seq(heroe => heroe.trabajo.get == Guerrero || (heroe.trabajo.get == Guerrero)),
//  (stats) => stats.copy(fuerza = stats.fuerza + 15))

//  Casco Vikingo: +10 hp, sólo lo pueden usar héroes con fuerza base > 30. Va en la cabeza.
object cascoVikingo extends Cabeza(_restricciones = List(Restricciones.statMinimo(Fuerza, 30)),
                                   _modificaciones = (heroe) => heroe.modificarStat(Stats(hp = 10)))

//  Palito mágico: +20 inteligencia, sólo lo pueden usar magos (o ladrones con más de 30 de inteligencia base). Una mano.
//object palitoMagico extends UnaMano(item = Item(restricciones = Seq(Restricciones), modificaciones = (stats) => stats.copy(hp = stats.hp + 10) ))

//  Armadura Elegante-Sport: +30 velocidad, -30 hp. Armadura.
object armaduraEleganteSport extends Torso(_modificaciones = (heroe) => heroe.modificarStat(Stats(velocidad = 30, hp = -30)))

//  Arco Viejo: +2 fuerza. Ocupa las dos manos.
object arcoViejo extends DosManos(_modificaciones = (heroe) => heroe.modificarStat(Stats(fuerza = 2)))

//  Escudo Anti-Robo: +20 hp. No pueden equiparlo los ladrones ni nadie con menos de 20 de fuerza base. Una mano.
//  Talismán de Dedicación: Todos los stats se incrementan 10% del valor del stat principal del trabajo.
//  Talismán del Minimalismo: +50 hp. -10 hp por cada otro ítem equipado.
// TODO: CHEQUEAR COMO QUEDARIA MEJOR
object talismanDelMinimalismo extends Talisman(_modificaciones = (heroe:Heroe) =>
                                               heroe.modificarStat(Stats(hp = 50))
                                               .sumar(Stats(hp = - (10 * heroe.inventario.cantidadItems))))

//  Vincha del búfalo de agua: Si el héroe tiene más fuerza que inteligencia, +30 a la inteligencia; de lo contrario +10 a todos los stats menos la inteligencia. Sólo lo pueden equipar los héroes sin trabajo. Sombrero.
object vinchaDelBufalo extends Cabeza(_restricciones = List(Restricciones.sinTrabajo),
                                      _modificaciones = (heroe) =>
                                                       if(Fuerza(heroe.statsBase) > Inteligencia(heroe.statsBase)) heroe.modificarStat(Stats(inteligencia = 30)) else heroe.modificarStat(Stats(inteligencia = 30)))

//  Talismán maldito: Todos los stats son 1.
object talismanMaldito extends Talisman(_modificaciones = (heroe) => heroe.statsBase.reset())

//  Espada de la Vida: Hace que la fuerza del héroe sea igual a su hp.