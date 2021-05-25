package ru.itmo.scs.pages;

import ru.itmo.scs.utils.Config;
import ru.itmo.scs.utils.ReadConfig;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by i.isaev on 14.04.2021.
 */
public class ProfilePage extends Page {

    private static final Config config = ReadConfig.getConfigFromExternalFile("config.yml");
    private static final Logger logger = Logger.getLogger(ProfilePage.class);

    public ProfilePage(WebDriver driver) {
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

    public String changeDescription(String description) {
        By icon = By.xpath("/html/body/div[2]/div[2]/div[3]/header/div/div/nav/ul/li[7]/div/span/button/div/figure");
        new WebDriverWait(driver, 5).until(driver -> driver.findElement(icon)).click();
        By logout = By.xpath("/html/body/div[2]/div[2]/div[3]/header/div/div/nav/ul/li[7]/div/aside/ul/li[13]/a");
        new WebDriverWait(driver, 5).until(driver -> driver.findElement(logout)).click();
        By loginL = By.xpath("/html/body/div[2]/div[1]/div[2]/header/div/div/nav/ul/li[5]");
        WebElement lll = new WebDriverWait(driver, 10)
                .until(webDriver1 -> webDriver1.findElement(loginL));

        return lll.getText();
    }

}
