package com.nybble.propify.carriershipping.provider;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;

import com.nybble.propify.carriershipping.entities.*;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nybble.propify.carriershipping.exception.AddressValidationException;
import com.nybble.propify.carriershipping.exception.ShippingLabelException;
import com.nybble.propify.carriershipping.exception.UpsException;
import com.nybble.propify.carriershipping.exception.UpsProviderApiException;
import com.nybble.propify.carriershipping.mapper.ShippingProviderMapper;
import com.nybble.propify.carriershipping.provider.oauth.OAuthInterceptor;
import com.nybble.propify.carriershipping.provider.oauth.OAuthTokenService;
import com.nybble.propify.carriershipping.provider.oauth.impl.UPSOAuthTokenService;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class UPSProviderApi {

    /**
     * Identifies the optional processing to be performed. If not present or invalid
     * value then an error will be sent back. Valid values: 1 - Address Validation 2
     * - Address Classification 3 - Address Validation and Address Classification
     */
    private static final String REQUEST_OPTION = "1";
    /**
     * United State code.
     */
    private static final String UNITED_STATE_CODE = "US";
    RestTemplate restTemplate;
    RestTemplate authRestTemplate;
    OAuthTokenService oAuthTokenService;
    ShippingProvider apiConfig;
    ShippingProviderMapper shippingProviderMapper;

    @Value("${shipping.sandbox}")
    private boolean sandboxEnabled;

    public UPSProviderApi(@Qualifier("providerApi") RestTemplate restTemplate,
            @Qualifier("oAuth") RestTemplate authRestTemplate, ShippingProviderMapper shippingProviderMapper) {
        this.restTemplate = restTemplate;
        this.shippingProviderMapper = shippingProviderMapper;
        this.authRestTemplate = authRestTemplate;
        this.apiConfig = loadApiConfig();
        this.restTemplate = getRestTemplate(restTemplate);
    }

    @PostConstruct
    public void init() {
        this.oAuthTokenService = (OAuthTokenService) new UPSOAuthTokenService(this.apiConfig, this.authRestTemplate, sandboxEnabled);
    }

    public XAVResponseAddressValidation validateAddress(AddressRequest addressRequest) {
        XAVResponseAddressValidation response = null;
        try {
            AddressKeyFormat addressKeyFormat = AddressKeyFormat.builder()
                    .addressLine(addressRequest.getAddressLine()).politicalDivision1(addressRequest.getState())
                    .politicalDivision2(addressRequest.getCity()).countryCode(UNITED_STATE_CODE).build();
            Request request = Request.builder().requestOption(REQUEST_OPTION).build();
            XAVRequest xavRequest = XAVRequest.builder().request(request).addressKeyFormat(addressKeyFormat).build();
            XAVRequestAddressValidation xavRequestAddressValidation = XAVRequestAddressValidation.builder()
                    .xavRequest(xavRequest).build();

            ObjectMapper mapper = new ObjectMapper();
            String addressToValidate;
            try {
                addressToValidate = mapper.writeValueAsString(xavRequestAddressValidation);
            } catch (JsonProcessingException e) {
                log.error("Error to parser the address to validate", e);
                throw new AddressValidationException("Error to parse the address to validate");
            }

            restTemplate.setInterceptors(Arrays.asList(new OAuthInterceptor(oAuthTokenService)));
            HttpEntity<String> requestHttpEntity = getRequestHttpEntity(addressToValidate, UUID.randomUUID().toString() );

            String requestoption = REQUEST_OPTION;
            String version = "v1";
            String pathname = "/addressvalidation/" + version + "/" + requestoption;
            response = restTemplate.postForObject(
                this.getApiEndpoint() + pathname,
                    requestHttpEntity, XAVResponseAddressValidation.class);

        } catch (Exception ex) {
            log.error("Error to invoke AddressValidation API", ex);

            if (ex.getMessage().contains("\"{\"response\":")) {
                UpsResponse responseStatus = new UpsResponse();
                String[] errors = ex.getMessage().split("\"response\":", 2);
                if (errors.length > 1) {
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        responseStatus = mapper.readValue(errors[1], UpsResponse.class);
                    } catch (JsonProcessingException e) {
                        log.error("Error to parse the error exception", e);
                        throw new AddressValidationException("Error to parse the error exception");
                    }

                } else {
                    List<UpsResponseError> upsResponseErrors = new ArrayList<>();
                    upsResponseErrors.add(UpsResponseError.builder().message(ex.getMessage())
                            .build());
                    responseStatus.setErrors(upsResponseErrors);
                }
                throw new UpsException(responseStatus);
            }

            throw new UpsProviderApiException(ex.getMessage());
        }

        return response;
    }

    /**
     * The Shipping API makes UPS shipping services available to client applications
     * that communicate with UPS using the Internet Shipping
     * <p>
     * <b>200</b> - successful operation
     * <p>
     * <b>401</b> - Unauthorized Request
     *
     * @return
     * @throws ShippingLabelException if an error occurs while attempting to invoke the API
     */
    public UpsShipmentApiResponse shipment(String transactionId, UpsShipmentRequest shipmentRequest) {
        UpsShipmentApiResponse response = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            String body;
            try {
                body = mapper.writeValueAsString(shipmentRequest);
            } catch (JsonProcessingException e) {
                throw new ShippingLabelException("Error parsing body");
            }
            restTemplate.setInterceptors(Arrays.asList(new OAuthInterceptor(oAuthTokenService)));
            HttpEntity<String> requestHttpEntity = getRequestHttpEntity(body, transactionId );

            String endpoint = "/shipments/v1/ship?additionaladdressvalidation=string";
            response = restTemplate.postForObject(this.getApiEndpoint() + endpoint,
                    requestHttpEntity, UpsShipmentApiResponse.class);

        } catch (Exception ex) {
            log.error("Something");
            //TODO handle error
        }

        return response;
    }

    public UpsTrackingApiResponse tracking(String transactionId, String trackingNumber) {
        UpsTrackingApiResponse response = null;
        try {
            restTemplate.setInterceptors(Arrays.asList(new OAuthInterceptor(oAuthTokenService)));
            HttpEntity<String> requestHttpEntity = getRequestHttpEntity("", transactionId);
            //TODO replace url
            String endpoint = "/track/v1/details/" + trackingNumber + "?locale=en_US&returnSignature=false";
            ResponseEntity<UpsTrackingApiResponse> response1 = restTemplate.exchange(
                    this.getApiEndpoint() + endpoint,
                    HttpMethod.GET,
                    requestHttpEntity,
                    UpsTrackingApiResponse.class,
                    1
            );
            response = response1.getBody();
    //    } catch (HttpClientErrorException.BadRequest bde) {
    //    } catch (HttpClientErrorException.NotFound nfe) {
            //TODO handle error
        } catch (Exception ex) {
            log.error("Something");
            //TODO handle error
        }
        return response;
    }

    private HttpEntity<String>  getRequestHttpEntity(String body, String transId){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("transId", transId.replace("-","").substring(0,32));
        headers.set("transactionSrc", "testing");
        return new HttpEntity<>(body, headers);
    }

    private ShippingProvider loadApiConfig(){
        Optional<ShippingProvider> providerConfig = shippingProviderMapper.getShippingProviderByName("UPS");
        if(providerConfig.isPresent()){
            return providerConfig.get();
        }

        throw new RuntimeException("Provider Config Not Found");
    }

    private String getApiEndpoint(){
        if(sandboxEnabled){
            return this.apiConfig.getSandboxUrl();
        } else {
            return this.apiConfig.getLiveUrl();
        }
    }

    private RestTemplate getRestTemplate(RestTemplate restTemplate) {
        restTemplate.setRequestFactory(geClientHttpRequestFactory());
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(new ObjectMapper());
        restTemplate.getMessageConverters().add(converter);
        return restTemplate;
    }

    private HttpComponentsClientHttpRequestFactory geClientHttpRequestFactory() {
        CloseableHttpClient httpClient = null;
        try {

            httpClient = HttpClientBuilder.create()
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .setSSLSocketFactory(getSSLConfiguration()).build();

        } catch (Exception e) {
            LogManager.getLogger(getClass().getName()).error("Failed to create connection instance: [Proxy]: ", e);
        }

        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setHttpClient(httpClient);
        clientHttpRequestFactory.setConnectTimeout(60000);
        return clientHttpRequestFactory;
    }

    private SSLConnectionSocketFactory getSSLConfiguration() {
        try {
            InputStream targetStream = new ByteArrayInputStream(Base64.getDecoder().decode(
                    this.apiConfig.getTruststore()
                            .getBytes("UTF-8")));
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(targetStream, this.apiConfig.getTruststorePassword().toCharArray());
            SSLContext sslContext = new SSLContextBuilder()
                    .loadKeyMaterial(
                            keyStore,
                            this.apiConfig.getTruststorePassword().toCharArray())
                    .build();

            return new SSLConnectionSocketFactory(sslContext);
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }

}
