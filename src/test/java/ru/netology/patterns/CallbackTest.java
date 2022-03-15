package ru.netology.patterns;

import com.codeborne.selenide.SelenideElement;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class CallbackTest {
    private WebDriver driver;

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
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void shouldSubmitRequestSuccess() {
        var validUser = DataHelper.Registration.generateUser("ru");
        var firstMeetingDate = DataHelper.generateDate(4);
        SelenideElement form =$(".form");
        form.$("[data-test-id=city] input").setValue(validUser.getCity());
        form.$("[data-test-id=date] input").doubleClick();
        form.$("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(firstMeetingDate);
        form.$("[data-test-id=name] input").setValue(validUser.getName());
        form.$("[data-test-id=phone] input").setValue(validUser.getPhone());
        form.$("[data-test-id=agreement]").click();
        form.$(".button").click();
        $("[data-test-id=success-notification]").shouldBe(visible);
    }

    @Test
    void shouldFormValidateWithoutAgreement() {
        var validUser = DataHelper.Registration.generateUser("ru");
        var firstMeetingDate = DataHelper.generateDate(4);
        SelenideElement form =$(".form");
        form.$("[data-test-id=city] input").setValue(validUser.getCity());
        form.$("[data-test-id=date] input").doubleClick();
        form.$("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(firstMeetingDate);
        form.$("[data-test-id=name] input").setValue(validUser.getName());
        form.$("[data-test-id=phone] input").setValue(validUser.getPhone());
        form.$(".button").click();
        form.$("[data-test-id=agreement]").shouldHave(cssClass("input_invalid"));
    }

    @Test
    void shouldFormValidateEmptyName() {
        var validUser = DataHelper.Registration.generateUser("ru");
        var firstMeetingDate = DataHelper.generateDate(4);
        SelenideElement form =$(".form");
        form.$("[data-test-id=city] input").setValue(validUser.getCity());
        form.$("[data-test-id=date] input").doubleClick();
        form.$("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(firstMeetingDate);
        form.$("[data-test-id=phone] input").setValue(validUser.getPhone());
        form.$("[data-test-id=agreement]").click();
        form.$(".button").click();
        form.$("[data-test-id=name].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldFormValidateEmptyNumber() {
        var validUser = DataHelper.Registration.generateUser("ru");
        var firstMeetingDate = DataHelper.generateDate(4);
        SelenideElement form =$(".form");
        form.$("[data-test-id=city] input").setValue(validUser.getCity());
        form.$("[data-test-id=date] input").doubleClick();
        form.$("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(firstMeetingDate);
        form.$("[data-test-id=name] input").setValue(validUser.getName());
        form.$("[data-test-id=agreement]").click();
        form.$(".button").click();
        form.$("[data-test-id=phone].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldFormValidateWrongName() {
        var validUser = DataHelper.Registration.generateUser("ru");
        var firstMeetingDate = DataHelper.generateDate(4);
        SelenideElement form =$(".form");
        form.$("[data-test-id=city] input").setValue(validUser.getCity());
        form.$("[data-test-id=date] input").doubleClick();
        form.$("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(firstMeetingDate);
        form.$("[data-test-id=name] input").setValue("kalynova valentina");
        form.$("[data-test-id=phone] input").setValue(validUser.getPhone());
        form.$("[data-test-id=agreement]").click();
        form.$(".button").click();
        form.$("[data-test-id=name].input_invalid .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldFormValidateWrongNumber() {
        var validUser = DataHelper.Registration.generateUser("ru");
        var firstMeetingDate = DataHelper.generateDate(4);
        SelenideElement form =$(".form");
        form.$("[data-test-id=city] input").setValue(validUser.getCity());
        form.$("[data-test-id=date] input").doubleClick();
        form.$("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(firstMeetingDate);
        form.$("[data-test-id=name] input").setValue(validUser.getName());
        form.$("[data-test-id=phone] input").setValue("77779270000000");
        form.$("[data-test-id=agreement]").click();
        form.$(".button").click();
        $("[data-test-id=success-notification]").shouldBe(visible);
    }

    @Test
    void shouldFormValidateWrongDate() {
        var validUser = DataHelper.Registration.generateUser("ru");
        SelenideElement form =$(".form");
        form.$("[data-test-id=city] input").setValue(validUser.getCity());
        form.$("[data-test-id=date] input").doubleClick();
        form.$("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue("32");
        form.$("[data-test-id=name] input").setValue(validUser.getName());
        form.$("[data-test-id=phone] input").setValue(validUser.getPhone());
        form.$("[data-test-id=agreement]").click();
        form.$(".button").click();
        form.$("[data-test-id=date] .input__sub").shouldHave(exactText("Неверно введена дата"));
    }

    @Test
    void shouldFormValidateNotAllowedDate() {
        var validUser = DataHelper.Registration.generateUser("ru");
        var firstMeetingDate = DataHelper.generateDate(1);
        SelenideElement form =$(".form");
        form.$("[data-test-id=city] input").setValue(validUser.getCity());
        form.$("[data-test-id=date] input").doubleClick();
        form.$("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(firstMeetingDate);
        form.$("[data-test-id=name] input").setValue(validUser.getName());
        form.$("[data-test-id=phone] input").setValue(validUser.getPhone());
        form.$("[data-test-id=agreement]").click();
        form.$(".button").click();
        form.$("[data-test-id=date] .input__sub").shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldFormValidateWrongCiti() {
        var validUser = DataHelper.Registration.generateUser("ru");
        var firstMeetingDate = DataHelper.generateDate(4);
        SelenideElement form =$(".form");
        form.$("[data-test-id=city] input").setValue("Несуществующий город");
        form.$("[data-test-id=date] input").doubleClick();
        form.$("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(firstMeetingDate);
        form.$("[data-test-id=name] input").setValue(validUser.getName());
        form.$("[data-test-id=phone] input").setValue(validUser.getPhone());
        form.$("[data-test-id=agreement]").click();
        form.$(".button").click();
        form.$("[data-test-id=city].input_invalid .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldSubmitRequestReRegister() {
        var validUser = DataHelper.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataHelper.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataHelper.generateDate(daysToAddForSecondMeeting);
        SelenideElement form =$(".form");
        form.$("[data-test-id=city] input").setValue(validUser.getCity());
        form.$("[data-test-id=date] input").doubleClick();
        form.$("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(firstMeetingDate);
        form.$("[data-test-id=name] input").setValue(validUser.getName());
        form.$("[data-test-id=phone] input").setValue(validUser.getPhone());
        form.$("[data-test-id=agreement]").click();
        form.$(".button").click();
        $("[data-test-id=success-notification]").shouldBe(visible);
        form.$("[data-test-id=date] input").doubleClick();
        form.$("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(secondMeetingDate);
        form.$(".button").click();
        $("[data-test-id=replan-notification]").shouldBe(visible);
        $("[data-test-id=replan-notification] button").click();
        $("[data-test-id=success-notification]").shouldBe(visible);
    }
}
