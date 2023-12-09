package com.nybble.propify.carriershipping.provider.oauth;

import com.nybble.propify.carriershipping.entities.OAuthToken;
import com.nybble.propify.carriershipping.mapper.CarrierUpsTokenMapper;
import com.nybble.propify.carriershipping.model.CarrierUpsToken;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class OAuthInterceptorTest {
    private OAuthInterceptor oAuthInterceptor;

    @Mock
    private ClientHttpRequestExecution mockClientHttpRequestExecution;

    @Mock
    private ClientHttpResponse mockClientHttpResponse;

    //@Mock
    private HttpHeaders httpHeaders;

    @Mock
    private HttpRequest mockHttpRequest;

    @Mock
    private OAuthTokenService oAuthTokenService;

    @Mock
    private CarrierUpsTokenMapper carrierUpsTokenMapper;

    @Before
    public void beforeEach()
    {
        oAuthInterceptor = new OAuthInterceptor(oAuthTokenService, carrierUpsTokenMapper);
    }

    @Test
    public void intercept_no_reusable_token() throws Exception
    {
        final ClientHttpResponse actualResult;
        final byte[] inputBody = "Interceptor Test".getBytes(StandardCharsets.UTF_8);

        Optional<OAuthToken> token = Optional.of(OAuthToken.builder()
                .accessToken("123456789")
                .expiresIn(3600L)
                .build());

        when(oAuthTokenService.getOAuthToken()).thenReturn(token);

        httpHeaders = new HttpHeaders();

        when(mockHttpRequest.getHeaders()).thenReturn(httpHeaders);

        oAuthInterceptor.intercept(mockHttpRequest, inputBody, mockClientHttpRequestExecution);

        assertEquals(1, httpHeaders.size());
        assertEquals("Bearer " + token.get().getAccessToken(), httpHeaders.get(HttpHeaders.AUTHORIZATION).get(0));
        verify(oAuthTokenService, times(1)).getOAuthToken();
        verify(carrierUpsTokenMapper, times(1)).getByToken();
    }

    @Test
    public void intercept_reusable_token() throws Exception
    {
        final ClientHttpResponse actualResult;
        final byte[] inputBody = "Interceptor Test".getBytes(StandardCharsets.UTF_8);

        Optional<OAuthToken> token = Optional.of(OAuthToken.builder()
                .accessToken("123456789")
                .expiresIn(3600L)
                .build());

        Optional<CarrierUpsToken> upsToken = Optional.ofNullable(CarrierUpsToken.builder()
                .token("9876543210")
                .id(1L)
                .expiredDate(Date.from(LocalDate.now().plusDays(5).atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .build());


        when(oAuthTokenService.getOAuthToken()).thenReturn(token);
        when(carrierUpsTokenMapper.getByToken()).thenReturn(upsToken);

        httpHeaders = new HttpHeaders();

        when(mockHttpRequest.getHeaders()).thenReturn(httpHeaders);

        oAuthInterceptor.intercept(mockHttpRequest, inputBody, mockClientHttpRequestExecution);

        assertEquals(1, httpHeaders.size());
        assertEquals("Bearer " + upsToken.get().getToken(), httpHeaders.get(HttpHeaders.AUTHORIZATION).get(0));
        verify(oAuthTokenService, times(0)).getOAuthToken();
        verify(carrierUpsTokenMapper, times(1)).getByToken();
    }

    @Test
    public void intercept_expired_token() throws Exception
    {
        final ClientHttpResponse actualResult;
        final byte[] inputBody = "Interceptor Test".getBytes(StandardCharsets.UTF_8);

        Optional<OAuthToken> token = Optional.of(OAuthToken.builder()
                .accessToken("123456789")
                .expiresIn(3600L)
                .build());

        Optional<CarrierUpsToken> upsToken = Optional.ofNullable(CarrierUpsToken.builder()
                .token("9876543210")
                .id(1L)
                .expiredDate(Date.from(LocalDate.now().plusDays(-5).atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .build());


        when(oAuthTokenService.getOAuthToken()).thenReturn(token);
        when(carrierUpsTokenMapper.getByToken()).thenReturn(upsToken);

        httpHeaders = new HttpHeaders();

        when(mockHttpRequest.getHeaders()).thenReturn(httpHeaders);

        oAuthInterceptor.intercept(mockHttpRequest, inputBody, mockClientHttpRequestExecution);

        assertEquals(1, httpHeaders.size());
        assertEquals("Bearer " + token.get().getAccessToken(), httpHeaders.get(HttpHeaders.AUTHORIZATION).get(0));
        verify(oAuthTokenService, times(1)).getOAuthToken();
        verify(carrierUpsTokenMapper, times(1)).getByToken();
    }
}
