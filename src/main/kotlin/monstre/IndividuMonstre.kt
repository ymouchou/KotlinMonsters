package monstre

import kotlin.math.roundToInt
import kotlin.random.Random

import dresseur.Entraineur

class IndividuMonstre(
    var id: Int,
    var nom: String,
    var espece: EspeceMonstre,
    var entraineur: Entraineur?=null,
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
        set(value) {
            field = value

            val estNiveau1 = niveau == 1

            while (field >= palierExp(niveau)) {
                levelUp()

                if (!estNiveau1) {
                    println("Le monstre $nom est maintenant niveau $niveau !")
                }
            }
        }

    /**
     *  @property pv  Points de vie actuels.
     * Ne peut pas être inférieur à 0 ni supérieur à [pvMax].
     */
    var pv: Int = pvMax
        get() = field
        set(nouveauPv) {
            field = nouveauPv.coerceIn(0, pvMax)
        }

    init {
        this.exp = expInit // applique le setter et déclenche un éventuel level-up
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

        pvMax += (espece.modPv * potentiel).roundToInt() + (-5..5).random()

        pv += pvMax - ancienPvMax
    }

    /**
     * Attaque un autre [IndividuMonstre] et inflige des dégâts.
     *
     * Les dégâts sont calculés de manière très simple pour le moment :
     * `dégâts = attaque - (défense / 2)` (minimum 1 dégât).
     *
     * @param cible Monstre cible de l'attaque.
     */

    fun attaquer(cible: IndividuMonstre) {
        val degatBrut = this.attaque
        var degatTotal = degatBrut - (cible.defense / 2)

        if (degatTotal < 1) {
            degatTotal = 1
        }

        val pvAvant = cible.pv

        cible.pv -= degatTotal

        val pvApres = cible.pv

        println("$nom inflige ${pvAvant - pvApres} dégâts à la cible ${cible.nom}")
    }

    /**
     * Demande au joueur de renommer le monstre.
     * Si l'utilisateur entre un texte vide, le nom n'est pas modifié.
     */
    fun renommer() {
        println("Renommer $nom ?")
        val nouveauNom = readln()

        if (!nouveauNom.isNullOrEmpty()) {
            nom = nouveauNom
        }
    }

    /**
     * Affiche l'art ASCII du monstre ainsi que ses caractéristiques.
     */
    fun afficheDetail() {
        val art = espece.afficheArt()
        val artLines = art.split("\n")

        val details = listOf(
            "Nom : $nom",
            "Niveau : $niveau",
            "PV : $pv / $pvMax",
            "Attaque : $attaque",
            "Défense : $defense",
            "Vitesse : $vitesse",
            "Attaque spéciale : $attaqueSpe",
            "Défense spéciale : $defenseSpe"
        )

        println("=== $nom ===")

        for (ligne in artLines) {
            println(ligne)
        }

        println()

        for (detail in details) {
            println(detail)
        }
    }


}