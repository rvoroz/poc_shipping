package com.nybble.propify.carriershipping.service;

import org.springframework.stereotype.Service;

import com.nybble.propify.carriershipping.model.ApiToken;

@Service
public interface ApiTokenService {
    ApiToken getToken(String token);
}
