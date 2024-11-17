package com.estebanbe.exploracolombiaapp.TabFragments.GastroFragments.Entities

data class Restaurant(
    var uid: String,
    var name: String? = null,
    var description: String? = null,
    var isOpen: Boolean? = null,
    var minPrice: Int? = null,
    var maxPrice: Int? = null,
    var time: String? = null,
    var imageUrl: String = ""
) {
    constructor() : this("", "", "", null, null, null, "", "")

    fun matchesFilter(filter : String) :Boolean{
        var boolean : Boolean = false
        return boolean
    }

    fun priceFilter(filter: String){

    }

    fun startsFilter(filter: String){

    }

    fun categoryFiler(filter: String){


    }
}
