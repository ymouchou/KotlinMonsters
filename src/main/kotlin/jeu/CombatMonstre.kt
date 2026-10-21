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
     * Permet au joueur d'effectuer une action pendant son tour (Attaquer, Objet, Changer de monstre).
     * @return true si le combat doit continuer, false sinon.
     */
    fun actionJoueur(): Boolean {
        if (gameOver()) {
            return false
        }

        val dresseur = monstreJoueur.entraineur

        println("\n=== Tour de ${dresseur?.nom ?: "Joueur"} ===")
        println("1. Attaquer")
        println("2. Utiliser un objet")
        println("3. Changer de monstre")
        print("Votre choix : ")

        val choixMenu = readln().toIntOrNull() ?: 3

        when (choixMenu) {
            1 -> {
                // Choix 1 : Attaquer
                monstreJoueur.attaquer(monstreSauvage)
            }
            2 -> {
                // Choix 2 : Utiliser un objet
                if (dresseur == null || dresseur.sacAItems.isEmpty()) {
                    println("Votre sac est vide !")
                } else {
                    println("=== Sac à items ===")
                    for ((idx, monItem) in dresseur.sacAItems.withIndex()) {
                        println("$idx : ${monItem.nom} - ${monItem.description}")
                    }
                    print("Choisissez l'index de l'objet : ")
                    val indexObjet = readln().toIntOrNull()

                    if (indexObjet != null && indexObjet in dresseur.sacAItems.indices) {
                        val objetSelectionne = dresseur.sacAItems[indexObjet]

                        if (objetSelectionne is item.Utilisable) {
                            val captureEffectuee = objetSelectionne.utiliser(monstreSauvage)
                            if (captureEffectuee) {
                                // Si le monstre est capturé, le combat s'arrête
                                return false
                            }
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
                if (dresseur == null || dresseur.equipeMonstre.isEmpty()) {
                    println("Aucun monstre disponible dans l'équipe.")
                } else {
                    println("=== Équipe de monstres (PV > 0) ===")
                    for ((idx, m) in dresseur.equipeMonstre.withIndex()) {
                        println("$idx : ${m.nom} (PV: ${m.pv}/${m.pvMax})")
                    }
                    print("Choisissez l'index du monstre : ")
                    val indexMonstre = readln().toIntOrNull()

                    if (indexMonstre != null && indexMonstre in dresseur.equipeMonstre.indices) {
                        val monstreRemplacant = dresseur.equipeMonstre[indexMonstre]

                        if (monstreRemplacant.pv <= 0) {
                            println("Impossible ! Ce monstre est KO")
                        } else {
                            println("${monstreRemplacant.nom} remplace ${monstreJoueur.nom}")
                            monstreJoueur = monstreRemplacant
                        }
                    } else {
                        println("Index invalide.")
                    }
                }
            }
            else -> {
                println("Choix invalide, tour passé.")
            }
        }

        // Par défaut, le combat continue
        return true
    }

    /**
     * Affiche les informations des monstres (niveau, PV) ainsi que leur art ASCII
     * pour le round en cours.
     */
    fun afficheCombat() {
        println("======== Début Round : $round ========")
        println("Niveau : ${monstreSauvage.niveau}")
        println("PV : ${monstreSauvage.pv} / ${monstreSauvage.pvMax}")

        println(monstreSauvage.espece.afficheArt(true))

        println(monstreJoueur.espece.afficheArt(false))

        println("Niveau : ${monstreJoueur.niveau}")
        println("PV : ${monstreJoueur.pv} / ${monstreJoueur.pvMax}")
    }

    /**
     * Fait jouer les deux monstres pour le tour en cours (le plus rapide commence).
     * Si le joueur décide de fuir, de capturer ou si le combat s'arrête, l'exécution s'interrompt.
     */
    fun jouer() {
        val joueurPlusRapide = monstreJoueur.vitesse >= monstreSauvage.vitesse

        afficheCombat()

        if (joueurPlusRapide) {
            val continuer = actionJoueur()
            if (!continuer) {
                return
            }
            actionAdversaire()
        } else {
            actionAdversaire()

            if (!gameOver()) {
                val continuer = actionJoueur()
                if (!continuer) {
                    return
                }
            }
        }
    }

    /**
     * Lance le combat et gère les rounds jusqu'à la victoire ou la défaite.
     *
     * Affiche un message de fin si le joueur perd et restaure les PV
     * de tous ses monstres.
     */
    fun lanceCombat() {
        val joueur = monstreJoueur.entraineur

        while (!gameOver() && !joueurGagne()) {
            this.jouer()
            println("======== Fin du Round : $round ========")
            round++
        }

        if (gameOver()) {
            joueur?.equipeMonstre?.forEach { monstre -> monstre.pv = monstre.pvMax }
            println("Game Over !")
        }
    }

}