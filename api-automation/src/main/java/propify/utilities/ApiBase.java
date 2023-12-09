package propify.utilities;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Method;

import static io.restassured.RestAssured.given;
import static propify.utilities.ConfigReader.getProperty;

public class ApiBase extends Driver {
    public static Logger log = LogManager.getLogger(ApiBase.class);
    public static String domain, token;
    public String className = "";
    public String testName = "";

    @BeforeMethod( alwaysRun = true )
    public void initTestMethod(Method method) {
        domain = getProperty("baseURI");
        token = getProperty("bearerToken");
        className = getClassName();
        testName = method.getName();
        log.info("************************ Start of test " +
                testName + " for class " + className + " *************************");
    }
    /**
     * This method is used to read json file
     *
     * @param filename - pass the file name of json
     * @return - will return the value of json file
     */
    public JSONObject readJSONFromFile(String filename) {

        JSONObject jsonObject = null;

        try {

            InputStream is = new FileInputStream(new File("src/test/resources/testdata/" + filename));
            if (is == null) {
                throw new NullPointerException("Cannot find resource file" + filename);
            }
            JSONTokener tokener = new JSONTokener(is);
            jsonObject = new JSONObject(tokener);

        } catch (FileNotFoundException e) {

            e.printStackTrace();
            log.error("Failed due to :::" + e.getMessage());
            org.testng.Assert.fail(e.getMessage());

        }

        return jsonObject;

    }
    /**
     * By providing an uri and a payload this method will return a response using a POST method to hit an API.
     *
     * @param uri uri used to send the request
     * @param payload payload used as the body of the request
     * @return returns the response obtained after hitting the API
     */
    @Step("Generate a response for a POST")
    public Response getResponseForPost(JSONObject payload, String uri) {
        Response response = null;

        try {

            log.info("Payload is:" + payload);
            //Hitting the API to get the response
            response = given().header(new Header("Authorization", "Bearer " + token)).filter(new AllureRestAssured()).
                    header("Content-Type", "application/json").
                    when().body(payload.toString()).post(uri).then().log().all().extract().response();
            log.info(response.asString());

        } catch (Exception e) {

            e.printStackTrace();
            log.error("Failed due to :::" + e.getMessage());
            org.testng.Assert.fail(e.getMessage());

        }

        return response;
    }

    /**
     * By providing an uri this method will return a response using a GET method to hit an API.
     *
     * @param uri uri used to send the request
     * @return returns the response obtained after hitting the API
     */
    @Step("Generate a response for a GET")
    public Response getResponseForGet(String uri) {
        Response response = null;

        try {

            //Hitting the API to get the response
            response = given().header(new Header("Authorization", "Bearer " + token)).filter(new AllureRestAssured()).
                    header("Content-Type", "application/json").
                    when().get(uri).then().log().all().extract().response();
            log.info(response.asString());

        } catch (Exception e) {

            e.printStackTrace();
            log.error("Failed due to :::" + e.getMessage());
            org.testng.Assert.fail(e.getMessage());

        }

        return response;
    }

    /*
    public String getDateFormat(Date fDate, String formatDt) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatDt);
        String date = sdf.format(fDate);
        return date;
    }

    public String getDateTimeFormat(Date fDate, String formatDt) {
        Date date = sdf.format(fDate);
        Timestamp ts =  new Timestamp(date.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat(formatDt);

        return date;
    }
    */

    public String getClassName() {
        if (className.isEmpty()) {
            className = this.getClass().getSimpleName();
        }
        return className;
    }


}
