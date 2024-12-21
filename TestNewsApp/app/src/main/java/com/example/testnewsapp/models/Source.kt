package com.example.testnewsapp.models

data class Source(
    val id: String,
    val name: String
){
override fun hashCode(): Int {
    return name.hashCode() // name is a String variable
}}