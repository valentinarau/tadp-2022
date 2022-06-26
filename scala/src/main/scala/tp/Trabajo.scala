package tp

case class Trabajo(statPrincipal: Stat, modificaciones: Stats => Stats)

case object Guerrero extends Trabajo(Fuerza, (stats) => stats.copy(fuerza = stats.fuerza + 15, hp = stats.hp + 10, inteligencia = stats.inteligencia - 10))

