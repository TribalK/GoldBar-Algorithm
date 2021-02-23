/********************************************************************************
 * Author: Derek Drackley
 * Project: GoldBarTest.java
 * 
 * Description: Using a website (defined in baseURL variable) to simulate the least
 * amount of weighings for nine gold bars, where one is fake.
 * 
 * Using Selenium to open the webpage and simulate inputs to produce outputs in the
 * most efficent manner.
 * 
 * Execution Method: javac -d ../../GoldBarTest.java in src/test/ folders,
 * then running java test.GoldBarTest in parent directories
 * 
 * Status: In-Progress
 ********************************************************************************/

package test;

import org.openqa.selenium.By;  
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.Scanner;

public class GoldBarTest {

	public static void main(String[] args) {
		
		WebDriver driver;
		Scanner input = new Scanner(System.in);
		
		//*[@id='root']/div/div[1]/div[5]/ol/li[1]  - CORRECT USAGE for xpath!
		
		//Get user's current directory (when tested by other engineer)
		String projectLocation = System.getProperty("user.dir");
		
		//Creating object of GoldBar class to call public methods
		GoldBar gold = new GoldBar();

		try
		{
			int count = 1;
			int leftNums = 5;	// Currently for testing purposes
			
			//TODO: Place while loop to determine when we find the true fake gold bar
			//Function is implemented in GoldBar.java but will not be set here until prepared
			
			//Establish the System to be using the Chrome WebDriver for displaying the web page
			System.setProperty("webdriver.chrome.driver", projectLocation+"\\driver\\chromedriver.exe");
			driver = new ChromeDriver();
			
			//Get URL of assignment
			String baseURL = "http://ec2-54-208-152-154.compute-1.amazonaws.com/";
			
			//The WebDriver object will navigate to that link in a new Chrome browser
			driver.get(baseURL);
			
			//Currently test examples to ensure numeric input and button functionality works successfully
			driver.findElement(By.id("left_0")).sendKeys("4");
			driver.findElement(By.id("left_1")).sendKeys("5");
			driver.findElement(By.id("left_2")).sendKeys("6");
			driver.findElement(By.id("left_3")).sendKeys("7");
			driver.findElement(By.id("left_4")).sendKeys("8");
			driver.findElement(By.id("right_0")).sendKeys("0");
			driver.findElement(By.id("right_1")).sendKeys("1");
			driver.findElement(By.id("right_2")).sendKeys("2");
			driver.findElement(By.id("right_3")).sendKeys("3");
			driver.findElement(By.id("weigh")).click();
			
			//Using xpath version of findElement to determine relative path for each new weighings.
			//May switch to using data structure to hold each new entry
			WebElement test = driver.findElement(By.xpath("//*[@id='root']/div/div[1]/div[5]/ol/li["+count+"]"));
			
			//Testing output
			System.out.printf("%s %d%n",test.getText(),test.getText().length());
			
			//Check if sign is currently valid (will be used for determining which numbers to utilize next
			System.out.println(gold.checkSign(leftNums, test));
			
			//Click reset button to flush inputs
			//Had difficulties initially setting the right find element technique due to the other reset ID
			//found when Inspecting, it constantly conflicted
			driver.findElement(By.xpath("//*[@id='root']/div/div[1]/div[4]/button[1]")).click();
			
			//Checking for reset button functionality without closing the webpage immediately
			System.out.println("Enter any character to proceed.");
			char testInput = input.next().charAt(0);
			
			//Close all windows of the new browser (vs. using driver.close())
			//Good to exit out of driver object to prevent possible memory leaks
			driver.quit();
		
			//Checking for exception related to WebDrive failing to connect
		} catch (WebDriverException e) {
			System.out.println("Code: "+e.toString()+" Exception Message : "+e.getMessage());
		}
		input.close();
	}

}