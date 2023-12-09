package com.nybble.propify.carriershipping.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.nybble.propify.carriershipping.entities.CarrierShippingResponse;
import com.nybble.propify.carriershipping.entities.ShippingRequest;
import com.nybble.propify.carriershipping.exception.CarrierNotFoundException;
import com.nybble.propify.carriershipping.service.impl.ShippingService;

@WebMvcTest(ShippingController.class)
@AutoConfigureMockMvc(addFilters = false)
class ShippingControllerTest {

        @MockBean
        private ShippingService shippingService;

        @Autowired
        private MockMvc mockMvc;

        @Test
        void generateTrackingNumberAndShippingLabel_whenCarrierIsOk_returnCarrierShippingResponse() throws Exception {
                CarrierShippingResponse carrierShippingResponse = CarrierShippingResponse.builder().build();
                when(shippingService.processShipmentGeneration(anyString(), any(ShippingRequest.class)))
                                .thenReturn(carrierShippingResponse);

                mockMvc.perform(post("/api/propify/shipping/1/generate")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(
                                                "{\r\n" + //
                                                                "  \"requestId\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\",\r\n"
                                                                + //
                                                                "  \"serviceType\": \"03\",\r\n" + //
                                                                "  \"shipFrom\": {\r\n" + //
                                                                "    \"address\": {\r\n" + //
                                                                "      \"city\": \"Alpharetta\",\r\n" + //
                                                                "      \"countryCode\": \"US\",\r\n" + //
                                                                "      \"postalCode\": 30005,\r\n" + //
                                                                "      \"postalCodeExt\": \"string\",\r\n" + //
                                                                "      \"stateCode\": \"GA\",\r\n" + //
                                                                "      \"streetAddress\": \"2311 York Rd\"\r\n" + //
                                                                "    },\r\n" + //
                                                                "    \"name\": \"T and T Designs\"\r\n" + //
                                                                "  },\r\n" + //
                                                                "  \"shipTo\": {\r\n" + //
                                                                "    \"address\": {\r\n" + //
                                                                "      \"city\": \"Alpharetta\",\r\n" + //
                                                                "      \"countryCode\": \"US\",\r\n" + //
                                                                "      \"postalCode\": 30005,\r\n" + //
                                                                "      \"postalCodeExt\": \"string\",\r\n" + //
                                                                "      \"stateCode\": \"GA\",\r\n" + //
                                                                "      \"streetAddress\": \"2311 York Rd\"\r\n" + //
                                                                "    },\r\n" + //
                                                                "    \"name\": \"T and T Designs\"\r\n" + //
                                                                "  },\r\n" + //
                                                                "  \"shippingAccountId\": \"C7R090\"\r\n" + //
                                                                "}"))
                                .andExpect(status().isCreated());
        }

        @Test
        void generateTrackingNumberAndShippingLabel_whenCarrierIsNotFound_returnResponseError() throws Exception {
                CarrierShippingResponse.builder().build();
                when(shippingService.processShipmentGeneration(anyString(), any(ShippingRequest.class)))
                                .thenThrow(new CarrierNotFoundException("carrier not found"));

                mockMvc.perform(post("/api/propify/shipping/1/generate")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(
                                                "{\r\n" + //
                                                                "  \"requestId\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\",\r\n"
                                                                + //
                                                                "  \"serviceType\": \"03\",\r\n" + //
                                                                "  \"shipFrom\": {\r\n" + //
                                                                "    \"address\": {\r\n" + //
                                                                "      \"city\": \"Alpharetta\",\r\n" + //
                                                                "      \"countryCode\": \"US\",\r\n" + //
                                                                "      \"postalCode\": 30005,\r\n" + //
                                                                "      \"postalCodeExt\": \"string\",\r\n" + //
                                                                "      \"stateCode\": \"GA\",\r\n" + //
                                                                "      \"streetAddress\": \"2311 York Rd\"\r\n" + //
                                                                "    },\r\n" + //
                                                                "    \"name\": \"T and T Designs\"\r\n" + //
                                                                "  },\r\n" + //
                                                                "  \"shipTo\": {\r\n" + //
                                                                "    \"address\": {\r\n" + //
                                                                "      \"city\": \"Alpharetta\",\r\n" + //
                                                                "      \"countryCode\": \"US\",\r\n" + //
                                                                "      \"postalCode\": 30005,\r\n" + //
                                                                "      \"postalCodeExt\": \"string\",\r\n" + //
                                                                "      \"stateCode\": \"GA\",\r\n" + //
                                                                "      \"streetAddress\": \"2311 York Rd\"\r\n" + //
                                                                "    },\r\n" + //
                                                                "    \"name\": \"T and T Designs\"\r\n" + //
                                                                "  },\r\n" + //
                                                                "  \"shippingAccountId\": \"C7R090\"\r\n" + //
                                                                "}"))
                                .andExpect(status().isBadRequest());
        }

}
