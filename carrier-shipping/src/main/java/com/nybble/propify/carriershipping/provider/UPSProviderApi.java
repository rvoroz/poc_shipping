package com.nybble.propify.carriershipping.provider;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;

import com.nybble.propify.carriershipping.entities.AddressKeyFormat;
import com.nybble.propify.carriershipping.entities.AddressRequest;
import com.nybble.propify.carriershipping.entities.Request;
import com.nybble.propify.carriershipping.mapper.CarrierUpsTokenMapper;
import com.nybble.propify.carriershipping.model.ShippingProvider;
import com.nybble.propify.carriershipping.entities.UpsResponse;
import com.nybble.propify.carriershipping.entities.UpsResponseError;
import com.nybble.propify.carriershipping.entities.UpsShipmentApiResponse;
import com.nybble.propify.carriershipping.entities.UpsShipmentRequest;
import com.nybble.propify.carriershipping.entities.UpsTrackingApiResponse;
import com.nybble.propify.carriershipping.entities.XAVRequest;
import com.nybble.propify.carriershipping.entities.XAVRequestAddressValidation;
import com.nybble.propify.carriershipping.entities.XAVResponseAddressValidation;
import com.nybble.propify.carriershipping.exception.*;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    /** Current UPS API version in use **/
    private static final String API_VERSION = "v1";
    RestTemplate restTemplate;
    RestTemplate authRestTemplate;
    OAuthTokenService oAuthTokenService;
    ShippingProvider apiConfig;
    ShippingProviderMapper shippingProviderMapper;
    CarrierUpsTokenMapper carrierUpsTokenMapper;


    @Value("${shipping.sandbox}")
    private boolean sandboxEnabled;

    private static final String US_COUNTRY_CODE = "US";

    public UPSProviderApi(@Qualifier("providerApi") RestTemplate restTemplate,
            @Qualifier("oAuth") RestTemplate authRestTemplate,
                          ShippingProviderMapper shippingProviderMapper,
                          CarrierUpsTokenMapper carrierUpsTokenMapper) {
        this.restTemplate = restTemplate;
        this.shippingProviderMapper = shippingProviderMapper;
        this.authRestTemplate = authRestTemplate;
        this.apiConfig = loadApiConfig();
        this.restTemplate = getRestTemplate(restTemplate);
        this.carrierUpsTokenMapper = carrierUpsTokenMapper;
    }

    @PostConstruct
    public void init() {
        this.oAuthTokenService = new UPSOAuthTokenService(this.apiConfig, this.authRestTemplate, sandboxEnabled);
    }

    public XAVResponseAddressValidation validateAddress(AddressRequest addressRequest) {
        XAVResponseAddressValidation response = null;
        String addressToValidate;
        List<String> addressLine = new ArrayList<>();
        addressLine.add(addressRequest.getStreetAddress());
        if (Strings.isNotBlank(addressRequest.getAdditionalInfoAddress()))
            addressLine.add(addressRequest.getAdditionalInfoAddress());
        try {
            AddressKeyFormat addressKeyFormat = AddressKeyFormat.builder()
                    .addressLine(addressLine).politicalDivision1(addressRequest.getStateCode())
                    .politicalDivision2(addressRequest.getCity())
                    .postcodePrimaryLow(addressRequest.getPostalCode()).countryCode(US_COUNTRY_CODE).build();
            Request request = Request.builder().requestOption(REQUEST_OPTION).build();
            XAVRequest xavRequest = XAVRequest.builder().request(request).addressKeyFormat(addressKeyFormat).build();
            XAVRequestAddressValidation xavRequestAddressValidation = XAVRequestAddressValidation.builder()
                    .xavRequest(xavRequest).build();

            ObjectMapper mapper = new ObjectMapper();

            addressToValidate = mapper.writeValueAsString(xavRequestAddressValidation);
        } catch (JsonProcessingException e) {
            log.error("Error to parser the address to validate", e);
            throw new AddressValidationException("Error to parse the address to validate");
        }
        try {
            restTemplate.setInterceptors(Arrays.asList(new OAuthInterceptor(oAuthTokenService, carrierUpsTokenMapper)));
            HttpEntity<String> requestHttpEntity = getRequestHttpEntity(addressToValidate,
                    UUID.randomUUID().toString());

            String requestoption = REQUEST_OPTION;
            String pathname = "/addressvalidation/" + API_VERSION + "/" + requestoption;
            response = restTemplate.postForObject(
                    this.getApiEndpoint() + pathname,
                    requestHttpEntity, XAVResponseAddressValidation.class);

        } catch (Exception ex) {
            handlerUpsProviderException(ex);
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
     * @throws ShipmentParserException if an error occurs while attempting to invoke
     *                                 the API
     */
    public UpsShipmentApiResponse shipment(String transactionId, UpsShipmentRequest shipmentRequest) {
        UpsShipmentApiResponse response = null;

        ObjectMapper mapper = new ObjectMapper();
        String body = buildShipmentBody(shipmentRequest, mapper);
        try {
            restTemplate.setInterceptors(Arrays.asList(new OAuthInterceptor(oAuthTokenService, carrierUpsTokenMapper)));
            HttpEntity<String> requestHttpEntity = getRequestHttpEntity(body, transactionId);

            String endpoint = buildShipmentEndPoint();
            response = restTemplate.postForObject(endpoint,
                    requestHttpEntity, UpsShipmentApiResponse.class);

        } catch (Exception ex) {
            handlerUpsProviderException(ex);
        }

        return response;
    }

    private String buildShipmentBody(UpsShipmentRequest shipmentRequest, ObjectMapper mapper) {
        String body;
        try {
            body = mapper.writeValueAsString(shipmentRequest);
        } catch (JsonProcessingException e) {
            throw new ShipmentParserException(e);
        }
        return body;
    }

    private void handlerUpsProviderException(Exception ex) {
        if (ex.getMessage().contains("\"{\"response\":")) {
            UpsResponse responseStatus = new UpsResponse();
            String[] errors = ex.getMessage().split("\"response\":", 2);
            if (errors.length > 1) {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    responseStatus = mapper.readValue(errors[1], UpsResponse.class);
                } catch (JsonProcessingException e) {
                    log.error("Error to parse the error exception", e);
                    throw new UpsProviderBadRequestException("Error to parse the error exception");
                }
            } else {
                List<UpsResponseError> upsResponseErrors = new ArrayList<>();
                upsResponseErrors.add(UpsResponseError.builder().message(ex.getMessage())
                        .build());
                responseStatus.setErrors(upsResponseErrors);
            }
            throw new UpsException(responseStatus);
        }
        throw new UpsProviderApiException(ex.getMessage(), ex);
    }

    private String buildShipmentEndPoint() {
        String pathname = "/shipments/" + API_VERSION + "/ship";
        String pathParams = "?additionaladdressvalidation=string";
        return this.getApiEndpoint().concat(pathname).concat(pathParams);
    }

    public UpsTrackingApiResponse tracking(String transactionId, String trackingNumber) {
        UpsTrackingApiResponse response = null;
        try {
            restTemplate.setInterceptors(Arrays.asList(new OAuthInterceptor(oAuthTokenService, carrierUpsTokenMapper)));
            HttpEntity<String> requestHttpEntity = getRequestHttpEntity("", transactionId);
            String endpoint = buildTrackingEndPoint(trackingNumber);
            ResponseEntity<UpsTrackingApiResponse> response1 = restTemplate.exchange(
                    endpoint,
                    HttpMethod.GET,
                    requestHttpEntity,
                    UpsTrackingApiResponse.class,
                    1l);
            response = response1.getBody();
        } catch (Exception ex) {
            handlerUpsProviderException(ex);
        }
        return response;
    }

    private String buildTrackingEndPoint(String trackingNumber) {
        String pathname = "/track/" + API_VERSION + "/details/" + trackingNumber;
        String pathParams = "?locale=en_US&returnSignature=false";
        return this.getApiEndpoint().concat(pathname).concat(pathParams);
    }

    private HttpEntity<String> getRequestHttpEntity(String body, String transId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("transId", transId.replace("-", "").substring(0, 32));
        headers.set("transactionSrc", "testing");
        return new HttpEntity<>(body, headers);
    }

    private ShippingProvider loadApiConfig() {
        Optional<ShippingProvider> providerConfig = shippingProviderMapper.getShippingProviderByName("UPS");
        if (providerConfig.isPresent()) {
            return providerConfig.get();
        }

        throw new UpsProviderApiException("Provider Config Not Found");
    }

    private String getApiEndpoint() {
        if (sandboxEnabled) {
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
            throw new UpsProviderApiException(ex.getMessage(), ex);
        }
    }

}
