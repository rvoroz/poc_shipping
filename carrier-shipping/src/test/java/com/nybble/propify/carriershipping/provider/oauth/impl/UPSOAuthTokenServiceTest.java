package com.nybble.propify.carriershipping.provider.oauth.impl;

import com.nybble.propify.carriershipping.entities.OAuthToken;
import com.nybble.propify.carriershipping.model.ShippingProvider;
import org.apache.http.HttpEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class UPSOAuthTokenServiceTest {
    private static final String OAUTH_TOKEN_PATH = "/security/v1/oauth/token";

    private UPSOAuthTokenService upsoAuthTokenService;

    private ShippingProvider upsConfig;
    @Mock
    RestTemplate restTemplate;

    @Before
    public void setup(){
        upsConfig = ShippingProvider.builder()
                .authLiveUrl("http://oauth.com")
                .clientId("123456789")
                .clientSecret("abcdefghijk")
                .build();
        upsoAuthTokenService = new UPSOAuthTokenService(upsConfig, restTemplate, false);
    }

    @Test
    public void oauth_get_token_with_success(){
        OAuthToken token = OAuthToken.builder()
                .accessToken("123456789")
                .expiresIn(3600L)
                .build();
        ResponseEntity<OAuthToken> result = new ResponseEntity<>(token, HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(), eq(OAuthToken.class))).thenReturn(result);

        Optional<OAuthToken> oAuthToken = upsoAuthTokenService.getOAuthToken();

        assertEquals(token.getAccessToken(), oAuthToken.get().getAccessToken());
    }

    @Test
    public void oauth_get_token_with_error(){

        when(restTemplate.postForEntity(anyString(), any(), eq(OAuthToken.class))).thenThrow(new RuntimeException("Connection Error"));

        Optional<OAuthToken> oAuthToken = upsoAuthTokenService.getOAuthToken();

        assertEquals(Optional.empty(), oAuthToken);
    }
}
