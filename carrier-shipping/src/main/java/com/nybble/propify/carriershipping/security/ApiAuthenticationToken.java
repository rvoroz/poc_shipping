package com.nybble.propify.carriershipping.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class ApiAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = 1L;
    private String token;

    public ApiAuthenticationToken(String token) {
        super(null);
        this.token = token;
    }

    public String getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

}