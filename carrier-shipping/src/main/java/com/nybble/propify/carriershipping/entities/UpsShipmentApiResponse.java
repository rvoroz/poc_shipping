package com.nybble.propify.carriershipping.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpsShipmentApiResponse {
    @JsonProperty("ShipmentResponse")
    private ShipmentResponse shipmentResponse;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShipmentResponse {
        @JsonProperty("Response")
        private Response response;
        @JsonProperty("ShipmentResults")
        private ShipmentResults shipmentResults;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShipmentResults {
        @JsonProperty("ShipmentIdentificationNumber")
        private String shipmentIdentificationNumber;
        @JsonProperty("PackageResults")
        private UpsShipmentApiResponse.PackageResults packageResults;
        @JsonProperty("LabelURL")
        private String labelURL;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PackageResults {
        @JsonProperty("TrackingNumber")
        private String trackingNumber;
        @JsonProperty("ShippingLabel")
        private ShippingLabel shippingLabel;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShippingLabel {
        @JsonProperty("ImageFormat")
        private ImageFormat imageFormat;
        @JsonProperty("GraphicImage")
        private String graphicImage;
        @JsonProperty("HTMLImage")
        private String htmlImage;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImageFormat {
        @JsonProperty("Code")
        private String code;
        @JsonProperty("Description")
        private String description;
    }
}




