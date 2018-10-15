package utils;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

/**
 * 登陆网站获取cookie
 *
 * @author fyn
 * @version 1.0
 */
public class CookieUtil {
    public static String getCookie() {
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability("chrome.page.settings.userAgent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
        ChromeDriver chromeDriver = new ChromeDriver(capabilities);
        chromeDriver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
        chromeDriver.get("https://data.newrank.cn/articleSearch.html");

        WebDriver.Window window = chromeDriver.manage().window();
        return "";
    }

    public static void main(String[] args) throws MalformedURLException {
        Capabilities chrome = DesiredCapabilities.chrome();
//        WebDriver driver = new RemoteWebDriver(new URL("http://localhost:9515"), chrome);
//        WebDriver driver = new RemoteWebDriver(new URL("http://120.25.105.218:9515"), chrome);

        System.setProperty("webdriver.chrome.driver", "C:\\Documents and Settings\\Administrator\\Local Settings\\Application Data\\Google\\Chrome\\Application\\chromedriver.exe");
        ChromeDriver driver = new ChromeDriver(chrome);
        driver.get("http://www.google.com");
        String title = driver.getTitle();
        driver.close();
    }


}
