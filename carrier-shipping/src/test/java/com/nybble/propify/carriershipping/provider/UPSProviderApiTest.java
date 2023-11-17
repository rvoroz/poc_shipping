package com.nybble.propify.carriershipping.provider;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.text.html.Option;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.nybble.propify.carriershipping.entities.AddressRequest;
import com.nybble.propify.carriershipping.entities.ShippingProvider;
import com.nybble.propify.carriershipping.entities.XAVResponseAddressValidation;
import com.nybble.propify.carriershipping.exception.UpsException;
import com.nybble.propify.carriershipping.exception.UpsProviderApiException;
import com.nybble.propify.carriershipping.mapper.ShippingProviderMapper;
import com.nybble.propify.carriershipping.mapper.repository.AddressRepository;

@RunWith(SpringRunner.class)
public class UPSProviderApiTest {

    private UPSProviderApi upsProviderApi;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RestTemplate authRestTemplate;

    @Mock
    private ShippingProviderMapper shippingProviderMapper;

     @Before
    public void setUp() {
        when(shippingProviderMapper.getShippingProviderByName(eq("UPS"))).thenReturn(getShippingProvider());
        upsProviderApi = new UPSProviderApi(restTemplate, authRestTemplate, shippingProviderMapper);
    }

    @Test
    public void validateAddress_thenReturnAddressValidationOk() throws JsonMappingException, JsonProcessingException {

        XAVResponseAddressValidation xavResponseAddressValidation = XAVResponseAddressValidation.builder()
                .build();
        when(restTemplate.postForObject(anyString(), any(HttpEntity.class), eq(XAVResponseAddressValidation.class)))
                .thenReturn(xavResponseAddressValidation);

        List<String> addressLine = new ArrayList<>();
        addressLine.add("26601 ALISO CREEK ROAD");
        addressLine.add("STE D");
        addressLine.add("ALISO VIEJO TOWN CENTER");
        addressLine.add("CA");
        AddressRequest addressRequest = AddressRequest.builder()
                .addressLine(addressLine)
                .city("ALISO VIEJO")
                .state("CA").build();

        upsProviderApi.validateAddress(addressRequest);

        verify(restTemplate, times(1)).postForObject(anyString(), any(HttpEntity.class),
                eq(XAVResponseAddressValidation.class));
    }

    @Test(expected = UpsProviderApiException.class)
    public void validateAddress_whenInvokeServiceFail_ThrowUpsProviderApiException() throws JsonMappingException, JsonProcessingException {
        when(restTemplate.postForObject(anyString(), any(HttpEntity.class), eq(XAVResponseAddressValidation.class))).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "error"));

        List<String> addressLine = new ArrayList<>();
        addressLine.add("26601 ALISO CREEK ROAD");
        addressLine.add("STE D");
        addressLine.add("ALISO VIEJO TOWN CENTER");
        addressLine.add("CA");
        AddressRequest addressRequest = AddressRequest.builder()
                .addressLine(addressLine)
                .city("ALISO VIEJO")
                .state("CA").build();

        upsProviderApi.validateAddress(addressRequest);
        verify(restTemplate, times(1)).postForObject(anyString(), any(HttpEntity.class),
                eq(XAVResponseAddressValidation.class));
    }

    @Test(expected = UpsException.class)
    public void validateAddress_thenReturnAddressValidationThrowException() throws JsonMappingException, JsonProcessingException {
        when(restTemplate.postForObject(anyString(), any(HttpEntity.class), eq(XAVResponseAddressValidation.class))).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "\"400 Bad Request: \"{\"response\":{\"errors\":[{\"code\":\"264002\",\"message\":\"Country Code is invalid or missing.\"}]}}\"\""));

        List<String> addressLine = new ArrayList<>();
        addressLine.add("26601 ALISO CREEK ROAD");
        addressLine.add("STE D");
        addressLine.add("ALISO VIEJO TOWN CENTER");
        addressLine.add("CA");
        AddressRequest addressRequest = AddressRequest.builder()
                .addressLine(addressLine)
                .city("ALISO VIEJO")
                .state("CA").build();

        upsProviderApi.validateAddress(addressRequest);
        verify(restTemplate, times(1)).postForObject(anyString(), any(HttpEntity.class),
                eq(XAVResponseAddressValidation.class));
    }

    private Optional<ShippingProvider> getShippingProvider(){
        ShippingProvider apiConfig = ShippingProvider.builder()
                .truststore("MIIGfgIBAzCCBjcGCSqGSIb3DQEHAaCCBigEggYkMIIGIDCCBhwGCSqGSIb3DQEHBqCCBg0wggYJAgEAMIIGAgYJKoZIhvcNAQcBMCkGCiqGSIb3DQEMAQYwGwQU/LSI4bMgxrUvYlELRt8CQnxar5QCAwDDUICCBcjEtlNpYDf8W/HMiWcIBx2yyJsx7CGF6a4uT6c40W8+VkRLNcQTVn9gpRvkTNILWAOyfXEp2f7HEesdpLCP8MF4tWStxb6uf/EnuZdGU5iUL71RKJwvYf2wzsMuElMml5XV+7c3mueRi5JwWKOGZwVXgqqMSy044dVBQdx8O9qArxHr9aXtj0vtrnM/9HOD9oWkcjc4ufnCYBZUIgBG6s9T5MPH0z6H5U2PWqkguDzqlaA1I8munLc41yMMbCxyV8XsFjdWYvc0iqlWJc43+Jbad/tM6BLxeY6huMUnbx6X2F40Bz2Gxcc8fJY0h/7UdYsb3lKwgl02UcVLHeyYzJiqUnDE97H15JE3+lGkCSvaYYegm74cJpdNvcTiP5d74AC0xEiQon8fxWsHSQcV6ozJSf95b32QKkqHPihjYpFK8TYFQPc0z+hrNcdKhD2BIwuSPv/OSzCuLUTOHRivlxx/u0W53mBIka5N/WgN3qcJBo0Cx6sEQe5+o1qdNpdNIWi5K7Y5KbqTkB4Pdk00L31mWnxD/bLa0u7bHWAQCNhu0FcbwRfsrFFpl5IA3yw4wPb+IeFIsaDYtR/zCebZcb7VUG9VhEs9dKQCCcCCfKnc7doo9Tm2xiyGTC03zsx4hj60PPwJOWrI892xVvLmagBkbZLT4AeHy1ET5qvhsBSGEe+Lhk7mPKVM1X0ZwYTwsq4WiOqwifz532yk223/3r5xv/HfOS+kH+umEpsK8b8MPijAHTZh5Jl8Yp5/yKni8RsLLeISVwSmC6WEK4xywTkhIFPaCDDRfyh/fZMSG4xlt0zk+IBWrTSxOfA9jb+8PQgcrzFja4a+i6HEGiFqaGde1XlzTlg0V20BNKIWzu3tqatco5fN9aNlT8mL2DGgv6lqjgZW1rpEOrJ9pCvjbhBvW3jnpx0+CnNkf+J4MROxdvd4uF0cTtK2khlA6frt6EVIdIlMzIaNkqrEyvMfK+hBGVZ2xegdw2xjtk59rLnx5UsHJ5W8LJbIsu9LnuRGBf7NVh67yzv/DtTvijHg3mu22KmoLlLFxIxln3zIgk939jSuSVFmtMGu2rSawUg4cur+G8rp06Yn7DxnUkyF44DNKSoNnKqAs5SUE0V4/Yvj9ik7gvrLvd19WCbCoVaJWG8Zb99W2/3W8KadF6IKy1urpb0EVs6XnQy/lyhDK7tTfMSTIa9qfFM1h/5ppbjo7kn4Nj5Alb5xff6KGMdsH5kCLahJqw17+ChKIU8jCSuqn1K6PDzK5Prl6/FNfNSNG4uAex1XCGuVxGwIt1r1t5EAUT/XWq7yhMGKPyGPuP/My4wUR2clg1j+1s1pYs/9EVqusK9k+qN7+k5UtDLDMV1xDbuAvRDdH69Sf/4KQtGAQoud13fhLfPYVbt4fvCnDGb6yI79ZMad6sXs3L77hJuiOKtcgmnVvrv4McsbAVnm0PTnxGNMM1kaPcygh2082xuc/qfhAzK5K5xZzgc4sbAzEjfHN0oCe4S6tl0nni7nQEpnxj3Roo8c2MeVVvHHPW9Y1UH13m0or1mpzv7cUqlt5kix3/6nEHzucUeI1o6qvjFis798P+2q1/ayv7iyAqXZ73sQ0tM4X0QJU6JAWbPXGP1yYulReBgQn3YeoZN8XbhJVrHGi9oI/3M/LxkgcDDJ7e6xV0XOUY44Zltx3CmQYgxZQOAAFGH9YHl6dVo9hWvy1Hswv+mVrQOaMTABT02j+E0CXRYSG+99w5SP0fJww8C442T+sbh8RcNH3IVDWRpeG2V+0nlmlHgPxum2Qn39Y4AJlZxzlK5Lb7oTxUpEnM/tD7WYjzYLixBws83Ef6Dlc7O3o1ZOMq5nf6z7Oefpmwd3btkwIk5jY222lMcUBnUi9ivS3E2oHwuDRrn8aY0HHo2u1k+UokVhol7TgDJqN2JWQhdkgt4OLDN1m/3yzY0nfFAXaKWd1LCM3vWetq+aumWx8kRQMD4wITAJBgUrDgMCGgUABBRaqr+7VGdcWOcqdpCEWAcsXO81YwQUsUKhVrgD53KCy4osiSj23vP/hRUCAwGGoA==")
                .truststorePassword("changeit").build();
        
        return Optional.of(apiConfig);
    }
}
