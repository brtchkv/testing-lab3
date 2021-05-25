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
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class AuthPageTest {

    private static final Logger logger = Logger.getLogger(AuthPageTest.class);
    private static final Config config = ReadConfig.getConfigFromExternalFile("config.yml");

    public Context context;
    public List<WebDriver> driverList;

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

    static Stream<Arguments> getWrongConn() {
        Stream.Builder<Arguments> str = Stream.builder();
        str.accept(arguments("", ""));
        str.accept(arguments("", "asdf"));
        str.accept(arguments("asdsdas@mail.c", ""));
        str.accept(arguments("asdsdas@m ail.c", "sdasd"));
        str.accept(arguments("asdsdas", "sdasd"));
        str.accept(arguments("asdsdas", ""));
        return str.build();
    }

    @ParameterizedTest(name = "{index}. {0} - {1}")
    @MethodSource("getWrongConn")
    @Order(1)
    void testWrongCreds(String login, String password) {
        driverList.forEach(webDriver -> {
            assertEquals("Error", PageFactory.initElements(webDriver, LoginPage.class)
                    .authorize(login, password));

            if (webDriver.getClass().getName().equals("FirefoxDriver")) webDriver.quit();
            else webDriver.close();
        });
    }

    @Test
    @Order(2)
    void testRightCreds() {
        driverList.forEach(webDriver -> {
            assertEquals("Messages", PageFactory.initElements(webDriver, LoginPage.class)
                    .authorize(config.getUsernameRegistered(), config.getPasswordRegistered()));

            if (webDriver.getClass().getName().equals("FirefoxDriver")) webDriver.quit();
            else webDriver.close();
        });
    }

    @Test
    @Order(3)
    void logOut() {
        driverList.forEach(webDriver -> {
            PageFactory.initElements(webDriver, LoginPage.class)
                    .authorize(config.getUsernameRegistered(), config.getPasswordRegistered());

            assertEquals("Sign In", PageFactory.initElements(webDriver, LoginPage.class)
                    .logOut());

            if (webDriver.getClass().getName().equals("FirefoxDriver")) webDriver.quit();
            else webDriver.close();
        });
    }

    @Test
    @Order(4)
    void testRegistrationViaEmail() {
        WebDriver driver = driverList.get(0);
        assertEquals("Messages", PageFactory.initElements(driver, LoginPage.class)
                .registerViaEmail(config.getEmailToRegister(), config.getPasswordToRegister(), config.getUsernameToRegister()));

        if (driver.getClass().getName().equals("FirefoxDriver")) driver.quit();
        else driver.close();
    }

    @Test
    @Order(5)
    void testRegistrationViaAlreadyRegisteredEmail() {
        driverList.forEach(webDriver -> {
            assertEquals("Error",  PageFactory.initElements(webDriver, LoginPage.class)
                    .registerViaEmail(config.getEmailRegistered(), config.getPasswordRegistered(),
                            config.getUsernameRegistered()));

            if (webDriver.getClass().getName().equals("FirefoxDriver")) webDriver.quit();
            else webDriver.close();
        });
    }

    @ParameterizedTest(name = "{index}. {0} - {1}")
    @MethodSource("getWrongConn")
    @Order(6)
    void testRegistrationWithWrongCredentials(String email, String password) {
        driverList.forEach(webDriver -> {
            assertEquals("Error", PageFactory.initElements(webDriver, LoginPage.class)
                    .registerViaEmail(email, password, email));

            if (webDriver.getClass().getName().equals("FirefoxDriver")) webDriver.quit();
            else webDriver.close();
        });
    }
}
