package tp

case class Trabajo(statPrincipal: Stat, modificaciones: Stats => Stats)

object Guerrero extends Trabajo(Fuerza, (stats) => stats.sumar(Stats(fuerza = 15, hp = 10, inteligencia = -10)))
object Mago extends Trabajo(Inteligencia, (stats) => stats.sumar(Stats(fuerza = -20, inteligencia = 20)))
object Ladron extends Trabajo(Velocidad, (stats) => stats.sumar(Stats(velocidad = 10, hp = -5)))
