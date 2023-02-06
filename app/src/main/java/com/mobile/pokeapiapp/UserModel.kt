package com.mobile.pokeapiapp

/**
 * Model do usuario
 */
data class UserModel(
    val name: String? = null,
    val favorites: List<Int>? = null
)
