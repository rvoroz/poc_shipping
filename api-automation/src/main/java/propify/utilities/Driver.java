package propify.utilities;


import com.aventstack.extentreports.ExtentTest;
import com.github.javafaker.Faker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.Locale;

public class Driver extends TestBench {

    public static final String USERNAME = "zulfikarayub_zFWO7w";
    public static final String AUTOMATE_KEY = "1XaTd3nehMwVYaYcVakR";
    public static final String URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub.browserstack.com/wd/hub";
    public static WebDriver driver;
    public static ThreadLocal<WebDriver> tdriver = new ThreadLocal<>();
    public static ExtentTest test;
    public static String runtype;
    public static Logger log = LogManager.getLogger(Driver.class);
    public static Faker faker = new Faker(new Locale("en-AR"));
    public static JavascriptExecutor js;

    public Driver() {
    }


    public void initTestMethod() {
        driver = geTDriver();
        js = (JavascriptExecutor) driver;
    }

    public WebDriver geTDriver() {
        try {
            runtype = ConfigReader.getProperty("runtype");
            if (runtype.equalsIgnoreCase("local")) {
                if (driver == null) {
                    switch (ConfigReader.getProperty("browser")) {
//                        case "chrome":
//                            WebDriverManager.chromedriver().setup();
//                            setDriver(new ChromeDriver());
//                            break;
//                        case "firefox":
//                            WebDriverManager.firefoxdriver().setup();
//                            setDriver(new FirefoxDriver());
//                            break;
//                        case "ie":
//                            WebDriverManager.iedriver().setup();
//                            setDriver(new InternetExplorerDriver());
//                            break;
//                        case "safari":
//                            WebDriverManager.getInstance(SafariDriver.class).setup();
//                            setDriver(new SafariDriver());
//                            break;
//                        case "headless-chrome":
//                            WebDriverManager.chromedriver().setup();
//                            setDriver(new ChromeDriver(new ChromeOptions().setHeadless(true)));
//                            break;
                    }

                }

            } else if (runtype.equalsIgnoreCase("remote")) {
                DesiredCapabilities caps = new DesiredCapabilities();

                caps.setCapability("os", "Windows");
                caps.setCapability("os_version", "10");
                caps.setCapability("browser", "Chrome");
                caps.setCapability("browser_version", "latest");
                caps.setCapability("browserstack.local", "false");
                caps.setCapability("browserstack.selenium_version", "4.0.0");
                URL browserStackUrl = new URL(URL);
                setDriver(new RemoteWebDriver(browserStackUrl, caps));
                //Implicit wait for 30 seconds
                //driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        driver = getDriver();
        tdriver.set(driver);
        return getWebDriver();
    }


    public static synchronized WebDriver getWebDriver() {
        return tdriver.get();
    }


}
