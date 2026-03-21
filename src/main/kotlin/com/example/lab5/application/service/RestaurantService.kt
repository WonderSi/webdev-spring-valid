package com.example.lab5.application.service

import com.example.lab5.domain.model.Dish
import com.example.lab5.domain.model.Restaurant
import com.example.lab5.domain.port.RestaurantRepositoryPort
import com.example.lab5.infrastructure.jpa.entity.DishEntity
import com.example.lab5.infrastructure.jpa.repository.DishJpaRepository
import com.example.lab5.infrastructure.jpa.repository.RestaurantJpaRepository
import org.springframework.stereotype.Service

@Service
class RestaurantService(
    private val restaurantRepositoryPort: RestaurantRepositoryPort,
    private val restaurantJpaRepository: RestaurantJpaRepository,
    private val dishJpaRepository: DishJpaRepository
) {
    fun findAll(): List<Restaurant> = restaurantRepositoryPort.findAll()

    fun findById(id: Long): Restaurant =
        restaurantRepositoryPort.findById(id)
            ?: throw NoSuchElementException("Restaurant with id=$id not found")

    fun create(restaurant: Restaurant): Restaurant =
        restaurantRepositoryPort.save(restaurant)

    fun update(id: Long, restaurant: Restaurant): Restaurant {
        restaurantRepositoryPort.findById(id)
            ?: throw NoSuchElementException("Restaurant with id=$id not found")
        return restaurantRepositoryPort.update(restaurant.copy(id = id))
    }

    fun delete(id: Long) {
        val deleted = restaurantRepositoryPort.deleteById(id)
        if (!deleted) throw NoSuchElementException("Restaurant with id=$id not found")
    }

    fun getMenu(restaurantId: Long): List<Dish> {
        val restaurant = restaurantJpaRepository.findById(restaurantId)
            .orElseThrow { NoSuchElementException("Restaurant with id=$restaurantId not found") }
        return restaurant.dishes.map { dish ->
            Dish(
                id = dish.id,
                name = dish.name,
                description = dish.description,
                price = dish.price,
                isAvailable = dish.isAvailable,
                restaurantId = dish.restaurant.id
            )
        }
    }

    fun addDish(restaurantId: Long, dish: Dish): Dish {
        val restaurant = restaurantJpaRepository.findById(restaurantId)
            .orElseThrow { NoSuchElementException("Restaurant with id=$restaurantId not found") }
        val entity = DishEntity(
            name = dish.name,
            description = dish.description,
            price = dish.price,
            isAvailable = dish.isAvailable,
            restaurant = restaurant
        )
        val saved = dishJpaRepository.save(entity)
        return Dish(
            id = saved.id,
            name = saved.name,
            description = saved.description,
            price = saved.price,
            isAvailable = saved.isAvailable,
            restaurantId = saved.restaurant.id
        )
    }
}