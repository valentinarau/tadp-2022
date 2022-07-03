import org.scalatest.matchers.should.Matchers._
import org.scalatest.freespec.AnyFreeSpec
import tp._

class TareaSpec extends AnyFreeSpec {
  val heroe = Heroe(statsBase = Stats().initializeWith(1))
  val equipo = Equipo("Los Borbotones", 0, List(heroe))

  val ladron = Heroe(Some(Ladron), Stats())

  "Tareas" - {

    "Facilidad" - {

      "Pelear contra monstruo" - {
        val pelearContraMonstruo = PelearContraMonstruo(22)

        "Equipo con lider sin trabajo tiene facilidad 10" in {
          pelearContraMonstruo.facilidad(equipo, heroe).isSuccess shouldBe true
          pelearContraMonstruo.facilidad(equipo, heroe).get shouldBe 10
        }

        "Equipo con lider Ladron tiene facilidad 10" in {
          pelearContraMonstruo.facilidad(equipo.incorporar(ladron), heroe).isSuccess shouldBe true
          pelearContraMonstruo.facilidad(equipo.incorporar(ladron), heroe).get shouldBe 10
        }

        "Equipo con lider Guerrero tiene facilidad 20" in {
          val guerrero = Heroe(Some(Guerrero), Stats())
          pelearContraMonstruo.facilidad(equipo.incorporar(guerrero), heroe).isSuccess shouldBe true
          pelearContraMonstruo.facilidad(equipo.incorporar(guerrero), heroe).get shouldBe 20
        }

      }

      "Forzar puerta" - {
        def inteligenciaHeroe() = Inteligencia(heroe.stats())

        "Equipo sin ladrones tiene facilidad igual a la inteligencia del heroe" in {
          ForzarPuerta.facilidad(equipo, heroe).isSuccess shouldBe true
          ForzarPuerta.facilidad(equipo, heroe).get shouldBe inteligenciaHeroe()
        }

        "Equipo con 2 ladrones tiene facilidad igual a la inteligencia del heroe + 20" in {
          val resultado = ForzarPuerta.facilidad(
            equipo
              .incorporar(ladron)
              .incorporar(ladron),
            heroe)
          resultado.isSuccess shouldBe true
          resultado.get shouldBe 20 + inteligenciaHeroe()
        }

      }

      "Robar Talisman" - {
        val robarTalisman = RobarTalisman(Talisman(modificador = (s, _) => s))

        "Equipo sin lider ladron no puede realizar la tarea" in {
          robarTalisman.facilidad(equipo, heroe).isSuccess shouldBe false
        }

        "Equipo con lider Ladron tiene facilidad igual a la velocidad del heroe" in {
          robarTalisman.facilidad(equipo.incorporar(ladron), heroe).isSuccess shouldBe true
          robarTalisman.facilidad(equipo.incorporar(ladron), heroe).get shouldBe Velocidad(heroe.stats())
        }

      }

    }

    "Intentar" - {
      val heroe = Heroe(statsBase = Stats(hp = 50))

      "Pelear contra monstruo" - {
        val danio = 22
        val pelearContraMonstruo = PelearContraMonstruo(danio)

        "Heroe con fuerza menor a 20 sufre danio" in {
          pelearContraMonstruo.intentar(heroe).isSuccess shouldBe true
          pelearContraMonstruo.intentar(heroe).get.stats() shouldBe heroe.stats() + Stats(hp = -danio)
        }

        "Heroe con fuerza mayor a 20 no sufre danio" in {
          val heroFuerte = heroe.modificarStats(Stats(fuerza = 50))
          pelearContraMonstruo.intentar(heroFuerte).isSuccess shouldBe true
          pelearContraMonstruo.intentar(heroFuerte).get.stats() shouldBe heroFuerte.stats()
        }
      }

      "Forzar puerta" - {
        def inteligenciaHeroe() = Inteligencia(heroe.stats())

        "Ladron no sufre danio" in {
          val ladron = heroe.especializarse(Ladron)
          ForzarPuerta.intentar(ladron).isSuccess shouldBe true
          ForzarPuerta.intentar(ladron).get.stats() shouldBe ladron.stats()
        }

        "Mago no sufre danio" in {
          val mago = heroe.especializarse(Mago)
          ForzarPuerta.intentar(mago).isSuccess shouldBe true
          ForzarPuerta.intentar(mago).get.stats() shouldBe mago.stats()
        }

        "Guerrero sufre 5 de danio y sube la fuerza en 1" in {
          val guerrero = heroe.especializarse(Guerrero)
          ForzarPuerta.intentar(guerrero).isSuccess shouldBe true
          ForzarPuerta.intentar(guerrero).get.stats() shouldBe
            guerrero.modificarStats(Stats(hp = -5, fuerza = 1)).stats()
        }

        "Heroe sin trabajo sufre 5 de danio y sube la fuerza en 1" in {
          ForzarPuerta.intentar(heroe).isSuccess shouldBe true
          ForzarPuerta.intentar(heroe).get.stats() shouldBe
            heroe.modificarStats(Stats(hp = -5, fuerza = 1)).stats()
        }
      }

      "Robar Talisman" - {
        val robarTalisman = RobarTalisman(Talisman(modificador = (s, _) => s))

        "El heroe adquiere un talisman" in {
          robarTalisman.intentar(heroe).isSuccess shouldBe true
          robarTalisman.intentar(heroe).get.inventario.talismanes.length shouldBe 1
        }

      }

    }
  }
}
