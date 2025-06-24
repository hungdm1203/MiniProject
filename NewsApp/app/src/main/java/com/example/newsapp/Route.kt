package com.example.newsapp

import kotlinx.serialization.Serializable

@Serializable
object HomePageScreen

@Serializable
data class NewArticlePageScreen(
    val url: String
)