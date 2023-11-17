package com.nybble.propify.carriershipping.mapper;

import com.nybble.propify.carriershipping.model.ServiceType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@Mapper
public interface ServiceTypeMapper {
    @Select("select id, code, label  from service_type where code = #{code}")
    Optional<ServiceType> getServiceType(@Param("code") String code);

}
