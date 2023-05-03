package com.example.challenge.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    @NotNull(message = "Product id cannot be null")
    private Long id;
    @NotBlank(message = "Product description cannot be null/empty/blank")
    private String description;
    @NotNull(message = "Product amount cannot be null")
    @Positive(message = "Product amount cannot be negative/zero")
    private BigDecimal amount;

}
