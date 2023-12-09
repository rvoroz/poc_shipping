package com.nybble.propify.carriershipping.mapper;


import com.nybble.propify.carriershipping.model.CarrierShipment;

import java.util.Optional;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CarrierShipmentMapper {

    @Insert("INSERT INTO carrier_shipment\n" +
            "            (request_id, carrier, tracking_number, label_id, shipping_label_image, provider_laber_url)\n" +
            "    VALUES(#{requestId}, #{carrier}, #{trackingNumber}, #{labelId}, #{shippingLabelImage}, #{providerLabelUrl})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(CarrierShipment carrierShipment);

    @Select("Select id, request_id resuestId, carrier, tracking_number trackingNumber, label_id labelId, shipping_label_image shippingLabelImage from carrier_shipment where label_id = #{labelId}")
    Optional<CarrierShipment> getByLabelId(@Param("labelId") String labelId);

}
