package com.nybble.propify.carriershipping.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.nybble.propify.carriershipping.entities.TrackingDetailResponse;
import com.nybble.propify.carriershipping.service.impl.TrackingService;

@WebMvcTest(TrackingController.class)
@AutoConfigureMockMvc(addFilters = false)
class TrackingControllerTest {

        @MockBean
        private TrackingService trackingService;

        @Autowired
        private MockMvc mockMvc;

        @Test
        void returnTrackingDetails_whenCarrierAndTrackingNumberAreOk_returnResponse() throws Exception {
                when(trackingService.findTrackPackageInformationByTrackingNumber(any(UUID.class),anyString(), anyString())).thenReturn(new TrackingDetailResponse(UUID.randomUUID(), "ups", "121212", new ArrayList<>()));

                mockMvc.perform(get("/api/propify/tracking/1/detail/22"))
                                .andExpect(status().isOk());
        }

}
