package tp

case class Stats(fuerza: Int, hp: Int, inteligencia: Int, velocidad: Int)

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