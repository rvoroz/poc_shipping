package com.nybble.poc.upstracking.service;

import org.springframework.stereotype.Service;

import com.nybble.poc.upstracking.entities.ApiToken;

@Service
public interface ApiTokenService {
    ApiToken getToken(String token);
}
