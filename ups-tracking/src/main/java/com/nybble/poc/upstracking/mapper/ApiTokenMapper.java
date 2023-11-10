package com.nybble.poc.upstracking.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.nybble.poc.upstracking.entities.ApiToken;

@Mapper
public interface ApiTokenMapper{
    @Select("select * from api_token where token = #{token}")
    ApiToken getByToken(@Param("token")String token);
}
