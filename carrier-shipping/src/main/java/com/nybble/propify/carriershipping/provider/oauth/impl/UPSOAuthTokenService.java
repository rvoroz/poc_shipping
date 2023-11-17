package com.nybble.propify.carriershipping.provider.oauth.impl;

import com.nybble.propify.carriershipping.entities.OAuthToken;
import com.nybble.propify.carriershipping.entities.ShippingProvider;
import com.nybble.propify.carriershipping.provider.oauth.OAuthTokenService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
public class UPSOAuthTokenService implements OAuthTokenService {


    /**
     *
     */
    private static final String OAUTH_TOKEN_PATH = "/security/v1/oauth/token";
    public static final String CLIENT_CREDENTIALS = "client_credentials";
    private ShippingProvider apiConfig;
    private final RestTemplate restTemplate;
    private boolean sanboxEnabled;


    public UPSOAuthTokenService(ShippingProvider upsConfig, RestTemplate restTemplate, boolean sandbox) {
        this.apiConfig = upsConfig;
        this.restTemplate = restTemplate;
        this.sanboxEnabled = sandbox;
    }

    @Override
    public Optional<OAuthToken> getOAuthToken() {

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setBasicAuth(apiConfig.getClientId(), this.apiConfig.getClientSecret());

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", CLIENT_CREDENTIALS);


            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

            ResponseEntity<OAuthToken> response = restTemplate.postForEntity(
                    this.getApiEndpoint().concat(OAUTH_TOKEN_PATH),
                    request,
                    OAuthToken.class);

            return Optional.of(response.getBody());

        } catch (Exception ex) {
            log.error("Error Obtaining OAUTH Token: ", ex);
            return Optional.empty();
        }
    }

    private String getApiEndpoint(){
        if(this.sanboxEnabled){
            return this.apiConfig.getAuthSandboxUrl();
        } else {
            return this.apiConfig.getAuthLiveUrl();
        }
    }
}