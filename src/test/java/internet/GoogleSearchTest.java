package internet;

// src/test/java/GoogleSearchTest.java
import internet.SetupTestDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.net.MalformedURLException;

public class GoogleSearchTest {
    public WebDriver driver;

	@BeforeClass(alwaysRun = true)
    @Parameters({"os", "browser", "url", "node"})
    public void setUp(String os, String browser, String url, String node) throws MalformedURLException {
        SetupTestDriver setupTestDriver = new SetupTestDriver(os, browser, url, node);
        driver = setupTestDriver.getDriver();
    }

    @Test
    public void googleTitleTest() {
        // validate page title test
        Assert.assertTrue(driver.getTitle().contentEquals("Google"));
    }

    @Test
    public void googleUrlTest() {
        // validate current url test
        Assert.assertTrue(driver.getCurrentUrl().contains("www.google.com"));
    }

    @Test
    public void googleSearchButtonTest() {
        // basic test to validate that search button is displayed, enabled and it's value
        WebElement searchButtonElement = driver.findElement(By.name("btnK"));
        //Assert.assertTrue(searchButtonElement.isDisplayed());
        Assert.assertTrue(searchButtonElement.isEnabled());
        Assert.assertTrue(searchButtonElement.getAttribute("value").contains("Google Search"));
    }

    @Test
    public void googleFeelingLuckyButtonTest() {
        // basic test to validate that feeling lucky button is displayed, enabled and it's value
        WebElement feelingLuckyElement = driver.findElement(By.name("btnI"));
        //Assert.assertTrue(feelingLuckyElement.isDisplayed());
        Assert.assertTrue(feelingLuckyElement.isEnabled());
        Assert.assertTrue(feelingLuckyElement.getAttribute("value").contains("I'm Feeling Lucky"));
    }

    @Test
    public void googleSearchBox() {
        // basic test to validate that search box displayed and enabled
        WebElement searchElement = driver.findElement(By.name("q"));
        Assert.assertTrue(searchElement.isDisplayed());
        Assert.assertTrue(searchElement.isEnabled());
    }



    @AfterClass(alwaysRun = true)
    public void closeBrowser() {
        driver.quit();
    }

}