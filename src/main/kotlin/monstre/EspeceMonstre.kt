package monstre

import java.io.File

/**
 * Représente une espèce de monstre dans le contexte du jeu.
 *
 * Une espèce de monstre décrit les caractéristques de base d'un type de monstre (comme un «modèle » ou une espèce).
 * Elle contient ses statistiques de base, ses multiplicateurs de croissance, son nom, son type et éventuellement son art ASCII.
 *
 *
 * @property id L'identifiant unique de l'espèce
 * @property nom Le nom de l'espèce
 * @property type Le type de l'espèce
 * @property baseAttaque La base de l'attaque
 * @property baseDefense La base de la défense
 * @property baseVitesse La base de la vitesse
 * @property baseAttaqueSpe La base de l'attaque spéciale
 * @property baseDefenseSpe La base de la défense spéciale
 * @property basePv La base des pv
 * @property modAttaque Le mode de l'attaque
 * @property modDefense Le mode de la défense
 * @property modVitesse Le mode de la vitesse
 * @property modAttaqueSpe Le mode de l'attaque spéciale
 * @property modDefenseSpe Le mode de ma défense spéciale
 * @property modPv Le mode des pv
 * @property description La description de l'espèce
 * @property particularites Les particularités de l'espèce
 * @property caractères Les caractères de l'espèce

 */

class EspeceMonstre(
    var id : Int,
    var nom: String,
    var type: String,
    val baseAttaque: Int,
    val baseDefense: Int,
    val baseVitesse: Int,
    val baseAttaqueSpe: Int,
    val baseDefenseSpe: Int,
    val basePv: Int,
    val modAttaque: Double,
    val modDefense: Double,
    val modVitesse: Double,
    val modAttaqueSpe: Double,
    val modDefenseSpe: Double,
    val modPv: Double,
    val description: String = "",
    val particularites: String = "",
    val caractères: String = "",
){
    /**
     * Affiche la représentation artistique ASCII du monstre.
     *
     * @param deFace Détermine si l'art affiché est de face (true) ou de dos (false).
     *               La valeur par défaut est true.
     * @return Une chaîne de caractères contenant l'art ASCII du monstre avec les codes couleur ANSI.
     *         L'art est lu à partir d'un fichier texte dans le dossier resources/art.
     */
    fun afficheArt(deFace: Boolean=true): String{
        val nomFichier = if(deFace) "front" else "back";
        val art=  File("src/main/resources/art/${this.nom.lowercase()}/$nomFichier.txt").readText()
        val safeArt = art.replace("/", "∕")
        return safeArt.replace("\\u001B", "\u001B")
    }

}