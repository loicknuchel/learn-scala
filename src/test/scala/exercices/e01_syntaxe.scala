package exercices

import support.WorkshopSuite

class e01_syntaxe extends WorkshopSuite {
  exercice("e1") {
    "a" shouldBe "a"
  }

  section("s1") {
    ignore("e2") {
      "a" shouldBe __
    }
    exercice("e3") {
      val a = ""
      "a" shouldBe __
    }
    exercice("e4") {
      "a" shouldBe "a"
    }
  }

  exercice("e5") {
    "a" shouldBe "a"
  }
}
