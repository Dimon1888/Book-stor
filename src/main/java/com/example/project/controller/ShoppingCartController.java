package com.example.project.controller;

import com.example.project.dto.CreateCartItemRequestDto;
import com.example.project.dto.ShoppingCartDto;
import com.example.project.dto.UpdateCartItemRequestDto;
import com.example.project.model.User;
import com.example.project.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping Cart Management", description = "Endpoints for managing shopping cart")
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    @Operation(summary = "Get user's shopping cart", description =
            "Retrieve current user's shopping cart")
    public ShoppingCartDto getShoppingCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.getShoppingCart(user.getId());
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @Operation(summary = "Add item to shopping cart", description =
            "Add a book to the user's shopping cart")
    public ShoppingCartDto addCartItem(
            Authentication authentication,
            @RequestBody @Valid CreateCartItemRequestDto requestDto
    ) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.addCartItem(user.getId(), requestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/items/{cartItemId}")
    @Operation(summary = "Update item quantity", description =
            "Update books quantity in the shopping cart")
    public ShoppingCartDto updateCartItem(
            Authentication authentication,
            @PathVariable Long cartItemId,
            @RequestBody @Valid UpdateCartItemRequestDto requestDto
    ) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.updateCartItem(user.getId(), cartItemId, requestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/items/{cartItemId}")
    @Operation(summary = "Remove item from cart", description =
            "Remove a book from the shopping cart")
    public ShoppingCartDto removeCartItem(
            Authentication authentication,
            @PathVariable Long cartItemId
    ) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.removeCartItem(user.getId(), cartItemId);
    }
}
