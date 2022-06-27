import org.scalatest.matchers.should.Matchers._
import org.scalatest.freespec.{AnyFreeSpec}
import tp._

class ProjectSpec extends AnyFreeSpec {


  "Trabajos" - {
    val baseStats = Stats()

    "Un Heroe con inteligencia base de 20, con trabajado de Guerrero, e inventario vacío" - {
      "debería tener una inteligencia de 10" in {
        val stats = baseStats.copy(inteligencia = 20)
        Heroe(Some(Guerrero), stats, Inventario()).stats().inteligencia shouldBe 10
      }
    }

    "Un Heroe con inteligencia base de 10, con trabajo de Guerrero, e inventario vacío" - {
      "debería tener una inteligencia de 1" in {
        val stats = baseStats.copy(inteligencia = 10)
        Heroe(Some(Guerrero), stats, Inventario()).stats().inteligencia shouldBe 1
      }
    }

    "Un Heroe con fuerza base de 15, con trabajo de Guerrero, e inventario vacío" - {
      "debería tener una fuerza de 30" in {
        val stats = baseStats.copy(fuerza = 15)
        Heroe(Some(Guerrero), stats, Inventario()).stats().fuerza shouldBe 30
      }
    }

    "Un Heroe con velocidad base de 40, con trabajo de Guerrero, e inventario vacío" - {
      "debería tener una velocidad de 40" in {
        val stats = baseStats.copy(velocidad = 40)
        Heroe(Some(Guerrero), stats, Inventario()).stats().velocidad shouldBe 40
      }
    }
  }
}