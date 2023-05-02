package com.example.challenge.service;

import com.example.challenge.entity.Cart;
import com.example.challenge.entity.Product;
import com.example.challenge.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class CartServiceTest {
    private CartService cartService;
    private CartRepository cartRepository = mock(CartRepository.class);
    private String fixedValidId = UUID.randomUUID().toString();
    private String fixedExpiredId = UUID.randomUUID().toString();

    @BeforeEach
    void setUp() {
        when(cartRepository.save(any(Cart.class)))
                .thenAnswer(i -> {
                    Cart cart = i.getArgument(0,Cart.class);
                    cart.setUuid(fixedValidId);
                    return cart;
                });

        when(cartRepository.findById(fixedValidId))
                .thenReturn(Optional.of(Cart.builder()
                        .uuid(fixedValidId)
                        .productList(new ArrayList<>())
                        .expireAt(LocalDateTime.now().plusMinutes(1))
                        .build()));
        when(cartRepository.findById(fixedExpiredId))
                .thenReturn(Optional.of(Cart.builder()
                        .uuid(fixedExpiredId)
                        .productList(new ArrayList<>())
                        .expireAt(LocalDateTime.now().minusMinutes(1))
                        .build()));

        when(cartRepository.findAll()).thenReturn(Arrays.asList(
                Cart.builder()
                        .uuid(UUID.randomUUID().toString())
                        .productList(new ArrayList<>())
                        .expireAt(LocalDateTime.now().minusMinutes(1))
                        .build(),
                Cart.builder()
                        .uuid(UUID.randomUUID().toString())
                        .productList(new ArrayList<>())
                        .expireAt(LocalDateTime.now().minusMinutes(3))
                        .build(),
                Cart.builder()
                        .uuid(UUID.randomUUID().toString())
                        .productList(new ArrayList<>())
                        .expireAt(LocalDateTime.now().plusMinutes(1))
                        .build()
        ));

        when(cartRepository.findById("non-existent-id"))
                .thenReturn(Optional.empty());

        cartService = new CartService(cartRepository);

    }

    @Test
    @DisplayName("Should get a new cart with expiration date 10 minutes from now")
    void getNewCart() {
        Cart cart = cartService.getNewCart();

        assertNotNull(cart);
        assertNotNull(cart.getUuid());
        assertTrue(CollectionUtils.isEmpty(cart.getProductList()));
        assertTrue(cart.getExpireAt().isAfter(LocalDateTime.now().plusMinutes(9)));
        assertTrue(cart.getExpireAt().isBefore(LocalDateTime.now().plusMinutes(10)));

        verify(this.cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    @DisplayName("Should get cart by id and update expiration date")
    void getCartByIdAndUpdatesExpiration() {
        Cart cart = cartService.getCartByIdAndUpdatesExpiration(fixedValidId);

        assertNotNull(cart);
        assertNotNull(cart.getUuid());
        assertTrue(cart.getExpireAt().isAfter(LocalDateTime.now().plusMinutes(9)));
        assertTrue(cart.getExpireAt().isBefore(LocalDateTime.now().plusMinutes(10)));

        verify(this.cartRepository, times(1)).findById(fixedValidId);
        verify(this.cartRepository, times(1)).save(any(Cart.class));

    }

    @Test
    @DisplayName("Should update cart product list and expiration date")
    void updateCart() {
        List<Product> productList = Arrays.asList(
                Product.builder()
                        .id(1l)
                        .amount(BigDecimal.ONE)
                        .description("test description").build(),
                Product.builder()
                        .id(2l)
                        .amount(BigDecimal.TEN)
                        .description("test description 2").build());

        Cart cart = cartService.updateCart(fixedValidId, productList);

        assertNotNull(cart);
        assertNotNull(cart.getUuid());
        assertEquals(2, cart.getProductList().size());
        assertTrue(cart.getExpireAt().isAfter(LocalDateTime.now().plusMinutes(9)));
        assertTrue(cart.getExpireAt().isBefore(LocalDateTime.now().plusMinutes(10)));

        verify(this.cartRepository, times(1)).findById(fixedValidId);
        verify(this.cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    @DisplayName("Should delete cart")
    void deleteCart() {
        cartService.deleteCart(fixedValidId);

        verify(this.cartRepository, times(1)).findById(fixedValidId);
        verify(this.cartRepository, times(1)).delete(any(Cart.class));
    }

    @Test
    @DisplayName("Should delete expired carts")
    void deleteExpiredCarts() {
        cartService.deleteExpiredCarts();
        verify(this.cartRepository, times(1)).findAll();
        verify(this.cartRepository, times(2)).delete(any(Cart.class));
    }

    @Test
    @DisplayName("Should throw exception when cart not found")
    void throwExceptionCartNotFound() {
        assertThrows(IllegalStateException.class, () -> cartService.getCartByIdAndUpdatesExpiration("non-existent-id"));
        verify(this.cartRepository, times(1)).findById("non-existent-id");

    }

    @Test
    @DisplayName("Should throw exception when trying to delete a cart that does not exist")
    void throwExceptionDeletingCartNotFound() {
        assertThrows(IllegalStateException.class, () -> cartService.deleteCart("non-existent-id"));
        verify(this.cartRepository, times(1)).findById("non-existent-id");

    }

    @Test
    @DisplayName("Should throw exception when cart has expired")
    void throwExceptionCartExpired() {
        assertThrows(IllegalStateException.class, () -> cartService.getCartByIdAndUpdatesExpiration(fixedExpiredId));
        verify(this.cartRepository, times(1)).findById(fixedExpiredId);
        verify(this.cartRepository, times(1)).delete(any(Cart.class));
    }
}