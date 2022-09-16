# GoldBar-Minimum-Weighing-Algorithm

Given nine gold bars in which all but one are of the same weight, the Minimum Weighing Algorithm will test the fewest number of times the 
nine gold bars would need to be weighed in order to determine which one is fake.

Given this information, the optimal algorithm is for only two weighings to be executed to find the fake gold bar.

The project will be using Selenium to simulate the results through https://tribalk-goldbar-gridview.netlify.app/. Java will be used as the
primary programming language on Eclipse IDE. Please ensure that you have a working Java Runtime Environment (using 1.8.0_281 during project).

Ensure that you also have a selenium server jar file loaded into classpath. The version ran for project: selenium-server-standalone-4.0.0-alpha-2.jar
The WebDriver used will be Google Chrome.

Execution Method is as follows: 

From the command line, you can navigate to the directory where you have downloaded the extracted zip file. Move to the src/test/ directory and run "javac -d ../../ GoldBarTest.java GoldBar.java" in command line to compile the files. The compilation will not work from just the current directory because it will not recognize ChromeDriver.exe from the driver directory. Go back two parent directories "cd ../../ " and run "java test.GoldBarTest" to execute the java application.
