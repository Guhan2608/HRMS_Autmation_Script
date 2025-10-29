package Pages.Login;

import Browser.DriverManager;
import Utils.Waits;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ForLogin {
    public static String url;

    public static void logInAction(String email, String password) {
        WebDriver driver = DriverManager.getDriver();
        Waits.forVisibility(By.id("Email")).sendKeys(email);
        driver.findElement(By.id("Password")).sendKeys(password);
        driver.findElement(By.id("login")).click();

    }
}
