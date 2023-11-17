package com.nybble.propify.carriershipping.mapper;

import com.nybble.propify.carriershipping.model.Shipper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@Mapper
public interface ShipperMapper {

    @Select("select s.id, s.name, s.shipper_number shipperNumber , a2.street_address streetAddress , a2.additional_info_address additionalInfoAddress, " +
            "a2.city , a2.country_code countryCode, a2.postal_code postalCode , a2.state_providence_code stateCode " +

            "from shipper s " +
            "left join address a2 on a2.id =s.address_id " +
            "where s.shipper_number = #{shipperNumber}")
    Optional<Shipper> findShipperInfoByShipperNumber(@Param("shipperNumber") String shipperNumber);

}