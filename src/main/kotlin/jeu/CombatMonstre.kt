package jeu

import monstre.IndividuMonstre

class CombatMonstre(
    var monstreJoueur: IndividuMonstre,
    var monstreSauvage: IndividuMonstre
) {

    var round: Int = 1

    /**
     * Vérifie si le joueur a perdu le combat.
     * Condition de défaite : Aucun monstre de l'équipe du joueur n'a de PV > 0.
     * @return true si le joueur a perdu, sinon false.
     */
    fun gameOver(): Boolean {
        val equipe = monstreJoueur.entraineur?.equipeMonstre ?: return false

        for (monstre in equipe) {
            if (monstre.pv > 0) {
                return false
            }
        }
        return true
    }

    /**
     * Indique si le joueur a gagné le combat.
     * @return true si le joueur a gagné, false sinon.
     */
    fun joueurGagne(): Boolean {
        // On récupère le joueur à partir de l'entraîneur du monstre
        val joueur = monstreJoueur.entraineur

        if (monstreSauvage.pv <= 0) {
            // Si l'entraîneur est renseigné, on affiche son nom, sinon un nom par défaut
            val nomJoueur = joueur?.nom ?: "Le joueur"
            println("$nomJoueur a gagné !")

            val gainExp = monstreSauvage.exp * 0.20
            monstreJoueur.exp += gainExp
            println("${monstreJoueur.nom} gagne $gainExp exp")
            return true
        } else {
            // Vérifie si le monstre sauvage appartient désormais au joueur
            if (joueur != null && monstreSauvage.entraineur == joueur) {
                println("${monstreSauvage.nom} a été capturé !")
                return true
            } else {
                return false
            }
        }
    }

    /**
     * Exécute l'action de l'adversaire (le monstre sauvage).
     * Si le monstre sauvage a encore des PV (> 0), il attaque le monstre du joueur.
     */
    fun actionAdversaire() {
        if (monstreSauvage.pv > 0) {
            monstreSauvage.attaquer(monstreJoueur)
        }
    }


    /**
     * Permet au joueur d'effectuer une action pendant son tour.
     * @return true si le combat doit continuer, false sinon.
     */
    fun actionJoueur(): Boolean {
        // 1. Vérifier si gameOver() est vrai
        if (gameOver()) {
            return false
        }

        val joueur = monstreJoueur.entraineur

        // 2. Afficher le menu d'actions (1, 2, 3, et ajout de 4, 5)
        println("\n=== Tour de ${joueur?.nom ?: "Joueur"} ===")
        println("1. Attaquer")
        println("2. Utiliser un objet")
        println("3. Changer de monstre")
        println("4. Fuir")
        println("5. Attendre")
        print("Votre choix : ")

        val choixAction = readln().toIntOrNull() ?: 5

        when (choixAction) {
            1 -> {
                // Choix 1 : Attaquer
                monstreJoueur.attaquer(monstreSauvage)
            }
            2 -> {
                // Choix 2 : Utiliser un objet
                if (joueur == null || joueur.sacAItems.isEmpty()) {
                    println("Votre sac est vide !")
                } else {
                    println("=== Sac à items ===")
                    for ((index, item) in joueur.sacAItems.withIndex()) {
                        println("$index : ${item.nom} - ${item.description}")
                    }
                    print("Choisissez l'index de l'objet : ")
                    val indexChoix = readln().toIntOrNull()

                    if (indexChoix != null && indexChoix in joueur.sacAItems.indices) {
                        val objetChoisi = joueur.sacAItems[indexChoix]

                        // Vérifier si l'objet implémente l'interface Utilisable
                        if (objetChoisi is item.Utilisable) {
                            val captureRéussie = objetChoisi.utiliser(monstreSauvage)
                            if (captureRéussie) {
                                // Si le monstre est capturé, le combat s'arrête (renvoie false)
                                return false
                            }
                            // Sinon, continuer le combat
                        } else {
                            println("Objet non utilisable")
                        }
                    } else {
                        println("Index invalide.")
                    }
                }
            }
            3 -> {
                // Choix 3 : Changer de monstre
                if (joueur == null || joueur.equipeMonstre.isEmpty()) {
                    println("Aucun monstre disponible dans l'équipe.")
                } else {
                    println("=== Équipe de monstres (PV > 0) ===")
                    for ((index, m) in joueur.equipeMonstre.withIndex()) {
                        println("$index : ${m.nom} (PV: ${m.pv}/${m.pvMax})")
                    }
                    print("Choisissez l'index du monstre : ")
                    val indexChoix = readln().toIntOrNull()

                    if (indexChoix != null && indexChoix in joueur.equipeMonstre.indices) {
                        val choixMonstre = joueur.equipeMonstre[indexChoix]

                        if (choixMonstre.pv <= 0) {
                            println("Impossible ! Ce monstre est KO")
                        } else {
                            println("${choixMonstre.nom} remplace ${monstreJoueur.nom}")
                            monstreJoueur = choixMonstre
                        }
                    } else {
                        println("Index invalide.")
                    }
                }
            }
            4 -> {
                // Choix 4 : Fuir
                println("${joueur?.nom ?: "Le joueur"} prend la fuite ! Fin du combat.")
                return false
            }
            5 -> {
                // Choix 5 : Attendre
                println("${monstreJoueur.nom} passe son tour et attend...")
            }
            else -> {
                println("Choix invalide, tour passé.")
            }
        }

        // Par défaut, le combat continue
        return true
    }
}