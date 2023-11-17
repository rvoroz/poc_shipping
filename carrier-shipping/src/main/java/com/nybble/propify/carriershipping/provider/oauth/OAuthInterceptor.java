package com.nybble.propify.carriershipping.provider.oauth;

import com.nybble.propify.carriershipping.entities.OAuthToken;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
public class OAuthInterceptor implements ClientHttpRequestInterceptor {
    private final OAuthTokenService oAuthTokenService;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {

        Optional<OAuthToken> oAuthToken = oAuthTokenService.getOAuthToken();
        if(oAuthToken.isPresent()) {
            request.getHeaders().add("Authorization", "Bearer " + oAuthToken.get().getAccessToken());
        }
        return execution.execute(request, body);
    }
}