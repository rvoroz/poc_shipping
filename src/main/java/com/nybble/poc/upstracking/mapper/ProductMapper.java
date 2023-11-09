package com.nybble.poc.upstracking.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.nybble.poc.upstracking.entities.Product;

@Mapper
public interface ProductMapper{
    @Select("select * from product where id = #{id}")
    Product getProduct(@Param("id")Long id);
    
    @Insert("insert into product (name, description) values(#{name}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Product product);
}
