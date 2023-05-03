package com.example.challenge.converter;

import com.example.challenge.DTO.CartDTO;
import com.example.challenge.entity.Cart;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DomainToDtoConverter {

    List<CartDTO> convert(List<Cart> domain);
    CartDTO convert(Cart domain);
}
