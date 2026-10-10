package com.example.project.service;

import com.example.project.dto.CreateCartItemRequestDto;
import com.example.project.dto.ShoppingCartDto;
import com.example.project.dto.UpdateCartItemRequestDto;
import com.example.project.model.User;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart(Long userId);

    ShoppingCartDto addCartItem(Long userId, CreateCartItemRequestDto requestDto);

    ShoppingCartDto updateCartItem(Long userId, Long cartItemId,
                                   UpdateCartItemRequestDto requestDto);

    ShoppingCartDto removeCartItem(Long userId, Long cartItemId);

    void createShoppingCartForUser(User user);
}
