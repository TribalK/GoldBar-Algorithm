/************************************************************************************
 * Author: Derek Drackley
 * Project: GoldBarTest.java
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

import org.openqa.selenium.WebElement;
import java.util.ArrayList;

//GoldBar class - object-oriented class for determine validity of all gold bars
public class GoldBar {
	private final int number_of_bars = 9; 
	
	//visited array will mark values as used so that no duplicate inputs will be made
	private boolean visited[] = new boolean[number_of_bars];
	
	//fakeBar array will mark values true once determined it is not fake
	private boolean fakeBar[] = new boolean[number_of_bars];
	
	//Constructor initializes each value of both arrays to false
	public GoldBar() {
		for(int i=0; i<number_of_bars; i++) {
			visited[i] = false;
			fakeBar[i] = false;
		}
	}
	
	//Returns number of bars
	public int getSize() {
		return number_of_bars;
	}
	
	public int insertFind() {
		for(int j=0; j<number_of_bars; j++) {
			if(!fakeBar[j] && !visited[j]) {
				visited[j] = true;

				return j;
			}
		}
		return -1;
	}
	
	//Check how many bars are still undetermined
	public boolean identifyFakeBar() {
		int numFake = 0;
		for(int i=0; i<number_of_bars; i++)
		{
			if(!fakeBar[i])
				numFake++;
		}
		
		return numFake > 1;
	}
	
	public int revealFakeBar() {
		for(int i=0; i<number_of_bars; i++)
		{
			if(!fakeBar[i])
				return i;
		}
		
		return -1;
	}
	
	//Checking sign of "Weighings" output for each number.
	public int checkSign(WebElement test, ArrayList<Integer> leftBowl, ArrayList<Integer> rightBowl, ArrayList<Integer> extraBowl) {
		
		int real_count = 0;
		
		if(test.getText().equals("<")) {
			setRealBars(rightBowl);
			setRealBars(extraBowl);
			real_count = rightBowl.size()+extraBowl.size();
		}
			
		else if(test.getText().equals(">")) {
			setRealBars(leftBowl);
			setRealBars(extraBowl);
			real_count = rightBowl.size()+extraBowl.size();
		}
		
		else if(test.getText().equals("=")) {
			setRealBars(leftBowl);
			setRealBars(rightBowl);
			real_count = rightBowl.size()+extraBowl.size();
		}
		
		else {
			System.out.println("Nothing worked.");
		}
		
		return real_count;
	}
	
	public void setRealBars(ArrayList<Integer> bowl) {
		for(int i=0; i<bowl.size(); i++)
		{
			int realBarVal = bowl.get(i);
			
			fakeBar[realBarVal] = true;
		}
	}

	public void resetFakeVisited() {
		for(int i=0; i<visited.length; i++)
		{
			if(!fakeBar[i])
				visited[i] = false;
		}
	}
	
	
	
	
	
}
