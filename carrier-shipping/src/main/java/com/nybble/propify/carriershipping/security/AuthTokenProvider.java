package com.nybble.propify.carriershipping.security;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.nybble.propify.carriershipping.model.ApiToken;
import com.nybble.propify.carriershipping.service.ApiTokenService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthTokenProvider implements AuthenticationProvider, Serializable {


    private static final long serialVersionUID = 1L;

    private static final String BEARER = "Bearer ";

    @Autowired
    private ApiTokenService apiTokenService;

    @Override
    public Authentication authenticate(Authentication authentication) {

        if (authentication.getCredentials() == null) {
            throw new BadCredentialsException(
                    ("Missing Token"));
        }

        // @formatter:off
        String token = Optional.ofNullable(authentication.getCredentials().toString())
                .map(value -> StringUtils.removeStart(value, BEARER)).map(String::trim)
                .orElseThrow(() -> new BadCredentialsException(
                        ("Missing Token")));
        // @formatter:on

        ApiToken accessToken = apiTokenService.getToken(token);

        if (Objects.isNull(accessToken)) {
            throw new BadCredentialsException(
                "Token Missmatch");
        }

        log.info("API token authenticated.");
        authentication.setAuthenticated(true);

        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ApiAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

