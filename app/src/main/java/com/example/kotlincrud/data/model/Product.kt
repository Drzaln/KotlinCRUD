package com.example.kotlincrud.data.model

import java.io.Serializable

data class Product(
    var name: String="",
    var price: Int=0,
    var image: String="",
    var id: Int?=null //nullable, karena saat post tidak menggunakan id, jadi bisa null
):Serializable