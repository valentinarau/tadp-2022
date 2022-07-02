import org.scalatest.matchers.should.Matchers._
import org.scalatest.freespec.AnyFreeSpec
import tp._

class MisionSpec extends AnyFreeSpec {

  "Mision" - {
    val misionBasica = Mision(List(PelearContraMonstruo(10)), Oro(100))

    val equipoBasico = Equipo("Solitario", heroes = List(Heroe(Some(Mago), Stats(hp = 50))))

    "Equipo unitario completa mision unitaria" in {
      misionBasica.intentar(equipoBasico).isSuccess shouldBe true
      misionBasica.intentar(equipoBasico).get.lider().get.stats().hp shouldBe 40
      misionBasica.intentar(equipoBasico).get.pozo shouldBe 100
    }

    "Equipo unitario falla en mision unitaria" in {
      val mision = misionBasica.copy(tareas = List(PelearContraMonstruo(100)))
      mision.intentar(equipoBasico).isSuccess shouldBe false
    }
  }
}
