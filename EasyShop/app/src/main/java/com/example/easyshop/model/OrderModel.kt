package com.example.easyshop.model

import com.google.firebase.Timestamp

data class OrderModel(
    var id: String="",
    var date: Timestamp=Timestamp.now(),
    var userId: String ,
    var items: Map<String,Long> = mapOf(),
    var status: String = "",
    var address: String = "",
    var phone: String = ""

)