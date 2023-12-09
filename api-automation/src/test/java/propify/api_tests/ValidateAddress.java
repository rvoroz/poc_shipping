package propify.api_tests;

import org.testng.annotations.Test;
import propify.utilities.ApiBase;
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
import org.testng.annotations.DataProvider;

import static io.restassured.RestAssured.given;
import static propify.utilities.ConfigReader.getProperty;

public class ValidateAddress extends ApiBase {
    public static Logger log = LogManager.getLogger(ValidateAddress.class);
    private String uri;

    @DataProvider(name = "ValidateAddressCreateData")
    public Object[][] getData() {
        return new Object[][]{
                { "validateAddress.json"},
        };
    }

    @Test(priority = 0, description = "Validate address", dataProvider = "ValidateAddressCreateData")
    @Description("Address validation for only one candidate")
    @Story("")
    @TmsLink("")
    public void validateSpecificAddress(String fileName) {

        uri = domain + "api/propify/addressValidation";

        try {

            JSONObject payload = readJSONFromFile(fileName);
            //Hitting the API to get the response
            Response response = getResponseForPost(payload, uri);
            //Validating the status code
            Assert.assertEquals(response.statusCode(), 200);

            //Validation of response
            Assert.assertEquals(response.jsonPath().get("candidates[0].streetAddress"), payload.get("streetAddress"));
            Assert.assertEquals(response.jsonPath().get("candidates[0].postalCode"), payload.get("postalCode"));
            Assert.assertEquals(response.jsonPath().get("candidates[0].countryCode"), "US");
            Assert.assertEquals(response.jsonPath().get("candidates[0].stateCode"), payload.get("stateCode"));
            Assert.assertEquals(response.jsonPath().get("candidates[0].city"), payload.get("city"));

        } catch (Exception e) {

            e.printStackTrace();
            log.error("Failed due to :::" + e.getMessage());
            org.testng.Assert.fail(e.getMessage());

        }

    }

    @Test(priority = 1, description = "Validate multiple addresses", dataProvider = "ValidateAddressCreateData")
    @Description("Address validation for multiple candidates")
    @Story("")
    @TmsLink("")
    public void validatePartialAddress(String fileName) {

        uri = domain + "api/propify/addressValidation";

        try {

            JSONObject payload = readJSONFromFile(fileName);
            payload.put("streetAddress", "26 ALISO CREEK");
            //Hitting the API to get the response
            Response response = getResponseForPost(payload, uri);
            //Validating the status code
            Assert.assertEquals(response.statusCode(), 200);

            //Validation of response
            for(int i = 0; i < payload.length()-1; i++) {
                Assert.assertTrue(response.jsonPath().get("candidates[" + i + "].streetAddress").toString().contains(payload.get("streetAddress").toString()));
            }

        } catch (Exception e) {

            e.printStackTrace();
            log.error("Failed due to :::" + e.getMessage());
            org.testng.Assert.fail(e.getMessage());

        }

    }
    @Test(priority = 2, description = "Invalid request", dataProvider = "ValidateAddressCreateData")
    @Description("Invalid request for address validation")
    @Story("")
    @TmsLink("")
    public void invalidRequestAddressValidation(String fileName) {

        uri = domain + "api/propify/addressValidation";

        try {

            JSONObject payload = readJSONFromFile(fileName);
            payload.put("city", "");
            payload.put("postalCode", "");
            payload.put("stateCode", "");
            //Hitting the API to get the response
            Response response = getResponseForPost(payload, uri);
            //Validating the status code
            Assert.assertEquals(response.statusCode(), 400);

            //Validation of response
            Assert.assertEquals(response.jsonPath().get("message").toString(),
                    "Invalid request. There should be at least one of the following fields: stateCode, city, postalCode");

        } catch (Exception e) {

            e.printStackTrace();
            log.error("Failed due to :::" + e.getMessage());
            org.testng.Assert.fail(e.getMessage());

        }

    }

}