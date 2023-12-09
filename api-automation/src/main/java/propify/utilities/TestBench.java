package propify.utilities;

import org.openqa.selenium.WebDriver;

public abstract class TestBench {

    public WebDriver driver;

    public WebDriver getDriver(){
        return this.driver;
    }

    public void setDriver(WebDriver driver){

        this.driver =  driver;

    }
}
