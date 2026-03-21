package com.example.lab5.web.dto

import com.example.lab5.domain.model.Order
import com.example.lab5.domain.model.OrderStatus
import java.time.LocalDateTime

data class OrderCreateRequest(
    val userId: Long,
    val dishIds: List<Long>
)

data class OrderStatusUpdateRequest(
    val status: OrderStatus
)

data class OrderResponse(
    val id: Long,
    val userId: Long,
    val status: OrderStatus,
    val createdAt: LocalDateTime,
    val dishes: List<DishResponse>
)

fun Order.toResponse() = OrderResponse(
    id = id,
    userId = userId,
    status = status,
    createdAt = createdAt,
    dishes = dishes.map { it.toResponse() }
)