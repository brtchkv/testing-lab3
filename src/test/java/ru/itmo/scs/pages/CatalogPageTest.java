package ru.itmo.scs.pages;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import ru.itmo.scs.*;
import ru.itmo.scs.exceptions.InvalidPropertiesException;
import ru.itmo.scs.utils.Config;
import ru.itmo.scs.utils.Properties;
import ru.itmo.scs.utils.ReadConfig;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * Created by i.isaev on 14.04.2021.
 */
public class CatalogPageTest {

    private static final Logger logger = Logger.getLogger(CatalogPageTest.class);
    private static final Config config = ReadConfig.getConfigFromExternalFile("config.yml");


    public Context context;
    public List<WebDriver> driverList;

    static Stream<Arguments> getLanguageLocalsAndExamples() {
        Stream.Builder<Arguments> str = Stream.builder();
        str.accept(arguments("en", "Popular professional services"));
        str.accept(arguments("du", "Beliebte professionelle Dienstleistungen"));
        str.accept(arguments("esp", "Servicios profesionales populares"));
        str.accept(arguments("fr", "Services les plus recherchés"));
        str.accept(arguments("port", "Serviços profissionais populares"));
        str.accept(arguments("it", "Servizi professionali popolari"));
        str.accept(arguments("ned", "Populaire professionele diensten"));
        return str.build();
    }

    static Stream<Arguments> getLanguageCurrencyLocals() {
        Stream.Builder<Arguments> str = Stream.builder();
        str.accept(arguments("usd"));
        str.accept(arguments("aud"));
        str.accept(arguments("cad"));
        str.accept(arguments("eur"));
        str.accept(arguments("gbp"));
        str.accept(arguments("ils"));
        str.accept(arguments("brl"));
        return str.build();
    }

    static Stream<Arguments> getSearchStrings() {
        Stream.Builder<Arguments> str = Stream.builder();
        str.accept(arguments("building mobile app"));
        str.accept(arguments("do the lab"));
        str.accept(arguments("get a diploma"));
        return str.build();
    }

    @BeforeEach
    public void setUp() {
        context = new Context();
        driverList = new ArrayList<>();

        try {
            Properties.getInstance().reading(context);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
        }

        if (context.getGeckodriver() != null) {
            System.setProperty(Constants.WEBDRIVER_FIREFOX_DRIVER, context.getGeckodriver());
            driverList.add(new FirefoxDriver());
        }

        if (context.getChromedriver() != null) {
            System.setProperty(Constants.WEBDRIVER_CHROME_DRIVER, context.getChromedriver());
            driverList.add(new ChromeDriver());
        }

        if (driverList.isEmpty()) throw new InvalidPropertiesException();
    }

    @Test
    void testPopularCategory() {
        driverList.forEach(webDriver -> {
            assertEquals("Website Design", PageFactory.initElements(webDriver, CatalogPage.class)
                    .getPopularCategories());
            if (webDriver.getClass().getName().equals("FirefoxDriver")) webDriver.quit();
            else webDriver.close();
        });
    }

    @ParameterizedTest(name = "{index}. {0}")
    @MethodSource("getLanguageCurrencyLocals")
    void testCurrencySwitch(String local) {
        driverList.forEach(webDriver -> {
            switch(local) {
                case "usd":
                case "aud":
                case "cad":
                    assertTrue(PageFactory.initElements(webDriver, CatalogPage.class)
                            .switchCurrency(local).contains("$"));
                    break;
                case "eur":
                    assertTrue(PageFactory.initElements(webDriver, CatalogPage.class)
                            .switchCurrency(local).contains("€"));
                    break;
                case "gbp":
                    assertTrue(PageFactory.initElements(webDriver, CatalogPage.class)
                            .switchCurrency(local).contains("£"));
                    break;
                case "ils":
                    assertTrue(PageFactory.initElements(webDriver, CatalogPage.class)
                            .switchCurrency(local).contains("₪"));
                    break;
                case "brl":
                    assertTrue(PageFactory.initElements(webDriver, CatalogPage.class)
                            .switchCurrency(local).contains("R$"));
                    break;
            }

            if (webDriver.getClass().getName().equals("FirefoxDriver")) webDriver.quit();
            else webDriver.close();
        });
    }

    @ParameterizedTest(name = "{index}. {0}")
    @MethodSource("getSearchStrings")
    void testFreelanceSearch(String searchString) {
        driverList.forEach(webDriver -> {
            assertTrue(PageFactory.initElements(webDriver, CatalogPage.class)
                    .searchForFreelance(searchString).contains(searchString));
            if (webDriver.getClass().getName().equals("FirefoxDriver")) webDriver.quit();
            else webDriver.close();
        });
    }
}
