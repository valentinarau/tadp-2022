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

  "Taberna" - {
    val pelearContraMonstruoFuerte = PelearContraMonstruo(1000)
    val pelearContraMonstruoDebil = PelearContraMonstruo(10)

    val equipo = Equipo(nombre = "Test", pozo = 0, heroes = List())
    val heroe = Heroe(statsBase = Stats())

    val guerrero = Heroe(Some(Guerrero), statsBase = Stats(fuerza = 10, hp = 541))
    val mago = Heroe(Some(Mago), statsBase = Stats(inteligencia = 10, hp = 541))
    val ladron = Heroe(Some(Ladron), statsBase = Stats(velocidad = 10, hp = 541))

    "Unica mision success" in {
      val equipoNuevo = equipo.incorporar(guerrero).incorporar(mago).incorporar(ladron)
      val mision = Mision(List(pelearContraMonstruoDebil), (equipo: Equipo) => equipo.copy(pozo = equipo.pozo + 100))

      Taberna(List(mision)).elegirMisionPara(equipoNuevo, (_, _) => true).isDefined shouldBe true
      Taberna(List(mision)).elegirMisionPara(equipoNuevo, (_, _) => true).get shouldBe mision
    }

    "Unica mision fallida" in {
      val equipoNuevo = equipo.incorporar(guerrero).incorporar(mago).incorporar(ladron)
      val mision = Mision(List(pelearContraMonstruoFuerte), Oro(100))
      Taberna(List(mision)).elegirMisionPara(equipoNuevo, (_, _) => true).isEmpty shouldBe true
    }

    "Dos misiones" in {
      val equipoNuevo = equipo.incorporar(guerrero).incorporar(mago).incorporar(ladron)
      val mision = Mision(List(pelearContraMonstruoDebil), Oro(100))
      val mision2 = Mision(List(pelearContraMonstruoDebil), Oro(1000))

      Taberna(List(mision, mision2)).elegirMisionPara(equipoNuevo, (e1, e2) => e1.pozo > e2.pozo).isDefined shouldBe true
      Taberna(List(mision, mision2)).elegirMisionPara(equipoNuevo, (e1, e2) => e1.pozo > e2.pozo).get shouldBe mision2
    }

    "Dos misiones falla" in {
      val equipoNuevo = equipo.incorporar(guerrero).incorporar(mago).incorporar(ladron)
      val mision = Mision(List(pelearContraMonstruoDebil), Oro(100))
      val mision2 = Mision(List(pelearContraMonstruoFuerte), Oro(1000))

      Taberna(List(mision, mision2)).elegirMisionPara(equipoNuevo, (e1, e2) => e1.pozo > e2.pozo).isDefined shouldBe true
      Taberna(List(mision, mision2)).elegirMisionPara(equipoNuevo, (e1, e2) => e1.pozo > e2.pozo).get shouldBe mision
    }

    "Sin misiones" in {
      val equipoNuevo = equipo.incorporar(guerrero).incorporar(mago).incorporar(ladron)
      Taberna(List()).elegirMisionPara(equipoNuevo, (_, _) => true).isEmpty shouldBe true
    }

    "Entrenar sale bien" in {
      val equipoNuevo = equipo.incorporar(guerrero).incorporar(mago).incorporar(ladron)
      val mision = Mision(List(pelearContraMonstruoDebil), Oro(100))
      val mision2 = Mision(List(pelearContraMonstruoDebil), Oro(1000))

      Taberna(List(mision,mision2)).entrenar(equipoNuevo).isSuccess shouldBe true
    }

    "Entrenar male sal" in {
      val equipoNuevo = equipo.incorporar(mago)
      val mision = Mision(List(pelearContraMonstruoFuerte), Oro(100))
      val mision2 = Mision(List(pelearContraMonstruoFuerte), Oro(1000))

      Taberna(List(mision,mision2)).entrenar(equipoNuevo).isSuccess shouldBe false
    }

    "Entrenar male sal con varios heroes" in {
      val equipoNuevo = equipo.incorporar(mago).incorporar(guerrero).incorporar(ladron)
      val mision = Mision(List(pelearContraMonstruoFuerte), Oro(100))
      val mision2 = Mision(List(pelearContraMonstruoFuerte), Oro(1000))

      Taberna(List(mision, mision2)).entrenar(equipoNuevo).isSuccess shouldBe false
    }
  }
}
