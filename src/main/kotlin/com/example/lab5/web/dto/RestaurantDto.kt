package com.example.lab5.web.dto

import com.example.lab5.domain.model.Restaurant

data class RestaurantCreateRequest(
    val name: String,
    val address: String
)

data class RestaurantUpdateRequest(
    val name: String,
    val address: String
)

data class RestaurantResponse(
    val id: Long,
    val name: String,
    val address: String
)

fun Restaurant.toResponse() = RestaurantResponse(id = id, name = name, address = address)

fun RestaurantCreateRequest.toDomain() = Restaurant(name = name, address = address)

fun RestaurantUpdateRequest.toDomain() = Restaurant(name = name, address = address)