package jeu

import dresseur.Entraineur
import monstre.EspeceMonstre
import monstre.IndividuMonstre
import monde.Zone

/**
 * Représente une partie en cours dans le jeu.
 * Note : 2 objets parties peuvent exister dans le même jeu.
 *
 * @property id L'identifiant unique de la partie.
 * @property joueur L'entraîneur qui joue la partie.
 * @property zone La zone où se situe actuellement le joueur.
 */
class Partie(
    var id: Int,
    var joueur: Entraineur,
    var zone: Zone
) {

    /**
     * Crée et propose 3 monstres starters (Springleaf, Flamkip, Aquamy) au joueur.
     * Le joueur en choisit un, peut le renommer, et le monstre est ajouté à son équipe.
     */
    fun choixStarter(
        especeSpringleaf: EspeceMonstre,
        especeFlamkip: EspeceMonstre,
        especeAquamy: EspeceMonstre
    ) {
        val monstre1 = IndividuMonstre(id = 1, nom = "Springleaf", espece = especeSpringleaf, expInit = 0.0)
        val monstre2 = IndividuMonstre(id = 2, nom = "Flamkip", espece = especeFlamkip, expInit = 0.0)
        val monstre3 = IndividuMonstre(id = 3, nom = "Aquamy", espece = especeAquamy, expInit = 0.0)

        var choixSelection: Int?

        do {
            println("\n=== Choisissez votre starter ===")
            println("--- 1. Détails de Springleaf ---")
            monstre1.afficheDetail()

            println("\n--- 2. Détails de Flamkip ---")
            monstre2.afficheDetail()

            println("\n--- 3. Détails d'Aquamy ---")
            monstre3.afficheDetail()

            print("\nEntrez votre choix (1, 2 ou 3) : ")
            choixSelection = readln().toIntOrNull()

            if (choixSelection !in 1..3) {
                println("Choix invalide. Veuillez réessayer.")
            }
        } while (choixSelection !in 1..3)

        val starter = when (choixSelection) {
            1 -> monstre1
            2 -> monstre2
            else -> monstre3
        }

        starter.renommer()

        joueur.equipeMonstre.add(starter)

        starter.entraineur = joueur

        println("${starter.nom} a rejoint l'équipe de ${joueur.nom} !")
    }


    /**
     * Permet d'inverser la position de deux monstres dans l'équipe du joueur.
     */
    fun modifierOrdreEquipe() {
        if (joueur.equipeMonstre.size <= 1) {
            println("Impossible de modifier l'ordre : il faut au moins 2 monstres dans l'équipe.")
            return
        }

        println("=== Votre équipe actuelle ===")
        for ((index, monstre) in joueur.equipeMonstre.withIndex()) {
            println("[$index] ${monstre.nom} (Niveau ${monstre.niveau})")
        }

        print("Entrez l'index du monstre à déplacer : ")
        val pos1 = readln().toIntOrNull()

        print("Entrez la nouvelle index cible : ")
        val pos2 = readln().toIntOrNull()

        if (pos1 != null && pos2 != null &&
            pos1 in joueur.equipeMonstre.indices &&
            pos2 in joueur.equipeMonstre.indices) {

            val temp = joueur.equipeMonstre[pos1]
            joueur.equipeMonstre[pos1] = joueur.equipeMonstre[pos2]
            joueur.equipeMonstre[pos2] = temp

            println("L'ordre de l'équipe a été modifié avec succès !")
        } else {
            println("Erreur : Les emplacements sélectionnés sont invalides ou vides.")
        }
    }


    /**
     * Affiche les informations de l'équipe du joueur, permet de voir les détails d'un monstre,
     * de modifier l'ordre ou de quitter.
     */
    fun examineEquipe() {
        while (true) {
            println("\n=== Équipe de ${joueur.nom} ===")
            if (joueur.equipeMonstre.isEmpty()) {
                println("Votre équipe est vide.")
                return
            }

            for ((index, monstre) in joueur.equipeMonstre.withIndex()) {
                println("[$index] ${monstre.nom} - Niveau : ${monstre.niveau} - PV : ${monstre.pv}/${monstre.pvMax}")
            }

            println("\nOptions :")
            println("- Tapez le **numéro** du monstre pour voir ses détails.")
            println("- Tapez **'m'** pour modifier l'ordre des monstres.")
            println("- Tapez **'q'** pour retourner au menu principal.")
            print("Votre choix : ")

            val saisie = readln().trim()

            when (saisie.lowercase()) {
                "q" -> {
                    return
                }
                "m" -> {
                    modifierOrdreEquipe()
                }
                else -> {
                    val indexMonstre = saisie.toIntOrNull()
                    if (indexMonstre != null && indexMonstre in joueur.equipeMonstre.indices) {
                        val monstreChoisi = joueur.equipeMonstre[indexMonstre]
                        println("\n--- Détails du monstre ---")
                        monstreChoisi.afficheDetail()
                    } else {
                        println("Choix invalide. Veuillez réessayer.")
                    }
                }
            }
        }
    }


    /**
     * Lance la boucle principale de jeu pour la partie en cours.
     * Indique la zone actuelle et propose les actions possibles.
     */
    fun jouer() {
        while (true) {
            println("\n========================================")
            println("Vous êtes actuellement dans la zone : ${zone.nom}")
            println("========================================")
            println("Actions possibles :")
            println("1 => Rencontrer un monstre sauvage")
            println("2 => Examiner l'équipe de monstres")
            println("3 => Aller à la zone suivante")
            println("4 => Aller à la zone précédente")
            println("5 => Quitter la partie")
            print("Votre choix : ")

            val choix = readln().toIntOrNull()

            when (choix) {
                1 -> {
                    // Rencontrer un monstre sauvage dans la zone actuelle
                    zone.rencontreMonstre(joueur)
                }
                2 -> {
                    // Examiner l'équipe de monstres
                    examineEquipe()
                }
                3 -> {
                    // Aller à la zone suivante
                    val suivante = zone.zoneSuivante
                    if (suivante != null) {
                        zone = suivante
                        println("Vous vous déplacez vers : ${zone.nom}")
                    } else {
                        println("Impossible : Il n'y a pas de zone suivante.")
                    }
                }
                4 -> {
                    // Aller à la zone précédente
                    val precedente = zone.zonePrecedente
                    if (precedente != null) {
                        zone = precedente
                        println("Vous retournez vers : ${zone.nom}")
                    } else {
                        println("Impossible : Il n'y a pas de zone précédente.")
                    }
                }
                5 -> {
                    println("Fin de la partie. À bientôt !")
                    break
                }
                else -> {
                    println("Choix invalide. Veuillez réessayer.")
                }
            }
        }
    }
}