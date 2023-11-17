package com.nybble.propify.carriershipping.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Package {

    public Package(String packagingCode, String dimensionsUnit, String length,
            String width, String height , String packageWeightUnit, String weight){
        this.packaging = new Packaging(packagingCode);
        this.dimensions = new Dimensions(new UnitOfMeasurement(dimensionsUnit), length, width, height);
        this.packageWeight = new PackageWeight(new UnitOfMeasurement(packageWeightUnit), weight);
    }
    @JsonProperty("Packaging")
    private Packaging packaging;
    @JsonProperty("Dimensions")
    private Dimensions dimensions;
    @JsonProperty("PackageWeight")
    private PackageWeight packageWeight;

}

@Data
class Packaging {
    Packaging(String code){
        this.code = code;
    }
    @JsonProperty("Code")
    private String code;
}

@Data
class UnitOfMeasurement {
    UnitOfMeasurement(String code){
        this.code = code;
    }
    @JsonProperty("Code")
    private String code;
}

@Data
class Dimensions {
    Dimensions (UnitOfMeasurement unitOfMeasurement, String length, String width, String height){
        this.unitOfMeasurement = unitOfMeasurement;
        this.length = length;
        this.width = width;
        this.height = height;
    }
    @JsonProperty("UnitOfMeasurement")
    private UnitOfMeasurement unitOfMeasurement;
    @JsonProperty("Length")
    private String length;
    @JsonProperty("Width")
    private String width;
    @JsonProperty("Height")
    private String height;
}

@Data
class PackageWeight {
    PackageWeight (UnitOfMeasurement unitOfMeasurement, String weight){
        this.unitOfMeasurement = unitOfMeasurement;
        this.weight = weight;
    }
    @JsonProperty("UnitOfMeasurement")
    private UnitOfMeasurement unitOfMeasurement;
    @JsonProperty("Weight")
    private String weight;
}