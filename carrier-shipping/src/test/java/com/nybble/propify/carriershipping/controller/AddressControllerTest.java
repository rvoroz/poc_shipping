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
import com.nybble.propify.carriershipping.service.AddressService;

@WebMvcTest(AddressController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AddressControllerTest {

    @MockBean
    private AddressService addressService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void validateAddressOk() throws Exception {
        AddressValidationResponse xavResponse = new AddressValidationResponse();
        when(addressService.addressValidation(any())).thenReturn(xavResponse);

        mockMvc.perform(post("/api/propify/addressValidation")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(
                        "{\"addressLine\": [\"26601 ALISO CREEK ROAD\",\"STE D\",\"ALISO VIEJO TOWN CENTER\", \"CA\"],\r\n"
                                + //
                                "\"state\": \"CA\", \"city\": \"ALISO VIEJO\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void validateAddress_WihtoutCity_returnErrorMessage() throws Exception {
        AddressValidationResponse xavResponse = new AddressValidationResponse();
        when(addressService.addressValidation(any())).thenReturn(xavResponse);

        MvcResult result = mockMvc.perform(post("/api/propify/addressValidation")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(
                        "{\"addressLine\": [\"26601 ALISO CREEK ROAD\",\"STE D\",\"ALISO VIEJO TOWN CENTER\", \"CA\"],\r\n"
                                + //
                                "\"state\": \"CA\"}"))
                .andExpect(status().isBadRequest()).andReturn();
        String content = result.getResponse().getContentAsString();
        Assert.assertEquals("{\"code\":\"1002\",\"message\":\"The field city is required\"}", content);
    }

    @Test
    public void validateAddress_WithoutAddressLine_returnErrorMessage() throws Exception {
        AddressValidationResponse xavResponse = new AddressValidationResponse();
        when(addressService.addressValidation(any())).thenReturn(xavResponse);

        MvcResult result = mockMvc.perform(post("/api/propify/addressValidation")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(
                        "{\"state\": \"CA\", \"city\": \"ALISO VIEJO\"}"))
                .andExpect(status().isBadRequest()).andReturn();
        String content = result.getResponse().getContentAsString();
        Assert.assertEquals("{\"code\":\"1002\",\"message\":\"The field addressLine is required\"}", content);
    }

    @Test
    public void validateAddress_WithoutState_returnErrorMessage() throws Exception {
        AddressValidationResponse xavResponse = new AddressValidationResponse();
        when(addressService.addressValidation(any())).thenReturn(xavResponse);

        MvcResult result = mockMvc.perform(post("/api/propify/addressValidation")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(
                        "{\"addressLine\": [\"26601 ALISO CREEK ROAD\",\"STE D\",\"ALISO VIEJO TOWN CENTER\", \"CA\"],\r\n"
                                + //
                                "\"city\": \"ALISO VIEJO\"}"))
                .andExpect(status().isBadRequest()).andReturn();
        String content = result.getResponse().getContentAsString();
        Assert.assertEquals("{\"code\":\"1002\",\"message\":\"The field state is required\"}", content);
    }
}
