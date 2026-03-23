package com.example.lab5.web.controller

import com.example.lab5.application.service.OrderService
import com.example.lab5.domain.model.OrderStatus
import com.example.lab5.web.dto.*
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/orders")
@Validated
class OrderController(
    private val orderService: OrderService
) {
    @GetMapping
    fun listOrders(
        @RequestParam(required = false) userId: Long?,
        @RequestParam(required = false) status: OrderStatus?
    ): ResponseEntity<List<OrderResponse>> =
        ResponseEntity.ok(orderService.findAll(userId, status).map { it.toResponse() })

    @GetMapping("/{id}")
    fun getOrderById(@PathVariable id: Long): ResponseEntity<OrderResponse> =
        ResponseEntity.ok(orderService.findById(id).toResponse())

    @PostMapping
    fun createOrder(@Valid @RequestBody request: OrderCreateRequest): ResponseEntity<OrderResponse> {
        val order = orderService.create(request.toDomain())
        return ResponseEntity.status(HttpStatus.CREATED).body(order.toResponse())
    }

    @PatchMapping("/{id}/status")
    fun updateOrderStatus(
        @PathVariable id: Long,
        @RequestBody request: OrderStatusUpdateRequest
    ): ResponseEntity<OrderResponse> =
        ResponseEntity.ok(orderService.updateStatus(id, request.status).toResponse())
}
