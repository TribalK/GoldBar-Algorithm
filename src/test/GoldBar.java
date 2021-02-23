package test;

import org.openqa.selenium.WebElement;

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
	
	//Check how many bars are still undetermined
	public boolean identifyFakeBar() {
		int numFake = 0;
		for(int i=0; i<number_of_bars; i++)
		{
			if(!fakeBar[i])
				numFake++;
		}
		
		return numFake >= 1;
	}
	
	//Checking sign of "Weighings" output for each number.
	public String checkSign(WebElement test) {
		
		if(test.getText().equals("<"))
			return "LESS THAN!";
		
		else if(test.getText().equals(">"))
			return "GREATER THAN!";
		
		else if(test.getText().equals("="))
			return "EQUAL!";
		
		else
			return "Nothing worked.";
	}
	
	
	
}
