package com.example.lab5.application.service

import com.example.lab5.domain.model.Dish
import com.example.lab5.domain.port.DishRepositoryPort
import com.example.lab5.infrastructure.jpa.repository.OrderJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DishService(
    private val dishRepositoryPort: DishRepositoryPort,
    private val orderJpaRepository: OrderJpaRepository
) {
    fun findAll(namePart: String?): List<Dish> =
        if (namePart != null) {
            dishRepositoryPort.findAllByNamePart(namePart)
        } else {
            dishRepositoryPort.findAll()
        }

    fun findById(id: Long): Dish =
        dishRepositoryPort.findById(id)
            ?: throw NoSuchElementException("Dish with id=$id not found")

    fun create(dish: Dish): Pair<Dish, Boolean> {
        val existing = dishRepositoryPort.findByName(dish.name)
        return if (existing != null) {
            Pair(existing, false)
        } else {
            Pair(dishRepositoryPort.save(dish), true)
        }
    }

    fun update(id: Long, dish: Dish): Dish {
        dishRepositoryPort.findById(id)
            ?: throw NoSuchElementException("Dish with id=$id not found")
        return dishRepositoryPort.update(dish.copy(id = id))
    }

    @Transactional
    fun delete(id: Long) {
        // Убираем блюдо из всех заказов перед удалением
        val orders = orderJpaRepository.findAll()
        orders.forEach { order ->
            order.dishes.removeIf { it.id == id }
            orderJpaRepository.save(order)
        }
        val deleted = dishRepositoryPort.deleteById(id)
        if (!deleted) throw NoSuchElementException("Dish with id=$id not found")
    }
}