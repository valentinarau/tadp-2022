package tp
import scala.math._

case class Stats(fuerza: Int = 1, hp: Int = 1, inteligencia: Int = 1, velocidad: Int = 1) {
  def sumar(stats: Stats): Stats = copy(
    fuerza       = fuerza + stats.fuerza,
    hp           = hp + stats.hp,
    inteligencia = inteligencia + stats.inteligencia,
    velocidad    = velocidad + stats.velocidad
  )

  def reset(): Stats = Stats()
}

trait Stat extends (Stats => Int)

case object Fuerza extends Stat {
  def apply(s: Stats): Int = s.fuerza
}

case object HP extends Stat {
  def apply(s: Stats): Int = s.hp
}

case object Inteligencia extends Stat {
  def apply(s: Stats): Int = s.inteligencia
}

case object Velocidad extends Stat {
  def apply(s: Stats): Int = s.velocidad
}