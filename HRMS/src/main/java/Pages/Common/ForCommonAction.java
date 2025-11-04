package Pages.Common;

import Browser.DriverManager;
import Pages.Employee.EditEmployee;
import Utils.ScreenshotUtil;
import Utils.Waits;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import java.time.Duration;
import java.util.List;

/**
 * <p>It for the common action should define in the Class </p>
 * <p>
 * Like Click Action form the DropDown
 * </p>
 * <p> For Fetch the Calendar Also</p>
 *
 */
public class ForCommonAction {

    /**
     * Selects an option from a custom dropdown (like UL/LI) based on visible text.
     *
     * @param listLocator   Locator for the dropdown list items (e.g., //ul/li).
     * @param valueToSelect The value to match against option text.
     * @return true if the value is found and clicked, false if not found.
     */
    public static String selectFromDropdown(By listLocator, String valueToSelect) {
        List<WebElement> options = Waits.forVisibilityOfAll(listLocator);

        for (WebElement option : options) {
            String passsed = option.getText().trim();
            if (passsed.equalsIgnoreCase(valueToSelect.trim())) {
                option.click();

                return passsed; // returns the actual selected value
            }
        }
        return null; // not found
    }

    public static void selectChosenDropdownValue(WebDriver driver, String dropdownId, String valueToSelect) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Step 1️⃣ Force open dropdown (Chosen.js keeps it hidden)
        WebElement chosenDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@id='" + dropdownId + "']//a[@class='chosen-single']")
        ));
        js.executeScript("arguments[0].click();", chosenDropdown);

        // Step 2️⃣ If not visible, manually set display:block on dropdown
        js.executeScript("document.querySelector('#" + dropdownId + " .chosen-drop').style.display='block';");

        // Step 3️⃣ Now find input
        WebElement inputBox = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@id='" + dropdownId + "']//input")
        ));
        inputBox.sendKeys(valueToSelect);

        // Step 4️⃣ Wait for options and click
        List<WebElement> options = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.xpath("//div[@id='" + dropdownId + "']//li[contains(@class,'active-result')]")
        ));

        for (WebElement opt : options) {
            if (opt.getText().equalsIgnoreCase(valueToSelect)) {
                js.executeScript("arguments[0].click();", opt);
                break;
            }
        }
    }


    public static String partialDD(By listLocator, String valueToSelect) {
        {
            List<WebElement> options = Waits.forVisibilityOfAll(listLocator);

            for (WebElement option : options) {
                String passsed = option.getText().trim();
                String parttxt = passsed.split("-")[0].trim();

                if (parttxt.equals(valueToSelect.trim())) {
                    option.click();

                    return passsed; // returns the actual selected value
                }
            }
            return null; // not found
        }
    }

    /**
     * Get form the calender
     *
     * @param month
     * @param year
     */

    public static void forCalender(String month, String year) {
        while (true) {
            WebElement element = Waits.forVisibility(By.xpath("(//th[@class='datepicker-switch' ])[2]"));
            String displayedText = element.getText().trim();
            String displayedYear = displayedText.replaceAll("[^0-9]", "");
            if (displayedYear.equals(year)) {
                break;
            } else if (Integer.parseInt(displayedYear) < Integer.parseInt(year)) {
                Waits.forClickability(By.xpath(
                        "(//th[@class='next'])[2]"
                )).click();
            } else {
                Waits.forClickability(By.xpath(
                        "(//th[@class='prev'])[2]"
                )).click();
            }
        }
        String xMonth = String.format("//span[@class='month' and text()='%s']", month.substring(0, 3));
        Waits.forVisibility(By.xpath(xMonth)).click();
        Reporter.log("Selected month = " + month, true);
        Reporter.log("Selected year = " + year, true);
    }

    public static void forCalenderWithDay(String day, String month, String year) {
        // Loop until the correct year is displayed
        while (true) {
            WebElement element = Waits.forVisibility(By.xpath("(//th[@class='datepicker-switch'])[2]"));
            String displayedText = element.getText().trim();
            String displayedYear = displayedText.replaceAll("[^0-9]", "");

            if (displayedYear.equals(year)) {
                break;
            } else if (Integer.parseInt(displayedYear) < Integer.parseInt(year)) {
                Waits.forClickability(By.xpath("(//th[@class='next'])[2]")).click();
            } else {
                Waits.forClickability(By.xpath("(//th[@class='prev'])[2]")).click();
            }
        }

        // Select month
        String xMonth = String.format("//span[@class='month' and text()='%s']", month.substring(0, 3));
        Waits.forClickability(By.xpath(xMonth)).click();
        Reporter.log("Selected month = " + month, true);

        // Select day
        String xDay = String.format("//td[@class='day' and text()='%s']", day);
        Waits.forClickability(By.xpath(xDay)).click();
        Reporter.log("Selected date = " + day + "-" + month + "-" + year, true);
    }


    public static void topassAlert(String num) {
        // wait for alert to show up
        Waits.forPresenceofElement(By.cssSelector("div.sweet-alert"));
        Waits.forVisibility(By.cssSelector("div.sweet-alert"));

        // input field inside alert
        By inputAlert = By.xpath("//input[@placeholder='0.00']");
        WebElement element = Waits.forClickability(inputAlert);
        element.clear();
        element.sendKeys(num);

        // confirm button
        By confirmBtn = By.xpath("//button[@class='confirm' and @tabindex='1']");
        WebElement confirm = Waits.forClickability(confirmBtn);
        confirm.click();

        Reporter.log("SweetAlert handled successfully with value: " + num, true);
    }

    public static void shiftDropdown(String dropdownId, String optionText) {
        WebDriver driver = DriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Actions actions = new Actions(driver);

        try {
            // 1️⃣ Normalize option text (append Workweek suffix)
            optionText = optionText.trim() + "-Day Workweek";

            System.out.println("🔽 Selecting '" + optionText + "' from dropdown: " + dropdownId);

            // 2️⃣ Click dropdown trigger (custom div)
            WebElement dropdownTrigger = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class,'dropdown-sin-" + dropdownId + "')]")
            ));
            actions.moveToElement(dropdownTrigger).click().perform();
            System.out.println("✅ Dropdown opened");

            // 3️⃣ Wait for list to appear
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul/li")));

            // 4️⃣ Find target <li> based on visible text
            String optionXpath = "//ul/li[contains(normalize-space(.),'" + optionText + "')]";
            WebElement optionElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(optionXpath)));

            // 5️⃣ Scroll and click via Actions
            js.executeScript("arguments[0].scrollIntoView(true);", optionElement);
            actions.moveToElement(optionElement).click().perform();

            System.out.println("✅ '" + optionText + "' selected successfully");

        } catch (Exception e) {
            System.out.println("❌ Failed to select '" + optionText + "' from dropdown: " + dropdownId);
            e.printStackTrace();
        }
    }


    /**
     * Selects a value from a Chosen.js dropdown (searchable or non-searchable)
     *
     * @param dropdownId the HTML 'id' of the <select> element (not the _chosen div)
     * @param optionText the visible text of the option to select
     */
    public static void selectChosenDropdown(String dropdownId, String optionText) {
        WebDriver driver = DriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Actions actions = new Actions(driver);
        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            js.executeScript("$('#" + dropdownId + "').trigger('chosen:open');");
            // Step 1: Scroll to dropdown and open it
//                System.out.println(driver.findElement(By.cssSelector("#" + dropdownId + "_chosen")).getAttribute("outerHTML"));

            WebElement chosenTrigger;
            try {
                chosenTrigger = wait.until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector("#" + dropdownId + "_chosen a.chosen-single")
                ));
            } catch (Exception e) {
                // fallback: multi-select chosen
                chosenTrigger = wait.until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector("#" + dropdownId + "_chosen .chosen-choices")
                ));
            }

            js.executeScript("arguments[0].scrollIntoView(true);", chosenTrigger);
            js.executeScript("arguments[0].click();", chosenTrigger);
            Thread.sleep(300); // animation delay

            // Step 2: Handle search input (if available)
            List<WebElement> searchInputs = driver.findElements(
                    By.cssSelector("#" + dropdownId + "_chosen .chosen-search input")
            );

            if (!searchInputs.isEmpty()) {
                WebElement searchInput = searchInputs.get(0);
                wait.until(ExpectedConditions.visibilityOf(searchInput));
                searchInput.clear();
                searchInput.sendKeys(optionText);
                Thread.sleep(500);
            }

            // Step 3: Wait for and select matching option
            List<WebElement> options = wait.until(ExpectedConditions
                    .presenceOfAllElementsLocatedBy(
                            By.cssSelector("#" + dropdownId + "_chosen .chosen-results li")
                    ));

            boolean found = false;

            for (WebElement option : options) {
                if (option.getText().trim().equalsIgnoreCase(optionText.trim())) {
                    wait.until(ExpectedConditions.elementToBeClickable(option));
                    actions.moveToElement(option).click().perform();
                    found = true;
                    break;
                }
            }

            if (!found)
                throw new RuntimeException("Option '" + optionText + "' not found in dropdown '" + dropdownId + "'");

            // Step 4: Verify selected value
            WebElement hiddenSelect = driver.findElement(By.id(dropdownId));
            String selectedValue = hiddenSelect.getAttribute("value");
            System.out.println("✅ Selected: " + optionText + " (value=" + selectedValue + ")");

        } catch (Exception e) {
            throw new RuntimeException(
                    "❌ Failed to select option '" + optionText + "' from dropdown '" + dropdownId + "': "
                            + e.getMessage(), e);
        }
    }

    public static void datefield(WebElement element, String value) {
        EditEmployee.cleartextbox(element);
        element.sendKeys(value);
    }

    public static void textBox(WebElement element, String value) {
        try {
            element.click();
            element.clear(); // ✅ clears existing value
            element.sendKeys(value);
            System.out.println("📝 Entered text: " + value);
        } catch (Exception e) {
            System.out.println("⚠️ Unable to enter text: " + e.getMessage());
            throw e;
        }
    }


    public static void selectIncrementOption(String option) {
        // Trim and normalize the input (ignore case)
        WebDriver driver = DriverManager.getDriver();
        option = option.trim().toLowerCase();

        String radioId;
        if (option.equals("yes")) {
            radioId = "rdoIncreamentYes";
        } else if (option.equals("no")) {
            radioId = "rdoIncreamentNo";
        } else {
            throw new IllegalArgumentException("Invalid option: " + option + ". Use 'Yes' or 'No'.");
        }

        WebElement radio = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(By.id(radioId)));
        radio.click();
    }


    public static void editActiveIncrementIfPresent() {
        WebDriver driver = DriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // Wait until increment details table is visible
            WebElement table = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.id("tblIncrementDetails"))
            );

            // Locate the active row
            List<WebElement> activeRows = table.findElements(
                    By.xpath(".//tbody/tr[td[normalize-space(text())='Active']]")
            );

            if (activeRows.isEmpty()) {
                System.out.println("ℹ️ No active increment found — skipping edit.");
                return;
            }

            WebElement activeRow = activeRows.get(0);

            // Find the <a> element (hyperlink) inside that row for editing
            WebElement editLink = activeRow.findElement(By.xpath(".//a[contains(@onclick,'EditIncrementInfo')]"));

            // Scroll into view and click
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", editLink);
            Thread.sleep(500); // small pause for stability

            editLink.click();
            System.out.println("✏️ Clicked edit link for active increment.");

        } catch (NoSuchElementException e) {
            System.out.println("⚠️ No active increment or edit link found.");
        } catch (Exception e) {
            System.out.println("❌ Error while editing active increment: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public static void selectUserAccess(String option) {
        WebDriver driver = DriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        option = option.trim().toLowerCase();

        // Locate inside the table structure
        String xpath;
        if (option.equals("yes")) {
            xpath = "//table[@id='txtEPSSAccessAllowed']//td//input[@id='rdoYes']";
        } else if (option.equals("no")) {
            xpath = "//table[@id='txtEPSSAccessAllowed']//td//input[@id='rdoNo']";
        } else {
            throw new IllegalArgumentException("Invalid option: " + option + ". Use 'Yes' or 'No'.");
        }

        WebElement radio = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));

        try {
            // Try a normal click first
            wait.until(ExpectedConditions.elementToBeClickable(radio)).click();
            System.out.println("Clicked using normal click: " + option);
//            ScreenshotUtil.takeScreenshot("user");
        } catch (Exception e1) {
            try {
                // If normal click fails, try Actions class
                new Actions(driver).moveToElement(radio).click().perform();
                System.out.println("Clicked using Actions class: " + option);
            } catch (Exception e2) {
                // If Actions also fails, use JavaScript click as last fallback
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", radio);
                System.out.println("Clicked using JavaScript click: " + option);
            }
        }
    }

    public static void waitForSuccessPopupAndClickOK() {
        WebDriver driver = DriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        try {
            // Wait for the popup title (Success or similar)
            By titleLocator = By.xpath("//h2[@id='swal2-title' and (contains(translate(text(),'SUCCESS','success'),'success'))]");
            WebElement successTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(titleLocator));

            // Wait for success message (handles variations)
            By messageLocator = By.xpath(
                    "//div[@id='swal2-html-container' and (" +
                            "contains(translate(text(),'SAVED','saved'),'saved') or " +
                            "contains(translate(text(),'SUCCESS','success'),'success')" +
                            ")]"
            );
            WebElement successMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(messageLocator));

            System.out.println("✅ Success popup detected: " + successMsg.getText());

            // Wait for OK button
            By okButtonLocator = By.xpath("//button[contains(@class,'swal2-confirm') and (text()='OK' or text()='Ok' or text()='ok')]");
            WebElement okButton = wait.until(ExpectedConditions.elementToBeClickable(okButtonLocator));

            // Scroll into view
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", okButton);
            Thread.sleep(300);

            try {
                okButton.click();
                System.out.println("✅ Clicked OK button (normal click).");
            } catch (Exception e1) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", okButton);
                System.out.println("✅ Clicked OK button (JS fallback).");
            }

            ScreenshotUtil.takeScreenshot("Success_Popup_OK_Clicked");

        } catch (Exception e) {
            System.out.println("⚠️ Success popup not found or OK button not clickable.");
            ScreenshotUtil.takeScreenshot("Success_Popup_Failed");
            e.printStackTrace();
        }
    }


    public static void selectYesNoOption(String baseId, String option) {
        WebDriver driver = DriverManager.getDriver();
        option = option.trim().toLowerCase();

        String radioId;
        if (option.equals("yes")) {
            radioId = baseId + "Yes";
        } else if (option.equals("no")) {
            radioId = baseId + "No";
        } else {
            throw new IllegalArgumentException("Invalid option: " + option + ". Use 'Yes' or 'No'.");
        }

        WebElement radio = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(By.id(radioId)));
        radio.click();
    }

    public static String handlePopupIfPresent() {
        try {
            WebDriver driver = DriverManager.getDriver();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

            // Wait for popup (if appears)
            WebElement popup = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector(".swal2-popup.swal2-modal.swal2-show")));

            // Get popup title and message
            String title = driver.findElement(By.id("swal2-title")).getText().trim();
            String message = driver.findElement(By.id("swal2-html-container")).getText().trim();

            String fullMessage = title + " - " + message;
            System.out.println("⚠️ Popup Detected: " + fullMessage);

            // Capture screenshot
            String ssPath = ScreenshotUtil.takeScreenshot("Popup_" + title.replaceAll("[^a-zA-Z0-9]", "_"));
            System.out.println("📸 Popup screenshot saved: " + ssPath);

            // Click OK button to close popup
            WebElement okButton = driver.findElement(By.cssSelector(".swal2-confirm"));
            okButton.click();

            // Return popup text to store in Excel
            return fullMessage + " | Screenshot: " + ssPath;

        } catch (TimeoutException e) {
            // No popup found, just continue
            return null;
        } catch (Exception e) {
            System.out.println("⚠️ Error while handling popup: " + e.getMessage());
            return null;
        }
    }

    public static void captureUserErrorBoxIfPresent() {
        WebDriver driver = DriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        try {
            // Wait until the UserError div is visible
            WebElement errorBox = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[@id='UserError']")
            ));

            if (errorBox.isDisplayed()) {
                // Scroll into view so it's clearly visible
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", errorBox);
                Thread.sleep(800); // brief pause for scroll animation

                // Log and take screenshot
                System.out.println("⚠️ Validation error box detected at the top of the screen!");
                ScreenshotUtil.takeScreenshot("UserError_ValidationBox_TopScreen");
            } else {
                System.out.println("✅ No UserError box detected on screen.");
            }
        } catch (TimeoutException te) {
            // Not found within timeout
            System.out.println("✅ No validation error box found (UserError div not visible).");
        } catch (Exception e) {
            System.out.println("⚠️ Could not capture UserError validation box screenshot.");
            e.printStackTrace();
        }
    }


    public static void clickSaveButton() {
        WebDriver driver = DriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Actions actions = new Actions(driver);

        // Use a single best locator first — keep others only as backup
        By[] locators = new By[]{
                By.xpath("//button[contains(@onclick,'ValidateEmployeeInfo')]"),
                By.xpath("//button[@type='button' and contains(.,'Save')]")
        };

        try {
            WebElement saveBtn = null;

            // ✅ Try to find and return the first visible button quickly
            for (By locator : locators) {
                List<WebElement> elements = driver.findElements(locator);
                if (!elements.isEmpty()) {
                    saveBtn = elements.get(0);
                    break;
                }
            }

            if (saveBtn == null)
                throw new NoSuchElementException("Save button not found with known locators.");

            // ✅ Scroll only if not visible on screen
            if (!saveBtn.isDisplayed()) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", saveBtn);
            }

            // ✅ Faster click – directly attempt elementToBeClickable
            try {
                wait.until(ExpectedConditions.elementToBeClickable(saveBtn)).click();
                System.out.println("✅ Clicked Save button normally");
            } catch (Exception e1) {
                // ✅ JS click fallback (skip Actions for speed)
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveBtn);
                System.out.println("✅ Clicked Save button using JavaScript click");
            }

            // ✅ Wait for popup or confirmation
            waitForSuccessPopupAndClickOK();
            captureUserErrorBoxIfPresent();

        } catch (Exception e) {
            System.out.println("❌ Failed to click Save button");
            ScreenshotUtil.takeScreenshot("Save_Button_Click_Failed");
            e.printStackTrace();
        }
    }


}






