package jeu

import monstre.IndividuMonstre

class CombatMonstre(
    var monstreJoueur: IndividuMonstre,
    var monstreSauvage: IndividuMonstre
) {

    var round: Int = 1

    /**
     * Vérifie si le joueur a perdu le combat.
     *
     * Condition de défaite :
     * - Aucun monstre de l'équipe du joueur n'a de PV > 0.
     *
     * @return true si le joueur a perdu, sinon false.
     */
    fun gameOver(): Boolean {
        return monstreJoueur.entraineur?.equipeMonstre?.none { it.pv > 0 } ?: false
    }

    fun jouerGagne(): Boolean {
        if(monstreSauvage.pv <= 0){
            println("[$joueur.$nom] a gagné")

            gainExp
        }
    }
}