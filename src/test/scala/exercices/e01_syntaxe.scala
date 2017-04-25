package exercices

import support.WorkshopSuite

class e01_syntaxe extends WorkshopSuite {
  exercice("Déclarer une variable") {
    // permet de déclarer une valeur (= constante)
    val constant: String = "abc"
    //constant = "def" // mais pas une valeur (vérifier puis laisser en commentaire)
    constant shouldBe __

    // permet de déclarer une variable (à utiliser au minimum)
    var variable: Int = 5
    variable = 42 // on peut réassigner une variable
    //variable = true // le type ne doit pas changer (vérifier puis laisser en commentaire)
    variable shouldBe __
  }

  exercice("Déclarer une fonction") {
    def add(a: Int, b: Int): Int = {
      return a + b;
    }

    add(2, 3) shouldBe __
  }
}
