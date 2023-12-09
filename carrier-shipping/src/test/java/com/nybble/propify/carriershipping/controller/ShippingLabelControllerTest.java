package com.nybble.propify.carriershipping.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.ByteArrayOutputStream;

import com.nybble.propify.carriershipping.exception.PDFGenerationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.nybble.propify.carriershipping.exception.CarrierLabelNotFoundException;
import com.nybble.propify.carriershipping.service.ShippingLabelService;

@WebMvcTest(ShippingLabelController.class)
@AutoConfigureMockMvc(addFilters = false)
class ShippingLabelControllerTest {

        @MockBean
        private ShippingLabelService shippingLabelService;

        @Autowired
        private MockMvc mockMvc;

        @Test
        void generatePdfLabel_whenLabelIsOk_returnByteResponse() throws Exception {
                when(shippingLabelService.generateLabel(anyString())).thenReturn(new ByteArrayOutputStream());

                mockMvc.perform(get("/public/shipping/label/1212121212"))
                                .andExpect(status().isOk());
        }

        @Test
        void generatePdfLabel_whenCarrierLabelNotFound_returnCarrierLabelNotFoundException() throws Exception {
                when(shippingLabelService.generateLabel(anyString())).thenThrow(new CarrierLabelNotFoundException("Error"));

                mockMvc.perform(get("/public/shipping/label/1212121212"))
                                .andExpect(status().isNotFound());
        }

        @Test
        void generatePdfLabel_whenCarrierLabel_returnPDFGenerationException() throws Exception {
                when(shippingLabelService.generateLabel(anyString())).thenThrow(new PDFGenerationException(new RuntimeException("Error")));

                mockMvc.perform(get("/public/shipping/label/1212121212"))
                        .andExpect(status().is5xxServerError());
        }

}
