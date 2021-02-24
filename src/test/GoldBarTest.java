/************************************************************************************
 * Author: Derek Drackley
 * Project File: GoldBarTest.java
 * 
 * Description: Using a website (defined in baseURL variable) to simulate the least
 * amount of weighings for nine gold bars, where one is fake.
 * 
 * Using Selenium to open the webpage and simulate inputs to produce outputs in the
 * most efficent manner.
 * 
 * Ensure that the selenium server jar file is loaded into classpath
 * Version ran for project: selenium-server-standalone-4.0.0-alpha-2.jar
 * 
 * Execution Method: Navigate to /src/test/ folders in location where you have
 * files stored. Run "javac -d ../../ GoldBarTest.java GoldBar.java" in command line,
 * then go back two parent directories and run "java test.GoldBarTest"
 * so ChromeDriver.exe can be recognized from driver directory
 *
 ***********************************************************************************/

package test;

//Importing essential Selenium packages/API
import org.openqa.selenium.By;  
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

//Importing non-Selenium packages
import java.util.Scanner;
import java.util.ArrayList;

//Driver class
public class GoldBarTest {

	public static void main(String[] args) {
		
		WebDriver driver;
		Scanner input = new Scanner(System.in);
		
		//Get user's current directory (when tested by other engineer)
		String projectLocation = System.getProperty("user.dir");
		
		//Creating object of GoldBar class to call public methods
		GoldBar gold = new GoldBar();

		try
		{
			int real_bars = 0;
			int number_of_bars = gold.getSize();

			//Establish the System to be using the Chrome WebDriver for displaying the web page
			System.setProperty("webdriver.chrome.driver", projectLocation+"\\driver\\chromedriver.exe");
			driver = new ChromeDriver();
			
			//Get URL of assignment
			String baseURL = "http://ec2-54-208-152-154.compute-1.amazonaws.com/";
			
			//The WebDriver object will navigate to that link in a new Chrome browser
			driver.get(baseURL);
			
			while(gold.identifyFakeBar())
			{
			
				int containerSize = (number_of_bars - real_bars)/3;
				ArrayList<Integer> leftBowl = new ArrayList<Integer>();
				ArrayList<Integer> rightBowl = new ArrayList<Integer>();
				ArrayList<Integer> extraBowl = new ArrayList<Integer>();
				
				for(int i=0; i<containerSize; i++) {
					int unvisitedNum = gold.insertFind();
					
					if(unvisitedNum != -1) {
						driver.findElement(By.id("left_"+i)).sendKeys(Integer.toString(unvisitedNum));
						leftBowl.add(unvisitedNum);
					}
				}
				
				for(int i=0; i<containerSize; i++) {
					int unvisitedNum = gold.insertFind();
					
					if(unvisitedNum != -1) {
						driver.findElement(By.id("right_"+i)).sendKeys(Integer.toString(unvisitedNum));
						rightBowl.add(unvisitedNum);
					}
				}
				
				for(int i=containerSize*2; i<number_of_bars; i++)
				{
					int unvisitedNum = gold.insertFind();
					
					if(unvisitedNum != -1)
						extraBowl.add(unvisitedNum);
					
				}
				//Currently test examples to ensure numeric input and button functionality works successfully
			
				driver.findElement(By.id("weigh")).click();
			
				//May switch to using data structure to hold each new entry
				WebElement test = driver.findElement(By.id("reset"));
				
				//Testing output
				System.out.printf("The sign found is: %s %n",test.getText());
				
				//Check if sign is currently valid (will be used for determining which numbers to utilize next
				int curr_real = gold.checkSign(test, leftBowl, rightBowl, extraBowl);
				
				real_bars += curr_real;
				
				//Click reset button to flush inputs
				//Had difficulties initially setting the right find element technique due to the other reset ID
				//found when Inspecting, it constantly conflicted
				driver.findElement(By.xpath("//*[@id='root']/div/div[1]/div[4]/button[1]")).click();
				
				gold.resetFakeVisited();

			}
			
			int fakeBarIndex = gold.revealFakeBar();
			//Click on box associated with the fake gold bar
			driver.findElement(By.id("coin_"+fakeBarIndex)).click();
			
			//Checking for reset button functionality without closing the webpage immediately
			System.out.println("Enter any character to proceed.");
			input.next().charAt(0);
			
			//Close all windows of the new browser (vs. using driver.close())
			//Good to exit out of driver object to prevent possible memory leaks
			
			//The driver should be set to null as well, to prevent the ChromeDriver.exe process from stacking
			driver.quit();
			driver = null;
		
			//Checking for exception related to WebDrive failing to connect
		} catch (WebDriverException e) {
			System.out.println("Code: "+e.toString()+" Exception Message : "+e.getMessage());
		}
		
		
		input.close();
	}

}