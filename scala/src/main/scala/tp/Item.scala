package tp

abstract class Item(val restriccion: Restriccion = _ => true, val modificador: Modificador = (s, _) => s){
  def puedeEquiparse(heroe: Heroe): Boolean = restriccion(heroe)
}

trait Modificador extends ((Stats, Heroe) => Stats)
trait Mano extends Item

case class UnaMano(override val restriccion: Restriccion= _ => true, override val modificador: Modificador) extends Mano
case class DosManos(override val restriccion: Restriccion= _ => true, override val modificador: Modificador) extends Item
case class Cabeza(override val restriccion: Restriccion= _ => true, override val modificador: Modificador) extends Item
case class Torso(override val restriccion: Restriccion= _ => true, override val modificador: Modificador) extends Item
case class Talisman(override val restriccion: Restriccion= _ => true, override val modificador: Modificador) extends Item

trait Restriccion extends (Heroe => Boolean)

case object SinTrabajo extends Restriccion {
  override def apply(h: Heroe): Boolean = h.trabajo.isEmpty
}

case class StatMinimo(stat: Stat, valor: Int) extends Restriccion {
  override def apply(h: Heroe): Boolean = stat(h.statsBase) > valor
}

case class TrabajoIgualA(t: Trabajo) extends Restriccion {
  override def apply(h: Heroe): Boolean = h.trabajo.contains(t)
}

case class TrabajoDistintoDe(trabajo: Trabajo) extends Restriccion {
  override def apply(h: Heroe): Boolean = !h.trabajo.contains(trabajo)
}

case class Or(restricciones: List[Restriccion]) extends Restriccion {
  override def apply(heroe: Heroe): Boolean = restricciones.exists(r => r(heroe))
}

case class And(restricciones: List[Restriccion]) extends Restriccion {
  override def apply(heroe: Heroe): Boolean = restricciones.forall(r => r(heroe))
}

//  Casco Vikingo: +10 hp, sólo lo pueden usar héroes con fuerza base > 30. Va en la cabeza.
object cascoVikingo extends Cabeza(
  restriccion = StatMinimo(Fuerza, 30),
  modificador = (stats: Stats, _) => stats + Stats(hp = 10))

//  Palito mágico: +20 inteligencia, sólo lo pueden usar magos (o ladrones con más de 30 de inteligencia base). Una mano.
object palitoMagico extends UnaMano(
  restriccion = Or(List(TrabajoIgualA(Mago), And(List(TrabajoIgualA(Ladron), StatMinimo(Inteligencia, 30))))),
  modificador = (stats:Stats, _) => stats + Stats(hp = 10))

//  Armadura Elegante-Sport: +30 velocidad, -30 hp. Armadura.
object armaduraEleganteSport extends Torso(modificador = (stats:Stats, _) => stats + Stats(velocidad = 30, hp = -30))

//  Arco Viejo: +2 fuerza. Ocupa las dos manos.
object arcoViejo extends DosManos(modificador = (stats: Stats, _) => stats + Stats(fuerza = 2))

//  Escudo Anti-Robo: +20 hp. No pueden equiparlo los ladrones ni nadie con menos de 20 de fuerza base. Una mano.
object EscudoAntiRobo extends UnaMano(
  restriccion = And(List(TrabajoDistintoDe(Ladron), StatMinimo(Fuerza, 20))),
  modificador = (stats: Stats, _) => stats + Stats(hp=20))

//  Talismán de Dedicación: Todos los stats se incrementan 10% del valor del stat principal del trabajo.
object TalismanDeDedicacion extends Talisman(
  modificador = (stats:Stats, h: Heroe) =>
    stats + h.trabajo.fold(Stats()) { t =>
      Stats().initializeWith(t.statPrincipal(h.statsBase) * 10 / 100)}
  )

//  Talismán del Minimalismo: +50 hp. -10 hp por cada otro ítem equipado.
object talismanDelMinimalismo extends Talisman(
  modificador = (stats:Stats, heroe:Heroe) => stats + Stats(hp = 50 - 10 * (heroe.inventario.cantidadItems - 1)))

//  Vincha del búfalo de agua: Si el héroe tiene más fuerza que inteligencia, +30 a la inteligencia; de lo contrario +10 a todos los stats menos la inteligencia.
//  Sólo lo pueden equipar los héroes sin trabajo. Sombrero.
object vinchaDelBufalo extends Cabeza(
  restriccion = SinTrabajo,
  modificador = (stats: Stats, heroe: Heroe) =>
    if (Fuerza(heroe.statsBase) > Inteligencia(heroe.statsBase)) stats + Stats(inteligencia = 30)
    else stats + Stats(fuerza = 10 , velocidad = 10 , hp = 10))

//  Talismán maldito: Todos los stats son 1.
object talismanMaldito extends Talisman(modificador = (_, _) => Stats().initializeWith(1))

//  Espada de la Vida: Hace que la fuerza del héroe sea igual a su hp.
object EspadaDeLaVida extends UnaMano(modificador = (stats:Stats, _) => stats.copy(fuerza = HP(stats)))
