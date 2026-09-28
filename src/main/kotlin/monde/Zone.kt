package monde

import monstre.EspeceMonstre

class Zone(
    var id: Int,
    var nom: String,
    var expZone: Int,
    var especesMonstres: MutableList<EspeceMonstre> = mutableListOf(),
    var zoneSuivante: Zone?,
    var zonePrecedente: Zone?,
){

    fun genereMonstre(){

    }

    fun rencontreMonstre(){

    }


}