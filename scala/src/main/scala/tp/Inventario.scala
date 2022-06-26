package tp

case class Inventario(izquierda:UnaMano) {
  def agregarItem(item: Item, heroe: Heroe): Inventario = {
    if (item.puedeEquiparse(heroe)) {
      item match {
        case UnaMano(r, m) => this.copy(izquierda = item.asInstanceOf) // Revisar implementación
        case _ => this
      }
    } else this
  }

  def modificarStats(stats:Stats): Stats = List.apply(izquierda).foldRight(stats)((i,s) => i.modificaciones(s))
}
