package monstre

import kotlin.math.roundToInt

import dresseur.Entraineur

class IndividuMonstre(
    var id: Int,
    var nom: String,
    var espece: EspeceMonstre,
    var entraineur: Entraineur?,
    var expInit: Double
){
    var niveau: Int = 1

    var attaque: Int = espece.baseAttaque + (-2..2).random()
    var defense: Int = espece.baseDefense + (-2..2).random()
    var vitesse: Int = espece.baseVitesse + (-2..2).random()
    var attaqueSpe: Int = espece.baseAttaqueSpe + (-2..2).random()
    var defenseSpe: Int = espece.baseDefenseSpe + (-2..2).random()

    var pvMax: Int = espece.basePv + (-5..5).random()

    var potentiel: Double = (5..20).random() / 10.0

    var exp: Double = 0.0

    /**
     *  @property pv  Points de vie actuels.
     * Ne peut pas être inférieur à 0 ni supérieur à [pvMax].
     */
    var pv: Int = pvMax
        get() = field
        set(nouveauPv) {
            field = nouveauPv.coerceIn(0, pvMax)
        }


    /**
     * Calcule l'expérience totale nécessaire pour atteindre un niveau donné.
     *
     * @param niveau Niveau cible.
     * @return Expérience cumulée nécessaire pour atteindre ce niveau.
     */

    fun palierExp(niveau: Int): Double {
        return 100.0 * (niveau - 1) * (niveau - 1)
    }

    fun levelUp(){
        niveau++

        attaque += (espece.modAttaque * potentiel).roundToInt() + (-2..2).random()
        defense += (espece.modDefense * potentiel).roundToInt() + (-2..2).random()
        vitesse += (espece.modVitesse * potentiel).roundToInt() + (-2..2).random()
        attaqueSpe += (espece.modAttaqueSpe * potentiel).roundToInt() + (-2..2).random()
        defenseSpe += (espece.modDefenseSpe * potentiel).roundToInt() + (-2..2).random()

        val ancienPvMax = pvMax

        pvMax += (espece.basePv * potentiel).roundToInt() + (-5..5).random()

        pv += pvMax - ancienPvMax
    }
}