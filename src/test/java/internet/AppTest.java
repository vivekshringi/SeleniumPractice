package internet;

import java.awt.AWTException;
import java.awt.RenderingHints.Key;
import java.awt.Robot;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

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
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class AppTest {
	WebDriver driver;
	static final String URL="http://localhost:9292";
	static int count =0;
	String downloadLoc = "/Users/vivshr/Downloads/";
	
	
	@BeforeClass
	public void setUp(){
		
		System.setProperty("webdriver.gecko.driver","geckodriver");
		FirefoxOptions options= new FirefoxOptions();
		FirefoxProfile profile = new FirefoxProfile();
		profile.setAcceptUntrustedCertificates(true);
		profile.setAssumeUntrustedCertificateIssuer(false);
		//Set Location to store files after downloading.
		profile.setPreference("browser.download.dir", downloadLoc);
		profile.setPreference("browser.download.folderList", 2);
 
		//Set Preference to not show file download confirmation dialogue using MIME types Of different file extension types.
		profile.setPreference("browser.helperApps.neverAsk.saveToDisk", 
		    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;txt"); 
		profile.setPreference("browser.helperApps.neverAsk.saveToDisk","application/pdf,application/x-pdf,application/octet-stream");
		profile.setPreference( "browser.download.manager.showWhenStarting", false );
		profile.setPreference( "pdfjs.disabled", true );
		
		profile.setPreference("geo.enabled", true);
		profile.setPreference("geo.provider.use_corelocation", false);
		profile.setPreference("geo.prompt.testing", false);
		profile.setPreference("geo.prompt.testing.allow", false);
		
        options.setProfile(profile);
        driver = new FirefoxDriver(options);
        driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
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
	// This scenarios is not working with java though support is available using ruby
	//@Test
	public void contextMenu() throws InterruptedException{
		driver.navigate().to(URL+"/context_menu"); 
        Actions action= new Actions(driver);
        action.contextClick(driver.findElement(By.id("hot-spot")))
        .sendKeys(Keys.ARROW_DOWN)
        .sendKeys(Keys.ARROW_DOWN)
        .sendKeys(Keys.ARROW_DOWN)
        .sendKeys(Keys.ARROW_DOWN)
        .sendKeys(Keys.ENTER)
        .build().perform();
        Thread.sleep(2000);
        Alert alert = driver.switchTo().alert();
        Assert.assertEquals(alert.getText(), "You selected a context menu");
        alert.accept();
        Assert.assertEquals(driver.findElement(By.cssSelector("h3")).getText(), "Context Menu");
        Thread.sleep(10000);
        
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
			   action.moveToElement(a).build().perform();
			   String aft_1=a.getCssValue("color");
			   String aft_2=a.getCssValue("font-size");
			   Assert.assertFalse(bef_1.equals(aft_1));
			   Assert.assertFalse(bef_2.equals(aft_2));
			   Assert.assertTrue(Arrays.asList("Home","About","Contact Us","Portfolio","Gallery").contains(a.getText()));
		}
	
	}
	
	//@Test //This scenario is not working and needs more investigation
	public void drag_and_dropTest() throws InterruptedException, FileNotFoundException{
		driver.navigate().to(URL+"/drag_and_drop");
		WebElement source= driver.findElement(By.id("column-a"));
        WebElement target= driver.findElement(By.id("column-b"));
       
        Actions a= new Actions(driver);
        a.dragAndDrop(source, target).build().perform();
       
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		Scanner sc = new Scanner(new FileInputStream(new File("dnd.js")));
		String inject = ""; 
		    while (sc.hasNext()) {          
		        String[] s = sc.next().split("\r\n");   
		        for (int i = 0; i < s.length; i++) {
		            inject += s[i];
		            inject += " ";
		        }           
		    } 
		    System.out.println(inject);
		    js.executeAsyncScript(inject);
		    
        Thread.sleep(2000);  
       Assert.assertEquals(source.getText(),"B");  
       Assert.assertEquals(target.getText(),"A");
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
		    System.out.println(j.getAttribute("src").subSequence(72, 73));
			Assert.assertTrue(Arrays.asList("1", "2", "3","4","5","6","7").contains(j.getAttribute("src").subSequence(72, 73)));
			
		}
		}
	}
	
	//@Test
	public void dynamicControls() {
		driver.navigate().to(URL+"/dynamic_controls");
        driver.findElement(By.id("checkbox")).click();
        Assert.assertEquals(driver.findElement(By.cssSelector("#checkbox-example > button")).getText(), "Remove");
        driver.findElement(By.cssSelector("#checkbox-example > button")).click();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading"))); 
        String a = driver.findElement(By.id("message")).getText();
        Assert.assertEquals(a, "It's gone!");
        Assert.assertEquals(driver.findElement(By.cssSelector("#checkbox-example > button")).getText(), "Add");
        driver.findElement(By.cssSelector("#checkbox-example > button")).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading"))); 
       a = driver.findElement(By.id("message")).getText();
        Assert.assertEquals(a, "It's back!");
	}
	
	//@Test
	public void dynamicLoading() {
		driver.navigate().to(URL+"/dynamic_loading");
		driver.findElement(By.linkText("Example 1: Element on page that is hidden")).click();
		String s = driver.getCurrentUrl();
		Assert.assertEquals(s, "http://localhost:9292/dynamic_loading/1");
		System.out.println(driver.findElement(By.cssSelector("#finish > h4")).getText());
		driver.findElement(By.cssSelector("button")).click();
	    WebDriverWait wait = new WebDriverWait(driver, 10);
	    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading"))); 
		System.out.println(driver.findElement(By.cssSelector("#finish > h4")).getText());
		driver.navigate().to(URL+"/dynamic_loading");
		driver.findElement(By.linkText("Example 2: Element rendered after the fact")).click();
		String s1 = driver.getCurrentUrl();
		Assert.assertEquals(s1, "http://localhost:9292/dynamic_loading/2");
		driver.findElement(By.cssSelector("button")).click();
	    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading"))); 
		System.out.println(driver.findElement(By.cssSelector("#finish > h4")).getText());
	}
	
	//@Test(groups = {"inProgeress"}) //Not working 
	public void exitIntent() throws AWTException {
		driver.navigate().to(URL+"/exit_intent");
		Assert.assertEquals(driver.getTitle(), "The Internet");
		/*Actions act = new Actions(driver);
		act.moveByOffset(0, 0).build().perform();*/
		Robot r = new Robot();
		r.mouseMove(0, 0);
		r.delay(1000);
		
		Alert alert = driver.switchTo().alert();
		alert.accept();
	}
	
	//@Test
	public void testFileDownload() throws InterruptedException {
		driver.navigate().to(URL+ "/download");
	    WebElement W = driver.findElement(By.cssSelector("#content > div > a:nth-child(2)"));
		System.out.println(downloadLoc + W.getText());
		W.click();
		Thread.sleep(2000);
		File downloadedFile = new File(downloadLoc + W.getText());
	    Assert.assertTrue(downloadedFile.exists());
	}
	
	//@Test
	public void testFileUpload() {
		driver.navigate().to(URL+ "/upload");
		String filePath = System.getProperty("user.dir") + "/_50.png";
		System.out.println(filePath);
		driver.findElement(By.id("file-upload")).sendKeys(filePath);
		driver.findElement(By.id("file-submit")).click();
		String upload_file = driver.findElement(By.id("uploaded-files")).getText();
		Assert.assertEquals(upload_file, "_50.png");
	}
	
	//@Test
	public void floatingMenu() throws InterruptedException {
		driver.navigate().to(URL+"/floating_menu");
		driver.manage().window().maximize();
		JavascriptExecutor js = (JavascriptExecutor) driver;
		String script = "window.scrollBy(0,1000)";
		js.executeScript(script);
		 List<WebElement> topMenuItems = driver.findElements(By.cssSelector("li"));
		 for(WebElement element : topMenuItems){
			   System.out.println(element.getText());
			   element.click();
			}
	}
	
	//@Test SMTP needs to configured
	public void forgotPassword() throws InterruptedException {
		driver.navigate().to(URL+"/forgot_password");
		driver.findElement(By.id("email")).sendKeys("hello@giikihello.com");
		driver.findElement(By.cssSelector(".icon-2x.icon-signin")).click();
	    Thread.sleep(10000);
	}
	
    //@Test
    public void formAuthentication() {
    	driver.navigate().to(URL+"/login");
    	driver.findElement(By.id("username")).sendKeys("tomsmith");
    	driver.findElement(By.id("password")).sendKeys("SuperSecretPassword!");
    	driver.findElement(By.cssSelector("button.radius")).click();
    	String s = driver.findElement(By.id("flash")).getText();
    	Assert.assertEquals(s, "You logged into a secure area!\n" + 
    			"×");
    }
    
    //@Test
    public void nestedFrameTest() {
    	driver.navigate().to(URL+"/nested_frames");
    	driver.switchTo().frame("frame-top");
    	driver.switchTo().frame("frame-left");
    	WebElement w = driver.findElement(By.tagName("body"));
    	Assert.assertEquals(w.getText(), "LEFT");
    	driver.switchTo().parentFrame();
    	driver.switchTo().frame("frame-middle");
    	w = driver.findElement(By.tagName("body"));
     	Assert.assertEquals(w.getText(), "MIDDLE");
    	driver.switchTo().parentFrame();
    	driver.switchTo().frame("frame-right");
    	w = driver.findElement(By.tagName("body"));
    	Assert.assertEquals(w.getText(), "RIGHT");
    	driver.switchTo().parentFrame();
    	driver.switchTo().parentFrame();
    	driver.switchTo().frame("frame-bottom");
    	w = driver.findElement(By.tagName("body"));
    	Assert.assertEquals(w.getText(), "BOTTOM");
    }
    
    //@Test
    public void iFrameTest() throws InterruptedException {
    	driver.navigate().to(URL+"/iframe");
    	driver.switchTo().frame("mce_0_ifr");
    	String a = driver.findElement(By.cssSelector("#tinymce")).getText();
    	System.out.println(a);
    	driver.findElement(By.cssSelector("#tinymce")).clear();
    	driver.findElement(By.cssSelector("#tinymce")).sendKeys("ALL GOOD");
    	Actions actionObj = new Actions(driver);
    	actionObj.keyDown(Keys.CONTROL)
    	         .sendKeys(Keys.chord("A"))
    	         .keyUp(Keys.CONTROL)
    	         .perform();
    	driver.switchTo().parentFrame();
    	driver.findElement(By.cssSelector("#mceu_3 > button > i")).click();
    	a=driver.findElement(By.cssSelector("#mceu_29")).getText();
    	System.out.println(a);
    	Assert.assertEquals(a, "p » strong");
    	driver.findElement(By.id("mceu_15-open")).click();
    	WebDriverWait wait = new WebDriverWait(driver, 10);
    	WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".mce-text")));
    	element.click();
    	a=driver.findElement(By.cssSelector("#mceu_29")).getText();
    	Assert.assertEquals(a, "p");
    	driver.switchTo().frame("mce_0_ifr");
    	a = driver.findElement(By.cssSelector("#tinymce")).getText();
     	Assert.assertEquals(a, ""); 	
    }
    
    @Test //To be done  
    public void getLocationTest() {
    	driver.navigate().to(URL+"/geolocation");
    	driver.findElement(By.cssSelector("#content > div > button")).click();
    	WebDriverWait wait = new WebDriverWait(driver, 10);
    	WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#map-link")));
    	element.click();
    	String latValue = driver.findElement(By.id("lat-value")).getText();
    	String longValue = driver.findElement(By.id("long-value")).getText();
    	System.out.println(latValue);
    	System.out.println(longValue);
    }
    
   @Test //To be done 
    public void getHorizontalSliderTest() throws InterruptedException {
    	driver.navigate().to(URL+"/horizontal_slider");
    	WebElement slider = driver.findElement(By.cssSelector("#content > div > div"));
    	 Actions move = new Actions(driver);
    	 move.dragAndDropBy(slider, 100, 0); 	Thread.sleep(4000);
    }
	@AfterClass
	public void tearUp(){
	    driver.quit();
	}
	
	
	
	
	

}
