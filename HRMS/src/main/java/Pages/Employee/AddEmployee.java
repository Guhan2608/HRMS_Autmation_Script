package Pages.Employee;

import Browser.DriverManager;
import Pages.Common.ForCommonAction;
import Utils.PropertyReader;
import Utils.Waits;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;

public class AddEmployee {
    public static String propertyFile = "D:\\IntelJ_IDE\\Program\\HRMS\\src\\main\\java\\Pages\\Employee\\employeepages.properties";


    public static void selectChosenDropdown1(String dropdownId, String optionText) {
        WebDriver driver = DriverManager.getDriver();
        Actions actions = new Actions(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            // Step 1: Open the Chosen dropdown
            js.executeScript("$('#" + dropdownId + "').trigger('chosen:open');");

            // Step 2: Wait for the dropdown options container to be visible
            WebElement optionsContainer = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.cssSelector("#" + dropdownId + "_chosen .chosen-results")
                    )
            );

            // Step 3: Find all options in the dropdown
            List<WebElement> options = optionsContainer.findElements(By.tagName("li"));
            boolean found = false;

            // Step 4: Loop through options and click the one matching optionText
            for (WebElement option : options) {
                if (option.getText().trim().equalsIgnoreCase(optionText.trim())) {
                    wait.until(ExpectedConditions.elementToBeClickable(option));
                    actions.moveToElement(option).click().perform();
                    found = true;
                    break;
                }
            }

            if (!found) {
                throw new RuntimeException("Option '" + optionText + "' not found in dropdown");
            }

            // Step 5: Optional verification using the hidden <select>
            WebElement hiddenSelect = driver.findElement(By.id(dropdownId));
            String selectedValue = hiddenSelect.getAttribute("value");
            if (selectedValue == null || selectedValue.isEmpty()) {
                throw new RuntimeException("Dropdown selection failed for " + optionText);
            }

            System.out.println("✅ Selected option: " + optionText + " (value=" + selectedValue + ")");

        } catch (Exception e) {
            throw new RuntimeException("Failed to select option '" + optionText + "' from dropdown '" + dropdownId + "': " + e.getMessage(), e);
        }
    }

    public static void selectChosenDropdownById1(String dropdownId, String optionText) {
        WebDriver driver = DriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Actions actions = new Actions(driver);

        try {
            // Step 1: Locate Chosen dropdown container
            WebElement dropdown = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//div[@id='" + dropdownId + "_chosen']")));

            // Step 2: Click dropdown using Actions (JS fallback)
            try {
                WebElement trigger = dropdown.findElement(By.cssSelector("a.chosen-single"));
                actions.moveToElement(trigger).pause(Duration.ofMillis(200)).click().perform();
                System.out.println("✅ Dropdown opened via Actions: " + dropdownId);
            } catch (Exception ex) {
                js.executeScript("arguments[0].click();", dropdown);
                System.out.println("⚡ Used JS executor to click dropdown: " + dropdownId);
            }

            Thread.sleep(300); // Let Chosen animate open

            // Step 3: Try to find input (searchable dropdown)
            List<WebElement> inputs = dropdown.findElements(By.xpath(".//input[@type='text']"));

            if (!inputs.isEmpty()) {
                // Searchable Chosen Dropdown
                WebElement input = inputs.get(0);
                input.clear();
                input.sendKeys(optionText);
                Thread.sleep(300); // wait for filter

                WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//div[@id='" + dropdownId + "_chosen']//li[contains(normalize-space(.),'" + optionText + "')]")));

                actions.moveToElement(option).pause(Duration.ofMillis(100)).click().perform();
                System.out.println("✅ Selected (searchable): " + optionText);
            } else {
                // Non-searchable Chosen Dropdown
                WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//div[@id='" + dropdownId + "_chosen']//li[contains(normalize-space(.),'" + optionText + "')]")));

                js.executeScript("arguments[0].scrollIntoView(true);", option);
                actions.moveToElement(option).pause(Duration.ofMillis(100)).click().perform();
                System.out.println("✅ Selected (non-searchable): " + optionText);
            }

            System.out.println("✅ Final Selected: '" + optionText + "' from dropdown: " + dropdownId);

        } catch (Exception e) {
            System.err.println("❌ Failed to select '" + optionText + "' from dropdown: " + dropdownId);
            e.printStackTrace();
        }
    }


    public static void selectChosenDropdownById(String dropdownId, String optionText) {
        WebDriver driver = DriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // Step 1: Click on the Chosen dropdown to open it
            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@id='" + dropdownId + "_chosen']")));
            try {
                // Try normal click first
                wait.until(ExpectedConditions.elementToBeClickable(dropdown)).click();
                System.out.println("Dropdown Clicked Normally");
            } catch (TimeoutException exception) {
                // If normal click fails, use JavaScript
                System.out.println("Normal click fails");
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", dropdown);
                System.out.println("⚡ Used JS click to open dropdown: " + dropdownId);
                System.out.println("Click Using JS executor");
            }

            // Step 2: Type the option text and select it
            WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[@id='" + dropdownId + "_chosen']//input[@type='text']")));
            input.sendKeys(optionText, Keys.TAB);

            System.out.println("✅ Selected '" + optionText + "' from dropdown: " + dropdownId);
        } catch (Exception e) {
            System.err.println("❌ Failed to select option '" + optionText + "' from dropdown: " + dropdownId);
            e.printStackTrace();
        }
    }


    public static void selectChosenDropdown(String dropdownId, String optionText) {
        WebDriver driver = DriverManager.getDriver();
        Actions actions = new Actions(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Open the Chosen dropdown
        js.executeScript("$('#" + dropdownId + "').trigger('chosen:open');");

        // Wait for the dropdown to be visible
        WebElement chosenDropdown = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#" + dropdownId + "_chosen .chosen-results"))
        );

        // Get all options
        List<WebElement> options = driver.findElements(By.cssSelector("#" + dropdownId + "_chosen .chosen-results li"));
        boolean found = false;

        for (WebElement option : options) {
            if (option.getText().trim().equalsIgnoreCase(optionText.trim())) {
                wait.until(ExpectedConditions.elementToBeClickable(option));
                actions.moveToElement(option).click().perform();
                found = true;
                break;
            }
        }

        if (!found) {
            throw new RuntimeException("Option '" + optionText + "' not found in dropdown");
        }

        // Verify selection in the hidden <select>
        WebElement hiddenSelect = driver.findElement(By.id(dropdownId));
        String selectedValue = hiddenSelect.getAttribute("value");

        if (selectedValue == null || selectedValue.isEmpty()) {
            throw new RuntimeException("Dropdown selection failed for " + optionText);
        }

        System.out.println("✅ Selected option: " + optionText + " (value=" + selectedValue + ")");
    }

    public static void selectDropdownWithMouse(String dropdownId, String optionText) {
        WebDriver driver = DriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Actions actions = new Actions(driver);
        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            // Step 1: Wait for dropdown to be clickable
            driver.findElement(By.xpath("//div[@id='drpNationality_chosen']")).click();
            driver.findElement(By.xpath("//div[@id='drpNationality_chosen']//input[@type='text']")).sendKeys(optionText);


            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id(dropdownId)));

            // Step 2: Scroll into view (optional but safe)
            js.executeScript("arguments[0].scrollIntoView(true);", dropdown);
            Thread.sleep(200);

            // Step 3: Click to open dropdown using Actions
            actions.moveToElement(dropdown).click().perform();
            Thread.sleep(300); // give dropdown time to expand

            // Step 4: Find the option element
            // (Note: options are inside the <select> tag)
            List<WebElement> options = dropdown.findElements(By.tagName("option"));
            boolean found = false;

            for (WebElement option : options) {
                String text = option.getText().trim();
                if (text.equalsIgnoreCase(optionText.trim())) {
                    // Step 5: Scroll option into view and click
                    js.executeScript("arguments[0].scrollIntoView(true);", option);
                    actions.moveToElement(option).click().perform();
                    found = true;
                    break;
                }
            }

            if (!found) {
                throw new RuntimeException("❌ Option '" + optionText + "' not found in dropdown '" + dropdownId + "'");
            }

            // Step 6: Log selected value
            String selectedValue = dropdown.getAttribute("value");
            System.out.println("✅ Selected option: " + optionText + " (value=" + selectedValue + ")");

        } catch (Exception e) {
            throw new RuntimeException(
                    "❌ Failed to select option '" + optionText + "' from dropdown '" + dropdownId + "': "
                            + e.getMessage(), e);
        }
    }

    public static void clickUsingJS(WebDriver driver, By locator) {
        WebElement element = driver.findElement(locator);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", element);  // scroll if hidden
        js.executeScript("arguments[0].click();", element);               // perform JS click
    }

    public static void addemp() {
        WebDriver driver = DriverManager.getDriver();
        PropertyReader prop = new PropertyReader(propertyFile);
        Waits.forVisibility(By.xpath(prop.get("empLink"))).click();
        Waits.forVisibility(By.xpath(prop.get("emppageLink"))).click();
        Waits.forVisibility(By.xpath(prop.get("addButton"))).click();
        WebElement firstName = Waits.forVisibility(By.id(prop.get("firstNameID")));
        String passvalue = "Guhan";
        firstName.sendKeys(passvalue);
        String name = firstName.getAttribute("value");
        Assert.assertEquals(passvalue, name);
        System.out.println("name = " + name);
        WebElement lastName = Waits.forVisibility(By.id(prop.get("lastNameID")));
        String passvalue1 = "S";
        lastName.sendKeys(passvalue1);
        String name1 = lastName.getAttribute("value");
        System.out.println("name1 = " + name1);
        Assert.assertEquals(passvalue1, name1);
        System.out.println("Emp page in " + driver.getTitle());
        /*
        Waits.forVisibility(By.xpath(prop.get("gederDD"))).click();
        Waits.forVisibility(By.xpath(prop.get("GenderInput"))).sendKeys("male");
        By genderListValue = By.xpath(prop.get("Genderlist"));
        String male = ForCommonAction.selectFromDropdown(genderListValue, "Male");
        System.out.println("male = " + male);
*/
        /*
        // Step 1️⃣ - Click dropdown using JS
        clickUsingJS(driver, By.xpath("//div[@id='drpGender_chosen']//a[@class='chosen-single']"));

// Step 2️⃣ - Force open the hidden dropdown (Chosen keeps it hidden by default)
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.querySelector('#drpGender_chosen .chosen-drop').style.display='block';");

// Step 3️⃣ - Now choose the value using JS (example: Male)
        WebElement option = driver.findElement(By.xpath("//div[@id='drpGender_chosen']//li[text()='MALE']"));
        js.executeScript("arguments[0].click();", option);
*/

//        clickUsingJS(driver, By.xpath(prop.get("gederDD")));
//        Waits.forVisibility(By.xpath(prop.get("GenderInput"))).sendKeys("male");
//        By genderListValue = By.xpath(prop.get("Genderlist"));
//        String male = ForCommonAction.selectFromDropdown(genderListValue, "Male");
//        System.out.println("male = " + male);
        // Locate the visible Chosen container and click to open the dropdown
        // Locate the visible clickable element
//        WebElement genderDropdown = driver.findElement(By.cssSelector("#drpGender_chosen a.chosen-single"));

// Use JS click to bypass interactable issues
        try {
            ForCommonAction.selectChosenDropdown("drpGender", "FEMALE");
            System.out.println("✅ Gender selected successfully");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Gender selection failed: " + e.getMessage());
        }

        Waits.forVisibility(By.xpath("//input[@id='SDateofBirth']")).sendKeys("09/07/2001", Keys.TAB);
//
//        selectDropdownWithMouse("drpNationality","Indian");
//        driver.findElement(By.xpath("//div[@id='drpNationality_chosen']")).click();
//        driver.findElement(By.xpath("//div[@id='drpNationality_chosen']//input[@type='text']")).sendKeys("Indian",Keys.TAB);
//        driver.findElement(By.xpath("//li[@class='active-result']")).click();
        selectChosenDropdownById("drpNationality", "Indian");
        Waits.forVisibility(By.xpath("//input[@id='txtPersonalEmail']")).sendKeys("123@gmail.com");
        selectChosenDropdownById("drpCountryCodeTelephone", "91");
//        driver.findElement(By.id("txtPhoneNo")).sendKeys("9003421415");
//        ForCommonAction.textBox(By.id("txtPhoneNo"), "9003421415");
        ForCommonAction.selectChosenDropdown("drpCountry", "India");
        // To Click the Shift
        driver.findElement(By.xpath("//a[normalize-space()='Shift']")).click();
        ForCommonAction.shiftDropdown("drpWorkWeekPattern","5.5");

    }

}
