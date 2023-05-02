package com.example.challenge.service;

import com.example.challenge.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class ProductService {

    private static final Logger LOGGER = Logger.getLogger(ProductService.class.getName());
    @Autowired
    private ProductRepository productRepository;

}
