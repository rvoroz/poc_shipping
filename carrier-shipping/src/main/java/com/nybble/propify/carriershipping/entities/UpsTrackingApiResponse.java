package com.nybble.propify.carriershipping.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpsTrackingApiResponse {
    @JsonProperty("trackResponse")
    private TrackResponse trackResponse;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TrackResponse {
        @JsonProperty("shipment")
        private List<Shipment> shipment;
        @JsonProperty("Response")
        private Response response;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Shipment {
        @JsonProperty("inquiryNumber")
        private String inquiryNumber;
        @JsonProperty("package")
        private List<Package> aPackage;
        @JsonProperty("LabelURL")
        private String labelURL;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Package {
        @JsonProperty("trackingNumber")
        private String trackingNumber;
        @JsonProperty("deliveryDate")
        private List<Date> deliveryDate;
        @JsonProperty("deliveryTime")
        private Optional<Time> deliveryTime;
        @JsonProperty("activity")
        private List<Activity> activity;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Date {
        @JsonProperty("type")
        private String type;
        @JsonProperty("date")
        private String date;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Time {
        @JsonProperty("type")
        private String type;
        @JsonProperty("endTime")
        private String endTime;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Activity {
        @JsonProperty("status")
        private Status status;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Status {
        @JsonProperty("type")
        private String type;
        @JsonProperty("description")
        private String description;
        @JsonProperty("code")
        private String code;
        @JsonProperty("statusCode")
        private String statusCode;
    }
}




