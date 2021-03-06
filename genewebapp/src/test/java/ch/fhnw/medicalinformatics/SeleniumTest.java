package ch.fhnw.medicalinformatics;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

public class SeleniumTest {
	
	

	@Test
	public void webTest() {
		
		if (!TestSettings.EXECUTE_WEBTESTS) return;
		
		System.setProperty("webdriver.chrome.driver", "D:\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();

		driver.get(TestSettings.URL);
		
		String pageTitle = driver.getTitle();
		
		WebElement soDropdown = driver.findElement(By.id("inputform:searchoption_input"));
		WebElement stInput = driver.findElement(By.id("inputform:searchterm"));
		WebElement retrieveButton = driver.findElement(By.id("inputform:retrieveButton"));
		
		Select searchOption = new Select(soDropdown);
		searchOption.selectByValue("Search by ID");
		
		stInput.sendKeys("70");
		retrieveButton.click();
		
		driver.close();
	}
	
}