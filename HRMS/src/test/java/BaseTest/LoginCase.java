package BaseTest;

import Pages.Employee.AddEmployee;
import Pages.Employee.AddEmployeeFromExcel;
import Pages.Employee.EditEmployee;
import Pages.Login.ForLogin;
import Utils.PropertyReader;
import Utils.Waits;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

public class LoginCase extends Prerequest {
    @Test
    public void case1() {
        WebDriver driver = getDriver();
        ForLogin lg = new ForLogin();

        lg.logInAction("guhaneswaran@abi-tech.com.sg", "Abitech@12345678");

        // Wait for the main page wrapper to appear
        Waits.forVisibility(By.id("page-wrapper"));
        AddEmployee emp = new AddEmployee();
        emp.addemp();
        // Now safe to get the title

        try {
            Thread.sleep(3000);
        } catch (Exception e) {
            System.out.println("e.getMessage() = " + e.getMessage());
        }
        String title = driver.getTitle();
        System.out.println("title = " + title);
    }

    @Test
    public void testAddEmployee() {
        WebDriver driver = getDriver();
        ForLogin lg = new ForLogin();
        lg.logInAction("guhaneswaran@abi-tech.com.sg", "Abitech@12345678");
        AddEmployeeFromExcel employeePage = new AddEmployeeFromExcel();
        employeePage.addemp();
        String inputfile = "D:\\Automation\\Input.xlsx";
        String locatorfile = "D:\\Automation\\Locators.xlsx";
        employeePage.fillEmployeeForm(locatorfile,inputfile);
//        EmployeeFormRunner emp = new EmployeeFormRunner();
//        emp.runner();

    }

    @Test
    public void verification(){
        WebDriver driver = getDriver();
        ForLogin lg = new ForLogin();
        lg.logInAction("ilangosubramaniyan@abi-tech.com.sg", "ckr@17");
        EditEmployee edit = new EditEmployee();
        edit.fetchEmployeeData();
        try{
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Test
    public void editEmployee(){
        WebDriver driver = getDriver();
        ForLogin lg = new ForLogin();
        lg.logInAction("ilangosubramaniyan@abi-tech.com.sg", "ckr@17");
        EditEmployee edit = new EditEmployee();
        edit.editDetails();
        try{
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

        PropertyReader prop = new PropertyReader("D:\\IntelJ_IDE\\Program\\HRMS\\src\\test\\sample.properties");
        String value = prop.get("emailid");
        System.out.println("value = " + value);
            String     value12="0 1234";
        System.out.println("value12 = " + value12);
        System.out.println("with trim"+value12.trim());

    }
}
