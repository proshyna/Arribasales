package testArribaPackage;


import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.annotations.AfterTest;
//import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import static java.lang.System.out;
import static org.junit.Assert.assertEquals;


public class MyFirstTest {


    //private WebDriver driver = new FirefoxDriver();
    static WebDriver driver;

    @Test
    public void testStartWebDriver() {
        System.setProperty("webdriver.chrome.driver", "D:\\SeleniumTest\\chromedriver_win32\\chromedriver.exe");
        driver =  new ChromeDriver();

        //driver.manage().window().maximize();
        StringBuffer verificationErrors = new StringBuffer();
        WebDriverWait myDynamicElement = new WebDriverWait(driver, 30);

        //<LOGIN TESTING: START>
        driver.navigate().to("http://user:greendev0987@arribasales.sandice.net");

        try {
            myDynamicElement.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='w0']//a[contains(@href,'upwork')]")));
        } catch (Exception e) {
            throw new MyFirstTest.TestError("FAIL! Link on UPWORK IS NOT FOUND");
        }

        driver.findElement(By.xpath("//div[@class='auth-clients']//a[contains(@href,'upwork')]")).click();
        //  driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

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
        try {
            myDynamicElement.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='layout']/div[2]/div/h1")));
        } catch (Exception e) {
            throw new MyFirstTest.TestError("FAIL!'Waiting for element present.Element '//div[@id='layout']/div[2]/div/h1' IS NOT FUND"); // Вызов ошибки о невыполненом условии(для уведомления по email)
        }
        try {
            assertEquals("Log in and get to work", driver.findElement(By.xpath("//div[@id='layout']/div[2]/div/h1")).getText());
        } catch (Exception e) {
            throw new MyFirstTest.TestError("FAIL!'Log in and get to work' IS NOT FUND"); // Вызов ошибки о невыполненом условии(для уведомления по email)
            //verificationErrors.append("FAIL!'Log in and get to work' IS NOT FUND");
        }
        //LogIn in with UpWork
        driver.findElement(By.id("login_username")).clear();
        driver.findElement(By.id("login_username")).sendKeys("maria_proshyna");
        driver.findElement(By.id("login_password")).clear();
        driver.findElement(By.id("login_password")).sendKeys("mynewpassword1");
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        driver.switchTo().window(homePage);  // switch back to parent window

        try {
            myDynamicElement.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li/a[contains(@href,'index')]")));
        } catch (Exception e) {
            throw new MyFirstTest.TestError("FAIL!' The Tab 'Search'(Poisk) IS NOT FOUND");
        }
        driver.findElement(By.xpath("//li/a[contains(@href,'index')]")).click();    // Click on menu Item "Poisk"

        //Wait for  string 'Опыт:'
        try {
            myDynamicElement.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='vspacer15']//div//label[contains(text(),'Beginner')]")));
        } catch (Exception e) {
            throw new MyFirstTest.TestError("FAIL! String 'Beginner:'(filter item) IS NOT FOUND");
        }
        //<LOGIN TESTING: FINISH>

        // <TESTING OF LAST SUCCESSFUL SYNCHRONIZATION: START>
        // Get CurrentDate
        Date now = new Date();
        Long currentUnixTime = new Long(now.getTime() / 1000);

        // Get successfulDate
        String SuccessfulDate = driver.findElement(By.xpath("//p")).getAttribute("data-unixtime");

        //Converting successfulDate in UnixTime
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss");
        Date date = null;
        try {
            date = dateFormat.parse(SuccessfulDate);
        } catch (ParseException e) {
            throw new MyFirstTest.TestError("FAIL! ParseExaptionError");
        }
        long successDateUnixTime = date.getTime() / 1000;

        //Count dateResult
        long dateResult = (currentUnixTime - successDateUnixTime);
        if (dateResult > 1800) {   //Seconds
            throw new MyFirstTest.TestError("FAIL! Current Time much then Time of Last successful synchronization more then 30 min !");
        }
        // <TESTING OF LAST SUCCESSFUL SYNCHRONIZATION: FINISH>

        // <FILTER AND SEARCH TESTING: START>
        driver.findElement(By.name("query")).clear();
        driver.findElement(By.name("query")).sendKeys("php");
        driver.findElement(By.xpath("(//input[@name='type[]'])[2]")).click();
        driver.findElement(By.xpath("//form[@id='search-form']//button[@type='submit']")).click();

        //Wait for Searching word
        try {
            myDynamicElement.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@id='search-list']/li//em")));
        } catch (Exception e) {
            throw new MyFirstTest.TestError("FAIL! Searching word IS NOT FOUND (xpath: '//ul[@id='search-list']/li//em')");
        }

        // Check Is the Searching word present
        String XPath = "//ul[@id='search-list']/li//em[contains(text(),'php')]";
        Boolean isElementPresent = driver.findElements(By.xpath(XPath)).size() != 0;
        if (isElementPresent) {
        } else {
            throw new MyFirstTest.TestError("FAIL! Searching word 'php' Is NOT Present On The Page");
        }

        // Verify element "Fixed" Not Present present
        Boolean isFixedPresent = driver.findElements(By.xpath("//strong[contains(text(),'Fixed')]")).size() != 0;
        if (!isFixedPresent) {
        } else {
            throw new MyFirstTest.TestError("FAIL! Payment type 'FIXED' Is Present On The Page, but filtered by 'Hourly'");
        }

        // Assert Is the filter option "Hourly" present
        try {
            assertEquals("Hourly", driver.findElement(By.xpath("//div[@class='pull-left']")).getText());
        } catch (Exception e) {
            throw new MyFirstTest.TestError("FAIL! 'Hourly' NOT FOUND");
        }
        //<FILTER AND SEARCH TESTING: FINISH>

        //Exit from http://arribasales.sandice.net
        driver.findElement(By.xpath("//button[@class='btn btn-default pull-right']")).click();
        driver.navigate().to("https://www.upwork.com");
        try {
            myDynamicElement.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='layout']//span[@class='caret']")));
        } catch (Exception e) {
            throw new MyFirstTest.TestError("FAIL! Upwork menu Log out Is Not Found");
        }
        driver.findElement(By.xpath("//div[@id='layout']//span[@class='caret']")).click();
        driver.findElement(By.xpath("//div[@id='skinny-nav']/ul[2]/li[4]/ul/li[4]/a")).click();

        //Shows all errors which were written in log file
        out.println(verificationErrors.toString());

    }

    @AfterTest
    public void afterTest() {
        driver.quit();
    }


    public class TestError extends RuntimeException {

        public TestError(String message) {  // Конструктор
            super(message);
        }
    }


   @AfterMethod
    public void takeScreenShotOnFailure(ITestResult testResult) throws IOException {
       if (testResult.getStatus() == ITestResult.FAILURE) {
           System.out.println(testResult.getStatus());
           File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
           FileUtils.copyFile(scrFile, new File("D:\\testScreenShot.jpg"));
       }

   }
}

// FileUtils.writeStringToFile(new File("test.txt"), "FAIL! Time is much then 30 sec!");
//verificationErrors.append("FAIL! Targeted Text Box Is NOT Present On The Page");



