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
  def cantidadItems: Int = items.size

  def agregarItem(item: Item, heroe: Heroe): Inventario =
    if(item.puedeEquiparse(heroe)) {
      item match {
        case Cabeza(_, _) => this.copy(cabeza = (cabeza.map(_.copy(item.restricciones, item.modificaciones))))
        case DosManos(_, _) => this.copy(manoDerecha = None,manoIzquierda = None,dosManos = (dosManos.map(_.copy(item.restricciones, item.modificaciones))))
        case UnaMano(_, _) =>
          if(manoDerecha.isEmpty) this.copy(dosManos = None, manoDerecha = manoDerecha.map(_.copy(item.restricciones,item.modificaciones)))
          else this.copy(dosManos = None, manoIzquierda = manoDerecha.map(_.copy(item.restricciones,item.modificaciones)))
        case Talisman(_, _) => this.copy(talismanes = talismanes.:::(List(item)))
        case Torso(_, _) => this.copy(torso = (torso.map(_.copy(item.restricciones, item.modificaciones))))
        case _ => this.copy(cabeza = (cabeza.map(_.copy(item.restricciones, item.modificaciones))))
      }
    } else this

  def items: List[Item] = List.apply(cabeza, torso, manoDerecha, manoIzquierda, dosManos).flatten ::: talismanes

  def modificar(heroe: Heroe): Stats = (items.foldLeft(heroe) {(h,i) => h.copy(statsBase = i.modificaciones(h))}).statsBase

}

