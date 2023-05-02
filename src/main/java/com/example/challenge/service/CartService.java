package com.example.challenge.service;

import com.example.challenge.entity.Cart;
import com.example.challenge.entity.Product;
import com.example.challenge.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@Service
public class CartService {
    private static final Logger LOGGER = Logger.getLogger(CartService.class.getName());

    @Autowired
    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Cart getNewCart(){
        Cart newCart = Cart.builder()
                .expireAt(LocalDateTime.now().plusMinutes(10))
                .productList(Collections.emptyList())
                .build();

        cartRepository.save(newCart);
        LOGGER.info("New cart with id: " + newCart.getUuid() + " has been created");
        return newCart;
    }

    private Cart getCartById(String id) throws IllegalStateException{
        Cart cart = cartRepository.findById(id).orElseThrow(() ->
                new IllegalStateException("Could not find a cart with this id: " + id ));

        if(LocalDateTime.now().isAfter(cart.getExpireAt())){
            cartRepository.delete(cart);
            LOGGER.info("Cart with id: " + cart.getUuid() + " has expired");
            throw new IllegalStateException("Cart with id: " + id + " has expired");
        }

        return cart;
    }

    public Cart getCartByIdAndUpdatesExpiration(String id) throws IllegalStateException{
        Cart cart = getCartById(id);
        cart.setExpireAt(LocalDateTime.now().plusMinutes(10));
        cartRepository.save(cart);

        return cart;
    }

    public Cart updateCart(String id, List<Product> productList ){
        Cart cart = getCartById(id);
        cart.setExpireAt(LocalDateTime.now().plusMinutes(10));

        List<Product> products = cart.getProductList();
        products.clear();
        products.addAll(productList);

        cartRepository.save(cart);
        LOGGER.info("Cart with id: " + id + " has been updated");
        return cart;
    }

    public void deleteCart(String id) throws IllegalStateException{
        Cart cart = cartRepository.findById(id).orElseThrow(() ->
                new IllegalStateException("Could not find a cart with this id: " + id ));

        LOGGER.info("Cart with id: " + id + " has been deleted");
        cartRepository.delete(cart);
    }

    @Scheduled(cron = "0 */1 * * * *")
    public void deleteExpiredCarts() {
        List<Cart> carts = cartRepository.findAll();
        LOGGER.info("Checking for expired carts...");

        for(Cart cart : carts){
            if(LocalDateTime.now().isAfter(cart.getExpireAt())){
                cartRepository.delete(cart);
                LOGGER.info("Cart with id: " + cart.getUuid() + " has expired");
            }
        }
    }

    public List<Cart> getAllCarts(){
        return cartRepository.findAll();
    }
}
