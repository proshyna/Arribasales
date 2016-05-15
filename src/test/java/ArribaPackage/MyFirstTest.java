package ArribaPackage;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class MyFirstTest {

    @Test
    public void startWebDriver() {
        WebDriver driver = new FirefoxDriver();
        //driver.manage().window().maximize();
        StringBuffer verificationErrors = new StringBuffer();

        //<LOGIN TESTING: START>
        driver.navigate().to("http://user:greendev0987@arribasales.sandice.net");

        driver.findElement(By.xpath("//div[@class='auth-clients']//a[contains(@href,'upwork')]")).click();


        String homePage = driver.getWindowHandle(); // Store your parent window
        String subWindowHandler = null;

        Set<String> handles = driver.getWindowHandles(); // get all window handles
        Iterator<String> iterator = handles.iterator();
        while (iterator.hasNext()) {
            subWindowHandler = iterator.next();
        }


        driver.switchTo().window(subWindowHandler); // switch to popup window
        // perform operations on popup

        //check "Log in and get to work" text exist

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        try {
            assertEquals("Log in and get to work", driver.findElement(By.xpath("//div[@id='layout']/div[2]/div/h1")).getText());
        } catch (Error e) {
            verificationErrors.append("FAIL!'Log in and get to work' IS NOT FUND");
        }

        //LogIn in with UpWork
        driver.findElement(By.id("login_username")).clear();
        driver.findElement(By.id("login_username")).sendKeys("maria_proshyna");
        driver.findElement(By.id("login_password")).clear();
        driver.findElement(By.id("login_password")).sendKeys("mynewpassword1");
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        driver.switchTo().window(homePage);  // switch back to parent window

        // wait for 60 sec for element "Poisk" displayed

        boolean isPoiskFound = false;
        for (int second = 0; second <= 60; second++) {
            try {
                if (driver.findElement(By.linkText("Поиск")).isDisplayed()) {
                    isPoiskFound = true;
                    break;
                }
            } catch (Exception e) {
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {

            }
        }
        if (isPoiskFound == false) {
            verificationErrors.append("FAIL! Menu Item 'Poisk' HES NOT FOUND");
        }

        driver.findElement(By.linkText("Поиск")).click();    // Click on menu Item "Poisk"

        //Wait for  string 'Опыт:'
        try {
            WebDriverWait wait = new WebDriverWait(driver, 15);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='vspacer15']//div[contains(text(),'Опыт:')]")));
        } catch (Exception e) {
            verificationErrors.append("FAIL! String 'Опыт:' IS NOT FOUND");
        }
        //<LOGIN TESTING: FINISH>

        // <TESTING OF LAST SUCCESSFUL SYNCHRONIZATION: START>
        // Get CurrentDate
        Date now = new Date();
        Long currentUnixTime = new Long(now.getTime() / 1000);

        // Get successfulDate
        String SuccessDate = driver.findElement(By.xpath("//p")).getAttribute("data-unixtime");

        //Converting successfulDate in UnixTime
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss");
        Date date = null;
        try {
            date = dateFormat.parse(SuccessDate);
        } catch (ParseException e) {
            verificationErrors.append("FAIL! ParseExaptionError".toString());
        }
        long successDateUnixTime = date.getTime() / 1000;

        //Count dateResult
        long dateResult = (currentUnixTime - successDateUnixTime);
        if (dateResult > 1800) {  //Seconds
            verificationErrors.append("FAIL! Time is much then 30 sec!");
        }
        // <TESTING OF LAST SUCCESSFUL SYNCHRONIZATION: FINISH>

        // <FILTER AND SEARCH TESTING: START>
        driver.findElement(By.name("query")).clear();
        driver.findElement(By.name("query")).sendKeys("php");
        driver.findElement(By.xpath("(//input[@name='type[]'])[2]")).click();
        driver.findElement(By.xpath("//form[@id='search-form']//button[@type='submit']")).click();

        //Wait for Searching word
        try {
            WebDriverWait wait = new WebDriverWait(driver, 15);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@id='search-list']/li//em")));
        } catch (Exception e) {
            verificationErrors.append("FAIL! Searching word IS NOT FOUND".toString());

        }

        // Check Is the Searching word present
        String XPath = "//ul[@id='search-list']/li//em[contains(text(),'php')]";
        Boolean isElementPresent = driver.findElements(By.xpath(XPath)).size() != 0;
        if (isElementPresent == true) {
        } else {
            verificationErrors.append("FAIL! Targeted Text Box Is NOT Present On The Page".toString());

        }

        // Verify element "Fixed" Not Present present
        Boolean isFixedPresent = driver.findElements(By.xpath("//strong[contains(text(),'Fixed')]")).size() != 0;
        if (isFixedPresent == false) {
        } else {
            verificationErrors.append("FAIL! Payment type 'FIXED'' Is Present On The Page");
        }

        // Assert Is the filter option "Hourly" present
        try {
            assertEquals("Hourly", driver.findElement(By.xpath("//div[@class='pull-left']")).getText());
        } catch (Exception e) {
            verificationErrors.append("FAIL! 'Hourly' NOT FOUND");
        }

        //<FILTER AND SEARCH TESTING: FINISH>

        //Exit from http://arribasales.sandice.net
        driver.findElement(By.xpath("//button[@class='btn btn-default pull-right']")).click();
        driver.navigate().to("https://www.upwork.com");
        driver.findElement(By.xpath("//div[@id='layout']//span[@class='caret']")).click();
        driver.findElement(By.xpath("//div[@id='skinny-nav']/ul[2]/li[4]/ul/li[4]/a")).click();

        //Shows all errors which were written in log file
        System.out.println(verificationErrors.toString());
    }
}





