package com.example.challenge.controller;


import com.example.challenge.entity.Cart;
import com.example.challenge.entity.Product;
import com.example.challenge.service.CartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/carts")
@Api( value = "REST API for carts", tags = {"Cart"} )
public class CartController {

    @Autowired
    private CartService cartService;

    @ApiOperation(
            value = "Gets all carts",
            response = Cart.class,
            responseContainer = "List",
            nickname = "getAllCarts"
    )
    @GetMapping
    public List<Cart> getAllCarts() {
        return cartService.getAllCarts();
    }

    @ApiOperation(
            value = "Gets new cart",
            notes = "Gets new cart cart with an expiration time of 10 minutes",
            response = Cart.class,
            nickname = "getNewCart"
    )
    @GetMapping(path = "new")
    public Cart getNewCart() {
        return cartService.getNewCart();
    }

    @ApiOperation(
            value = "Gets a cart",
            notes = "Gets a cart by its id",
            response = Cart.class,
            nickname = "getCartById"
    )
    @GetMapping(path = "{id}")
    public ResponseEntity<?> getCartById(@PathVariable("id") String id){
        try{
            return ResponseEntity.ok( cartService.getCartByIdAndUpdatesExpiration(id) );
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation(
            value = "Updates a cart product list",
            notes = "Updates a cart product list",
            response = Cart.class,
            nickname = "updateCart")
    @PutMapping(path = "{id}")
    public ResponseEntity<?> updateCart(
            @PathVariable("id") String id,
            @RequestBody List<Product> productList
    ){
        try{
            return ResponseEntity.ok( cartService.updateCart( id, productList ) );
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation(
            value = "Deletes a cart",
            notes = "Deletes a cart by its id",
            nickname = "deleteCart"
    )
    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> deleteCart( @PathVariable("id") String id ){
        try{
            cartService.deleteCart(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}

