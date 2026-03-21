package com.example.lab5.application.service

import com.example.lab5.domain.model.Dish
import com.example.lab5.domain.model.Order
import com.example.lab5.domain.model.OrderStatus
import com.example.lab5.domain.port.OrderRepositoryPort
import com.example.lab5.infrastructure.jpa.repository.DishJpaRepository
import org.springframework.stereotype.Service

@Service
class OrderService(
    private val orderRepositoryPort: OrderRepositoryPort,
    private val dishJpaRepository: DishJpaRepository
) {
    fun findAll(userId: Long?, status: OrderStatus?): List<Order> =
        orderRepositoryPort.findAll(userId, status)

    fun findById(id: Long): Order =
        orderRepositoryPort.findById(id)
            ?: throw NoSuchElementException("Order with id=$id not found")

    fun create(userId: Long, dishIds: List<Long>): Order {
        if (dishIds.isEmpty()) {
            throw IllegalArgumentException("dishIds must not be empty")
        }
        val dishes = dishJpaRepository.findAllById(dishIds)
        if (dishes.size != dishIds.distinct().size) {
            throw IllegalArgumentException("Some dishes not found")
        }
        val order = Order(
            userId = userId,
            dishes = dishes.map {
                Dish(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    price = it.price,
                    isAvailable = it.isAvailable,
                    restaurantId = it.restaurant.id
                )
            }
        )
        return orderRepositoryPort.save(order)
    }

    fun updateStatus(id: Long, status: OrderStatus): Order {
        val order = findById(id)
        validateStatusTransition(order.status, status)
        return orderRepositoryPort.update(order.copy(status = status))
    }

    private fun validateStatusTransition(current: OrderStatus, next: OrderStatus) {
        val allowed = mapOf(
            OrderStatus.PENDING to setOf(OrderStatus.CONFIRMED, OrderStatus.CANCELLED),
            OrderStatus.CONFIRMED to setOf(OrderStatus.DELIVERED, OrderStatus.CANCELLED),
            OrderStatus.DELIVERED to emptySet(),
            OrderStatus.CANCELLED to emptySet()
        )
        if (next !in allowed[current]!!) {
            throw IllegalArgumentException(
                "Invalid status transition from $current to $next"
            )
        }
    }
}