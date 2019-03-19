package internet;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

public class PageObject{
	WebDriver driver;
	
    @FindBy(css="p")
    WebElement messageElement;

    @FindBys(@FindBy(tagName="img"))
    List<WebElement> images;
    
    @FindBy(xpath="//div[@id='content']/div/div/div/div/a")
    WebElement challangingDom_Element;
    
    @FindBy(id = "canvas")
    WebElement challangingDom_canvas;
    
    @FindBys(@FindBy(xpath="//div[@id='content']/div/div/div/div/a"))
    List<WebElement> challangingDom_buttons;
    
    

    
}