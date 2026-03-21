package com.example.lab5.application.service

import com.example.lab5.domain.model.User
import com.example.lab5.domain.port.UserRepositoryPort
import com.example.lab5.infrastructure.jpa.repository.OrderJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepositoryPort: UserRepositoryPort,
    private val orderJpaRepository: OrderJpaRepository
) {
    fun findAll(): List<User> = userRepositoryPort.findAll()

    fun findById(id: Long): User = userRepositoryPort.findById(id)
        ?: throw NoSuchElementException("User with id=$id not found")

    fun create(user: User): Pair<User, Boolean> {
        val existing = userRepositoryPort.findByEmail(user.email)
        return if (existing != null) {
            Pair(existing, false)
        } else {
            Pair(userRepositoryPort.save(user), true)
        }
    }

    fun update(id: Long, user: User): User {
        userRepositoryPort.findById(id)
            ?: throw NoSuchElementException("User with id=$id not found")
        return userRepositoryPort.update(user.copy(id = id))
    }

    @Transactional
    fun delete(id: Long) {
        // Удаляем все заказы пользователя перед удалением
        val orders = orderJpaRepository.findAllByUserIdOrderByCreatedAtDesc(id)
        orderJpaRepository.deleteAll(orders)

        val deleted = userRepositoryPort.deleteById(id)
        if (!deleted) throw NoSuchElementException("User with id=$id not found")
    }
}