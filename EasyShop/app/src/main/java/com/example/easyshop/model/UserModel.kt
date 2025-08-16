package com.example.easyshop.model

data class UserModel(
    val name: String="",
    val email: String="",
    val uid: String="",
    val cartItems: Map<String,Long> = emptyMap(),
    val address: String ="",
    val phone: String ="",
    val favoriteItems: List<String> = emptyList()
)
