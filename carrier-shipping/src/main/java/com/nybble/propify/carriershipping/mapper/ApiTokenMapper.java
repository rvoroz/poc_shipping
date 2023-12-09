package com.nybble.propify.carriershipping.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.nybble.propify.carriershipping.model.ApiToken;

@Mapper
public interface ApiTokenMapper{
    @Select("select * from api_token where token = #{token}")
    ApiToken getByToken(@Param("token")String token);
}
