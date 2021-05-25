package ru.itmo.scs.pages;

import ru.itmo.scs.Constants;
import ru.itmo.scs.Context;
import ru.itmo.scs.exceptions.InvalidPropertiesException;
import ru.itmo.scs.utils.Config;
import ru.itmo.scs.utils.Properties;
import ru.itmo.scs.utils.ReadConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class ProfilePageTest {

    private static final Logger logger = Logger.getLogger(ProfilePageTest.class);
    private static final Config config = ReadConfig.getConfigFromExternalFile("config.yml");

    public Context context;
    public List<WebDriver> driverList;

    static Stream<Arguments> getDescriptions() {
        Stream.Builder<Arguments> str = Stream.builder();
        str.accept(arguments("Test Description 1"));
        str.accept(arguments("Test Description 2"));
        str.accept(arguments("Test Description 3"));
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

    @ParameterizedTest(name = "{index}. {0}")
    @MethodSource("getDescriptions")
    void testDescriptionChange(String descr) {
        driverList.forEach(webDriver -> {
            PageFactory.initElements(webDriver, LoginPage.class)
                    .authorize(config.getUsernameRegistered(), config.getPasswordRegistered());
            assertEquals(descr, PageFactory.initElements(webDriver, ProfilePage.class)
                    .changeDescription(config.getUsernameRegistered(), descr));

            if (webDriver.getClass().getName().equals("FirefoxDriver")) webDriver.quit();
            else webDriver.close();
        });
    }

    @Test
    void testAddToFavourites() {
        driverList.forEach(webDriver -> {
            PageFactory.initElements(webDriver, LoginPage.class)
                    .authorize(config.getUsernameRegistered(), config.getPasswordRegistered());

            assertTrue(PageFactory.initElements(webDriver, CatalogPage.class)
                    .addToFavourites("get alison diploma certificate").contains("alison"));
            if (webDriver.getClass().getName().equals("FirefoxDriver")) webDriver.quit();
            else webDriver.close();
        });
    }
}
