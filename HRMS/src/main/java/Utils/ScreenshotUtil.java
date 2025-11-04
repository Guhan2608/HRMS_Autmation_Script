package Utils;

import Browser.DriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtil {
    public static String takeScreenshot(String methodName) {
        String screenshotPath = null;
        try {
            WebDriver driver = DriverManager.getDriver();
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            screenshotPath = "D:\\Automation_ScreenShots\\" + methodName + "_" + timestamp + ".png";

            File dest = new File(screenshotPath);
            FileHandler.copy(screenshot, dest);

            System.out.println("📸 Screenshot captured: " + dest.getAbsolutePath());
        } catch (Exception e) {
            System.out.println("⚠️ Screenshot failed: " + e.getMessage());
        }
        return screenshotPath; // ✅ return even if it’s null (safe fallback)
    }

}

