package ru.itmo.scs.pages;

import ru.itmo.scs.utils.Config;
import ru.itmo.scs.utils.ReadConfig;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProfilePage extends Page {

    private static final Config config = ReadConfig.getConfigFromExternalFile("config.yml");

    public ProfilePage(WebDriver driver) {
        super(driver);
    }

    public String changeDescription(String username, String description) {
        driver.get(config.getWebsite() + "/" + username);

        Wait<WebDriver> wait = new WebDriverWait(driver, 30);
        wait.until(new Function<WebDriver, Boolean>() {
            public Boolean apply(WebDriver driver) {
                return String
                        .valueOf(((JavascriptExecutor) driver).executeScript("return document.readyState"))
                        .equals("complete");
            }
        });

        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        By icon = By.xpath("/html/body/div[2]/div[3]/div[2]/div[2]/section[1]/div[1]/div/div[2]/div[2]/div[2]/i");
        new WebDriverWait(driver, 10).until(driver -> driver.findElement(icon));
        driver.findElement(icon).click();

        By input = By.xpath("//*[@id=\"SellerCard-component\"]/div/div[2]/div[2]/div[2]/form/input");
        new WebDriverWait(driver, 10).until(driver -> driver.findElement(input));
        driver.findElement(input).clear();
        driver.findElement(input).sendKeys(description);

        By saveDescriptionButton = By.xpath("//*[@id=\"SellerCard-component\"]" +
                "/div/div[2]/div[2]/div[2]/form/div/button[2]");
        new WebDriverWait(driver, 10)
                .until(webDriver1 -> webDriver1.findElement(saveDescriptionButton));
        driver.findElement(saveDescriptionButton).click();

        new WebDriverWait(driver, 10)
                .until(webDriver1 -> webDriver1.findElement(By.xpath("//*[@id=\"SellerCard-component\"]/div/div[2]/div[2]/div[2]/small")));

        return driver.findElement(By.xpath("//*[@id=\"SellerCard-component\"]/div/div[2]/div[2]/div[2]/small")).getText();
    }

}
