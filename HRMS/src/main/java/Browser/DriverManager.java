package Browser;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;

public class DriverManager {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    // Driver executable locations
    public static String edgeLocation   = "D:\\Edge driver\\msedgedriver.exe";
    public static String chromeLocation = "D:\\Chrome Driver\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe";
    public static String testChromeLoaction="D:\\Chrome Driver\\chrome-win64\\chromedriver-win64\\chromedriver.exe";

    //     Set up driver based on browser type
    public static void setDriver(String browser) {
        WebDriver driverInstance;

        switch (browser.toLowerCase()) {
            case "chrome":
                System.setProperty("webdriver.chrome.driver", chromeLocation);
                driverInstance = new ChromeDriver();
                break;

            case "edge":
                System.setProperty("webdriver.edge.driver", edgeLocation);
                driverInstance = new EdgeDriver();
                break;

            default:
                throw new IllegalArgumentException("❌ Unsupported browser: " + browser);
        }

        driver.set(driverInstance);
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void removeDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}