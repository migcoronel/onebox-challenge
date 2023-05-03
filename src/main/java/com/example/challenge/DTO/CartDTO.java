package com.example.challenge.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;


@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {

    private String uuid;
    private LocalDateTime expireAt;
    private List<ProductDTO> productList;

}
