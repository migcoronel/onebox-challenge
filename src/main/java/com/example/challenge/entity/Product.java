package com.example.challenge.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "product")
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    private Long id;
    private String description;
    private BigDecimal amount;

}
