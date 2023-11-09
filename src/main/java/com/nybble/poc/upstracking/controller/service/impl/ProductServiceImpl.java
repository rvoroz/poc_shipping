
package com.nybble.poc.upstracking.controller.service.impl;

import org.springframework.stereotype.Service;

import com.nybble.poc.upstracking.controller.service.ProductService;
import com.nybble.poc.upstracking.entities.Product;
import com.nybble.poc.upstracking.mapper.ProductMapper;

@Service
public class ProductServiceImpl implements ProductService{
    private ProductMapper productMapper;

    public ProductServiceImpl(ProductMapper productMapper){
        this.productMapper = productMapper;
    }

    @Override
    public Product getProductById(Long id) {
        return  productMapper.getProduct(id);
    }

    @Override
    public Product addProduct(Product product) {
        productMapper.insert(product);
        return product;
    }

    
}