package Pages.Employee;

import Browser.DriverManager;
import Pages.Common.ForCommonAction;
import Utils.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditEmployee {

    public static String inputfile = "D:\\Automation\\Input.xlsx";
    public static String locaorfile = "D:\\Automation\\Locators.xlsx";

    public static void forEmployeeSelection(String Empcode) {

        WebElement search = Waits.forVisibility(By.xpath("//input[@placeholder='Search']"));
        search.clear();
        search.sendKeys(Empcode);
    }

    public static void openTab(String tabName) {
        if (tabName == null || tabName.isEmpty()) return;
        try {
            WebDriver driver = DriverManager.getDriver();
            driver.findElement(By.xpath("//a[normalize-space()='" + tabName + "']")).click();
        } catch (Exception e) {
            System.out.println("⚠️ Tab not found: " + tabName);
        }
    }

    public static void forSearchButton() {
        WebDriver driver = DriverManager.getDriver();
        driver.findElement(By.id("SearchButton")).click();

    }

    public static String fetchvalueTexbox(By locator) {
        WebElement dailyrate = Waits.forVisibility(locator);
        String value = dailyrate.getAttribute("value").trim();
        return value;
    }

    public static Map<String, String> fetchActiveIncrementRow() {
        WebDriver driver = DriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        Map<String, String> activeRowData = new HashMap<>();

        try {
            // Wait for the table to be visible
            WebElement table = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.id("tblIncrementDetails"))
            );

            // Find the row where Status = 'Active'
            WebElement activeRow = table.findElement(
                    By.xpath(".//tbody/tr[td[normalize-space(text())='Active']]")
            );

            // Get all cells from the active row
            List<WebElement> cells = activeRow.findElements(By.tagName("td"));

            // Safely extract each cell (based on your table’s structure)
            String effectiveStartDate = cells.get(1).getText().trim();
            String basicSalary = cells.get(2).getText().trim();
            String incrementedBasic = cells.get(3).getText().trim();
            String status = cells.get(7).getText().trim();

            // Store in Map
            activeRowData.put("EffectiveStartDate", effectiveStartDate);
            activeRowData.put("BasicSalary", basicSalary);
            activeRowData.put("IncrementedBasicSalary", incrementedBasic);
            activeRowData.put("Status", status);

            Reporter.log("Fetched Active Row Data: " + activeRowData, true);

        } catch (NoSuchElementException e) {
            Reporter.log("No active row found in increment details table", true);
        } catch (Exception e) {
            Reporter.log("Error fetching active row: " + e.getMessage(), true);
        }

        return activeRowData;
    }

    public static String forUser() {
        WebDriver driver = DriverManager.getDriver();
        WebElement yesRadio = driver.findElement(By.id("rdoYes"));
        WebElement noRadio = driver.findElement(By.id("rdoNo"));
        String actualValue = yesRadio.isSelected() ? "yes" : (noRadio.isSelected() ? "no" : "none");
        return actualValue;
    }


    public static void toCancel(String inputValue) {
        WebDriver driver = DriverManager.getDriver();
        if (inputValue.trim().equalsIgnoreCase("yes")) {
            WebElement yesRadio = driver.findElement(By.id("rdoActiveYes"));
            yesRadio.click();
        } else if (inputValue.trim().equalsIgnoreCase("no")) {
            WebElement noRadio = driver.findElement(By.id("rdoActiveNo"));
            noRadio.click();
        }
        driver.switchTo().alert().accept();

    }


    public static String forStatus() {
        WebDriver driver = DriverManager.getDriver();

        WebElement yesRadio = driver.findElement(By.id("rdoActiveYes"));
        WebElement noRadio = driver.findElement(By.id("rdoActiveNo"));

        String actualValue = yesRadio.isSelected() ? "Active" :
                (noRadio.isSelected() ? "Cancel" : "None");

        Reporter.log("Employee status: " + actualValue, true);
        return actualValue;
    }


    public static void openEmp(String empCode) {

        forEmployeeSelection(empCode);
        WebDriver driver = DriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // Wait for table to load
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tblEmps")));

            // Re-locate employee link every time
            By empLocator = By.xpath("//td[@class='footable-first-visible']/a[normalize-space(text())='" + empCode + "']");
            WebElement emp = wait.until(ExpectedConditions.elementToBeClickable(empLocator));

            // Scroll & click with JS (safe for dynamic tables)
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", emp);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", emp);

            Reporter.log("Clicked employee: " + empCode, true);

        } catch (StaleElementReferenceException e) {
            Reporter.log("Stale element, retrying for: " + empCode, true);
            // Retry once
            WebElement emp = driver.findElement(By.xpath("//td[@class='footable-first-visible']/a[normalize-space(text())='" + empCode + "']"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", emp);
        } catch (Exception e) {
            Reporter.log("Employee number not found or not clickable: " + empCode, true);
        }
    }


    public static Map<String, String> forEmpinTable(String empCode) {
        forEmployeeSelection(empCode);
        WebDriver driver = DriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // Wait for table to load
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tblEmps")));

            // Re-locate employee link every time
            By empLocator = By.xpath("//td[@class='footable-first-visible']/a[normalize-space(text())='" + empCode + "']");
            WebElement emp = wait.until(ExpectedConditions.elementToBeClickable(empLocator));

            // Scroll & click with JS (safe for dynamic tables)
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", emp);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", emp);

            Reporter.log("Clicked employee: " + empCode, true);

        } catch (StaleElementReferenceException e) {
            Reporter.log("Stale element, retrying for: " + empCode, true);
            // Retry once
            WebElement emp = driver.findElement(By.xpath("//td[@class='footable-first-visible']/a[normalize-space(text())='" + empCode + "']"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", emp);
        } catch (Exception e) {
            Reporter.log("Employee number not found or not clickable: " + empCode, true);
        }
        // Get the Employee Name
        WebElement empNameField = Waits.forVisibility(By.xpath("//input[@id='txtEmployeeName']"));
        System.out.println("Employee Name founded ");
        System.out.println("id = " + empNameField.getAttribute("id"));
        System.out.println("valule = " + empNameField.getAttribute("value"));


        String EmpName = empNameField.getAttribute("value").trim();
        System.out.println("EmpName = " + EmpName);
        System.out.println("Feteched Employee Value");
        String DOB = fetchvalueTexbox(By.xpath("//input[@id='SDateofBirth']"));
        String personalEmail = fetchvalueTexbox(By.xpath("//input[@id='txtPersonalEmail']"));

        openTab("Employment");
        String actualValue;
        String status = forStatus();

// fetch department
        WebElement depText = Waits.forVisibility(By.xpath("//div[@id='DepartmentID_chosen']//span"));
        String department = depText.getText().trim();

        String depValue;
        if (department == null || department.isEmpty()) {
            depValue = "Nothing selected in DEP";
        } else {
            depValue = department;
        }

        WebElement desText = Waits.forVisibility(By.xpath("//div[@id='drpDesignation_chosen']//span"));
        String designation = desText.getText().trim();

        String desValue;
        if (designation == null || designation.isEmpty()) {
            desValue = "Nothing selected in DES";
        } else {
            desValue = designation;
            System.out.println("Designation: " + desValue);
        }


        String userStatus = forUser();

        if (userStatus.equalsIgnoreCase("yes")) {
            WebElement chosenText = driver.findElement(By.xpath("//div[@id='drpUserRoles_chosen']//span"));
            String selectedRole = chosenText.getText().trim();

            if (selectedRole == null || selectedRole.isEmpty()) {
                actualValue = "Nothing Selected";
            } else {
                actualValue = selectedRole;
            }

            System.out.println("User role: " + actualValue);

        } else if (userStatus.equalsIgnoreCase("no")) {
            actualValue = "No User role";
        } else {
            actualValue = "Not Applicable";
        }


        openTab("Passport Details/ID Details");
        String wpExpiryDate = fetchvalueTexbox(By.xpath("//input[@id='SPermitExpiryDate']"));
        if (wpExpiryDate == null || wpExpiryDate.trim().isEmpty()) {
            wpExpiryDate = "No date are present";
        }

        String passportNumber = fetchvalueTexbox(By.xpath("//input[@id='txtPassportNumber']"));
        String finNumber = fetchvalueTexbox(By.xpath("//input[@id='txtFinNo']"));
        String workPermitNumber = fetchvalueTexbox(By.xpath("//input[@id='txtWorkPermitNo']"));
        String ppExpiryDate = fetchvalueTexbox(By.xpath("//input[@id='SPassportExpiryDate']"));
        if (ppExpiryDate == null || ppExpiryDate.trim().isEmpty()) {
            ppExpiryDate = "No date are present";
        }
        openTab("Pay");
        WebElement dailyrate = Waits.forVisibility(By.xpath("//input[@id='txtSalaryRate']"));
        String amount = dailyrate.getAttribute("value").trim();
        System.out.println("amount = " + amount);
        String basicSalary = fetchvalueTexbox(By.xpath("//input[@id='CustomizeBasicSal']"));

        Map<String, String> activeIncrement = fetchActiveIncrementRow();

        if (!activeIncrement.isEmpty()) {
            System.out.println("Active Increment Row Details:");
            System.out.println("Start Date: " + activeIncrement.get("EffectiveStartDate"));
            System.out.println("Basic Salary: " + activeIncrement.get("BasicSalary"));
            System.out.println("Incremented Basic: " + activeIncrement.get("IncrementedBasicSalary"));
            System.out.println("Status: " + activeIncrement.get("Status"));
        } else {
            System.out.println("No active increment record found.");
        }

        //click  offical and Doj
       /* String dateOFJoining = fetchDoj();
        if (dateOFJoining != null) {
            Reporter.log("Date of Joining: " + dateOFJoining, true);
        } else {
            Reporter.log("Date of joining not able to select ",true);
        }
        String desgination = fetchDesgination();
        String company= fetchcompanyName();
        String isActive= fetchLastPhysicalDate();
*/
        driver.navigate().back();
        try {
            Thread.sleep(900);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Waits.forClickability(By.xpath("//span[normalize-space()='All Employees']")).click();


        // Below method was to Clear the x icon in the main Employee DropDown
//        clearEmployees();
        Map<String, String> empData = new HashMap<>();
        empData.put("Name", EmpName);
        empData.put("Daily Wages", amount);
        empData.put("At Increment Basic Salary", activeIncrement.get("BasicSalary"));
        empData.put("Incremented Basic", activeIncrement.get("IncrementedBasicSalary"));
        empData.put("Status", activeIncrement.get("Status"));
        empData.put("Work Permint Expiry Date", wpExpiryDate);
        empData.put("EmpStatus", status);
        empData.put("Passport Number", passportNumber);
        empData.put("Fin Number", finNumber);
        empData.put("Work Permit Number", workPermitNumber);
        empData.put("PassPor Expiry Date", ppExpiryDate);
        empData.put("Basic Salary", basicSalary);
        empData.put("Date of Birth", DOB);
        empData.put("Personal Email", personalEmail);
        empData.put("User Access", userStatus);
        empData.put("Role", actualValue);
        empData.put("Department", depValue);
        empData.put("Designation", desValue);
        /*
        empData.put("Designation", desgination);
        empData.put("Company", company);
        empData.put("IsActive", isActive);
        empData.put("DOJ", dateOFJoining);
*/
        return empData;

    }

    public static void fetchEmployeeData() {
        AddEmployeeFromExcel.addemp();
        forSearchButton();

        String empfile = "D:\\Automation\\EmployeeListNew.xlsx";
        fetchEmployeeDataFromExcel(empfile, "Sheet1");


    }


    public static void fetchEmployeeDataFromExcel(String excelPath, String sheetName) {
        WebDriver driver = DriverManager.getDriver();

        List<Map<String, String>> empList = ExcelUtil.readExcel(excelPath, sheetName);
        System.out.println("Total employees found in Excel: " + empList.size());
        int rowIndex = 1; // assuming header is in row 0
        Excel.loadExcel(excelPath, sheetName);
        for (Map<String, String> empRow : empList) {
            String empCode = empRow.get("Employee Code");

            if (empCode == null || empCode.trim().isEmpty()) {
                System.out.println("Skipping empty employee code row");
                continue;
            }

            System.out.println("=====================================");
            System.out.println("Fetching details for Employee: " + empCode);
            System.out.println("=====================================");

            try {
                Map<String, String> empDetails = forEmpinTable(empCode);

                if (empDetails != null && !empDetails.isEmpty()) {

                    // ✅ Write values to Excel columns
                    Excel.setCellValue(excelPath, rowIndex, 1, empDetails.get("Name"));                           // Col B
                    Excel.setCellValue(excelPath, rowIndex, 2, empDetails.get("Daily Wages"));                    // Col C
                    Excel.setCellValue(excelPath, rowIndex, 3, empDetails.get("At Increment Basic Salary"));       // Col D
                    Excel.setCellValue(excelPath, rowIndex, 4, empDetails.get("Incremented Basic"));              // Col E
                    Excel.setCellValue(excelPath, rowIndex, 5, empDetails.get("Status"));                         // Col F
                    Excel.setCellValue(excelPath, rowIndex, 6, empDetails.get("Work Permint Expiry Date"));       // Col G
                    Excel.setCellValue(excelPath, rowIndex, 7, empDetails.get("EmpStatus"));                      // Col H
                    Excel.setCellValue(excelPath, rowIndex, 8, empDetails.get("Passport Number"));                // Col I
                    Excel.setCellValue(excelPath, rowIndex, 9, empDetails.get("Fin Number"));                     // Col J
                    Excel.setCellValue(excelPath, rowIndex, 10, empDetails.get("Work Permit Number"));            // Col K
                    Excel.setCellValue(excelPath, rowIndex, 11, empDetails.get("PassPor Expiry Date"));           // Col L
                    Excel.setCellValue(excelPath, rowIndex, 12, empDetails.get("Basic Salary"));// Col M
                    Excel.setCellValue(excelPath, rowIndex, 13, empDetails.get("Date of Birth")); // col N
                    Excel.setCellValue(excelPath, rowIndex, 14, empDetails.get("Personal Email")); // col O
                    Excel.setCellValue(excelPath, rowIndex, 15, empDetails.get("User Access")); //p
                    Excel.setCellValue(excelPath, rowIndex, 16, empDetails.get("Role"));//q
                    Excel.setCellValue(excelPath, rowIndex, 17, empDetails.get("Department"));//r
                    Excel.setCellValue(excelPath, rowIndex, 18, empDetails.get("Designation"));//s
                }

                rowIndex++; // move to next row

            } catch (Exception e) {
                System.out.println("Error fetching data for " + empCode + ": " + e.getMessage());
                rowIndex++;
            }
        }
        Excel.saveExcel(excelPath);
        Excel.closeExcel();

        System.out.println("✅ All employee details written to Excel successfully.");
    }

    public static void cleartextbox(WebElement element) {
        element.click();
        element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        element.sendKeys(Keys.DELETE);
    }



    public static void editDetails() {
        AddEmployeeFromExcel.addemp();

        String editinputFile = "D:\\Automation\\EditInput.xlsx", editLocatorfile = "D:\\Automation\\EditLocators.xlsx";
        editfillEmployeeForm(editLocatorfile, editinputFile);

    }


    private static boolean validateEnteredValueEnhanced(WebDriver driver, WebElement element, String expectedValue, String fieldType, String locatorValue) {
        try {
            String actualValue = "";

            switch (fieldType.toLowerCase()) {
                case "date":
                case "textbox":
                    String value = element.getAttribute("value");
                    actualValue = (value != null && !value.trim().isEmpty())
                            ? value.trim()
                            : element.getText().trim();
                    break;


                case "dropdown":
                case "dropdownbyid":
                case "dropdownbyid1":
                    try {
                        WebElement chosenText = driver.findElement(By.xpath("//div[@id='" + locatorValue + "_chosen']//span"));
                        actualValue = chosenText.getText().trim();
                    } catch (Exception e) {
                        actualValue = element.getText().trim();
                    }

                    // Case-insensitive "contains" comparison for dropdowns only
                    boolean dropdownMatch = actualValue.toLowerCase().contains(expectedValue.trim().toLowerCase());
                    if (dropdownMatch) {
                        System.out.println("✅ Dropdown Validation Passed → Expected: [" + expectedValue + "] | Actual: [" + actualValue + "]");
                    } else {
                        System.out.println("❌ Dropdown Validation Failed → Expected: [" + expectedValue + "] | Actual: [" + actualValue + "]");
                    }
                    return dropdownMatch;

                case "user":
                    WebElement yesRadio = driver.findElement(By.id("rdoYes"));
                    WebElement noRadio = driver.findElement(By.id("rdoNo"));
                    actualValue = yesRadio.isSelected() ? "yes" : (noRadio.isSelected() ? "no" : "none");
                    break;

                case "shiftdropdown":
                    WebElement selectedShift = driver.findElement(By.xpath("//span[@class='dropdown-selected']"));
                    actualValue = selectedShift.getText().trim();
                    break;

                case "radio":
                    if (element.isSelected()) actualValue = element.getAttribute("value").trim();
                    break;

                default:
                    return true; // Skip validation for unknown types
            }

            // Normal (exact) validation for other field types
            boolean match = actualValue.trim().toLowerCase().contains(expectedValue.trim().toLowerCase());
            if (match) {
                System.out.println("✅ Validation Passed → Expected: [" + expectedValue + "] | Actual: [" + actualValue + "]");
            } else {
                System.out.println("❌ Validation Failed → Expected: [" + expectedValue + "] | Actual: [" + actualValue + "]");
            }
            return match;

        } catch (Exception e) {
            System.out.println("⚠️ Validation skipped for locator: " + locatorValue + " → " + e.getMessage());
            return false;
        }
    }




    public static void editfillEmployeeForm(String locatorFile, String dataFile) {

        WebDriver driver = DriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        List<Map<String, String>> locatorList = ExcelUtil.readExcel(locatorFile, "Locators");
        List<Map<String, String>> dataList = ExcelUtil.readExcel(dataFile, "Sheet1");

        List<Map<String, String>> resultList = new ArrayList<>();

        int passCount = 0, failCount = 0;

        for (int empIndex = 0; empIndex < dataList.size(); empIndex++) {
            Map<String, String> dataRow = dataList.get(empIndex);
            String empNumber = dataRow.get("Employee Code");

            // ✅ Optional fallback if cell is blank or missing
            if (empNumber == null || empNumber.trim().isEmpty()) {
                empNumber = "Emp_" + (empIndex + 1);
            }

//            String empNumber = dataRow.getOrDefault("EmployeeNumber", "Emp_" + (empIndex + 1));
            boolean isEmployeePassed = true;
            List<String> failedFields = new ArrayList<>();
            List<String> screenshotPaths = new ArrayList<>();

            System.out.println("\n==============================");
            System.out.println("👤 Starting Employee: " + empNumber);
            System.out.println("==============================");
/*
            // ✅ Click Add Employee Button for each new employee
            try {
                WebElement addBtn = Waits.forVisibility(By.xpath("//a[@class='btn btn-outline btn-success open-btn']"));
                addBtn.click();
                System.out.println("➕ Opened Add Employee form for: " + empNumber);
            } catch (Exception e) {
                System.out.println("⚠️ Unable to click Add Employee button for " + empNumber);
                ScreenshotUtil.takeScreenshot("AddButtonFailed_" + empNumber);
                continue; // skip to next employee
            }

 */
            forSearchButton();
            openEmp(empNumber);

            Map<String, String> result = new HashMap<>();
            result.put("EmployeeNumber", empNumber);

            for (Map<String, String> locatorRow : locatorList) {
                String fieldName = locatorRow.get("FieldName");
                try {
                    String locatorType = locatorRow.get("LocatorType");
                    String locatorValue = locatorRow.get("LocatorValue");
                    String fieldType = locatorRow.get("FieldType");
                    String tabName = locatorRow.get("TabName");

                    openTab(tabName);

                    By locator = LocatorHelper.getBy(locatorType, locatorValue);
                    if (locator == null) {
                        System.out.println("⚠️ Skipping invalid locator for field: " + fieldName);
                        continue;
                    }

                    WebElement element = driver.findElement(locator);
                    String inputValue = dataRow.get(fieldName);

                    if (inputValue == null || inputValue.trim().isEmpty()) {
                        System.out.println("⚠️ Skipping field: " + fieldName + " (empty input)");
                        continue;
                    }

                    switch (fieldType.trim().toLowerCase()) {
                        case "textbox":
                            ForCommonAction.textBox(element, inputValue);
                            break;
                        case "editactiveincrement":
                            if (inputValue.equalsIgnoreCase("yes")) {
                                ForCommonAction.editActiveIncrementIfPresent();
                            } else {
                                System.out.println("No Action Needed ");
                            }
                            break;
                        case "cleardate":
                            cleartextbox(element);
                            break;
                        case "userstatus":
                            toCancel(inputValue);
                            break;
                        case "date":

                            ForCommonAction.datefield(element, inputValue);

                            break;
                        case "dropdown":
                            ForCommonAction.selectChosenDropdown(locatorValue, inputValue);
                            break;
                        case "dropdownbyid":
                            AddEmployee.selectChosenDropdownById(locatorValue, inputValue);
                            break;
                        case "dropdownbyid1":
                            AddEmployee.selectChosenDropdownById1(locatorValue, inputValue);
                            break;
                        case "shiftdropdown":
                            ForCommonAction.shiftDropdown(locatorValue, inputValue);
                            break;
                        case "click":
                            if (inputValue.equalsIgnoreCase("yes")) {
                                wait.until(ExpectedConditions.elementToBeClickable(element)).click();
                            }
                            break;
                        case "radio":
                            ForCommonAction.selectYesNoOption(locatorValue, inputValue);
                            break;
                        case "user":
                            ForCommonAction.selectUserAccess(inputValue);
                            break;
                        default:
                            System.out.println("⚠️ Unknown field type: " + fieldType);
                    }

                    boolean isValid = validateEnteredValueEnhanced(driver, element, inputValue, fieldType, locatorValue);
                    if (!isValid) {
                        isEmployeePassed = false;
                        failedFields.add(fieldName);
                        String ssPath = ScreenshotUtil.takeScreenshot("ValidationFailed_" + fieldName + "_" + empNumber);
                        screenshotPaths.add(ssPath);
                    }

                } catch (Exception e) {
                    isEmployeePassed = false;
                    failedFields.add(fieldName);
                    String ssPath = ScreenshotUtil.takeScreenshot("Error_" + fieldName + "_" + empNumber);
                    screenshotPaths.add(ssPath);
                    System.out.println("❌ Error while filling field: " + fieldName);
                    e.printStackTrace();
                }
            }

            // ✅ Try to save
            try {
                ForCommonAction.clickSaveButton();
                System.out.println("💾 Clicked Save for Employee: " + empNumber);

                String popupMessage = ForCommonAction.handlePopupIfPresent();
                if (popupMessage != null) {
                    isEmployeePassed = false;
                    failedFields.add("Popup Error");
                    result.put("PopupMessage", popupMessage);
                    System.out.println("⚠️ Popup handled → " + popupMessage);
                } else {
                    System.out.println("✅ Saved successfully for Employee: " + empNumber);
                }

            } catch (Exception e) {
                isEmployeePassed = false;
                failedFields.add("Save Button");
                String ssPath = ScreenshotUtil.takeScreenshot("SaveFailed_" + empNumber);
                screenshotPaths.add(ssPath);
            }

            result.put("Result", isEmployeePassed ? "PASS" : "FAIL");
            result.put("FailedFields", failedFields.isEmpty() ? "-" : String.join(", ", failedFields));
            result.put("Screenshots", screenshotPaths.isEmpty() ? "-" : String.join(", ", screenshotPaths));
            result.put("PopupMessage", result.getOrDefault("PopupMessage", "-"));
            result.put("Timestamp", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));

            resultList.add(result);

            if (isEmployeePassed) passCount++;
            else failCount++;
        }

        ExcelUtil.writeExcel(dataFile, "Result", resultList);

        System.out.println("\n===== Test Summary =====");
        System.out.println("✅ Total Employees: " + dataList.size());
        System.out.println("✅ Passed: " + passCount);
        System.out.println("❌ Failed: " + failCount);
        System.out.println("=========================");
    }


}
