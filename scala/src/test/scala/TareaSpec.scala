import org.scalatest.matchers.should.Matchers._
import org.scalatest.freespec.AnyFreeSpec
import tp._

class TareaSpec extends AnyFreeSpec {
  val heroe = Heroe(statsBase = Stats().initializeWith(1))
  val equipo = Equipo("Los Borbotones", 0, List(heroe))

  val ladron = Heroe(Some(Ladron), Stats())

  "Tareas - Facilidad" - {

    "Pelear contra monstruo" - {
      val pelearContraMonstruo = PelearContraMonstruo(22)

      "Equipo con lider sin trabajo tiene facilidad 10" in {
        pelearContraMonstruo.facilidad(equipo, heroe) shouldBe Some(10)
      }

      "Equipo con lider Ladron tiene facilidad 10" in {
        pelearContraMonstruo.facilidad(equipo.incorporar(ladron), heroe) shouldBe Some(10)
      }

      "Equipo con lider Guerrero tiene facilidad 20" in {
        val guerrero = Heroe(Some(Guerrero), Stats())
        pelearContraMonstruo.facilidad(equipo.incorporar(guerrero), heroe) shouldBe Some(20)
      }

    }

    "Forzar puerta" - {
      def inteligenciaHeroe() = Inteligencia(heroe.stats())

      "Equipo sin ladrones tiene facilidad igual a la inteligencia del heroe" in {
        ForzarPuerta.facilidad(equipo, heroe) shouldBe Some(inteligenciaHeroe())
      }

      "Equipo con 2 ladrones tiene facilidad igual a la inteligencia del heroe + 20" in {
        ForzarPuerta.facilidad(
          equipo
            .incorporar(ladron)
            .incorporar(ladron),
          heroe) shouldBe
        Some(inteligenciaHeroe() + 20)
      }

    }

    "Robar Talisman" - {
      val robarTalisman = RobarTalisman(Talisman(modificador = (s,_) => s))

      "Equipo sin lider ladron no puede realizar la tarea" in {
        robarTalisman.facilidad(equipo, heroe) shouldBe Some(None)
      }

      "Equipo con lider Ladron tiene facilidad igual a la velocidad del heroe" in {
        robarTalisman.facilidad(equipo.incorporar(ladron), heroe) shouldBe Some(Velocidad(heroe.stats()))
      }

    }

  }
}
