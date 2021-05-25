package ru.itmo.scs.pages;

import lombok.SneakyThrows;
import ru.itmo.scs.utils.Config;
import ru.itmo.scs.utils.ReadConfig;
import java.time.Duration;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage extends Page {

    private static final Config config = ReadConfig.getConfigFromExternalFile("config.yml");
    private static final Logger logger = Logger.getLogger(LoginPage.class);

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public String authorize(String username, String password) {

        driver.get(config.getWebsite() + "/login");

        (new WebDriverWait(driver, 5)).until(driver -> driver.findElement(By
                .xpath("/html/body/div[2]/div[3]/div/section/div/div/div/div/form/div/button")));

        WebElement loginInputElement = driver.findElement(By
                .xpath("//*[@id=\"login\"]"));
        WebElement passwordInputElement = driver.findElement(By
                .xpath("//*[@id=\"password\"]"));
        WebElement loginButtonElement = driver.findElement(By
                .xpath("/html/body/div[2]/div[3]/div/section/div/div/div/div/form/div/button"));

        loginInputElement.sendKeys(username);
        passwordInputElement.sendKeys(password);

        loginButtonElement.click();

        By messagesHeader = By.xpath("/html/body/div[2]/div[2]/div[3]/header/div/div/nav/ul/li[4]/div/span/button");
        try {
            (new WebDriverWait(driver, 5)).until(driver -> driver.findElement(messagesHeader));
        } catch (Exception e) {
            return "Error";
        }

        return driver.findElement(messagesHeader).getText();
    }

    public String logOut() {
        By icon = By.xpath("/html/body/div[2]/div[2]/div[3]/header/div/div/nav/ul/li[7]/div/span/button/div/figure");
        new WebDriverWait(driver, 5).until(driver -> driver.findElement(icon)).click();
        By logout = By.xpath("/html/body/div[2]/div[2]/div[3]/header/div/div/nav/ul/li[7]/div/aside/ul/li[13]/a");
        new WebDriverWait(driver, 5).until(driver -> driver.findElement(logout)).click();
        By loginL = By.xpath("/html/body/div[2]/div[1]/div[2]/header/div/div/nav/ul/li[5]");
        WebElement lll = new WebDriverWait(driver, 10)
                .until(webDriver1 -> webDriver1.findElement(loginL));

        return lll.getText();
    }

    public String registerViaEmail(String email, String password, String username) {

        driver.get(config.getWebsite() + "/login");

        (new WebDriverWait(driver, 5)).until(driver -> driver.findElement(By
                .xpath("/html/body/div[2]/div[3]/div/section/div/div/div/div/div/button")));

        WebElement registerButton = driver.findElement(By
                .xpath("/html/body/div[2]/div[3]/div/section/div/div/div/div/div/button"));
        registerButton.click();

        WebElement emailInput = driver.findElement(By.xpath("/html/body/div[2]/div[3]/div/section/div/div/div/div/div" +
                "[1]/form/div/div/div/div/input"));
        emailInput.sendKeys(email);

        WebElement submitEmail = driver.findElement(By.xpath("/html/body/div[2]/div[3]/div/section/div/div/div/div/div[1]/form/div/button"));
        submitEmail.click();

        try {
            (new WebDriverWait(driver, 5)).until(driver -> driver.findElement(By
                    .xpath("/html/body/div[2]/div[3]/div/section/div/div/div/div/div[1]/form/div/div/p")));
            if (driver.findElement(By.xpath("/html/body/div[2]/div[3]/div/section/div/div/div/div/div[1]/form/div/div/p"))
                    .getSize() != null) {
                return "Error";
            }
        } catch (Exception e) {
            logger.info("Registering with new email");
        }

        (new WebDriverWait(driver, 5)).until(driver -> driver.findElement(By.xpath("/html/body/div[2]/div[3]/div/section/div/div/div" +
                "/div/div[1]/form/div[1]/div/div/input")));

        WebElement usernameInput = driver.findElement(By.xpath("/html/body/div[2]/div[3]/div/section/div/div/div" +
                "/div/div[1]/form/div[1]/div/div/input"));

        usernameInput.sendKeys(username);

        WebElement passwordInput = driver.findElement(By.xpath("//*[@id=\"password\"]"));

        passwordInput.sendKeys(password);

        WebElement registerButtonComplete = driver.findElement(By.xpath("/html/body/div[2]/div[3]/div/section/div/div" +
                "/div/div/div[1]/form/button"));

        registerButtonComplete.click();

        By messagesHeader = By.xpath("/html/body/div[2]/div[2]/div[3]/header/div/div/nav/ul/li[4]/div/span/button");
        try {
            (new WebDriverWait(driver, 5)).until(driver -> driver.findElement(messagesHeader));
        } catch (Exception e) {
            return "Error";
        }

        return driver.findElement(messagesHeader).getText();
    }

}
