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
        case c: Cabeza => copy(cabeza = Some(c))
        case dm: DosManos => copy(manoDerecha = None,manoIzquierda = None,dosManos = Some(dm))
        case m: UnaMano =>
          if(manoDerecha.isEmpty) copy(dosManos = None, manoDerecha = Some(m))
          else copy(dosManos = None, manoIzquierda = Some(m))
        case t: Talisman => copy(talismanes = t :: talismanes)
        case t: Torso => copy(torso = Some(t))
        case _ => this
      }
    } else this

  def items: List[Item] = List.apply(cabeza, torso, manoDerecha, manoIzquierda, dosManos).flatten ::: talismanes

  def modificar(heroe: Heroe): Stats = items.foldLeft(heroe.statsBase) {(stats, i) => i.modificador(stats, heroe)}

}

