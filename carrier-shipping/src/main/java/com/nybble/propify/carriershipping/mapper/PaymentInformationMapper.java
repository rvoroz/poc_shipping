package com.nybble.propify.carriershipping.mapper;

import com.nybble.propify.carriershipping.model.Payment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@Mapper
public interface PaymentInformationMapper {
    @Select("select id , `type` , account_number accountNumber " +
            "from payment_information pi2 " +
            "where account_number = #{accountNumber}")
    Optional<Payment> getPaymentInfoByAccountNumber(@Param("accountNumber") String accountNumber);

}
