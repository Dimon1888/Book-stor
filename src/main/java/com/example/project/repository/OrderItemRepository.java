package com.example.project.repository;

import com.example.project.model.OrderItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findAllByOrderIdAndOrderUserId(Long orderId, Long userId);

    Optional<OrderItem> findByIdAndOrderIdAndOrderUserId(Long id, Long orderId, Long userId);

    Optional<OrderItem> findByIdAndOrderId(Long id, Long orderId);
}
