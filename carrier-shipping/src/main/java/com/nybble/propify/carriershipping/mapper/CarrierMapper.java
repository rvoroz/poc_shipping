package com.nybble.propify.carriershipping.mapper;

import com.nybble.propify.carriershipping.entities.Carrier;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@Mapper
public interface CarrierMapper {
    @Select("select id, label from carrier c where c.label like #{carrierLabel} ")
    Optional<Carrier> findCarrierByLabel(@Param("carrierLabel") String carrierLabel);

}
