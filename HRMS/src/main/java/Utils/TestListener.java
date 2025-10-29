package Utils;

import Browser.DriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestListener implements ITestListener {

    @Override

    public void onTestFailure(ITestResult result) {
        Object testClass = result.getInstance();
        WebDriver driver = DriverManager.getDriver();

        String methodName = result.getName();
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());


        try {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String screenshotPath = "D:\\Automation_ScreenShots";

            File folder = new File(screenshotPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            String fileName = methodName + "_" + timestamp + ".png";
            File dest = new File(folder, fileName);

            FileHandler.copy(screenshot, dest);
            System.out.println("📸 Screenshot captured: " + dest.getAbsolutePath());

        } catch (Exception e) {
            System.out.println("❌ Screenshot capture failed: " + e.getMessage());
        }

        System.out.println("❌ Test Failed: " + result.getName());
        System.out.println("Reason: " + result.getThrowable());
    }


    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("✅ Test Passed: " + result.getName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("⚠ Test Skipped: " + result.getName());
    }
}
