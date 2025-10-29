package BaseTest;

import Browser.DriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import java.time.Duration;

public class Prerequest {
    public static String url;

    @BeforeMethod
    @Parameters("browser")
    public void setUp(String browser){
        url="http://abi-tech.us:8082/";
        Reporter.log(">>> Starting browser: " + browser + " | Thread: " + Thread.currentThread().getId(), true);
        DriverManager.setDriver(browser);
        WebDriver driver = DriverManager.getDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        driver.get(url);
    }
    @AfterMethod
    public void tearDownClass() {
        DriverManager.removeDriver();
    }

    // To make driver accessible in child classes
    public WebDriver getDriver() {
        return DriverManager.getDriver();
    }

}
