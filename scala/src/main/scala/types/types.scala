import tp._

package object types {
  type Restriccion = Heroe => Boolean
  type PredicadoEquipoMision = (Equipo, Equipo) => Boolean
}

