package propify.utilities;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    //This class will READ THE DATA FROM configuration.properties file!!!!!!!
    //create a properties instance. Data type=Properties, instance name =properties
    private static Properties properties;

    static {
        //path of teh configuration.properties file
        String path = "configuration.properties";
        try {
            //Openning the configuration.properties file
            FileInputStream fileInputStream = new FileInputStream(path);
            //loading the file
            properties = new Properties();
            properties.load(fileInputStream);
            //closing the fileq
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Create a method to READ
    //This method weill get the KEY and return the VALUE
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

//    TESTING IF OUR LOGIC WORKS
//    public static void main(String[] args) {
//        System.out.println(properties.getProperty("url"));
//    }

    public static String getJsonProp(String prop) {

        JSONParser parser = new JSONParser();
        Object obj = null;

        try {
            obj = parser.parse(new FileReader("config.json"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = (JSONObject) obj;
        return jsonObject.get(prop).toString();
    }

}