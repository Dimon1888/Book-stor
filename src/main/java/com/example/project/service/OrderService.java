package com.example.project.service;

import com.example.project.dto.order.CreateOrderRequestDto;
import com.example.project.dto.order.OrderItemResponseDto;
import com.example.project.dto.order.OrderResponseDto;
import com.example.project.dto.order.UpdateOrderStatusRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponseDto placeOrder(Long userId, CreateOrderRequestDto requestDto);

    List<OrderResponseDto> getOrderHistory(Long userId, Pageable pageable);

    OrderResponseDto updateOrderStatus(Long orderId, UpdateOrderStatusRequestDto requestDto);

    List<OrderItemResponseDto> getOrderItemsByOrderId(Long userId, Long orderId);

    OrderItemResponseDto getOrderItemByOrderIdAndItemId(Long userId, Long orderId, Long itemId);
}
