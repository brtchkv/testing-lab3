package ru.itmo.scs.pages;

import ru.itmo.scs.utils.Config;
import ru.itmo.scs.utils.ReadConfig;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by i.isaev on 14.04.2021.
 */
public class CatalogPage extends Page {

    private static final Config config = ReadConfig.getConfigFromExternalFile("config.yml");

    public CatalogPage(WebDriver driver) {
        super(driver);
    }

    public String getPopularCategories() {
        driver.get(config.getWebsite());

        By xpath = By.xpath("/html/body/div[2]/div[2]/div[2]/div/div[1]/div[2]/div/div[2]/ul/li[1]/a");
        WebElement catalog = driver.findElement(xpath);
        catalog.click();

        By xpathDesign = By.xpath("/html/body/div[2]/div[3]/div/div/div/div[2]/header/header/div[1]/h1");

        (new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(xpathDesign).getText().toLowerCase().startsWith("website");
            }
        });

        return driver.findElement(xpathDesign).getText();
    }

    public String switchWebsiteLanguage(String local) {
        driver.get(config.getWebsite());

        By xpathSwitcher = By.xpath("/html/body/div[2]/div[1]/div[2]/header/div/div/nav/ul/li[3]/div/div[1]");
        WebElement languageSwitch = driver.findElement(xpathSwitcher);
        languageSwitch.click();

        switch(local) {
            case "en":
                driver.findElement(By.xpath("/html/body/div[2]/div[1]/div[2]/header/div/div/nav/ul/li[3" +
                                "]/div/div[1]/aside/div/section/a[1]/div")).click();
                break;
            case "du":
                driver.findElement(By.xpath("/html/body/div[2]/div[1]/div[2]/header/div/div/nav/ul/li[3]" +
                        "/div/div[1]/aside/div/section/a[2]/div")).click();
                break;
            case "esp":
                driver.findElement(By.xpath("/html/body/div[2]/div[1]/div[2]/header/div/div/" +
                        "nav/ul/li[3]/div/div[1]/aside/div/section/a[3]/div")).click();
                break;
            case "fr":
                driver.findElement(By.xpath("/html/body/div[2]/div[1]/div[2]/header/div/" +
                        "div/nav/ul/li[3]/div/div[1]/aside/div/section/a[4]/div")).click();
                break;
            case "port":
                driver.findElement(By.xpath("/html/body/div[2]/div[1]/div[2]/header/div/" +
                        "div/nav/ul/li[3]/div/div[1]/aside/div/section/a[5]/div")).click();
                break;
            case "it":
                driver.findElement(By.xpath("/html/body/div[2]/div[1]/div[2]/header/div/" +
                        "div/nav/ul/li[3]/div/div[1]/aside/div/section/a[6]/div")).click();
                break;
            case "ned":
                driver.findElement(By.xpath("/html/body/div[2]/div[1]/div[2]/header/div/" +
                        "div/nav/ul/li[3]/div/div[1]/aside/div/section/a[7]/div")).click();
                break;
        }

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        Wait<WebDriver> wait = new WebDriverWait(driver, 30);
        wait.until(new Function<WebDriver, Boolean>() {
            public Boolean apply(WebDriver driver) {
                return String
                        .valueOf(((JavascriptExecutor) driver).executeScript("return document.readyState"))
                        .equals("complete");
            }
        });

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        By xpathTitle = By.xpath("/html/body/div[2]/div[2]/div[2]/div/div[3]/div/h2");

        (new WebDriverWait(driver, 10)).until(driver -> driver.findElement(xpathTitle));

        return driver.findElement(xpathTitle).getText();
    }

    public String switchCurrency(String currency) {
        this.getPopularCategories();

        By xpathSwitcher = By.xpath("/html/body/div[2]/div[2]/div[3]/header/div/div/nav/ul/li[3]/div/div[2]/span/div");
        WebElement currencySwitch = driver.findElement(xpathSwitcher);
        currencySwitch.click();

        switch(currency) {
            case "usd":
                driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[3]/header/div/div/nav/ul/li[3]/" +
                        "div/div[2]/aside/div/section/button[1]/div")).click();
                break;
            case "eur":
                driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[3]/header/div/div/nav/ul/" +
                        "li[3]/div/div[2]/aside/div/section/button[2]/div")).click();
                break;
            case "gbp":
                driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[3]/header/div/div" +
                        "/nav/ul/li[3]/div/div[2]/aside/div/section/button[3]/div")).click();
                break;
            case "aud":
                driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[3]/header/" +
                        "div/div/nav/ul/li[3]/div/div[2]/aside/div/section/button[4]/div")).click();
                break;
            case "cad":
                driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[3]/header/div/" +
                        "div/nav/ul/li[3]/div/div[2]/aside/div/section/button[5]/div")).click();
                break;
            case "ils":
                driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[3]/header/div/" +
                        "div/nav/ul/li[3]/div/div[2]/aside/div/section/button[6]/div")).click();
                break;
            case "brl":
                driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[3]/header/div/div/nav/ul" +
                        "/li[3]/div/div[2]/aside/div/section/button[7]/div")).click();
                break;
        }

        Wait<WebDriver> wait = new WebDriverWait(driver, 30);
        wait.until(new Function<WebDriver, Boolean>() {
            public Boolean apply(WebDriver driver) {
                return String
                        .valueOf(((JavascriptExecutor) driver).executeScript("return document.readyState"))
                        .equals("complete");
            }
        });

        By xpathTitle = By.xpath("/html/body/div[1]/div[3]/div/div/div/div[7]/div/div/div[1]/div/div/footer/a/span");

        (new WebDriverWait(driver, 10)).until(driver -> driver.findElement(xpathTitle));

        return driver.findElement(xpathTitle).getText();
    }

}
