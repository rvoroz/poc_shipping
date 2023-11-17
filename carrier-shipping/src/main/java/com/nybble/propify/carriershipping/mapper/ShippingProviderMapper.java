package com.nybble.propify.carriershipping.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.nybble.propify.carriershipping.entities.ShippingProvider;

@Mapper
public interface ShippingProviderMapper {

    @Select("select * from shipping_provider where name = #{providerName}")
    public Optional<ShippingProvider> getShippingProviderByName(@Param("providerName") String providerName);
}