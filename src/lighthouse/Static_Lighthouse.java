package lighthouse;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Static_Lighthouse {
    WebDriver driver;
    String baseURL;
    String portNumber;
    FileWriter urls;
    FileWriter url;

    @BeforeClass
    public void setUp() throws IOException {

        // 2. launch browser
        ChromeDriver chromeDriver=new ChromeDriver();
        Capabilities cap=chromeDriver.getCapabilities();
        Map<String, Object> myCap=cap.asMap();
        System.out.println(((Map)myCap.get("goog:chromeOptions")).get("debuggerAddress"));
        portNumber= ((String) ((Map)myCap.get("goog:chromeOptions")).get("debuggerAddress")).split(":")[1];

        baseURL="https://www.qualcomm.com/";

        ChromeOptions options = new ChromeOptions();
        String port="127.0.0.1:"+portNumber;
        options.setExperimentalOption("debuggerAddress",port );
        driver = new ChromeDriver(options);
        // 3. Maximize window
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        driver.get(baseURL);
        createBatFile();
        urls=new FileWriter("urls.txt");
    }




    @Test
    public void test() throws IOException {
        // 4. Click on 'Products' link
        WebElement products = driver.findElement(By.id("/products-8843"));
        products.click();

        // 5. Click on 'Bluetooth' link and verify user landed on desired page
        WebElement bluetooth = driver.findElement(By.id("/products/bluetooth-8846"));
        bluetooth.click();

        addUrl(driver.getCurrentUrl());

        // 6. Click on 'Support' link and verify user landed on desired page
        WebElement support = driver.findElement(By.id("/support-8913"));
        support.click();

        WebElement prod_support = driver.findElement(By.id("/support-12320"));
        prod_support.click();

        addUrl(driver.getCurrentUrl());

        // 7.Click on 'Company' link
        WebElement company = driver.findElement(By.id("/company/about-8928"));
        company.click();

        // 8. Click on '5G & Wireless Technology' and verify user landed on desired page
        WebElement wireless = driver.findElement(By.id("/research/5g-9175"));
        wireless.click();

        addUrl(driver.getCurrentUrl());

    }

    @AfterClass
    public void cleanUp() throws InterruptedException, IOException {
        //   9. Close browser.
        urls.close();

        Process p = Runtime.getRuntime().exec("cmd /c start /wait cmd.exe /C url.bat");
        System.out.println("Waiting for batch file ...");
        p.waitFor();
        System.out.println("Batch file done.");
        Thread.sleep(3000);
        driver.close();
        System.out.println("Quit");
    }


    public void createBatFile() throws IOException {
        url = new FileWriter("url.bat");
        String LHcmd="@For /F \"UseBackQ Delims=\" %%A In (\"urls.txt\") Do @LightHouse \"%%A\" --port="+portNumber+" --formFactor=desktop --screenEmulation.disabled --chrome-flags=\"--headless\" ";
        url.write(LHcmd);
        url.close();

    }
    public void addUrl(String url) throws IOException {
        urls.write(url+"\n");
    }

}
