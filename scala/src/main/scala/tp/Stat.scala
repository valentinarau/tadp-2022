package tp
import scala.math._

case class Stats(fuerza: Int = 1, hp: Int = 1, inteligencia: Int = 1, velocidad: Int = 1) {
  def sumar(stats: Stats): Stats = this.copy(
    fuerza       = max(1, this.fuerza + stats.fuerza),
    hp           = max(1, this.hp + stats.hp),
    inteligencia = max(1, this.inteligencia + stats.inteligencia),
    velocidad    = max(1, this.velocidad + stats.velocidad)
  )

  def reset(): Stats = Stats()
}

trait Stat extends (Stats => Int)

case object Fuerza extends Stat {
  def apply(s: Stats): Int = s.fuerza
}

case object HP extends Stat {
  def apply(s: Stats): Int = s.fuerza
}

case object Inteligencia extends Stat {
  def apply(s: Stats): Int = s.fuerza
}

case object Velocidad extends Stat {
  def apply(s: Stats): Int = s.fuerza
}