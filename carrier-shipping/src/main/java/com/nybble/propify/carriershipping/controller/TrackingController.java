package com.nybble.propify.carriershipping.controller;

import com.nybble.propify.carriershipping.entities.TrackingDetailResponse;
import com.nybble.propify.carriershipping.service.impl.TrackingService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
     * Get tracking detail by tracking number
     *
     * @param trackingNumber
     * @return track Detaisl
     */
    @ApiOperation( value = "This API will get tracking detail by tracking number."
            ,  authorizations = @Authorization(value = "Bearer"))
    @GetMapping("/{carrier}/detail/{trackingNumber}")
    public ResponseEntity<TrackingDetailResponse>  returnTrackingDetails(@PathVariable("carrier") @Parameter(example = "ups") String carrier,
                                                                         @PathVariable("trackingNumber") @Parameter(example = "1ZXXXXXXXXXXXXXXXX") String trackingNumber){
        UUID requestId = UUID.randomUUID();
        log.info("returnTrackingDetails - START - Request {} -trackingNumber {} - {} carrier ", requestId, trackingNumber,carrier.toUpperCase());
        TrackingDetailResponse trackDetail = trackingService.findTrackPackageInformationByTrackingNumber(requestId, carrier, trackingNumber);
        log.info("returnTrackingDetails - END - Request {} - trackingNumber {} - {} carrier ", requestId, trackingNumber, carrier.toUpperCase());
        return new ResponseEntity<>(trackDetail, HttpStatus.OK);
    }

}
