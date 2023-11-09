package com.nybble.poc.upstracking.controller.service;

import org.springframework.stereotype.Service;

import com.nybble.poc.upstracking.entities.Product;

public interface ProductService {
    public Product getProductById(Long id);
    public Product addProduct(Product product);
}
