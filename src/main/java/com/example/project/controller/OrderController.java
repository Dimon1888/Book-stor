package com.example.project.controller;

import com.example.project.dto.order.CreateOrderRequestDto;
import com.example.project.dto.order.OrderItemResponseDto;
import com.example.project.dto.order.OrderResponseDto;
import com.example.project.dto.order.UpdateOrderStatusRequestDto;
import com.example.project.model.User;
import com.example.project.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order Management", description = "Endpoints for managing orders")
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Place an order", description = "Place a new order"
            + " based on current shopping cart items")
    public OrderResponseDto placeOrder(@AuthenticationPrincipal User user,
                                       @RequestBody @Valid CreateOrderRequestDto requestDto) {
        return orderService.placeOrder(user.getId(), requestDto);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get order history", description = "Retrieve order"
            + " history for the authenticated user")
    public List<OrderResponseDto> getOrderHistory(@AuthenticationPrincipal User user,
                                                  Pageable pageable) {
        return orderService.getOrderHistory(user.getId(), pageable);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update order status", description = "Update status of an order by admin")
    public OrderResponseDto updateOrderStatus(@PathVariable Long id,
                                              @RequestBody @Valid UpdateOrderStatusRequestDto
                                                      requestDto) {
        return orderService.updateOrderStatus(id, requestDto);
    }

    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get order items", description = "Retrieve all items"
            + " for a specific order")
    public List<OrderItemResponseDto> getOrderItems(@AuthenticationPrincipal User user,
                                                    @PathVariable Long orderId) {
        return orderService.getOrderItemsByOrderId(user.getId(), orderId);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get specific order item", description = "Retrieve specific"
            + " item details within an order")
    public OrderItemResponseDto getOrderItemById(@AuthenticationPrincipal User user,
                                                 @PathVariable Long orderId,
                                                 @PathVariable Long itemId) {
        return orderService.getOrderItemByOrderIdAndItemId(user.getId(), orderId, itemId);
    }
}
