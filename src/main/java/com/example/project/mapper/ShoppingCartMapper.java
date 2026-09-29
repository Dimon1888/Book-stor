package com.example.project.mapper;

import com.example.project.config.MapperConfig;
import com.example.project.dto.CartItemResponseDto;
import com.example.project.dto.ShoppingCartDto;
import com.example.project.model.CartItem;
import com.example.project.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {
    @Mapping(target = "userId", source = "user.id")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    CartItemResponseDto toCartItemDto(CartItem cartItem);
}
