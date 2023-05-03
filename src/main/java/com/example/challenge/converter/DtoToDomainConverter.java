package com.example.challenge.converter;

import com.example.challenge.DTO.ProductDTO;
import com.example.challenge.entity.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DtoToDomainConverter {

    List<Product> convert(List<ProductDTO> dto);
    Product convert(ProductDTO dto);
}
