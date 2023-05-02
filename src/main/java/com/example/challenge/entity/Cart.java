package com.example.challenge.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "cart")
@Entity
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "uuid", unique = true, columnDefinition = "VARCHAR(255)")
    private String uuid;
    private LocalDateTime expireAt;
    @OneToMany(targetEntity = Product.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cart_id" , nullable = false)
    private List<Product> productList;

}
