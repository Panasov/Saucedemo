package testgroup;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.*;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


@Epic("Saucademo Tessting")
@Feature("Login Page signIn")
public class LoginPageTest {
    private static final Logger log = LogManager.getLogger(LoginPageTest.class);
    private WebDriver driver;
    private LoginPage loginPage;
    private InventoryPage inventoryPage;

    @Step("Делаем скриншот: {testName}")
    private void takeScreenshot(String testName) {
        if (driver != null) {
            try {
                File screenshotsDir = new File("screenshots");
                if (!screenshotsDir.exists()) {
                    screenshotsDir.mkdir();
                }
                String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
                File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                File destination = new File("screenshots/" + testName + "_" + timestamp + ".png");
                FileUtils.copyFile(screenshot, destination);
                log.info("Скриншот сохранён: {}", destination.getAbsolutePath());
            } catch (Exception e) {
                log.error("Не удалось сохранить скриншот: {}", e.getMessage());
            }
        }
    }


    @BeforeMethod
    @Step("Настройка теста: открытие браузера и страницы LoginPage")
    public void setUp() {
        log.info("========== НАЧАЛО НАСТРОЙКИ ==========");
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        driver.manage().window().maximize();
        loginPage = new LoginPage(driver);
        inventoryPage = new InventoryPage(driver);
        log.info("Открываем сайт https://www.saucedemo.com/");
        driver.get("https://www.saucedemo.com/");
        log.info("Настройка завершена");
    }

    @Test
    @Description("Проверка, что мы на нужном сайте")
    public void openSiteTest() {
        log.info("========== ТЕСТ: openSiteTest ==========");
        String title = loginPage.getTitle();
        log.info("Заголовок страницы: {}", title);
        Assert.assertEquals(title, "Swag Labs");
        log.info("Заголовок проверен");
    }

    @DataProvider(name = "usernamesData")
    public Object[][] getusernamesData() {
        return new Object[][]{
                {"standard_user", "secret_sauce", true},
                {"locked_out_user", "secret_sauce", false},
                {"problem_user", "secret_sauce", true},
                {"performance_glitch_user", "secret_sauce", true},
                {"error_user", "secret_sauce", true},
                {"visual_user", "secret_sauce", true}
        };
    }

    @Test(dataProvider = "usernamesData")
    @Description("Проверка авторизации на сайте с разными данными")
    @Story("Пользователь заполняет форму")
    public void authorizationTest(String username, String userpassword, boolean shouldSucceed) {
        log.info("========== ПАРАМЕТРИЗОВАННЫЙ ТЕСТ ==========");
        log.info("Пользователь: {}, должен войти: {}", username, shouldSucceed);
        loginPage.clickButtonLogin(username, userpassword);
        String currentUrl = driver.getCurrentUrl();
        log.info("Текущий URL: {}", currentUrl);
        if (shouldSucceed) {
            Assert.assertTrue(currentUrl.contains("inventory"), "URL не содержит /inventory");
            log.info("Вход выполнен для: {}", username);
        } else {
            Assert.assertFalse(currentUrl.contains("inventory"), "Вход не должен был выполниться");
            log.info("Вход не выполнен для: {}, как и ожидалось", username);
        }
    }

    @AfterMethod
    @Step("Завершение теста: скриншот при падении и закрытие браузера")
    public void afterTest(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            log.error("Тест упал: {}", result.getName());
            takeScreenshot(result.getName());
        }
        log.info("========== ЗАВЕРШЕНИЕ ТЕСТА ==========");
        if (driver != null) {
            driver.quit();
            log.info("Браузер закрыт");
        }
    }
}
