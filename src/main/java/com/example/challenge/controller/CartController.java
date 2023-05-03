package com.example.challenge.controller;


import com.example.challenge.DTO.CartDTO;
import com.example.challenge.DTO.ProductListUpdateDTO;
import com.example.challenge.converter.DomainToDtoConverter;
import com.example.challenge.converter.DtoToDomainConverter;
import com.example.challenge.entity.Cart;
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

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/carts")
@Api( value = "REST API for carts", tags = {"Cart"} )
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private DtoToDomainConverter dtoToDomainConverter;
    @Autowired
    private DomainToDtoConverter domainToDtoConverter;

    @ApiOperation(
            value = "Gets all carts",
            response = CartDTO.class,
            responseContainer = "List",
            nickname = "getAllCarts"
    )
    @GetMapping
    public ResponseEntity<List<CartDTO>> getAllCarts() {
        List<CartDTO> dtoList = domainToDtoConverter.convert(cartService.getAllCarts());
        return ResponseEntity.ok(dtoList);
    }

    @ApiOperation(
            value = "Gets new cart",
            notes = "Gets new cart cart with an expiration time of 10 minutes",
            response = CartDTO.class,
            nickname = "getNewCart"
    )
    @GetMapping(path = "new")
    public ResponseEntity<CartDTO> getNewCart() {
        CartDTO dto = domainToDtoConverter.convert(cartService.getNewCart());
        return ResponseEntity.ok(dto);
    }

    @ApiOperation(
            value = "Gets a cart",
            notes = "Gets a cart by its id",
            response = CartDTO.class,
            nickname = "getCartById"
    )
    @GetMapping(path = "{id}")
    public ResponseEntity<CartDTO> getCartById(@PathVariable("id") String id){

        CartDTO dto = domainToDtoConverter.convert(cartService.getCartByIdAndUpdatesExpiration(id));
        return ResponseEntity.ok(dto);
    }

    @ApiOperation(
            value = "Updates a cart product list",
            notes = "Updates a cart product list",
            response = CartDTO.class,
            nickname = "updateCart")
    @PutMapping(path = "{id}")
    public ResponseEntity<CartDTO> updateCart(
            @PathVariable("id") String id,
            @RequestBody @Valid ProductListUpdateDTO productListUpdateDTO
    ){
        Cart domain = cartService.updateCart(id, dtoToDomainConverter.convert(productListUpdateDTO.getProductList()));
        CartDTO dto = domainToDtoConverter.convert(domain);
        return ResponseEntity.ok(dto);
    }

    @ApiOperation(
            value = "Deletes a cart",
            notes = "Deletes a cart by its id",
            nickname = "deleteCart"
    )
    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> deleteCart( @PathVariable("id") String id ){

        cartService.deleteCart(id);
        return ResponseEntity.ok().build();
    }


}

