import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class AppTest {
	WebDriver driver;
	static final String URL="http://the-internet.herokuapp.com";
	static int count =0;
	
	
	@BeforeClass
	public void setUp(){
		driver = new FirefoxDriver();
		driver.get(URL);
	}
	
	//@Test 
	public void basicAuth(){
	    driver.get("http://admin:admin@the-internet.herokuapp.com/basic_auth");
	    Assert.assertEquals(driver.findElement(By.cssSelector("p")).getText(), "Congratulations! You must have the proper credentials.");;
	}
	
	//@Test
	public void brokenImageVerification() throws ClientProtocolException, IOException{
		
		driver.navigate().to(URL+"/broken_images");
		List <WebElement> images = driver.findElements(By.tagName("img"));
		for(WebElement l : images){
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(l.getAttribute("src"));
			HttpResponse response = client.execute(request);
			if(response.getStatusLine().getStatusCode()!=200){
			  System.out.println(l.getAttribute("src")+" is  a Broken image");
			  count++;
			}
			
		}
		System.out.println(count+" images are broken out of "+images.size());
	}
	
	//@Test
	public void challengingDOM(){
	driver.navigate().to(URL+"/challenging_dom");
	for(int i=0;i<10;i++){
	driver.findElement(By.xpath("//div[@id='content']/div/div/div/div/a")).click();
	List <WebElement> buttons = driver.findElements(By.xpath("//div[@id='content']/div/div/div/div/a"));
	for(WebElement a : buttons){
		Assert.assertTrue(Arrays.asList("baz", "foo", "qux","bar").contains(a.getText()));
		System.out.println((a.getText()+"Text Color is "+a.getCssValue("background-color")));
	}
	System.out.println(driver.findElement(By.id("canvas")).getText());
	System.out.println(driver.findElement(By.id("canvas")).getLocation());
	System.out.println(driver.findElement(By.id("canvas")).getSize());
	 //((JavascriptExecutor)driver).executeScript("document.");
	//To do here is how to get text from canvas and verify. to get canvas text we have perform java script method what exactly i have to use that needs to be checked"
	// How to convert rgba value to hex code
	}
	}
	
	//@Test
	public void checkBoxTest() throws InterruptedException{
		driver.navigate().to(URL+"/checkboxes");
		List <WebElement> l = driver.findElements(By.xpath("//input[@type='checkbox']"));
		for(WebElement a:l){
			a.click();
			System.out.println(a.getAttribute("Checked"));
		}
	
	}
	
	//@Test
	public void contextMenu() throws InterruptedException{
		driver.navigate().to(URL+"/context_menu"); 
        Actions action= new Actions(driver);
        action.contextClick(driver.findElement(By.id("hot-spot"))).build().perform();
        action.sendKeys(Keys.ARROW_DOWN).build().perform();
        action.sendKeys(Keys.ARROW_DOWN).build().perform();
        action.sendKeys(Keys.ARROW_DOWN).build().perform();
        action.sendKeys(Keys.ARROW_DOWN).build().perform();
        action.sendKeys(Keys.ARROW_DOWN).build().perform();
        action.sendKeys(Keys.ENTER).build().perform();
        Alert alert = driver.switchTo().alert();
        Assert.assertEquals(alert.getText(), "You selected a context menu");
        alert.accept();
        Assert.assertEquals(driver.findElement(By.cssSelector("h3")).getText(), "Context Menu");
        
	}
	
	
	//@Test
	public void disappearingElementsTest() throws InterruptedException{
		driver.navigate().to(URL+"/disappearing_elements");
		driver.navigate().refresh();
		List <WebElement> l = driver.findElements(By.cssSelector("li > a"));
		for(WebElement a:l){
			   String bef_1=a.getCssValue("color");
			   String bef_2=a.getCssValue("font-size");
			   Actions action= new Actions(driver);
			   Thread.sleep(5000);
			   action.moveToElement(a).build().perform();
			   Thread.sleep(5000);
			   String aft_1=a.getCssValue("color");
			   String aft_2=a.getCssValue("font-size");
			   Assert.assertFalse(bef_1.equals(aft_1));
			   Assert.assertFalse(bef_2.equals(aft_2));
			   Assert.assertTrue(Arrays.asList("Home","About","Contact Us","Portfolio","Gallery").contains(a.getText()));
		}
	
	}
	
	//@Test
	public void drag_and_dropTest() throws InterruptedException{
		driver.navigate().to(URL+"/drag_and_drop");
                WebElement source= driver.findElement(By.id("column-a"));
        WebElement target= driver.findElement(By.id("column-b"));
       System.out.println(source.getAttribute("style"));
       System.out.println(source.getLocation());
       Actions a= new Actions(driver);
       a.moveByOffset(240, 90);
       a.clickAndHold(source);
       a.moveToElement(source,490, 90);
       a.release();
       a.build().perform();
        //new Actions(driver).dragAndDropBy(source, 400, 400).build().perform();
       // new Actions(driver).dragAndDrop(source, target).build().perform();
        //new Actions(driver).clickAndHold(source).moveToElement(source, 400, 400).build().perform();
        //new Actions(driver).moveToElement(source).clickAndHold().moveToElement(source,215,0).release().build().perform();
        
        Thread.sleep(2000);
		}
	
	//@Test
	public void dropDownTest() throws InterruptedException{
		driver.navigate().to(URL+"/dropdown");
		Select S = new Select(driver.findElement(By.id("dropdown")));
		S.selectByIndex(1);
		System.out.println((S.getFirstSelectedOption()).getText());
		S.selectByValue("1");
		System.out.println((S.getFirstSelectedOption()).getText());
		S.selectByVisibleText("Option 2");
		String s=(S.getFirstSelectedOption()).getText();
		System.out.println(s);
		
	}
	
	//@Test
	public void dynamicContent() throws InterruptedException{
		for(int i =0;i<10;i++){
		driver.navigate().to(URL+"/dynamic_content");
		List <WebElement> images = driver.findElements(By.xpath("//div/img"));
		for(WebElement j : images){
		    System.out.println(j.getAttribute("src").subSequence(84, 85));
			Assert.assertTrue(Arrays.asList("1", "2", "3","4","5","6","7").contains(j.getAttribute("src").subSequence(84, 85)));
			
		}
		}
	}
	
	//@Test
	public void dynamicControls() {
		driver.navigate().to(URL+"/dynamic_controls");
        driver.findElement(By.id("checkbox")).click();
        Assert.assertEquals(driver.findElement(By.id("btn")).getText(), "Remove");
        driver.findElement(By.id("btn")).click();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading"))); 
        String a = driver.findElement(By.id("message")).getText();
        Assert.assertEquals(a, "It's gone!");
        Assert.assertEquals(driver.findElement(By.id("btn")).getText(), "Add");
        driver.findElement(By.id("btn")).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading"))); 
       a = driver.findElement(By.id("message")).getText();
        Assert.assertEquals(a, "It's back!");
	}
	
	//@Test
	public void dynamicLoading() {
		driver.navigate().to(URL+"/dynamic_loading");
		driver.findElement(By.linkText("Example 1: Element on page that is hidden")).click();
		String s = driver.getCurrentUrl();
		Assert.assertEquals(s, "http://the-internet.herokuapp.com/dynamic_loading/1");
		System.out.println(driver.findElement(By.cssSelector("#finish > h4")).getText());
		driver.findElement(By.cssSelector("button")).click();
	    WebDriverWait wait = new WebDriverWait(driver, 10);
	    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading"))); 
		System.out.println(driver.findElement(By.cssSelector("#finish > h4")).getText());
		driver.navigate().to(URL+"/dynamic_loading");
		driver.findElement(By.linkText("Example 2: Element rendered after the fact")).click();
		String s1 = driver.getCurrentUrl();
		Assert.assertEquals(s1, "http://the-internet.herokuapp.com/dynamic_loading/2");
		driver.findElement(By.cssSelector("button")).click();
	    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading"))); 
		System.out.println(driver.findElement(By.cssSelector("#finish > h4")).getText());
	}
	
	@Test
	public void exitIntent() {
		driver.navigate().to(URL+"/exit_intent");
		Assert.assertEquals(driver.getTitle(), "The Internet");
		Actions act = new Actions(driver);
		act.moveByOffset(0, 0);
		Alert alert = driver.switchTo().alert();
		alert.accept();
		

	}
	
	
	@AfterClass
	public void tearUp(){
	    driver.close();
	    driver.quit();
	}
	
	
	
	
	

}
