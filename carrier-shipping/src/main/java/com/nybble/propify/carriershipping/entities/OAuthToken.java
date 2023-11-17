package com.nybble.propify.carriershipping.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OAuthToken {
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("issued_at")
    private Long issuedAt;
    @JsonProperty("client_id")
    private String clientId;
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("expires_in")
    private Long expiresIn;
    @JsonProperty("status")
    private String status;
}
