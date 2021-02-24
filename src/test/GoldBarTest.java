/************************************************************************************
 * Author: Derek Drackley
 * Project File: GoldBarTest.java
 * 
 * Description: Using a website (defined in baseURL variable) to simulate the least
 * amount of weighings for nine gold bars, where one is fake.
 * 
 * Given nine gold bars where only one consists of a fake weight, it should only
 * take two tests at minimum to determine the correct fake weight.
 * 
 * Prject is using Selenium to open the webpage and simulate inputs to produce outputs
 * in the most efficent manner. The ChromeDriver will be used for this example.
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
import java.util.List;

//Driver class
public class GoldBarTest {

	public static void main(String[] args) {
		
		//Create WebDriver object
		WebDriver driver;
		Scanner input = new Scanner(System.in);
		
		//Get user's current directory (when tested by other engineer)
		String projectLocation = System.getProperty("user.dir");
		
		//Creating object of GoldBar class to call public methods
		GoldBar gold = new GoldBar();

		try
		{
			//Initialized variables that will aid with WebDriver process
			int real_bars = 0;
			int number_of_bars = gold.getSize();
			int count = 1;

			//Establish the System to be using the Chrome WebDriver for displaying the web page
			System.setProperty("webdriver.chrome.driver", projectLocation+"\\driver\\chromedriver.exe");
			driver = new ChromeDriver();
			
			//Get URL of assignment
			String baseURL = "http://ec2-54-208-152-154.compute-1.amazonaws.com/";
			
			//The WebDriver object will navigate to that link in a new Chrome browser
			driver.get(baseURL);
			
			List<WebElement> elements = new ArrayList<WebElement>();
			
			//Loop will run until the fake bar of the group has been identified
			//The method in GoldBar.java returns false once one or fewer bars have been identified.
			while(gold.identifyFakeBar())
			{
				//Take size of container based on total number of bars and 
				int containerSize = (number_of_bars - real_bars)/3;
				
				//Won't be an issue in our current setup, however was accounting for the possibility
				//of other number_of_bar values being used. If it were using an example such as 4 bars,
				//it would risk the possibility of infinitely looping because there wouldn't be enough
				//bars to fill in the first two ArrayLists.
				if((containerSize == 0) && (number_of_bars-real_bars)%3 == 2)
					containerSize = 1;
					
				//Creating three ArrayLists, one to hold the values for the two bowls and extra values.
				ArrayList<Integer> leftBowl = new ArrayList<Integer>();
				ArrayList<Integer> rightBowl = new ArrayList<Integer>();
				ArrayList<Integer> extraBowl = new ArrayList<Integer>();
				
				//Filling leftBowl container up to containerSize
				for(int i=0; i<containerSize; i++) {
					//find unvisited value
					int unvisitedNum = gold.insertFind();
					
					//Element is added based on the id noted by it's index and the first unvisited value
					//Then the value is added to the bowl
					driver.findElement(By.id("left_"+i)).sendKeys(Integer.toString(unvisitedNum));
					leftBowl.add(unvisitedNum);
				}
				
				//Filling rightBowl container up to containerSize
				for(int i=0; i<containerSize; i++) {
					//find unvisited value
					int unvisitedNum = gold.insertFind();
					
					//Element is added based on the id noted by it's index and the first unvisited value
					//Then the value is added to the bowl
					driver.findElement(By.id("right_"+i)).sendKeys(Integer.toString(unvisitedNum));
					rightBowl.add(unvisitedNum);
				}
				
				//Remaining items are 
				for(int i=containerSize*2; i<number_of_bars-real_bars; i++)
				{
					//find unvisited value
					int unvisitedNum = gold.insertFind();
					
					//Precaution since insertFind can return -1, however this shouldn't occur normally given our setup
					//This shouldn't occur with the first two loops 
					if(unvisitedNum != -1)
						extraBowl.add(unvisitedNum);
				}
			
				//Weigh button is clicked to query weighing list
				driver.findElement(By.id("weigh")).click();
				
				//Using count, add the next table's element to the elements list.
				//(could not get it working with just grabbing the full ordered list xpath after the while loop),
				//so I am using a counter variable to keep track of each new weigh entry
				elements.add(driver.findElement(By.xpath("//*[@id='root']/div/div[1]/div[5]/ol/li["+count+"]")));
			
				//May switch to using data structure to hold each new entry
				WebElement test = driver.findElement(By.id("reset"));
				
				//The checkSign() method determines which values from the bowls have been proven to be real gold bars
				//Returns an integer of converted bars from that run
				int curr_real = gold.checkSign(test, leftBowl, rightBowl, extraBowl);
				
				//The number of real bars is accumulated into a total, this total is used for the next run
				//if the identifyFakeBar method returns true
				real_bars += curr_real;
				
				//Click reset button to flush inputs
				//Had difficulties initially setting the right find element technique due to the other reset ID
				//found when Inspecting, it constantly conflicted
				driver.findElement(By.xpath("//*[@id='root']/div/div[1]/div[4]/button[1]")).click();
				
				//Resets visited boolean values of bars that were left unproven
				gold.resetFakeVisited();
				
				//Increment count at the end of process
				count++;
			}
			
			System.out.println("Yay! You find it!");
			
			System.out.println("All weighings:");
			
			//Display full list of weighings from page
			for(WebElement weighings : elements)
				System.out.println(weighings.getText());
			
			//Get index of only remaining fake gold bar
			int fakeBarIndex = gold.revealFakeBar();
			
			//Click on box associated with the fake gold bar
			driver.findElement(By.id("coin_"+fakeBarIndex)).click();
			
			System.out.printf("The fake bar is %d%n%n", fakeBarIndex);
			
			//Keeping the webpage open for the test engineer to examine the page before they decide to close it.
			System.out.println("Enter any character, then press enter to proceed.");
			input.next().charAt(0);
			
			System.out.println("The driver will be closing. Please wait a few moments for the process to complete.");
			//Closes all windows of the new browser and
			//Exit out of driver object to prevent possible memory leaks
			driver.close();
			driver.quit();
			
			//The driver should be set to null as well, to prevent the ChromeDriver.exe process from stacking
		
			//Checking for exception related to WebDrive failing to connect
		} catch (WebDriverException e) {
			System.out.println("Code: "+e.toString()+" Exception Message : "+e.getMessage());
		}
		
		input.close();
	}

}