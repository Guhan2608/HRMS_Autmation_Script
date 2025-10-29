package Utils;

import Browser.DriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class Waits {

    private static final int DEFAULT_TIMEOUT = 15;

    public static WebDriverWait getWait() {
        return new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public static WebElement forVisibility(By locator) {
        return getWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    public static void forPageLoad() {
        WebDriver driver = DriverManager.getDriver();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    public static WebElement forClickability(By locator) {
        return getWait().until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static boolean forInvisibility(By locator) {
        return getWait().until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static void forTitleContains(String titleFragment) {
        getWait().until(ExpectedConditions.titleContains(titleFragment));
    }

    public static List<WebElement> forVisibilityOfAll(By locator) {
        return getWait().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }


    public static void forSweetAlertReady(By alertLocator) {
        // wait for presence
        WebElement popup = getWait().until(ExpectedConditions.presenceOfElementLocated(alertLocator));
        // wait for visible class
        getWait().until(ExpectedConditions.attributeContains(popup, "class", "visible"));
    }


    public static WebElement forPresenceofElement(By locator) {
        return getWait().until(ExpectedConditions.presenceOfElementLocated(locator));
    }

}
