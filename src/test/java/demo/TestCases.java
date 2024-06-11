package demo;

import java.time.Duration;
import java.util.List;
import java.util.Set;

//import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class TestCases {

    public static ChromeDriver driver;
    String homepageurl = "https://www.youtube.com/";
    WebDriverWait wait;
    Actions act;
    JavascriptExecutor js;

    public static void main(String[] args) {

        TestCases t1 = new TestCases();

        t1.testCase01();
        t1.testCase02();
        t1.testCase03();
        t1.testCase04();

    }

    @BeforeMethod
    public void StartBrowser() {

        System.out.println("Creating browser instance");

        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        wait = new WebDriverWait(driver, Duration.ofSeconds(40));
        act = new Actions(driver);
        js = (JavascriptExecutor) driver;

    }

    @AfterMethod
    public void endTest() {

        System.out.println("End Test: TestCases");

        driver.close();
        driver.quit();

    }

    private void openURL(String url) {

        driver.get(url);

    }

    @Test
    public void testCase01() {

        System.out.println("Start Test case: testCase01");

        boolean status;
        try {
            openURL(homepageurl);

            String currenturl = getURL();
            Assert.assertEquals(homepageurl, currenturl, "CurrentURL is not equal to expected");

            status = ClickAboutButtton();
            Assert.assertTrue(status, "Error in click on About button");

            status = DisplayAboutText();
            Assert.assertTrue(status, "Error in displaying About Message");

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("end Test case: testCase01");
    }
    
    @Test
    private void testCase02() {

        System.out.println("Start Test case: testCase02");
        
        boolean status;
        SoftAssert softassert = new SoftAssert();

        try {

            openURL(homepageurl);

            status = selectMoviesTab();
            Assert.assertTrue(status,"error in selecting movies tab and scrooling to extreme right");

            String expmaturity = "A";
            String maturity = getContentMaturity();
            softassert.assertEquals(maturity,expmaturity,"Maturity does not match");

            String actualgenre = getContentGenre();
            boolean matching = actualgenre.contains("Comedy") || actualgenre.contains("Animation");
            softassert.assertTrue(matching,"actual genre not matching with the expected genre");

            softassert.assertAll();

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("TestCase02 successfully executed : Content Maturity and Genre matches as per expected");
        System.out.println("end Test case: testCase02");
    }

    @Test
    private void testCase03() {

        System.out.println("Start Test case: testCase03");
        
        boolean status = false;
        SoftAssert softassert = new SoftAssert();

        try {

            openURL(homepageurl);

            status = selectMusicTab();
            Assert.assertTrue(status,"error in selecting music tab and scrolling to extreme right");

            String playlist_title = getTitleOfPlaylist();
            System.out.println("Name of the Playlist : "+playlist_title);

            int track_no = getTrackNo();
            System.out.println("Name of the Playlist : "+track_no);
            status = (track_no <= 50);
            softassert.assertTrue(status, "more than 50 tracks are present in a playlist");

            softassert.assertAll();

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("end Test case: testCase03");
    }

    

    @Test
    private void testCase04() {

        System.out.println("Start Test case: testCase04");
        
        try {

            openURL(homepageurl);

            selectNewsTab();
            
            printNewstitleandBody();

            
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("end Test case: testCase04");
    }

    
    private void printNewstitleandBody() {
        
        int count = 0;
        int votes = 0;

        try{

            Thread.sleep(2000);
            WebElement news_post = driver.findElement(By.xpath("//span[text()='Latest news posts']"));
            js.executeScript("arguments[0].scrollIntoView(true);",news_post);

            List<WebElement> parent = driver.findElements(By.xpath("//div[@id='dismissible' and @role='link']"));
            
            for(int i=0; i<3; i++){
                
                wait.until(ExpectedConditions.visibilityOf(parent.get(i)));

                WebElement title = parent.get(i).findElement(By.xpath(".//yt-formatted-string[@id='home-content-text']/span[1]"));
                System.out.println("Title of News : "+title.getText());
                
                WebElement body = parent.get(i).findElement(By.xpath(".//yt-formatted-string[@id='home-content-text']/span[1]/following-sibling::a"));
                System.out.println("This is content of news :" +body.getText());

                count = getlikes(parent.get(i).findElement(By.xpath(".//div[@id='toolbar']//span[@id='vote-count-middle']")).getText());
                votes = votes + count ;
            }

            System.out.println(votes);

        }catch(Exception e){
            e.printStackTrace(); 
        }

    }

    private boolean selectNewsTab() {

        try{

            WebElement menu_button = driver.findElement(By.xpath("//button[@id='button' and @aria-label='Guide']"));
            wait.until(ExpectedConditions.visibilityOf(menu_button));
            menu_button.click();

            WebElement news_button = driver.findElement(By.xpath("//div[@id='items']//a[@title='News']"));
            wait.until(ExpectedConditions.visibilityOf(news_button));
            news_button.click();

            Thread.sleep(2000);
        
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
        
    }


    private int getlikes(String text) {
        
        if(text.contains("K")){
            int count = (Integer.parseInt(text.replaceAll("[^0-1]","").trim())) * 1000 ;
            return count;
        }

        else if(text.contains("M")){
            int count = (Integer.parseInt(text.replaceAll("[^0-1]","").trim())) * 1000000 ;
            return count;
        }

        else{
            int count = Integer.parseInt(text.trim()) ;
            return count;
        }

    }

    private int getTrackNo() {
        
        int trackcount = 0;
        
        try{
           
            List<WebElement> track_no = driver.findElements(By.xpath("//span[contains(text(),'Biggest Hits')]//ancestor::div[@class='grid-subheader style-scope ytd-shelf-renderer']/following-sibling::div[@id='contents']//ytd-compact-station-renderer//a/p"));
            int i = track_no.size() - 1;
            wait.until(ExpectedConditions.visibilityOf(track_no.get(i)));
            
            trackcount = extractdigitfromstring(track_no.get(i).getText());
            

        }catch(Exception e){

            e.printStackTrace();
            return 0;
            
        }
        
        return trackcount;
    }

    private int extractdigitfromstring(String text) {

        return Integer.parseInt(text.trim().split(" ")[0]);

    }

    private String getTitleOfPlaylist() {
        try{
            
            List<WebElement> title = driver.findElements(By.xpath("//span[contains(text(),'Biggest Hits')]//ancestor::div[@class=\"grid-subheader style-scope ytd-shelf-renderer\"]/following-sibling::div[@id='contents']//ytd-compact-station-renderer//a/h3"));
            wait.until(ExpectedConditions.visibilityOf(title.get(title.size()-1)));
            
            String title_data = title.get(title.size()-1).getText();
            return title_data;

        }catch(Exception e){
            e.printStackTrace();
            return "playlist name not found";
        }
    }

    private boolean selectMusicTab() {
        
        try{

            WebElement menu_button = driver.findElement(By.xpath("//button[@id='button' and @aria-label='Guide']"));
            wait.until(ExpectedConditions.visibilityOf(menu_button));
            menu_button.click();

            WebElement music_button = driver.findElement(By.xpath("//yt-formatted-string[text()='Music']"));
            wait.until(ExpectedConditions.visibilityOf(music_button));
            music_button.click();

            Thread.sleep(2000);
            
            WebElement title = driver.findElement((By.xpath("//span[contains(text(),'Biggest Hits')]")));
            js.executeScript("arguments[0].scrollIntoView(true);",title);

            Thread.sleep(1000);

            WebElement parent = driver.findElement((By.xpath("//span[contains(text(),'Biggest Hits')]//ancestor::div[@class='grid-subheader style-scope ytd-shelf-renderer']/following-sibling::div[@id='contents']")));
            
            for(int i=0; i<3; i++){
                WebElement rightarrow_button = parent.findElement(By.xpath(".//div[@id='right-arrow']/ytd-button-renderer//button[@aria-label='Next']//yt-touch-feedback-shape/div"));
                Thread.sleep(2000);
                js.executeScript("arguments[0].click();",rightarrow_button);
            }


        }catch(Exception e){
            e.printStackTrace();
            return false;
        }

        return true;

    }

    private String getContentGenre() {
        String genre_text = "";
        try{

            WebElement genre = driver.findElement(By.xpath("//div[@id=\"items\"]/ytd-grid-movie-renderer[16]/a/span"));
            wait.until(ExpectedConditions.visibilityOf(genre));
           
            genre_text = genre.getText().replaceAll("[^A-Za-z]","");
            

        }catch(Exception e){
            e.printStackTrace();
            return "no genre found";
        }

        return genre_text;
    }

    private String getContentMaturity() {

        String maturity_text = "";
        try{

            WebElement maturity = driver.findElement(By.xpath("//div[@id=\"items\"]/ytd-grid-movie-renderer[16]/ytd-badge-supported-renderer/div[2]"));
            wait.until(ExpectedConditions.visibilityOf(maturity));
            maturity_text = maturity.getAttribute("aria-label");
            

        }catch(Exception e){
            e.printStackTrace();
            return "Maturity string not found";
        }
        
        return maturity_text;
    }

    private boolean selectMoviesTab() {
        try{
            WebElement menu_button = driver.findElement(By.xpath("//button[@id='button' and @aria-label='Guide']"));
            wait.until(ExpectedConditions.visibilityOf(menu_button));
            menu_button.click();

            WebElement movies_button = driver.findElement(By.xpath("//div[@id='items']//a[@title='Movies']"));
            wait.until(ExpectedConditions.visibilityOf(movies_button));
            movies_button.click();
                      
            Thread.sleep(3000);
            
            for(int i=0; i<3; i++){
                WebElement rightarrow_button = driver.findElement(By.xpath("//div[@id=\"right-arrow\"]/ytd-button-renderer/yt-button-shape/button/yt-touch-feedback-shape/div/div[2]"));
                Thread.sleep(2000);
                rightarrow_button.click();
            }
        
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private boolean DisplayAboutText() {

        List<WebElement> about_text = driver
                .findElements(By.xpath("//h1[contains(text(),'About YouTube')]/following-sibling::p"));
        wait.until(ExpectedConditions.visibilityOf(about_text.get(about_text.size() - 1)));
        for (WebElement text : about_text) {
            System.out.println(text.getText());
        }
        return true;
    }

    private boolean ClickAboutButtton() {

        WebElement guide_button = driver.findElement(By.xpath("//button[@id='button' and @aria-label='Guide']"));
        guide_button.click();

        WebElement about_link = driver.findElement(By.xpath("//a[text()='About']"));
        js.executeScript("arguments[0].scrollIntoView();", about_link);
        about_link.click();

        return true;

    }

    private String getURL() {
        return driver.getCurrentUrl();
    }
}
