package com.nybble.poc.upstracking.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nybble.poc.upstracking.entities.Product;
import com.nybble.poc.upstracking.service.ProductService;

import lombok.extern.slf4j.Slf4j;

@RequestMapping("/product")
@RestController
@Slf4j
public class ProductController {

    ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }
    
    @GetMapping("/get/{id}")
    public Product get(@PathVariable("id") Long id){
        log.info("Product Id Search {}", id);
        return productService.getProductById(id);
    }

    @PostMapping("/add")
    public Product get(@RequestBody Product product){
        log.info("Add Product  {}", product);
        return productService.addProduct(product);
    }
}
