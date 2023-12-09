package com.nybble.propify.carriershipping.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.nybble.propify.carriershipping.entities.AddressValidationResponse;
import com.nybble.propify.carriershipping.exception.AddressValidationRequestException;
import com.nybble.propify.carriershipping.service.AddressService;

@WebMvcTest(AddressController.class)
@AutoConfigureMockMvc(addFilters = false)
class AddressControllerTest {

        @MockBean
        private AddressService addressService;

        @Autowired
        private MockMvc mockMvc;

        @Test
        void validateAddressOk() throws Exception {
                AddressValidationResponse xavResponse = new AddressValidationResponse();
                when(addressService.addressValidation(any())).thenReturn(xavResponse);

                mockMvc.perform(post("/api/propify/addressValidation")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(
                                                "{\"streetAddress\": \"26601 ALISO CREEK ROAD\",\"additionalInfoAddress\": \"STE D ALISO VIEJO TOWN CENTER\",\r\n"
                                                                + //
                                                                "\"state\": \"CA\", \"city\": \"ALISO VIEJO\", \"postalCode\": \"92656\"}"))
                                .andExpect(status().isOk());
        }

        @Test
        void validateAddress_WihtoutCity_returnErrorMessage() throws Exception {
                AddressValidationResponse xavResponse = new AddressValidationResponse();
                when(addressService.addressValidation(any())).thenReturn(xavResponse);

                mockMvc.perform(post("/api/propify/addressValidation")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(
                                                "{\"streetAddress\": \"26601 ALISO CREEK ROAD\",\"additionalInfoAddress\": \"STE D ALISO VIEJO TOWN CENTER\",\r\n"
                                                                + //
                                                                "\"state\": \"CA\", \"postalCode\": \"92656\"}"))
                                .andExpect(status().isOk()).andReturn();
        }

        @Test
        void validateAddress_WithoutAddressLine_returnErrorMessage() throws Exception {
                AddressValidationResponse xavResponse = new AddressValidationResponse();
                when(addressService.addressValidation(any())).thenReturn(xavResponse);

                MvcResult result = mockMvc.perform(post("/api/propify/addressValidation")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(
                                                "{\"additionalInfoAddress\": \"STE D ALISO VIEJO TOWN CENTER\",\r\n" + //
                                                                "\"state\": \"CA\", \"city\": \"ALISO VIEJO\", \"postalCode\": \"92656\"}"))
                                .andExpect(status().isBadRequest()).andReturn();
                String content = result.getResponse().getContentAsString();
                Assert.assertEquals("{\"code\":\"2002\",\"message\":\"The field streetAddress is required\"}", content);
        }

        @Test
        void validateAddress_WithoutState_returnok() throws Exception {
                AddressValidationResponse xavResponse = new AddressValidationResponse();
                when(addressService.addressValidation(any())).thenReturn(xavResponse);

                mockMvc.perform(post("/api/propify/addressValidation")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(
                                                "{\"streetAddress\": \"26601 ALISO CREEK ROAD\",\"additionalInfoAddress\": \"STE D ALISO VIEJO TOWN CENTER\",\r\n"
                                                                + //
                                                                "\"city\": \"ALISO VIEJO\", \"postalCode\": \"92656\"}"))
                                .andExpect(status().isOk()).andReturn();
        }

        @Test
        void validateAddress_WithoutStateCityAndPostalCode_returnErrorMessage() throws Exception {
                when(addressService.addressValidation(any())).thenThrow(new AddressValidationRequestException());

                mockMvc.perform(post("/api/propify/addressValidation")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(
                                                "{\"streetAddress\": \"26601 ALISO CREEK ROAD\",\"additionalInfoAddress\": \"STE D ALISO VIEJO TOWN CENTER\"\r\n"))
                                .andExpect(status().isBadRequest()).andReturn();
                
        }
}
