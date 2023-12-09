package com.nybble.propify.carriershipping.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nybble.propify.carriershipping.model.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Data
public class UpsShipmentRequest {

    public UpsShipmentRequest(com.nybble.propify.carriershipping.model.Shipper shipperModel,
                              Payment payment,
                              String serviceTypeCode,
                              ShipRequest shipFrom,
                              ShipRequest shipTo,
                              Package aPackage,
                              String labelLinksIndicator
                                ){
        Shipment shipment = Shipment.builder()
                .shipper(new Shipper(shipperModel))
                .paymentInformation(new PaymentInformation(payment.getType(), payment.getAccountNumber()))
                .service(new Service(serviceTypeCode) )
                .shipFrom(new Ship (shipFrom.getName(), shipFrom.getAddress().getStreetAddress(), "",
                        shipFrom.getAddress().getStateCode(), shipFrom.getAddress().getCity(), shipFrom.getAddress().getPostalCode() ,
                        shipFrom.getAddress().getCountryCode()))
                .shipTo(new Ship (shipTo.getName(), shipTo.getAddress().getStreetAddress(), "",
                        shipTo.getAddress().getStateCode(), shipTo.getAddress().getCity(), shipTo.getAddress().getPostalCode() ,
                        shipTo.getAddress().getCountryCode()))
                .shipmentServiceOptions(new ShipmentServiceOptions(labelLinksIndicator))
                .aPackage(aPackage)
                .build();
        this.shipmentRequest = new ShipmentRequest(shipment);
    }
    @JsonProperty("ShipmentRequest")
    private ShipmentRequest shipmentRequest;
}

@Data
class ShipmentRequest {
    ShipmentRequest(Shipment shipment) {
        this.shipment = shipment;
    }
    @JsonProperty("Shipment")
    private Shipment shipment;
}


@Data
@Builder
class Shipment {
    @JsonProperty("Shipper")
    private Shipper shipper;
    @JsonProperty("ShipFrom")
    private Ship shipFrom;
    @JsonProperty("ShipTo")
    private Ship shipTo;
    @JsonProperty("PaymentInformation")
    private PaymentInformation paymentInformation;
    @JsonProperty("Service")
    private Service service;
    @JsonProperty("Package")
    private Package aPackage;
    @JsonProperty("ShipmentServiceOptions")
    private ShipmentServiceOptions shipmentServiceOptions;
}

@Data
class Address {

    Address (String streetAddress, String additionalInfoAddress, String  stateCode, String city, String postalCode, String countryCode) {
        if (StringUtils.isBlank(additionalInfoAddress)){
            this.addressLine = Collections.singletonList(streetAddress);
        } else {
            this.addressLine = Arrays.asList(streetAddress,additionalInfoAddress);
        }
        this.stateProvinceCode = stateCode;
        this.city = city;
        this.postalCode = postalCode;
        this.countryCode = countryCode;
    }
    @JsonProperty("AddressLine")
    private List<String> addressLine;
    @JsonProperty("StateProvinceCode")
    private String stateProvinceCode;
    @JsonProperty("City")
    private String city;
    @JsonProperty("PostalCode")
    private String postalCode;
    @JsonProperty("CountryCode")
    private String countryCode;
}

@Data
@Builder
@AllArgsConstructor
class Shipper {

    Shipper(com.nybble.propify.carriershipping.model.Shipper shipperModel) {
        this.name = shipperModel.getName();
        this.shipperNumber = shipperModel.getShipperNumber();
        this.address = new Address(shipperModel.getStreetAddress(), shipperModel.getAdditionalInfoAddress(),
                shipperModel.getStateCode(), shipperModel.getCity(), shipperModel.getPostalCode(),
                shipperModel.getCountryCode());
    }
    @JsonProperty("Name")
    private String name;
    @JsonProperty("ShipperNumber")
    private String shipperNumber;
    @JsonProperty("Address")
    private Address address;
}

@Data
class Ship {
    Ship (String name, String streetAddress, String additionalInfoAddress, String  stateCode, String city, String postalCode , String countryCode){
        this.name = name;
        this.address = new Address(streetAddress, additionalInfoAddress, stateCode, city, postalCode , countryCode);
    }
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Address")
    private Address address;
}

@Data
class PaymentInformation {

    PaymentInformation(String type, String accountNumber){
        this.shipmentCharge = new ShipmentCharge(type, accountNumber);
    }
    @JsonProperty("ShipmentCharge")
    private ShipmentCharge shipmentCharge;
}

@Data
class ShipmentCharge {
    ShipmentCharge (String type, String accountNumber) {
        this.type = type;
        this.billShipper = new BillShipper(accountNumber);
    }
    @JsonProperty("Type")
    private String type;
    @JsonProperty("BillShipper")
    private BillShipper billShipper;
}

@Data
class BillShipper {
    BillShipper (String accountNumber){
        this.accountNumber = accountNumber;
    }
    @JsonProperty("AccountNumber")
    private String accountNumber;
}

@Data
class Service {
    Service (String code){
        this.code = code;
    }
    @JsonProperty("Code")
    private String code;
}

@Data
class ShipmentServiceOptions {
    ShipmentServiceOptions(String labelLinksIndicator) {
        this.labelDelivery = new LabelDelivery(labelLinksIndicator);
    }
    @JsonProperty("LabelDelivery")
    private LabelDelivery labelDelivery;
}

@Data
class LabelDelivery {
    LabelDelivery (String labelLinksIndicator){
        this.labelLinksIndicator = labelLinksIndicator;
    }
    @JsonProperty("LabelLinksIndicator")
    private String labelLinksIndicator;
}
@Data
class LabelSpecification {
    @JsonProperty("LabelImageFormat")
    private LabelImageFormat labelImageFormat;

    @JsonProperty("HTTPUserAgent")
    private String httpUserAgent;
}

@Data
class LabelImageFormat {
    @JsonProperty("Code")
    String code;
    @JsonProperty("Description")
    String description;
}