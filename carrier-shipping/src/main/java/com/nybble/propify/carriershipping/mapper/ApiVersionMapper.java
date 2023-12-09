package com.nybble.propify.carriershipping.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.nybble.propify.carriershipping.model.ApiVersion;

@Mapper
public interface ApiVersionMapper{
    @Select("select * from api_version where id = #{id}")
    ApiVersion getApiVersion(@Param("id")Long id);
    
    @Insert("insert into api_version (name, description) values(#{name}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(ApiVersion apiVersion);
}
