/************************************************************************************
 * Author: Derek Drackley
 * Project File: GoldBar.java
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

//Import packages needed for certain lines to work
import org.openqa.selenium.WebElement;
import java.util.ArrayList;

//GoldBar class - object-oriented class for determine validity of all gold bars
public class GoldBar {
	//9 bars in total for this simulated example
	private final int number_of_bars = 9; 
	
	//creating boolean array that will mark values as used so that no duplicate inputs will be made
	//during the weighing process
	private boolean visited[] = new boolean[number_of_bars];
	
	//creating a second boolean array that will mark values true once determined it is not fake
	private boolean realBar[] = new boolean[number_of_bars];
	
	//Constructor 
	//initializes each value of both arrays to false
	public GoldBar() {
		for(int i=0; i<number_of_bars; i++) {
			visited[i] = false;
			realBar[i] = false;
		}
	}
	
	//getSize() method
	//parameters: none
	//Returns number of bars
	public int getSize() {
		return number_of_bars;
	}
	
	//insertFind() method
	//parameters: none
	//Finds first unvisited bar that has not been marked as real
	//Returns index of current bar, otherwise return -1
	public int insertFind() {
		for(int j=0; j<number_of_bars; j++) {
			if(!realBar[j] && !visited[j]) {
				visited[j] = true;

				return j;
			}
		}
		return -1;
	}
	
	//identifyFakeBar() method
	//parameters: none
	//Check how many bars are still undetermined
	//Returns true if there are more than one undetermined fake bars, returns false if 1 or less (as extra precaution)
	public boolean identifyFakeBar() {
		//Counter is set for number of undetermined bars
		int numFake = 0;
		for(int i=0; i<number_of_bars; i++)
		{
			//Increment counter if it is not yet determined
			if(!realBar[i])
				numFake++;
		}
		
		return numFake > 1;
	}
	
	//revealFakeBar() method
	//parameters: none
	//Used after identifyFakeBar() has been determined as false, reveals the only fake bar left undetermined in the realBar array
	public int revealFakeBar() {
		for(int i=0; i<number_of_bars; i++)
		{
			//return index of fake bar
			if(!realBar[i])
				return i;
		}
		
		return -1;
	}
	
	//checkSign() method
	//parameters: WebElement of comparison symbol, the three ArrayLists used to hold values inside and outside of the bowls
	//Counter value is initialized to determine how many bars in the current run have been guaranteed to be real
	//Returns counter value
	public int checkSign(WebElement test, ArrayList<Integer> leftBowl, ArrayList<Integer> rightBowl, ArrayList<Integer> extraBowl) {
		
		//Initializing counter
		int real_count = 0;
		
		//The following if-else statements compares WebElement as a string value against all possible outcomes
		//The answer will determine which bowls hold the gold bars are confirmed to be real
		
		if(test.getText().contains("<")) {
			setRealBars(rightBowl);
			setRealBars(extraBowl);
			real_count = rightBowl.size()+extraBowl.size();
		}
			
		else if(test.getText().contains(">")) {
			setRealBars(leftBowl);
			setRealBars(extraBowl);
			real_count = leftBowl.size()+extraBowl.size();
		}
		
		else if(test.getText().contains("=")) {
			setRealBars(leftBowl);
			setRealBars(rightBowl);
			real_count = leftBowl.size()+rightBowl.size();
		}
		
		//This is mainly for debug purposes, it should not normally come across this line during execution
		else {
			System.out.println("Nothing worked.");
		}
		
		return real_count;
	}
	
	//setRealBars() method
	//parameters: current ArrayList bowl
	//Changes the boolean value for the gold bar inside of the bowl to true, no longer having to use the bar again
	public void setRealBars(ArrayList<Integer> bowl) {
		for(int i=0; i<bowl.size(); i++)
		{
			//get value of bowl from index
			int realBarVal = bowl.get(i);
			
			//set realBar boolean value to true
			realBar[realBarVal] = true;
		}
	}

	//resetFakeVisited() method
	//parameters: none
	//Resets the visited boolean values for the remaining bars that have yet to be proven real.
	//This is to ensure the values are still usable when the new ArrayLists are filled again.
	public void resetFakeVisited() {
		for(int i=0; i<visited.length; i++)
		{
			if(!realBar[i])
				visited[i] = false;
		}
	}
}