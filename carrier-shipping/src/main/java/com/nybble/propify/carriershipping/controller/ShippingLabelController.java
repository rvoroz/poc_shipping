package com.nybble.propify.carriershipping.controller;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nybble.propify.carriershipping.service.ShippingLabelService;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("public/shipping")
@RestController
@Slf4j
public class ShippingLabelController {
    
    private final ShippingLabelService shippingLabelService;

    public ShippingLabelController(ShippingLabelService shippingLabelService) {
        this.shippingLabelService = shippingLabelService;
    }

    /**
     * The Generate PDF shipping label api return a PDF with shipping label information by a label id.
     *
     * @param labelId
     * @return ShippingResponse
     */
    @GetMapping("/label/{labelId}")
    @ApiOperation(value = "Generates Label in PDF format", produces = "application/pdf")
    public ResponseEntity<byte[]> generatePdfLabel(@PathVariable("labelId") @Parameter(example = "0d39a27e-f82b-4b20-b318-b4b8157c23cf") String labelId) {
        log.info("Generating PDF Label");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        // Here you have to set the actual filename of your pdf
        String filename = "Electronic Label".concat(".pdf");
        headers.setContentDisposition(ContentDisposition.builder("attachment")
          .filename(filename)
          .build());
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(shippingLabelService.generateLabel(labelId).toByteArray(), headers, HttpStatus.OK);
    }
}
