package ru.itmo.scs.pages;

import ru.itmo.scs.utils.Config;
import ru.itmo.scs.utils.ReadConfig;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import org.openqa.selenium.*;
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

    public String searchForFreelance(String searchString) {
        driver.get(config.getWebsite());

        By xpath = By.xpath("//*[@id=\"main-wrapper\"]/div[2]/div[2]/div/div[1]/div[2]/div/div[1]/form/input");
        WebElement search = driver.findElement(xpath);
        search.sendKeys(searchString);

        By xpathSearchButton = By.xpath("//*[@id=\"main-wrapper\"]/div[2]/div[2]/div/div[1]/div[2]/div/div[1]/form" +
                "/button");
        WebElement searchButton = driver.findElement(xpathSearchButton);
        searchButton.click();

        By xpathSearchResults = By.xpath("//*[@id=\"perseus-app\"]/div/div/div[1]/div/span");

        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(xpathSearchResults).getText().toLowerCase().contains(searchString);
            }
        });

        return driver.findElement(xpathSearchResults).getText().toLowerCase();
    }

    public String addToFavourites(String courseName) {
        By xpath = By.xpath("/html/body/div[2]/div[2]/div[3]/header/div/div/div/div/form/input");

        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.xpath("/html/body/div[2]/div[2]/div[3]/header" +
                        "/div/div/div/div/form/button")).getText().toLowerCase().contains("search");
            }
        });

        WebElement search = driver.findElement(xpath);
        search.sendKeys(courseName);
        search.sendKeys(Keys.ENTER);

        By xpathSearchResults = By.xpath("/html/body/div[2]/div[3]/div/div/div/div[1]/div/span");

        (new WebDriverWait(driver, 20)).until(driver -> driver.findElement(xpathSearchResults));

        try {
            By xpathFavButton = By.xpath("//*[@id=\"perseus-app\"]/div/div/div[5]/div/div/div[1]/div/div/footer/div/div[2]/span/button");
            (new WebDriverWait(driver, 20)).until(driver -> driver.findElement(xpathFavButton));
            WebElement favButton = driver.findElement(xpathFavButton);
            favButton.click();
        } catch (Exception e) {
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            By xpathFavButton = By.xpath("//*[@id=\"perseus-app\"]/div/div/div[5]/div/div/div[1]/div/div/footer/div/div[2]/span/button");
            (new WebDriverWait(driver, 20)).until(driver -> driver.findElement(xpathFavButton));
            WebElement favButton = driver.findElement(xpathFavButton);
            favButton.click();
        }

        By xpathFavListButton = By.xpath("/html/body/div[2]/div[3]/div/div/div/div[5]/div/div/div[1]" +
                "/div/div/footer/div/div[2]/span/button");
        WebElement favListButton = driver.findElement(xpathFavListButton);
        favListButton.click();

        driver.get(config.getWebsite() + "/lists/my_lists?source=header_navigation");

        By xpathAddedCourse = By.xpath("/html/body/div[2]/div[3]/div/section/div/section[2]/section/div/div/a");

        (new WebDriverWait(driver, 10)).until(driver -> driver.findElement(xpathAddedCourse));

        String playListUrl = driver.findElement(xpathAddedCourse).getAttribute("href");
        driver.get(playListUrl);

        (new WebDriverWait(driver, 10)).until(driver -> driver.findElement(By.xpath("/html/body/" +
                "div[2]/div[3]/div/section/div" +
                "/section[2]/div[2]/div[1]/div/div/div/h3/a")));

        String courseReturnedName = driver.findElement(By.xpath("/html/body/div[2]/div[3]/div/section/div" +
                "/section[2]/div[2]/div[1]/div/div/div/h3/a")).getText().toLowerCase();

        By xpathFavCleanUpButton = By.xpath("/html/body/div[2]/div[3]/div/section/div/" +
                "section[2]/div[2]/div[1]/div/div/div/footer/div/div[2]/span/button");
        WebElement favCleanUpButton = driver.findElement(xpathFavCleanUpButton);
        favCleanUpButton.click();

        return courseReturnedName;
    }

//    public String switchWebsiteLanguage(String local) {
//        driver.get(config.getWebsite());
//
//        By xpathSwitcher = By.xpath("/html/body/div[2]/div[1]/div[2]/header/div/div/nav/ul/li[3]/div/div[1]");
//        WebElement languageSwitch = driver.findElement(xpathSwitcher);
//        languageSwitch.click();
//
//        switch(local) {
//            case "en":
//                driver.findElement(By.xpath("/html/body/div[2]/div[1]/div[2]/header/div/div/nav/ul/li[3" +
//                                "]/div/div[1]/aside/div/section/a[1]/div")).click();
//                break;
//            case "du":
//                driver.findElement(By.xpath("/html/body/div[2]/div[1]/div[2]/header/div/div/nav/ul/li[3]" +
//                        "/div/div[1]/aside/div/section/a[2]/div")).click();
//                break;
//            case "esp":
//                driver.findElement(By.xpath("/html/body/div[2]/div[1]/div[2]/header/div/div/" +
//                        "nav/ul/li[3]/div/div[1]/aside/div/section/a[3]/div")).click();
//                break;
//            case "fr":
//                driver.findElement(By.xpath("/html/body/div[2]/div[1]/div[2]/header/div/" +
//                        "div/nav/ul/li[3]/div/div[1]/aside/div/section/a[4]/div")).click();
//                break;
//            case "port":
//                driver.findElement(By.xpath("/html/body/div[2]/div[1]/div[2]/header/div/" +
//                        "div/nav/ul/li[3]/div/div[1]/aside/div/section/a[5]/div")).click();
//                break;
//            case "it":
//                driver.findElement(By.xpath("/html/body/div[2]/div[1]/div[2]/header/div/" +
//                        "div/nav/ul/li[3]/div/div[1]/aside/div/section/a[6]/div")).click();
//                break;
//            case "ned":
//                driver.findElement(By.xpath("/html/body/div[2]/div[1]/div[2]/header/div/" +
//                        "div/nav/ul/li[3]/div/div[1]/aside/div/section/a[7]/div")).click();
//                break;
//        }
//
//        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//
//        Wait<WebDriver> wait = new WebDriverWait(driver, 30);
//        wait.until(new Function<WebDriver, Boolean>() {
//            public Boolean apply(WebDriver driver) {
//                return String
//                        .valueOf(((JavascriptExecutor) driver).executeScript("return document.readyState"))
//                        .equals("complete");
//            }
//        });
//
//        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//
//        By xpathTitle = By.xpath("/html/body/div[2]/div[2]/div[2]/div/div[3]/div/h2");
//
//        (new WebDriverWait(driver, 10)).until(driver -> driver.findElement(xpathTitle));
//
//        return driver.findElement(xpathTitle).getText();
//    }

    public String switchCurrency(String currency) {
        this.getPopularCategories();

        By xpathSwitcher = By.xpath("/html/body/div[2]/div[2]/div[3]/header/div/div/nav/ul/li[3]/div/div[2]/span/div");
        WebElement currencySwitch = driver.findElement(xpathSwitcher);
        currencySwitch.click();

        switch (currency) {
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
