package tp

case class Inventario(cabeza: Option[Cabeza]      = None,
                      torso: Option[Torso]        = None,
                      manoDerecha: Option[UnaMano]   = None,
                      manoIzquierda: Option[UnaMano] = None,
                      dosManos: Option[DosManos]      = None,
                      talismanes: List[Talisman]   = List()) {

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

  def items:List[Item] = List.apply(cabeza.get,torso.get,manoDerecha.get,manoIzquierda.get,dosManos.get) ++ talismanes

  def modificar(heroe: Heroe): Stats = (items.foldLeft(heroe) { (h,i) => h.copy(statsBase = i.modificar(h)) }).statsBase

}
