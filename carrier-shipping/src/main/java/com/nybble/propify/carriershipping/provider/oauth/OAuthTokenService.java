package com.nybble.propify.carriershipping.provider.oauth;

import java.util.Optional;

import com.nybble.propify.carriershipping.entities.OAuthToken;

public interface OAuthTokenService {
    Optional<OAuthToken> getOAuthToken();
}
