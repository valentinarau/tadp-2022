package tp

case class Stats(fuerza: Int = 0, hp: Int = 0, inteligencia: Int = 0, velocidad: Int = 0) {
  def +(stats: Stats): Stats = copy(
    fuerza       = fuerza + stats.fuerza,
    hp           = hp + stats.hp,
    inteligencia = inteligencia + stats.inteligencia,
    velocidad    = velocidad + stats.velocidad,
  )

  def initializeWith(value: Int): Stats = copy(
    fuerza       = value,
    hp           = value,
    inteligencia = value,
    velocidad    = value,
  )

  def refinar(): Stats = copy(
    fuerza       = Math.max(1, fuerza),
    hp           = Math.max(1, hp),
    inteligencia = Math.max(1, inteligencia),
    velocidad    = Math.max(1, velocidad),
  )
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
