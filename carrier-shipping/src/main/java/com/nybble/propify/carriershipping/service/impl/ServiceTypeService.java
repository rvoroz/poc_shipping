package com.nybble.propify.carriershipping.service.impl;

import com.nybble.propify.carriershipping.mapper.ServiceTypeMapper;
import org.springframework.stereotype.Service;

@Service
public class ServiceTypeService {

    private final ServiceTypeMapper serviceTypeMapper;

    public ServiceTypeService(ServiceTypeMapper serviceTypeMapper) {
        this.serviceTypeMapper = serviceTypeMapper;
    }

    public boolean isServiceTypeAvailable(String code) {
        return serviceTypeMapper.getServiceType(code).isPresent();
    }

}
