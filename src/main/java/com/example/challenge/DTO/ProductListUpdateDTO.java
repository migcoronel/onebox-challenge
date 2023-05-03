package com.example.challenge.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ProductListUpdateDTO {

    @Valid
    @NotEmpty(message = "Product list cannot be null/empty")
    private List<ProductDTO> productList;
}
