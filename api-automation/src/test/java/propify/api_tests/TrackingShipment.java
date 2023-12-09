package propify.api_tests;

import io.qameta.allure.Description;
import io.qameta.allure.Story;
import io.qameta.allure.TmsLink;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.junit.Assert;
import org.testng.annotations.Test;
import propify.utilities.ApiBase;

import static io.restassured.RestAssured.given;
import static propify.utilities.ConfigReader.getProperty;

public class TrackingShipment extends ApiBase {
    public static Logger log = LogManager.getLogger(TrackingShipment.class);
    private String uri;


    @Test(priority = 0, description = "Generating and tracking number")
    @Description("Generating and tracking")
    @Story("")
    @TmsLink("")
    public void trackingShipment() {

        uri = domain + "api/propify/shipping/ups/generate";
        String trackingNumber = "";

        try {

            //Getting tracking number
            JSONObject payload = readJSONFromFile("generateShipment.json");
            Response responseShipment = getResponseForPost(payload, uri);
            Assert.assertEquals(responseShipment.statusCode(), 201);
            trackingNumber = responseShipment.jsonPath().getString("trackingNumber");
            log.info("Tracking num is ...." + trackingNumber);

            Assert.assertFalse(trackingNumber.isEmpty());

            //Final end point
            uri = domain + "api/propify/tracking/ups/detail/" + trackingNumber;

            //Hitting the API to get the response
            Response response = getResponseForGet(uri);
            //Validating the status code
            Assert.assertEquals(response.statusCode(), 200);
            //Validation of response
            Assert.assertEquals(response.jsonPath().getString("requestTrackingNumber"), trackingNumber);

        } catch (Exception e) {

            e.printStackTrace();
            log.error("Failed due to :::" + e.getMessage());
            org.testng.Assert.fail(e.getMessage());

        }

    }

    @Test(priority = 1, description = "Invalid tracking number")
    @Description("Invalid tracking number")
    @Story("")
    @TmsLink("")
    public void invalidTracking() {

        String trackingNumber = faker.number().digits(3);
        uri = domain + "api/propify/tracking/ups/detail/" + trackingNumber;

        log.info("Invalid tracking num... " + trackingNumber);

        try {

            //Hitting the API to get the response
            Response response = getResponseForGet(uri);
            //Validating the status code
            Assert.assertEquals(response.statusCode(), 400);
            //Validation of response
            Assert.assertEquals(response.jsonPath().getString("message"), "Invalid inquiry number");

        } catch (Exception e) {

            e.printStackTrace();
            log.error("Failed due to :::" + e.getMessage());
            org.testng.Assert.fail(e.getMessage());

        }


    }

    @Test(priority = 2, description = "Error when selecting an invalid carrier")
    @Description("Error when selecting an invalid carrier")
    @Story("")
    @TmsLink("")
    public void trackingWithInvalidShippingCarrier() {

        uri = domain + "api/propify/shipping/ups/generate";
        String trackingNumber = "", carrier="FEDEX";

        try {

            //Getting tracking number
            JSONObject payload = readJSONFromFile("generateShipment.json");
            Response responseShipment = getResponseForPost(payload, uri);
            Assert.assertEquals(responseShipment.statusCode(), 201);
            trackingNumber = responseShipment.jsonPath().getString("trackingNumber");

            log.info("Tracking num is ...." + trackingNumber);

            //Final end point
            uri = domain + "api/propify/tracking/" + carrier + "/detail/" + trackingNumber;

            //Hitting the API to get the response
            Response response = getResponseForGet(uri);
            //Validating the status code
            Assert.assertEquals(response.statusCode(), 400);
            //Validation of response
            Assert.assertEquals(response.jsonPath().getString("message"), "Carrier with label '" + carrier + "' was not found");

        } catch (Exception e) {

            e.printStackTrace();
            log.error("Failed due to :::" + e.getMessage());
            org.testng.Assert.fail(e.getMessage());

        }


    }

}
