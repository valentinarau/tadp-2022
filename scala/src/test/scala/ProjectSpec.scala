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
          val heroe = Heroe(None, baseStats.copy(fuerza = 40), Inventario())
          val heroeEquipado = heroe.copy(inventario = heroe.inventario.agregarItem(cascoVikingo, heroe))
          heroeEquipado.stats().hp shouldBe 20
        }
      }
    }

  }

  "Items - Restriccion" - {
    val modificadorIdempotente: Modificador = (s: Stats, _:Heroe) => s
    val heroe = Heroe(statsBase = Stats().initializeWith(10))

    "Item equipable por heroes sin trabajo" - {
      val item = UnaMano(restriccion = SinTrabajo, modificadorIdempotente)
      item.puedeEquiparse(heroe) shouldBe(true)
      item.puedeEquiparse(heroe.especializarse(Mago)) shouldBe(false)
    }

    "Item equipable por Guerreros" - {
      val item = UnaMano(restriccion = TrabajoIgualA(Guerrero), modificadorIdempotente)
      item.puedeEquiparse(heroe.especializarse(Guerrero)) shouldBe(true)
      item.puedeEquiparse(heroe.especializarse(Mago)) shouldBe(false)
    }

    "Item equipable por Ladrones con Inteligencia > 30" - {

      val item = Talisman(restriccion = And(List(TrabajoIgualA(Ladron),StatMinimo(Inteligencia, 30))), modificadorIdempotente)
      item.puedeEquiparse(heroe.especializarse(Ladron)) shouldBe(false)
      item.puedeEquiparse(Heroe(statsBase = Stats(inteligencia = 40))) shouldBe(false)
      item.puedeEquiparse(Heroe(statsBase = Stats(inteligencia = 40)).especializarse(Ladron)) shouldBe(true)
    }

    "Item equipable por Magos o Guerreros" - {
      val espadaDelDestino = DosManos(restriccion = Or(List(TrabajoIgualA(Mago),TrabajoIgualA(Guerrero))), modificadorIdempotente)
      espadaDelDestino.puedeEquiparse(heroe) shouldBe(false)
      espadaDelDestino.puedeEquiparse(heroe.especializarse(Ladron)) shouldBe(false)
      espadaDelDestino.puedeEquiparse(heroe.especializarse(Mago)) shouldBe(true)
      espadaDelDestino.puedeEquiparse(heroe.especializarse(Guerrero)) shouldBe(true)
    }

    "Item no equipable por Magos" - {
      val pistola = UnaMano(restriccion = TrabajoDistintoDe(Mago), modificadorIdempotente)
      pistola.puedeEquiparse(heroe) shouldBe(true)
      pistola.puedeEquiparse(heroe.especializarse(Mago)) shouldBe(false)
    }
  }

  "Items - Modificador" - {
    val heroe = Heroe(statsBase = Stats())
    val cetro = UnaMano(modificador = (s:Stats, _) => s + Stats(inteligencia = 10))
    val espada = UnaMano(modificador = (s:Stats, _) => s + Stats(fuerza = 15))
    val yelmo = Torso(modificador = (s: Stats, _) => s + Stats(hp = 25))
    val sombreroDeHermes = Cabeza(modificador = (s:Stats, _) => s + Stats(velocidad = 55))
    val gemaDeLaDesolacion = Talisman(modificador = (s:Stats, _) => s + Stats().initializeWith(-50))

    "Heroe con cetro deberia tener 10 de inteligencia" - {
      heroe.equipar(cetro).stats() shouldBe Stats(inteligencia = 10)
    }

    "Heroe con espada deberia tener 15 de fuerza" - {
      heroe.equipar(espada).stats() shouldBe Stats(fuerza = 15)
    }

    "Heroe con yelmo deberia tener 25 de hp" - {
      heroe.equipar(yelmo).stats() shouldBe Stats(hp = 25)
    }

    "Heroe con el sombrero de Hermes deberia tener 55 de velocidad" - {
      heroe.equipar(sombreroDeHermes).stats() shouldBe Stats(velocidad = 55)
    }

    "Heroe con espada y sombrero de Hermes" - {
      heroe.equipar(sombreroDeHermes).equipar(espada).stats() shouldBe
        Stats(velocidad = 55, fuerza = 10)
    }

    "Heroe con todos los items" - {
      heroe
        .equipar(yelmo)
        .equipar(cetro)
        .equipar(sombreroDeHermes)
        .equipar(espada)
        .stats() shouldBe
        Stats(fuerza = 15, hp = 25, inteligencia = 10, velocidad = 55)

    }

    "Heroe con stats minimos" - {
      heroe.equipar(gemaDeLaDesolacion).stats() shouldBe Stats().initializeWith(1)
    }

  }
}
