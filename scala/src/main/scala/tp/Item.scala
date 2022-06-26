package tp
import types._

case class Item(restricciones: Seq[Restriccion] = Seq(), modificaciones: Stats => Stats = (stats) => stats) {
  def puedeEquiparse(heroe: Heroe): Boolean = restricciones.forall(restriccion => restriccion(heroe))
}

case class UnaMano(override val restricciones: Seq[Restriccion], override val modificaciones: Stats => Stats) extends Item

case object PalitoMagico extends UnaMano(Seq(heroe => heroe.trabajo == Guerrero || (heroe.trabajo == Guerrero)),
  (stats) => stats.copy(fuerza = stats.fuerza + 15))
