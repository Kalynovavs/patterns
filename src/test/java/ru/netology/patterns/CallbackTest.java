package ru.netology.patterns;

import com.codeborne.selenide.SelenideElement;
import com.github.javafaker.Faker;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class CallbackTest {
    private WebDriver driver;
    private LocalDateTime dateNow;
    private DateTimeFormatter dtf;
    private String dateAllowable;
    private Faker faker;
    private String addr;

    @BeforeAll
    static void setUpAll() {
        //System.setProperty("webdriver.chrome.driver", "webdriver//chromedriver");
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        options.addArguments("disable-infobars");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        options.addArguments("--disable-extensions");
        options.addArguments("--no-sandbox");
        driver = new ChromeDriver(options);
        open("http://localhost:9999");
        dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        dateNow = LocalDateTime.now();
        dateAllowable = dtf.format(dateNow.plusDays(4));
        faker = new Faker(new Locale("ru"));
        do {
            addr = faker.address().cityName();
        } while (addr == "Тольятти" || addr == "Тольятти" || addr == "Новокузнецк");
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void shouldSubmitRequestSuccess() {
        String name = faker.name().fullName();
        String tel = faker.phoneNumber().phoneNumber();
        SelenideElement form =$(".form");
        form.$("[data-test-id=city] input").setValue(addr);
        form.$("[data-test-id=date] input").doubleClick();
        form.$("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(dateAllowable);
        form.$("[data-test-id=name] input").setValue(name);
        form.$("[data-test-id=phone] input").setValue(tel);
        form.$("[data-test-id=agreement]").click();
        form.$(".button").click();
        $("[data-test-id=success-notification]").shouldBe(visible);
    }

    @Test
    void shouldFormValidateWithoutAgreement() {
        String name = faker.name().fullName();
        String tel = faker.phoneNumber().phoneNumber();
        SelenideElement form =$(".form");
        form.$("[data-test-id=city] input").setValue(addr);
        form.$("[data-test-id=date] input").doubleClick();
        form.$("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(dateAllowable);
        form.$("[data-test-id=name] input").setValue(name);
        form.$("[data-test-id=phone] input").setValue(tel);
        form.$(".button").click();
        form.$("[data-test-id=agreement]").shouldHave(cssClass("input_invalid"));
    }

    @Test
    void shouldFormValidateEmptyName() {
        String tel = faker.phoneNumber().phoneNumber();
        SelenideElement form =$(".form");
        form.$("[data-test-id=city] input").setValue(addr);
        form.$("[data-test-id=date] input").doubleClick();
        form.$("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(dateAllowable);
        form.$("[data-test-id=phone] input").setValue(tel);
        form.$("[data-test-id=agreement]").click();
        form.$(".button").click();
        form.$("[data-test-id=name].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldFormValidateEmptyNumber() {
        String name = faker.name().fullName();
        SelenideElement form =$(".form");
        form.$("[data-test-id=city] input").setValue(addr);
        form.$("[data-test-id=date] input").doubleClick();
        form.$("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(dateAllowable);
        form.$("[data-test-id=name] input").setValue(name);
        form.$("[data-test-id=agreement]").click();
        form.$(".button").click();
        form.$("[data-test-id=phone].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldFormValidateWrongName() {
        String tel = faker.phoneNumber().phoneNumber();
        SelenideElement form =$(".form");
        form.$("[data-test-id=city] input").setValue(addr);
        form.$("[data-test-id=date] input").doubleClick();
        form.$("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(dateAllowable);
        form.$("[data-test-id=name] input").setValue("kalynova valentina");
        form.$("[data-test-id=phone] input").setValue(tel);
        form.$("[data-test-id=agreement]").click();
        form.$(".button").click();
        form.$("[data-test-id=name].input_invalid .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldFormValidateWrongNumber() {
        String name = faker.name().fullName();
        SelenideElement form =$(".form");
        form.$("[data-test-id=city] input").setValue(addr);
        form.$("[data-test-id=date] input").doubleClick();
        form.$("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(dateAllowable);
        form.$("[data-test-id=name] input").setValue(name);
        form.$("[data-test-id=phone] input").setValue("77779270000000");
        form.$("[data-test-id=agreement]").click();
        form.$(".button").click();
        $("[data-test-id=success-notification]").shouldBe(visible);
    }

    @Test
    void shouldFormValidateWrongDate() {
        String name = faker.name().fullName();
        String tel = faker.phoneNumber().phoneNumber();
        SelenideElement form =$(".form");
        form.$("[data-test-id=city] input").setValue(addr);
        form.$("[data-test-id=date] input").doubleClick();
        form.$("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue("32");
        form.$("[data-test-id=name] input").setValue(name);
        form.$("[data-test-id=phone] input").setValue(tel);
        form.$("[data-test-id=agreement]").click();
        form.$(".button").click();
        form.$("[data-test-id=date] .input__sub").shouldHave(exactText("Неверно введена дата"));
    }

    @Test
    void shouldFormValidateNotAllowedDate() {
        String name = faker.name().fullName();
        String tel = faker.phoneNumber().phoneNumber();
        SelenideElement form =$(".form");
        form.$("[data-test-id=city] input").setValue(addr);
        form.$("[data-test-id=date] input").doubleClick();
        form.$("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(dtf.format(dateNow.plusDays(1)));
        form.$("[data-test-id=name] input").setValue(name);
        form.$("[data-test-id=phone] input").setValue(tel);
        form.$("[data-test-id=agreement]").click();
        form.$(".button").click();
        form.$("[data-test-id=date] .input__sub").shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldFormValidateWrongCiti() {
        String name = faker.name().fullName();
        String tel = faker.phoneNumber().phoneNumber();
        SelenideElement form =$(".form");
        form.$("[data-test-id=city] input").setValue("Несуществующий город");
        form.$("[data-test-id=date] input").doubleClick();
        form.$("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(dateAllowable);
        form.$("[data-test-id=name] input").setValue(name);
        form.$("[data-test-id=phone] input").setValue(tel);
        form.$("[data-test-id=agreement]").click();
        form.$(".button").click();
        form.$("[data-test-id=city].input_invalid .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldSubmitRequestReRegister() {
        String name = faker.name().fullName();
        String tel = faker.phoneNumber().phoneNumber();
        SelenideElement form =$(".form");
        form.$("[data-test-id=city] input").setValue(addr);
        form.$("[data-test-id=date] input").doubleClick();
        form.$("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(dateAllowable);
        form.$("[data-test-id=name] input").setValue(name);
        form.$("[data-test-id=phone] input").setValue(tel);
        form.$("[data-test-id=agreement]").click();
        form.$(".button").click();
        $("[data-test-id=success-notification]").shouldBe(visible);
        form.$("[data-test-id=date] input").doubleClick();
        form.$("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(dtf.format(dateNow.plusDays(6)));
        form.$(".button").click();
        $("[data-test-id=replan-notification]").shouldBe(visible);
        $("[data-test-id=replan-notification] button").click();
        $("[data-test-id=success-notification]").shouldBe(visible);
    }
}
