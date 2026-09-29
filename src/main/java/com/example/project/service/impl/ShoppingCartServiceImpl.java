package com.example.project.service.impl;

import com.example.project.dto.CreateCartItemRequestDto;
import com.example.project.dto.ShoppingCartDto;
import com.example.project.dto.UpdateCartItemRequestDto;
import com.example.project.exception.EntityNotFoundException;
import com.example.project.mapper.ShoppingCartMapper;
import com.example.project.model.Book;
import com.example.project.model.CartItem;
import com.example.project.model.ShoppingCart;
import com.example.project.model.User;
import com.example.project.repository.BookRepository;
import com.example.project.repository.CartItemRepository;
import com.example.project.repository.ShoppingCartRepository;
import com.example.project.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartMapper shoppingCartMapper;

    @Override
    @Transactional(readOnly = true)
    public ShoppingCartDto getShoppingCart(Long userId) {
        return shoppingCartMapper.toDto(getShoppingCartByUserId(userId));
    }

    @Override
    public ShoppingCartDto addCartItem(Long userId, CreateCartItemRequestDto requestDto) {
        ShoppingCart shoppingCart = getShoppingCartByUserId(userId);
        Book book = bookRepository.findById(requestDto.bookId())
                .orElseThrow(() -> new EntityNotFoundException("Can't find book by id: "
                        + requestDto.bookId()));

        CartItem cartItem = shoppingCart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(requestDto.bookId()))
                .findFirst()
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setShoppingCart(shoppingCart);
                    newItem.setBook(book);
                    shoppingCart.getCartItems().add(newItem);
                    return newItem;
                });

        cartItem.setQuantity(cartItem.getQuantity() + requestDto.quantity());
        return shoppingCartMapper.toDto(shoppingCartRepository.save(shoppingCart));
    }

    @Override
    public ShoppingCartDto updateCartItem(Long userId, Long cartItemId,
                                          UpdateCartItemRequestDto requestDto) {
        ShoppingCart shoppingCart = getShoppingCartByUserId(userId);
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(cartItemId,
                        shoppingCart.getId())
                .orElseThrow(() -> new EntityNotFoundException("Can't find cart item by id: "
                        + cartItemId));

        cartItem.setQuantity(requestDto.quantity());
        return shoppingCartMapper.toDto(shoppingCartRepository.save(shoppingCart));
    }

    @Override
    public ShoppingCartDto removeCartItem(Long userId, Long cartItemId) {
        ShoppingCart shoppingCart = getShoppingCartByUserId(userId);
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(cartItemId,
                        shoppingCart.getId())
                .orElseThrow(() -> new EntityNotFoundException("Can't find cart item by id: "
                        + cartItemId));

        shoppingCart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);
        return shoppingCartMapper.toDto(shoppingCartRepository.save(shoppingCart));
    }

    @Override
    public void createShoppingCartForUser(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    private ShoppingCart getShoppingCartByUserId(Long userId) {
        return shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find shopping "
                        + "cart for user with id: " + userId));
    }
}
