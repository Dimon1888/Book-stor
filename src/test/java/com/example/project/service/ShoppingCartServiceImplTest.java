package com.example.project.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.example.project.dto.CreateCartItemRequestDto;
import com.example.project.dto.ShoppingCartDto;
import com.example.project.exception.EntityNotFoundException;
import com.example.project.mapper.ShoppingCartMapper;
import com.example.project.model.Book;
import com.example.project.model.ShoppingCart;
import com.example.project.repository.BookRepository;
import com.example.project.repository.CartItemRepository;
import com.example.project.repository.ShoppingCartRepository;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import com.example.project.service.impl.ShoppingCartServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceImplTest {

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ShoppingCartMapper shoppingCartMapper;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @Test
    @DisplayName("Get shopping cart by valid user ID - Returns ShoppingCartDto")
    void getShoppingCart_ValidUserId_ReturnsShoppingCartDto() {
        Long userId = 1L;
        ShoppingCart cart = new ShoppingCart();
        ShoppingCartDto expectedDto = new ShoppingCartDto(1L, userId, Collections.emptySet());

        when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(shoppingCartMapper.toDto(cart)).thenReturn(expectedDto);

        ShoppingCartDto actual = shoppingCartService.getShoppingCart(userId);

        assertNotNull(actual);
        verify(shoppingCartRepository).findByUserId(userId);
    }

    @Test
    @DisplayName("Get shopping cart for non-existing user - Throws EntityNotFoundException")
    void getShoppingCart_InvalidUserId_ThrowsEntityNotFoundException() {
        Long invalidUserId = 999L;
        when(shoppingCartRepository.findByUserId(invalidUserId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService.getShoppingCart(invalidUserId));
    }

    @Test
    @DisplayName("Add item to shopping cart - Success")
    void addCartItem_ValidRequest_ReturnsUpdatedShoppingCartDto() {
        Long userId = 1L;
        Long bookId = 10L;
        CreateCartItemRequestDto requestDto = new CreateCartItemRequestDto(bookId, 2);

        ShoppingCart cart = new ShoppingCart();
        cart.setCartItems(new HashSet<>());
        Book book = new Book();
        book.setId(bookId);

        ShoppingCartDto expectedDto = new ShoppingCartDto(1L, userId, Collections.emptySet());

        when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(shoppingCartRepository.save(any(ShoppingCart.class))).thenReturn(cart);
        when(shoppingCartMapper.toDto(cart)).thenReturn(expectedDto);

        ShoppingCartDto result = shoppingCartService.addCartItem(userId, requestDto);

        assertNotNull(result);
        verify(shoppingCartRepository).save(cart);
    }
}
