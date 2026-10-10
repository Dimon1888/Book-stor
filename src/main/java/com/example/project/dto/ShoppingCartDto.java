package com.example.project.dto;

import java.util.Set;

public record ShoppingCartDto(
        Long id,
        Long userId,
        Set<CartItemResponseDto> cartItems
) {
}
