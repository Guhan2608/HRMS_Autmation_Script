package Pages.Employee;


import Browser.DriverManager;
import Utils.ExcelUtil;
import Utils.LocatorHelper;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class EmployeeFormRunner {

    public static void runner() {

        String inputFile = "D:\\Automation\\Input.xlsx";
        String locatorFile = "D:\\Automation\\Locators.xlsx";

        // ✅ Read Excel Data
        List<Map<String, String>> locatorList = ExcelUtil.readExcel(locatorFile, "Locators");
        List<Map<String, String>> dataList = ExcelUtil.readExcel(inputFile, "Sheet1");

        System.out.println("========= Locator List =========");
        locatorList.forEach(System.out::println);

        System.out.println("\n========= Data List =========");
        dataList.forEach(System.out::println);

        WebDriver driver = DriverManager.getDriver();
//        driver.get("https://your-test-site-url.com");

        Map<String, String> data = dataList.get(0); // assuming single row for now
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        for (Map<String, String> locatorRow : locatorList) {
            String fieldName = locatorRow.get("FieldName");
            String locatorType = locatorRow.get("LocatorType");
            String locatorValue = locatorRow.get("LocatorValue");
            String fieldType = locatorRow.get("FieldType");
            String tabName = locatorRow.get("TabName");

            try {
                // 🧠 Match field from data sheet by partial name
                String dataKey = findMatchingKey(data, fieldName);
                if (dataKey == null) {
                    System.err.println("⚠️ No data found for field: " + fieldName);
                    continue;
                }

                String inputValue = data.get(dataKey);
                if (inputValue == null || inputValue.isEmpty()) {
                    System.err.println("⚠️ Empty value for field: " + fieldName);
                    continue;
                }

                By locator = LocatorHelper.getBy(locatorType, locatorValue);

                // ✅ Wait and find
                WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                js.executeScript("arguments[0].scrollIntoView(true);", element);

                switch (fieldType.toLowerCase()) {
                    case "textbox":
                        element.clear();
                        element.sendKeys(inputValue);
                        break;

                    case "dropdown":
                        try {
                            new Select(element).selectByVisibleText(inputValue);
                        } catch (Exception e) {
                            element.click();
                            element.sendKeys(inputValue, Keys.ENTER);
                        }
                        break;

                    case "checkbox":
                        boolean shouldSelect = inputValue.equalsIgnoreCase("true");
                        if (element.isSelected() != shouldSelect) element.click();
                        break;

                    default:
                        System.err.println("⚠️ Unknown field type: " + fieldType);
                }

                System.out.println("✅ Filled field: " + fieldName + " → " + inputValue);

            } catch (TimeoutException e) {
                System.err.println("⏳ Timeout: Could not find element for " + fieldName);
            } catch (NoSuchElementException e) {
                System.err.println("❌ Invalid locator: " + locatorType + " = " + locatorValue);
            } catch (ElementNotInteractableException e) {
                System.err.println("⚠️ Element not interactable for: " + fieldName);
            } catch (Exception e) {
                System.err.println("❌ Unexpected error on field: " + fieldName + " → " + e.getMessage());
            }
        }

        driver.quit();
    }

    // 🔍 Helper to match keys like "FirstName" vs "txtFirstName"
    private static String findMatchingKey(Map<String, String> dataMap, String fieldName) {
        for (String key : dataMap.keySet()) {
            if (fieldName.toLowerCase().contains(key.toLowerCase()) ||
                    key.toLowerCase().contains(fieldName.toLowerCase())) {
                return key;
            }
        }
        return null;
    }
}

