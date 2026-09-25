package com.example.project.service.impl;

import com.example.project.dto.order.CreateOrderRequestDto;
import com.example.project.dto.order.OrderItemResponseDto;
import com.example.project.dto.order.OrderResponseDto;
import com.example.project.dto.order.UpdateOrderStatusRequestDto;
import com.example.project.exception.EntityNotFoundException;
import com.example.project.mapper.OrderItemMapper;
import com.example.project.mapper.OrderMapper;
import com.example.project.model.Order;
import com.example.project.model.OrderItem;
import com.example.project.model.ShoppingCart;
import com.example.project.model.Status;
import com.example.project.model.User;
import com.example.project.repository.OrderItemRepository;
import com.example.project.repository.OrderRepository;
import com.example.project.repository.ShoppingCartRepository;
import com.example.project.repository.user.UserRepository;
import com.example.project.service.OrderService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    @Transactional
    public OrderResponseDto placeOrder(Long userId, CreateOrderRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find user with id: " + userId));

        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find shopping cart for user with id: " + userId));

        if (shoppingCart.getCartItems().isEmpty()) {
            throw new IllegalStateException("Shopping cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus(Status.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(requestDto.getShippingAddress());

        BigDecimal total = shoppingCart.getCartItems().stream()
                .map(item -> item.getBook().getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal(total);

        Set<OrderItem> orderItems = shoppingCart.getCartItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setBook(cartItem.getBook());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPrice(cartItem.getBook().getPrice());
                    return orderItem;
                })
                .collect(Collectors.toSet());

        order.setOrderItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        shoppingCart.getCartItems().clear();
        shoppingCartRepository.save(shoppingCart);

        return orderMapper.toDto(savedOrder);
    }

    @Override
    public List<OrderResponseDto> getOrderHistory(Long userId, Pageable pageable) {
        return orderRepository.findAllByUserId(userId, pageable).stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public List<OrderItemResponseDto> getOrderItemsByOrderId(Long userId, Long orderId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find order with id: " + orderId + " for user: " + userId));
        return order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemResponseDto getOrderItemByOrderIdAndItemId(
            Long userId, Long orderId, Long itemId) {
        OrderItem orderItem = orderItemRepository
                .findByIdAndOrderIdAndOrderUserId(itemId, orderId, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find order item with id: " + itemId
                                + " for order: " + orderId + " and user: " + userId));

        return orderItemMapper.toDto(orderItem);
    }

    @Override
    @Transactional
    public OrderResponseDto updateOrderStatus(
            Long orderId, UpdateOrderStatusRequestDto requestDto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find order with id: " + orderId));
        order.setStatus(requestDto.getStatus());
        return orderMapper.toDto(orderRepository.save(order));
    }
}
