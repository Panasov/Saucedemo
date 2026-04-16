package testgroup;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;

public class LoginPage {
    private static final Logger log = LogManager.getLogger(LoginPage.class);
    private WebDriver driver;
    private WebDriverWait wait;

    private By title = By.xpath("//title[text()= 'Swag Labs']");
    private By login = By.xpath("//input[@id= 'user-name']");
    private By password = By.xpath("//input[@id= 'password']");
    private By buttonLogin = By.xpath("//input[@id= 'login-button']");

    public void LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    }
}
