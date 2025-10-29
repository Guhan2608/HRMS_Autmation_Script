package Pages.Employee;

import Browser.DriverManager;
import Pages.Common.ForCommonAction;
import Utils.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static Utils.LocatorHelper.getBy;

public class AddEmployeeFromExcel {
    public static WebDriver driver = DriverManager.getDriver();
    public static String inputfile = "D:\\Automation\\Input.xlsx";
    public static String locaorfile = "D:\\Automation\\Locators.xlsx";


    public void switchToTab(String tabName) {
        WebDriver driver = DriverManager.getDriver();
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            // Locate tab by visible text
            WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//ul[@class='nav nav-tabs']//a[normalize-space(text())='" + tabName + "']")
            ));
            tab.click();
            Thread.sleep(1000); // small wait for tab content to load
            System.out.println("🔁 Switched to tab: " + tabName);
        } catch (Exception e) {
            System.out.println("⚠️ Could not switch to tab: " + tabName);
            Utils.ScreenshotUtil.takeScreenshot("TabSwitch_" + tabName);
        }
    }

    public void openTab1(String tabName) {

        switch (tabName.toLowerCase()) {
            case "personal details":
                driver.findElement(By.xpath("//a[normalize-space()='Personal Details']")).click();
                break;

            case "employment":
                driver.findElement(By.xpath("//a[text()='Employment']")).click();
                break;

            case "passport details/id details":
                driver.findElement(By.xpath("//a[contains(text(),'Passport Details')]")).click();
                break;

            case "shift":
                driver.findElement(By.xpath("//a[normalize-space()='Shift']")).click();
                break;

            case "pay":
                driver.findElement(By.xpath("//a[text()='Pay']")).click();
                break;

            case "academic":
                driver.findElement(By.xpath("//a[text()='Academic']")).click();
                break;

            case "certification":
                driver.findElement(By.xpath("//a[text()='Certification']")).click();
                break;

            case "accident":
                driver.findElement(By.xpath("//a[text()='Accident']")).click();
                break;

            default:
                System.out.println("Invalid Tab Name: " + tabName);
                break;
        }
    }

    public void fillForm(Map<String, String> inputData, Map<String, LocatorUtil.LocatorInfo> locators) {
        String currentTab = "";
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        for (String field : inputData.keySet()) {
            try {
                LocatorUtil.LocatorInfo info = locators.get(field);
                if (info == null) continue;

                // ✅ Switch tab if needed
                if (!info.tabName.equalsIgnoreCase(currentTab)) {
                    switchToTab(info.tabName);
                    currentTab = info.tabName;
                }

                // Find element
                By locator = getBy(info.locatorType, info.locatorValue);
                WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                Actions actions = new Actions(driver);

                switch (info.fieldType.toLowerCase()) {
                    case "textbox":
                        element.clear();
                        element.sendKeys(inputData.get(field));
                        System.out.println("✅ Entered text in " + field);
                        break;

                    case "dropdown":
                        ForCommonAction.selectChosenDropdown(info.locatorValue, inputData.get(field));
                        System.out.println("✅ Selected dropdown " + field);
                        break;

                    case "radio":
                        if (inputData.get(field).equalsIgnoreCase("true") && !element.isSelected()) {
                            actions.moveToElement(element).click().perform();
                            System.out.println("✅ Selected radio button " + field);
                        }
                        break;

                    default:
                        System.out.println("⚠️ Unknown field type for " + field);
                }

            } catch (Exception e) {
                System.out.println("❌ Error filling field " + field + ": " + e.getMessage());
                Utils.ScreenshotUtil.takeScreenshot("Error_" + field);
            }
        }
    }

    public static void addemp() {
        WebDriver driver = DriverManager.getDriver();
        PropertyReader prop = new PropertyReader(AddEmployee.propertyFile);
        Waits.forVisibility(By.xpath(prop.get("empLink"))).click();
        Waits.forVisibility(By.xpath(prop.get("emppageLink"))).click();
        Waits.forVisibility(By.xpath(prop.get("addButton"))).click();
    }

    public static void main(String[] args) {
        String inputfile = "D:\\Automation\\Input.xlsx";
        String locatorfile = "D:\\Automation\\Locators.xlsx";

        List<Map<String, String>> locatorList = ExcelUtil.readExcel(locatorfile, "Locators");
        List<Map<String, String>> dataList = ExcelUtil.readExcel(inputfile, "Sheet1");

        System.out.println("----- Locator List -----");
        printListOfMaps(locatorList);

        System.out.println("\n----- Data List -----");
        printListOfMaps(dataList);
        Map<String, String> dataRow = dataList.get(0); // first row for now

        for (Map<String, String> locatorRow : locatorList) {

            String fieldName = locatorRow.get("FieldName");
            String locatorType = locatorRow.get("LocatorType");
            String locatorValue = locatorRow.get("LocatorValue");
            String fieldType = locatorRow.get("FieldType"); // TextBox, Dropdown, etc.
            String tabName = locatorRow.get("TabName");
            System.out.println("------------");
            System.out.println("locatorValue = " + locatorValue);
        }
    }

    public static void printListOfMaps(List<Map<String, String>> list) {
        for (int i = 0; i < list.size(); i++) {
            System.out.println("Row " + (i + 1) + ":");
            Map<String, String> map = list.get(i);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                System.out.println("  " + entry.getKey() + " = " + entry.getValue());
            }
            System.out.println(); // blank line between rows
        }
    }

    private void validateEnteredValue(WebDriver driver, WebElement element, String expectedValue, String fieldType, String locatorValue) {
        try {
            String actualValue = "";

            switch (fieldType.toLowerCase()) {
                case "textbox":
                    actualValue = element.getAttribute("value").trim();
                    break;


                case "dropdown":
                case "dropdownbyid":
                case "dropdownbyid1":

                    // Handle both normal <select> and Chosen dropdowns
                    try {
                        // For Chosen dropdowns: look for the visible span inside the container
                        WebElement chosenText = driver.findElement(By.xpath("//div[@id='" + locatorValue + "_chosen']//span"));
                        actualValue = chosenText.getText().trim();
                    } catch (Exception e) {
                        // Fallback for normal <select>
                        actualValue = element.getText().trim();
                    }
                    break;
                case "user":
                    try {
                        // Locate both radio buttons under the User Access table
                        WebElement yesRadio = driver.findElement(By.xpath("//table[@id='txtEPSSAccessAllowed']//td//input[@id='rdoYes']"));
                        WebElement noRadio = driver.findElement(By.xpath("//table[@id='txtEPSSAccessAllowed']//td//input[@id='rdoNo']"));

                        if (yesRadio.isSelected()) {
                            actualValue = "yes";
                        } else if (noRadio.isSelected()) {
                            actualValue = "no";
                        } else {
                            actualValue = "none";
                        }

                        if (actualValue.equalsIgnoreCase(expectedValue.trim())) {
                            System.out.println("✅ User Access Validation Passed → Expected: [" + expectedValue + "] | Actual: [" + actualValue + "]");
                        } else {
                            System.out.println("❌ User Access Validation Failed → Expected: [" + expectedValue + "] | Actual: [" + actualValue + "]");
                            ScreenshotUtil.takeScreenshot("UserAccessValidationFailed_" + locatorValue);
                        }
                    } catch (Exception e) {
                        System.out.println("⚠️ Error while validating User Access radio buttons → " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;

                case "shiftdropdown":
                    try {
                        WebElement selectedShift = driver.findElement(By.xpath("//span[@class='dropdown-selected']"));
                        actualValue = selectedShift.getText().trim();
                    } catch (Exception e) {
                        System.out.println("⚠️ Unable to find shift dropdown selected text for locator: " + locatorValue);
                    }
                    break;

                case "radio":
                    if (element.isSelected()) {
                        actualValue = element.getAttribute("value").trim();
                    }
                    break;

                default:
                    System.out.println("No validation logic for fieldType: " + fieldType);
                    return;
            }

            if (actualValue.trim().toLowerCase().contains(expectedValue.trim().toLowerCase())) {
                System.out.println("✅ Validation Passed → Expected: [" + expectedValue + "] | Actual: [" + actualValue + "]");
            } else {
                System.out.println("❌ Validation Failed → Expected: [" + expectedValue + "] | Actual: [" + actualValue + "]");
                ScreenshotUtil.takeScreenshot("ValidationFailed_" + locatorValue);
            }

        } catch (Exception e) {
            System.out.println("⚠️ Validation skipped due to error for element: " + locatorValue);
            e.printStackTrace();
        }
    }


    public void fillEmployeeForm(String locatorFile, String dataFile) {
        WebDriver driver = DriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        List<Map<String, String>> locatorList = ExcelUtil.readExcel(locatorFile, "Locators");
        List<Map<String, String>> dataList = ExcelUtil.readExcel(dataFile, "Sheet1");

        Map<String, String> dataRow = dataList.get(0); // first row for now

        for (Map<String, String> locatorRow : locatorList) {
            try {
                String fieldName = locatorRow.get("FieldName");
                String locatorType = locatorRow.get("LocatorType");
                String locatorValue = locatorRow.get("LocatorValue");
                String fieldType = locatorRow.get("FieldType"); // TextBox, Dropdown, etc.
                String tabName = locatorRow.get("TabName");

                // open the tab before interacting
                System.out.println("---Open the Tab---");
                openTab(tabName);
                System.out.println("Locator type: " + locatorValue);
                System.out.println("locatorType = " + locatorType);

                By locator = LocatorHelper.getBy(locatorType, locatorValue);
                if (locator == null) {
                    System.out.println("⚠️ Skipping field: " + fieldName + " (invalid or missing locator)");
                    continue;
                }

                WebElement element = driver.findElement(locator);
                String inputValue = dataRow.get(fieldName);

                // ✅ Skip this field if inputValue is null or empty
                if (inputValue == null || inputValue.trim().isEmpty()) {
                    System.out.println("⚠️ Skipping field: " + fieldName + " (no input value provided)");
                    continue;
                }

                // proceed with action based on field type
                switch (fieldType.trim().toLowerCase()) {
                    case "textbox":
                        String selected = ForCommonAction.textBox(element, inputValue);
                        validateEnteredValue(driver, element, inputValue, fieldType, locatorValue);
                        break;

                    case "dropdown":
                        ForCommonAction.selectChosenDropdown(locatorValue, inputValue);
                        validateEnteredValue(driver, element, inputValue, fieldType, locatorValue);
                        break;

                    case "dropdownbyid":
                        AddEmployee.selectChosenDropdownById(locatorValue, inputValue);
                        validateEnteredValue(driver, element, inputValue, fieldType, locatorValue);
                        break;

                    case "dropdownbyid1":
                        AddEmployee.selectChosenDropdownById1(locatorValue, inputValue);
                        validateEnteredValue(driver, element, inputValue, fieldType, locatorValue);
                        break;

                    case "shiftdropdown":
                        ForCommonAction.shiftDropdown(locatorValue, inputValue);
                        validateEnteredValue(driver, element, inputValue, fieldType, locatorValue);
                        break;

                    case "click":
                        if (inputValue.equalsIgnoreCase("yes")) {
                            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
                            System.out.println("✅ Clicked element = " + element.isSelected());
                        } else {
                            System.out.println("ℹ️ No action for click (value != 'yes')");
                        }
                        break;

                    case "radio":
                        ForCommonAction.selectYesNoOption(locatorValue, inputValue);
                        break;

                    case "user":
                        ForCommonAction.selectUserAccess(inputValue);
                        validateEnteredValue(driver, element, inputValue, fieldType, locatorValue);
                        break;

                    default:
                        System.out.println("⚠️ Unknown field type: " + fieldType);
                }

            } catch (Exception e) {
                ScreenshotUtil.takeScreenshot("Error_" + locatorRow.get("FieldName"));
                e.printStackTrace();
            }
        }

        // finally save
        ForCommonAction.clickSaveButton();
        System.out.println("✅ Saved successfully");
    }


    public void openTab(String tabName) {
        if (tabName == null || tabName.isEmpty()) return;
        try {
            WebDriver driver = DriverManager.getDriver();
            driver.findElement(By.xpath("//a[normalize-space()='" + tabName + "']")).click();
        } catch (Exception e) {
            System.out.println("⚠️ Tab not found: " + tabName);
        }
    }


}

