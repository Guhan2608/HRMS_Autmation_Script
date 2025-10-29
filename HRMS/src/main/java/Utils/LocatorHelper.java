package Utils;

import org.openqa.selenium.By;

public class LocatorHelper {

    public static By getBy(String locatorType, String locatorValue) {
        // Handle null or empty inputs gracefully
        if (locatorType == null || locatorType.trim().isEmpty() ||
                locatorValue == null || locatorValue.trim().isEmpty()) {
            System.out.println("⚠️ Skipping: locatorType or locatorValue is null/empty.");
            return null; // don't throw exception
        }

        switch (locatorType.toLowerCase()) {
            case "id":
                return By.id(locatorValue);
            case "name":
                return By.name(locatorValue);
            case "xpath":
                return By.xpath(locatorValue);
            case "css":
                return By.cssSelector(locatorValue);
            case "classname":
                return By.className(locatorValue);
            case "linktext":
                return By.linkText(locatorValue);
            case "partiallinktext":
                return By.partialLinkText(locatorValue);
            case "tagname":
                return By.tagName(locatorValue);
            default:
                System.out.println("⚠️ Invalid locator type: " + locatorType);
                return null;
        }
    }
}
