package com.nybble.poc.upstracking.service.impl;

import org.springframework.stereotype.Service;

import com.nybble.poc.upstracking.entities.ApiToken;
import com.nybble.poc.upstracking.mapper.ApiTokenMapper;
import com.nybble.poc.upstracking.service.ApiTokenService;

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
