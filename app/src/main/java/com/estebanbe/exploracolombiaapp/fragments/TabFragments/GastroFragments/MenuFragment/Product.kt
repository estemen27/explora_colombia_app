package com.estebanbe.exploracolombiaapp.fragments.TabFragments.GastroFragments.MenuFragment

data class Product(
    val productUid: String = "",
    val category: String = "",
    val commerceUid: String = "",
    val imageUrl: String = "",
    val description: String = "",
    val redentionPoints: Int = 0,
    val state: String = "",
    val price: Float = 0.0f
)