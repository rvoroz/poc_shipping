package com.nybble.propify.carriershipping.provider.oauth;

import com.nybble.propify.carriershipping.entities.OAuthToken;


import com.nybble.propify.carriershipping.mapper.CarrierUpsTokenMapper;
import com.nybble.propify.carriershipping.model.CarrierUpsToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
public class OAuthInterceptor implements ClientHttpRequestInterceptor {
    private final OAuthTokenService oAuthTokenService;
    private final CarrierUpsTokenMapper carrierUpsTokenMapper;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {

        Optional<CarrierUpsToken> carrierUpsToken = carrierUpsTokenMapper.getByToken();
        if (carrierUpsToken.isPresent()) {
            if (carrierUpsToken.get().getExpiredDate().after(new Date())) {
                request.getHeaders().add("Authorization", "Bearer " + carrierUpsToken.get().getToken());
            } else {
                    refreshUpsToken(request, false);
            }
        } else {
            refreshUpsToken(request,true);
        }
        return execution.execute(request, body);
    }

    private void refreshUpsToken(HttpRequest request, boolean insert) {
        Optional<OAuthToken> oAuthToken = oAuthTokenService.getOAuthToken();
        if(oAuthToken.isPresent()) {
            request.getHeaders().add("Authorization", "Bearer " + oAuthToken.get().getAccessToken());
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, oAuthToken.get().getExpiresIn().intValue() - 5);
        if (insert) {
            carrierUpsTokenMapper.insert(oAuthToken.get().getAccessToken(), calendar.getTime());
        } else {
            carrierUpsTokenMapper.update(oAuthToken.get().getAccessToken(), calendar.getTime());
        }
    }
}