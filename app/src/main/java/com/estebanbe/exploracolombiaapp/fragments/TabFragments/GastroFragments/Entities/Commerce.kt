package com.estebanbe.exploracolombiaapp.fragments.TabFragments.GastroFragments.Entities

data class Commerce(
    var uid: String,
    var name: String? = null,
    var commerceType : String? = null,
    var description: String? = null,
    var isOpen: Boolean? = null,
    var minPrice: Int? = null,
    var maxPrice: Int? = null,
    var time: String? = null,
    var imageUrl: String = ""
) {
    constructor() : this("", "", "", "",null, null, null, "", "")

}
