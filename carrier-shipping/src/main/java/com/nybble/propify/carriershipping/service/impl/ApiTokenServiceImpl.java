package com.nybble.propify.carriershipping.service.impl;

import org.springframework.stereotype.Service;

import com.nybble.propify.carriershipping.entities.ApiToken;
import com.nybble.propify.carriershipping.mapper.ApiTokenMapper;
import com.nybble.propify.carriershipping.service.ApiTokenService;

@Service
public class ApiTokenServiceImpl implements ApiTokenService {
    private final ApiTokenMapper apiTokenMapper;


    public ApiTokenServiceImpl(ApiTokenMapper apiTokenMapper){
        this.apiTokenMapper = apiTokenMapper;
    }

    @Override
    public ApiToken getToken(String token) {
        return apiTokenMapper.getByToken(token);
    }
    
}
