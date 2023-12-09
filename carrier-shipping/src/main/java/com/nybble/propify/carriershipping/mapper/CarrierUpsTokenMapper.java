package com.nybble.propify.carriershipping.mapper;

import com.nybble.propify.carriershipping.model.CarrierUpsToken;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.Optional;

@Mapper
public interface CarrierUpsTokenMapper {
    @Select("select id, token, expired_date expiredDate from carrier_ups_token ")
    Optional<CarrierUpsToken> getByToken();

    @Update("update carrier_ups_token " +
            "    set token = #{token}, " +
            "        expired_date = #{expiredDate} ")
    void update(@Param("token") String token, @Param("expiredDate") Date expiredDate);

    @Insert("INSERT INTO carrier_ups_token\n" +
            "            (token, expired_date)\n" +
            "    VALUES(#{token}, #{expiredDate})")
    void insert(@Param("token") String token, @Param("expiredDate") Date expiredDate);

}
