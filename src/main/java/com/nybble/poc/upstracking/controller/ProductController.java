package com.nybble.poc.upstracking.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nybble.poc.upstracking.controller.service.ProductService;
import com.nybble.poc.upstracking.entities.Product;

@RequestMapping("/product")
@RestController
public class ProductController {

    ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }
    
    @GetMapping("/get/{id}")
    public Product get(@PathVariable("id") Long id){
        return productService.getProductById(id);
    }

    @PostMapping("/add")
    public Product get(@RequestBody Product product){
        return productService.addProduct(product);
    }
}
