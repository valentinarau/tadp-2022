package tp

case class Trabajo(statPrincipal: Stat, modificaciones: Stats)

object Guerrero extends Trabajo(Fuerza, (Stats(fuerza = 15, hp = 10, inteligencia = -10)))
object Mago extends Trabajo(Inteligencia, (Stats(fuerza = -20, inteligencia = 20)))
object Ladron extends Trabajo(Velocidad, (Stats(velocidad = 10, hp = -5)))
