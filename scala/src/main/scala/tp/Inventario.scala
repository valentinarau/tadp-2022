package tp

case class Inventario(cabeza: Option[Cabeza]      = None,
                      torso: Option[Torso]        = None,
                      manoDerecha: Option[Mano]   = None,
                      manoIzquierda: Option[Mano] = None,
                      dosManos: Option[Mano]      = None,
                      talismanes: Seq[Talisman]   = Seq()) {

//  def agregarItem(item: Item, heroe: Heroe): Inventario = {
//    if (item.puedeEquiparse(heroe)) {
//      item match {
//        case UnaMano(r, m) => this.copy(izquierda = item.asInstanceOf) // Revisar implementación
//        case _ => this
//      }
//    } else this
//  }
//
//  def modificarStats(stats:Stats): Stats = List.apply(izquierda).foldRight(stats)((i,s) => i.modificaciones(s))
  def cantidadItems: Int = ???
  def agregarItem(item: Item, heroe: Heroe): Inventario = ???
  def modificar(stats: Stats): Stats = ???
}
