package com.nybble.propify.carriershipping.security;

import com.nybble.propify.carriershipping.entities.TrackingDetailResponse;
import com.nybble.propify.carriershipping.model.ApiToken;
import com.nybble.propify.carriershipping.service.ApiTokenService;
import com.nybble.propify.carriershipping.service.ShippingLabelService;
import com.nybble.propify.carriershipping.service.impl.TrackingService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SecurityConfigurationTest {

    public static final String OAUTH_TOKEN = "eYqK8plSaPfZIqemQe5uxRtlkGC6wf66";
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @MockBean
    ApiTokenService apiTokenService;

    @MockBean
    private TrackingService trackingService;

    @MockBean
    private ShippingLabelService shippingLabelService;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        when(apiTokenService.getToken(anyString())).thenReturn(ApiToken.builder().token(OAUTH_TOKEN).build());
    }

    @Test
    public void security_public() throws Exception {
        when(shippingLabelService.generateLabel(anyString())).thenReturn(new ByteArrayOutputStream());
        mvc.perform(get("/public/shipping/label/de8a6f21-9c16-441e-9f29-35ddf09b63d8").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void security_authenticated() throws Exception {
        when(trackingService.findTrackPackageInformationByTrackingNumber(any(UUID.class),anyString(), anyString())).thenReturn(new TrackingDetailResponse(UUID.randomUUID(), "ups", "121212", new ArrayList<>()));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + OAUTH_TOKEN);
        mvc.perform(get("/api/propify/tracking/1/detail/22")
            .headers(headers))
                .andExpect(status().isOk());
    }
}
