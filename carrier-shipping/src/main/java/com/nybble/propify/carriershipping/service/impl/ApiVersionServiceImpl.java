
package com.nybble.propify.carriershipping.service.impl;

import org.springframework.stereotype.Service;

import com.nybble.propify.carriershipping.model.ApiVersion;
import com.nybble.propify.carriershipping.mapper.ApiVersionMapper;
import com.nybble.propify.carriershipping.service.ApiVersionService;

@Service
public class ApiVersionServiceImpl implements ApiVersionService{
    private final ApiVersionMapper apiVersionMapper;

    public ApiVersionServiceImpl(ApiVersionMapper apiVersionMapper){
        this.apiVersionMapper = apiVersionMapper;
    }

    @Override
    public ApiVersion getApiVersionById(Long id) {
        return  apiVersionMapper.getApiVersion(id);
    }

    @Override
    public ApiVersion addApiVersion(ApiVersion apiVersion) {
        apiVersionMapper.insert(apiVersion);
        return apiVersion;
    }

    
}