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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import propify.utilities.ApiBase;

import java.util.Locale;

import static io.restassured.RestAssured.given;
import static propify.utilities.ConfigReader.getProperty;

public class GenerateShipment extends ApiBase {
    private static final Logger log = LogManager.getLogger(GenerateShipment.class);
    private String uri;


    @DataProvider(name = "ShippingCreateData")
    public Object[][] getData() {
        return new Object[][]{
                { "generateShipment.json"},
        };
    }

    @DataProvider(name = "accountID")
    public Object[][] getAccountData() {
        return new Object[][]{
                {"generateShipment.json", ""},
                {"generateShipment.json", "232312"},
        };
    }
    //For future TCs
    @DataProvider(name = "serviceTypesTestingData")
    public Object[][] getServiceData() {
        return new Object[][]{
                {"generateShipment.json", "02"},
                {"generateShipment.json", "03"}
        };
    }


    @Test(priority = 0, description = "Shipping generation", dataProvider = "ShippingCreateData" )
    @Description("Shipping Order")
    @Story("")
    @TmsLink("")
    public void generateShippingAddress(String fileName) {

        uri = domain + "api/propify/shipping/ups/generate";

        try {

            JSONObject payload = readJSONFromFile(fileName);
            //Hitting the API to get the response
            Response response = getResponseForPost(payload, uri);
            //Validating the status code
            Assert.assertEquals(response.statusCode(), 201);

            //Validation of response
            Assert.assertFalse(response.jsonPath().getString("trackingNumber").isEmpty());
            Assert.assertFalse(response.jsonPath().getString("labelUrl").isEmpty());

        } catch (Exception e) {

            e.printStackTrace();
            log.error("Failed due to :::" + e.getMessage());
            org.testng.Assert.fail(e.getMessage());

        }

    }

    @Test(priority = 1, description = "Invalid country code", dataProvider = "ShippingCreateData" )
    @Description("Invalid country code")
    @Story("")
    @TmsLink("")
    public void invalidCountryCode(String fileName) {

        uri = domain + "api/propify/shipping/ups/generate";
        String countryCode = faker.country().currencyCode();

        try {

            JSONObject payload = readJSONFromFile(fileName);
            payload.getJSONObject("shipFrom").getJSONObject("address").put("countryCode",countryCode);
            //hitting the API to get the response
            Response response = getResponseForPost(payload, uri);
            // validating the status code
            Assert.assertEquals(response.statusCode(), 400);

            //validation of response
            Assert.assertEquals(response.jsonPath().getString("message"), "Missing or invalid ship from country code");

        } catch (Exception e) {

            e.printStackTrace();
            log.error("Failed due to :::" + e.getMessage());
            org.testng.Assert.fail(e.getMessage());

        }

    }

    @Test(priority = 1, description = "Invalid state code", dataProvider = "ShippingCreateData" )
    @Description("Invalid state code")
    @Story("")
    @TmsLink("")
    public void invalidStateCode(String fileName) {

        uri = domain + "api/propify/shipping/ups/generate";
        String stateCode = "ZZZ";

        try {

            JSONObject payload = readJSONFromFile(fileName);
            payload.getJSONObject("shipFrom").getJSONObject("address").put("stateCode", stateCode);
            //Hitting the API to get the response
            Response response = getResponseForPost(payload, uri);
            //Validating the status code
            Assert.assertEquals(response.statusCode(), 400);

            //Validation of response
            Assert.assertEquals(response.jsonPath().getString("message"), stateCode + " is not a valid state for the specified shipment.");

        } catch (Exception e) {

            e.printStackTrace();
            log.error("Failed due to :::" + e.getMessage());
            org.testng.Assert.fail(e.getMessage());

        }

    }
    @Test(priority = 3, description = "Invalid postal code", dataProvider = "ShippingCreateData")
    @Description("Invalid postal code")
    @Story("")
    @TmsLink("")
    public void invalidPostalCode(String fileName) {

        uri = domain + "api/propify/shipping/ups/generate";
        String postCode = "1" + faker.number().digits(4);

        try {

            //Validation for shipFrom field
            JSONObject payload = readJSONFromFile(fileName);
            payload.getJSONObject("shipFrom").getJSONObject("address").put("postalCode", postCode);
            //Hitting the API to get the response
            Response response = getResponseForPost(payload, uri);
            //Validating the status code
            Assert.assertEquals(response.statusCode(), 400);

            //Validation of response
            Assert.assertEquals(response.jsonPath().getString("message"), "The postal code " + postCode + " is invalid for GA United States.");

            //Same validation for shipTo
            payload = readJSONFromFile(fileName);
            payload.getJSONObject("shipTo").getJSONObject("address").put("postalCode", postCode);
            //Hitting the API to get the response
            response = getResponseForPost(payload, uri);
            //Validating the status code
            Assert.assertEquals(response.statusCode(), 400);

            Assert.assertEquals(response.jsonPath().getString("message"), "The postal code " + postCode + " is invalid for GA United States.");

        } catch (Exception e) {

            e.printStackTrace();
            log.error("Failed due to :::" + e.getMessage());
            org.testng.Assert.fail(e.getMessage());

        }

    }

    @Test(priority = 4, description = "Invalid City / Empty City", dataProvider = "ShippingCreateData")
    @Description("Invalid City / Empty City")
    @Story("")
    @TmsLink("")
    public void invalidCity(String fileName) {

        uri = domain + "api/propify/shipping/ups/generate";
        String city = "";

        try {

            //Validation for shipFrom field
            JSONObject payload = readJSONFromFile(fileName);
            payload.getJSONObject("shipFrom").getJSONObject("address").put("city", city);
            //Hitting the API to get the response
            Response response = getResponseForPost(payload, uri);
            //Validating the status code
            Assert.assertEquals(response.statusCode(), 400);

            //Validation of response
            Assert.assertEquals(response.jsonPath().getString("message"), "Missing or invalid ship from city");

            //Same validation for shipTo
            payload = readJSONFromFile(fileName);
            payload.getJSONObject("shipTo").getJSONObject("address").put("city", city);
            //Hitting the API to get the response
            response = getResponseForPost(payload, uri);
            //Validating the status code
            Assert.assertEquals(response.statusCode(), 400);

            Assert.assertEquals(response.jsonPath().getString("message"), "Missing or invalid ship to city");

        } catch (Exception e) {

            e.printStackTrace();
            log.error("Failed due to :::" + e.getMessage());
            org.testng.Assert.fail(e.getMessage());

        }

    }

    @Test(priority = 5, description = "service type testing", dataProvider = "serviceTypesTestingData")
    @Description("service type testing")
    @Story("")
    @TmsLink("")
    public void serviceTypes(String fileName, String serviceType) {

        uri = domain + "api/propify/shipping/ups/generate";

        try {

            //Validation for shipFrom field
            JSONObject payload = readJSONFromFile(fileName);
            payload.put("serviceType", serviceType);
            //Hitting the API to get the response
            Response response = getResponseForPost(payload, uri);
            //Validating the status code
            Assert.assertEquals(response.statusCode(), 201);

            //Validation of response
            Assert.assertEquals(response.jsonPath().getString("carrier"), "UPS");
            Assert.assertFalse(response.jsonPath().getString("requestId").isEmpty());
            Assert.assertFalse(response.jsonPath().getString("trackingNumber").isEmpty());
            Assert.assertFalse(response.jsonPath().getString("labelUrl").isEmpty());


        } catch (Exception e) {

            e.printStackTrace();
            log.error("Failed due to :::" + e.getMessage());
            org.testng.Assert.fail(e.getMessage());

        }

    }

    @Test(priority = 6, description = "service ID testing", dataProvider = "accountID")
    @Description("service ID testing")
    @Story("")
    @TmsLink("")
    public void shippingAccountID(String fileName, String serviceId) {

        uri = domain + "api/propify/shipping/ups/generate";

        try {

            //Validation for shipFrom field
            JSONObject payload = readJSONFromFile(fileName);
            payload.put("shippingAccountId", serviceId);
            //Hitting the API to get the response
            Response response = getResponseForPost(payload, uri);
            //Validating the status code
            Assert.assertEquals(response.statusCode(), 400);

            //Validation of response
            Assert.assertEquals(response.jsonPath().getString("message"),
                    "Payment for accountNumber '" + serviceId + "' was not found");


        } catch (Exception e) {

            e.printStackTrace();
            log.error("Failed due to :::" + e.getMessage());
            org.testng.Assert.fail(e.getMessage());

        }

    }

}
