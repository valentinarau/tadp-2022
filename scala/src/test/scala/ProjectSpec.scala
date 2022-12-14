import org.scalatest.matchers.should.Matchers._
import org.scalatest.freespec.AnyFreeSpec
import tp._

class ProjectSpec extends AnyFreeSpec {

  "Trabajos" - {
    val baseStats = Stats()

    "Un Heroe sin trabajo, e inventario vacío" - {
      "debería no tener trabajo" in {
        val heroe = Heroe(statsBase = baseStats, inventario = Inventario())
        heroe.trabajo shouldBe None
      }
    }

    "Un Heroe con trabajo de Guerrero, e inventario vacío" - {
      "su trabajo debería tener como stat principal a la Fuerza" in {
        val heroe = Heroe(Some(Guerrero), baseStats, Inventario())
        heroe.trabajo.get.statPrincipal shouldBe Fuerza
      }

      "con inteligencia base de 20 debería tener una inteligencia de 10" in {
        val stats = baseStats.copy(inteligencia = 20)
        Heroe(Some(Guerrero), stats, Inventario()).stats().inteligencia shouldBe 10
      }

      "con inteligencia base de 10, debería tener una inteligencia de 1" in {
        val stats = baseStats.copy(inteligencia = 10)
        Heroe(Some(Guerrero), stats, Inventario()).stats().inteligencia shouldBe 1
      }

      "con fuerza base de 15, debería tener una fuerza de 30" in {
        val stats = baseStats.copy(fuerza = 15)
        Heroe(Some(Guerrero), stats, Inventario()).stats().fuerza shouldBe 30
      }

      "con velocidad base de 40, no debería variar y debería tener una velocidad de 40" in {
        val stats = baseStats.copy(velocidad = 40)
        Heroe(Some(Guerrero), stats, Inventario()).stats().velocidad shouldBe 40
      }
    }

    "Un Heroe con trabajo de Mago, e inventario vacío" - {
      "su trabajo debería tener como stat principal a la Inteligencia" in {
        val heroe = Heroe(Some(Mago), baseStats, Inventario())
        heroe.trabajo.get.statPrincipal shouldBe Inteligencia
      }
    }

    "Un Heroe con trabajo de Ladrón, e inventario vacío" - {
      "su trabajo debería tener como stat principal a la Velocidad" in {
        val heroe = Heroe(Some(Ladron), baseStats, Inventario())
        heroe.trabajo.get.statPrincipal shouldBe Velocidad
      }

      "con velocidad base de 40, debería tener una velocidad de 50" in {
        val stats = baseStats.copy(velocidad = 40)
        Heroe(Some(Ladron), stats, Inventario()).stats().velocidad shouldBe 50
      }

      "con HP base de 15, debería tener una HP de 10" in {
        val stats = baseStats.copy(hp = 15)
        Heroe(Some(Ladron), stats, Inventario()).stats().hp shouldBe 10
      }

      "con Fuerza base de 70, no debería variar y debería tener una Fuerza de 70" in {
        val stats = baseStats.copy(fuerza = 70)
        Heroe(Some(Ladron), stats, Inventario()).stats().fuerza shouldBe 70
      }
    }
  }

  "Inventario" - {
    val baseStats = Stats()

    "Un Heroe sin trabajo, e inventario vacío" - {
      "debería tener un inventario vacío" in {
        val heroe = Heroe(None, baseStats, Inventario())
        heroe.inventario.items shouldBe empty
      }
    }

    "Un Heroe sin trabajo" - {
      "con Fuerza base de 30, no debería poder equiparse con un Casco Vikingo" in {
        val heroe = Heroe(None, baseStats.copy(fuerza = 30), Inventario())
        heroe.inventario.agregarItem(cascoVikingo, heroe).items contains cascoVikingo shouldBe false
      }

      "con Fuerza base de 40 y HP de 10, e inventario inicial vacío, al equiparse con un Casco Vikingo" - {
        "debería tener HP de 20" in {
          val heroe = Heroe(None, baseStats.copy(fuerza = 40, hp = 10), Inventario())
          val heroeEquipado = heroe.copy(inventario = heroe.inventario.agregarItem(cascoVikingo, heroe))
          heroeEquipado.stats().hp shouldBe 20
        }
      }
    }

  }

  "Items - Restriccion" - {
    val modificadorIdempotente: Modificador = (s: Stats, _:Heroe) => s
    val heroe = Heroe(statsBase = Stats().initializeWith(10))

    "Item equipable por heroes sin trabajo" in {
      val item = UnaMano(restriccion = SinTrabajo, modificadorIdempotente)
      item.puedeEquiparse(heroe) shouldBe(true)
      item.puedeEquiparse(heroe.especializarse(Mago)) shouldBe(false)
    }

    "Item equipable por Guerreros" in {
      val item = UnaMano(restriccion = TrabajoIgualA(Guerrero), modificadorIdempotente)
      item.puedeEquiparse(heroe.especializarse(Guerrero)) shouldBe(true)
      item.puedeEquiparse(heroe.especializarse(Mago)) shouldBe(false)
    }

    "Item equipable por Ladrones con Inteligencia > 30" in {

      val item = Talisman(restriccion = And(List(TrabajoIgualA(Ladron),StatMinimo(Inteligencia, 30))), modificadorIdempotente)
      item.puedeEquiparse(heroe.especializarse(Ladron)) shouldBe(false)
      item.puedeEquiparse(Heroe(statsBase = Stats(inteligencia = 40))) shouldBe(false)
      item.puedeEquiparse(Heroe(statsBase = Stats(inteligencia = 40)).especializarse(Ladron)) shouldBe(true)
    }

    "Item equipable por Magos o Guerreros" in {
      val espadaDelDestino = DosManos(restriccion = Or(List(TrabajoIgualA(Mago),TrabajoIgualA(Guerrero))), modificadorIdempotente)
      espadaDelDestino.puedeEquiparse(heroe) shouldBe(false)
      espadaDelDestino.puedeEquiparse(heroe.especializarse(Ladron)) shouldBe(false)
      espadaDelDestino.puedeEquiparse(heroe.especializarse(Mago)) shouldBe(true)
      espadaDelDestino.puedeEquiparse(heroe.especializarse(Guerrero)) shouldBe(true)
    }

    "Item no equipable por Magos" in {
      val pistola = UnaMano(restriccion = TrabajoDistintoDe(Mago), modificadorIdempotente)
      pistola.puedeEquiparse(heroe) shouldBe(true)
      pistola.puedeEquiparse(heroe.especializarse(Mago)) shouldBe(false)
    }
  }

  "Items - Modificador" - {
    val heroe = Heroe(statsBase = Stats())
    val stats = Stats().initializeWith(1)
    val cetro = UnaMano(modificador = (s:Stats, _) => s + Stats(inteligencia = 10))
    val espada = UnaMano(modificador = (s:Stats, _) => s + Stats(fuerza = 15))
    val yelmo = Torso(modificador = (s: Stats, _) => s + Stats(hp = 25))
    val sombreroDeHermes = Cabeza(modificador = (s:Stats, _) => s + Stats(velocidad = 55))
    val gemaDeLaDesolacion = Talisman(modificador = (s:Stats, _) => s + Stats().initializeWith(-50))

    "Heroe con cetro deberia tener 10 de inteligencia" in {
      heroe.equipar(cetro).stats() shouldBe stats.copy(inteligencia = 10)
    }

    "Heroe con espada deberia tener 15 de fuerza" in {
      heroe.equipar(espada).stats() shouldBe stats.copy(fuerza = 15)
    }

    "Heroe con yelmo deberia tener 25 de hp" in {
      heroe.equipar(yelmo).stats() shouldBe stats.copy(hp = 25)
    }

    "Heroe con el sombrero de Hermes deberia tener 55 de velocidad" in {
      heroe.equipar(sombreroDeHermes).stats() shouldBe stats.copy(velocidad = 55)
    }

    "Heroe con espada y sombrero de Hermes" in {
      heroe.equipar(sombreroDeHermes).equipar(espada).stats() shouldBe
        stats.copy(velocidad = 55, fuerza = 15)
    }

    "Heroe con todos los items" in {
      heroe
        .equipar(yelmo)
        .equipar(cetro)
        .equipar(sombreroDeHermes)
        .equipar(espada)
        .stats() shouldBe
        Stats(fuerza = 15, hp = 25, inteligencia = 10, velocidad = 55)

    }

    "Heroe con stats minimos" in {
      heroe.equipar(gemaDeLaDesolacion).stats() shouldBe stats
    }

  }

  "Equipo" - {
    val equipo = Equipo(nombre = "Test", pozo = 0, heroes = List())
    val heroe = Heroe(statsBase = Stats())

    val guerrero = Heroe(Some(Guerrero), statsBase = Stats(fuerza = 10))
    val mago = Heroe(Some(Mago), statsBase = Stats(inteligencia = 10))
    val ladron = Heroe(Some(Ladron), statsBase = Stats(velocidad = 10))

    "Un equipo sin lider definido" in {
      val equipoNuevo = equipo.incorporar(guerrero).incorporar(guerrero)
      equipoNuevo.lider() shouldBe None
    }

    "Un equipo con lider definido" in {
      val equipoNuevo = equipo.incorporar(guerrero).incorporar(mago).incorporar(ladron)
      equipoNuevo.lider().isDefined shouldBe true
      equipoNuevo.lider().get shouldBe mago
    }

    "Al incorporar un Heroe a un Equipo, debería pertenecer a éste" in {
        equipo.incorporar(heroe).heroes.contains(heroe) shouldBe true
    }

    "Reemplazar un Heroe de un Equipo" in {
      equipo.incorporar(heroe).reemplazar(heroe, heroe.copy(statsBase = Stats(hp = 10))).heroes.contains(heroe) shouldBe false
    }

    "Mejor heroe según stat principal" in {
      val equipoNuevo = equipo.incorporar(ladron).incorporar(mago).incorporar(guerrero)
      equipoNuevo.mejorSegun(h => h.valorStatPrincipal().get).get shouldBe mago
    }

    "Mejor heroe por facilidad" in {
      val equipoNuevo = equipo.incorporar(ladron).incorporar(guerrero).incorporar(mago)
      equipoNuevo.mejorHeroePorFacilidad(ForzarPuerta).get shouldBe mago
    }

    "Equipo con Heroe intenta tarea" in {
      val equipoNuevo = equipo.incorporar(guerrero)
      equipoNuevo.intentar(PelearContraMonstruo(1)).isSuccess shouldBe true
    }

    "Equipo con Heroe intenta tarea fallida" in {
      val equipoNuevo = equipo.incorporar(mago)
      equipoNuevo.intentar(PelearContraMonstruo(50)).isSuccess shouldBe false
    }

    "Equipo vacío intenta tarea" in {
      equipo.intentar(PelearContraMonstruo(1)).isSuccess shouldBe false
    }

    "Obtener item que no resulta positivo" in {
      val equipoNuevo = equipo.incorporar(ladron).incorporar(mago).incorporar(guerrero)
      equipoNuevo.obtenerItem(talismanMaldito).pozo shouldBe 500
    }

    "Obtener item que resulta positivo" in {
      val equipoNuevo = equipo.incorporar(ladron).incorporar(guerrero)
      equipoNuevo.obtenerItem(armaduraEleganteSport).heroes.find(_.es(Ladron)).get.inventario.torso.get shouldBe armaduraEleganteSport
    }
  }
}
