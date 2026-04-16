package testgroup;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;

public class LoginPage {
    private static final Logger log = LogManager.getLogger(LoginPage.class);
    private WebDriver driver;
    private WebDriverWait wait;

    private By loginField = By.xpath("//input[@id= 'user-name']");
    private By passwordField = By.xpath("//input[@id= 'password']");
    private By buttonLogin = By.xpath("//input[@id= 'login-button']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        log.info("LoginPage инициализирован");
    }

    public String getTitle() {
        log.info("Получаем заголовок страницы");
        return driver.getTitle();

    }

    public LoginPage typeLogin(String username) {
        log.info("Вводим логин: {}", username);
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginField));
        driver.findElement(loginField).sendKeys(username);
        return this;
    }

    public LoginPage typePassword(String userpassword) {
        log.info("Вводим пароль");
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));
        driver.findElement(passwordField).sendKeys(userpassword);
        return this;
    }

    public InventoryPage clickButtonLogin(String username, String userpassword) {
        log.info("Нажимаем кнопку Login");
        typeLogin(username);
        typePassword(userpassword);
        driver.findElement(buttonLogin).click();
        return new InventoryPage(driver);
    }
}
