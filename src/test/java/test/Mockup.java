package test;
import java.rmi.UnexpectedException;
import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.MatchLevel;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.selenium.StitchMode;
import com.applitools.eyes.selenium.fluent.Target;

public class Mockup {
    private static WebDriver driver;

    private static CompareEyes eyes;

    private static String appName = "My Application Name4";
    private static String testName = "My Test Name4";
    private static RectangleSize viewPortSize = new RectangleSize(1336, 580);

    private static String htmlFileLocation = "file:///D:/WorkSpace/Mockimages/src/test/java/htmlfile/mockup.html";

    private static ArrayList<String[]> dataBase = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        try {
        	
        	System.setProperty("webdriver.chrome.driver", "D:\\chromedriver.exe");
        	
            driver = new ChromeDriver();

            initEyes();

            eyes.setEnableComparison(true);

            initDataBase();

           eyes.open(driver, appName, testName, viewPortSize);

            uploadMockups();

            eyes.switchToComparisonMode(driver);

            runTest();

           eyes.close();

            driver.quit();

        }catch(Exception e)
        {
        	throw(e);
        }
    }

    public static void initEyes(){

        // Create CompareEyes instance
        eyes = new CompareEyes();
        // Set API key
        eyes.setApiKey("NAmpc3W104LH3aWtYVd2LFiHhenrzV0rmYqDdUSWv0EL4110");
        // Set batch
        eyes.setBatch(new BatchInfo("MyBatch2"));
        // Set match level to Layout
        eyes.setMatchLevel(MatchLevel.STRICT);
        // Enable full page screenshot with CSS stitching
        eyes.setForceFullPageScreenshot(true);
        eyes.setStitchMode(StitchMode.CSS);
    }

    public static void initDataBase(){

        // Creating a database of URL<->Mockup pairs.
        dataBase.add(new String[]{"http://www.applitools.com", "C:/Users/Public/Pictures/Sample Pictures/Chrysanthemum.jpg"});
        //dataBase.add(new String[]{"https://help.applitools.com", "C:/Users/Public/Pictures/Sample Pictures/Desert.jpg"});
    }

    public static void uploadMockups() throws InterruptedException{

        String width;
        String getWidthCommand = "function getWidth() { var scrollWidth = document.documentElement.scrollWidth; var bodyScrollWidth = document.body.scrollWidth; return Math.max(scrollWidth, bodyScrollWidth);}; return getWidth();";
        String hideScrollbarCommand = "document.body.style.overflow = 'hidden';";

        // Taking screenshots of the mockups
        for (String entry[]:dataBase) {
            //driver.get(entry[0]);
            eyes.setHideScrollbars(true);
            ((JavascriptExecutor) driver).executeScript(hideScrollbarCommand);
            width = ((JavascriptExecutor) driver).executeScript(getWidthCommand).toString();
            driver.get(htmlFileLocation);
            ((JavascriptExecutor) driver).executeScript("addImage('" + entry[1] + "','" + width + "');");
            Thread.sleep(3000);
            eyes.check(entry[0], Target.region(By.cssSelector("img")).timeout(0));
        }
    }

    public static void runTest(){

        // Taking screenshots of the URLs
        for (String entry[]:dataBase) {
            driver.get(entry[0]);
            eyes.check(entry[0], Target.window());
        }
    }

}
