package com.nybble.propify.carriershipping.controller;

import com.nybble.propify.carriershipping.dto.TrackingDetailResponse;
import com.nybble.propify.carriershipping.service.impl.TrackingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/api/propify/tracking")
@RestController
@Slf4j
public class TrackingController {

    private  final TrackingService trackingService;

    public TrackingController(TrackingService trackingService) {
        this.trackingService = trackingService;
    }

    /**
     * TODO
     *
     * @param trackingNumber
     * @return track Detaisl
     */
    @GetMapping("/detail/{carrier}/{trackingNumber}")
    public TrackingDetailResponse returnTrackingDetails(@PathVariable("carrier") String carrier,
                                        @PathVariable("trackingNumber") String trackingNumber){
        UUID requestId = UUID.randomUUID();
        log.info("returnTrackingDetails - START - Request {} -trackingNumber {} - {} carrier ", requestId, trackingNumber,carrier.toUpperCase());
        TrackingDetailResponse trackDetail = trackingService.findTrackPackageInformationByTrackingNumber(requestId, carrier, trackingNumber);
        log.info("returnTrackingDetails - END - Request {} - trackingNumber {} - {} carrier ", requestId, trackingNumber, carrier.toUpperCase());
        return trackDetail;
    }

}
