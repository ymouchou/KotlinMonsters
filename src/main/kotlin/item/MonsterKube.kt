package item

import dresseur.Entraineur
import monstre.IndividuMonstre
import kotlin.random.Random

class MonsterKube(
    id: Int,
    nom: String,
    description: String,
    var chanceCapture: Double,
    var joueur: Entraineur
) : Item(id, nom, description), Utilisable {

    override fun utiliser(cible: IndividuMonstre): Boolean {
        println("Vous lancez le Monster Kube !")

        // Vérifier si le monstre appartient déjà à un entraîneur
        if (cible.entraineur != null) {
            println("Le monstre ne peut pas être capturé.")
            return false
        }

        // Calcul de la chance de capture
        val ratioVie = cible.pv.toDouble() / cible.pvMax.toDouble()

        var chanceEffective = chanceCapture * (1.5 - ratioVie)

        // Minimum de 5 %
        chanceEffective = chanceEffective.coerceAtLeast(5.0)

        // Tirage aléatoire entre 0 et 100
        val nbAleatoire = Random.nextDouble(0.0, 100.0)

        if (nbAleatoire <= chanceEffective) {
            println("Le monstre est capturé !")

            // Demander un nouveau nom
            println("Entrez un nouveau nom pour le monstre :")
            val nouveauNom = readln()

            if (nouveauNom.isNotEmpty()) {
                cible.nom = nouveauNom
            }

            // Ajouter le monstre à l'équipe ou à la boîte
            if (joueur.equipeMonstre.size >= 6) {
                joueur.boiteMonstre.add(cible)
                println("L'équipe est pleine, le monstre est envoyé dans la boîte.")
            } else {
                joueur.equipeMonstre.add(cible)
            }

            // Le monstre appartient maintenant au joueur
            cible.entraineur = joueur

            return true
        } else {
            println("Presque ! Le Kube n'a pas pu capturer le monstre.")
            return false
        }
    }
}
