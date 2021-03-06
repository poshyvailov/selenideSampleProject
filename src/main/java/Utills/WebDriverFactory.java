package Utills;

import com.google.common.collect.ImmutableMap;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.Command;
import org.openqa.selenium.remote.CommandExecutor;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.Response;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;


public class WebDriverFactory {

    private static final ThreadLocal<WebDriver> webDriver = new ThreadLocal<>();

    public static WebDriver getDriver() {
        return webDriver.get();
    }

//    public static void setWebDriver(WebDriver driver) {
//        webDriver = driver;
//    }
//
//    public static void closeDriver() {
//        webDriver.quit();
//    }

    public static void createInstance(String browserName) {

        DesiredCapabilities capability = null;
        WebDriver driver = null;

        if (browserName.toLowerCase().contains("firefox")) {
            WebDriverManager.firefoxdriver().setup(); // Аналог - System.setProperty("webdriver.chrome.driver","D:\List_of_Jar\chromedriver.exe"); и руками не кладем фафлик в папку
            driver = new FirefoxDriver();
        } else if (browserName.toLowerCase().contains("internet")) {
            WebDriverManager.iedriver().setup();
            driver = new InternetExplorerDriver();
        } else if (browserName.toLowerCase().contains("chrome")) {
//      WebDriverManager.chromedriver().version("78.0.3904.70").setup();
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        } else {
            driver = new ChromeDriver();
        }

        driver.manage().window().maximize();
        // Implicit Wait. Will wait constant amount of time for every element.
        //  driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS) ;

        // Simulate slow network speed - network throttle

        Map<String, String> map = new HashMap<>();
        map.put("offline", "false");
        map.put("latency", "10");
        map.put("download_throughput", "1024");
        map.put("upload_throughput", "1024");
        CommandExecutor executor = ((ChromeDriver) driver).getCommandExecutor();
        try {
            Response response = executor.execute(
                    new Command(((ChromeDriver) driver).getSessionId(), "setNetworkConditions", ImmutableMap.of("network_conditions", ImmutableMap.copyOf(map)))
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        webDriver.set(driver);
    }

}
