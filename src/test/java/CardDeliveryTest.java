import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class CardDeliveryTest {

    @BeforeEach
    void openLink() {
        open("http://localhost:9999");
    }

    LocalDate today = LocalDate.now();
    LocalDate newDate = today.plusDays(3);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Test
    void shouldSubmitRequest() {

        SelenideElement form = $(".form");
        form.$("[data-test-id=city] input").setValue("Москва");
        form.$("[data-test-id=date] input").doubleClick().sendKeys(formatter.format(newDate));
        form.$("[data-test-id=name] input").setValue("Иванов Иван");
        form.$("[data-test-id=phone] input").setValue("+79999999999");
        form.$("[data-test-id=agreement]").click();
        form.$(".button").click();
        $(withText("Успешно!")).waitUntil(visible, 15000);
        $("[data-test-id='notification'] .notification__content").shouldHave(exactText("Встреча успешно забронирована на " + formatter.format(newDate)));
    }

    @Test
    void shouldSubmitRequest2() {
        SelenideElement form = $(".form");
        form.$("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").setValue(LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        form.$("[data-test-id=name] input").setValue("Иванов Федор");
        form.$("[data-test-id=phone] input").setValue("+79999999999");
        form.$("[data-test-id=agreement]").click();
        form.$(".button").click();
        $(withText("Успешно!")).waitUntil(visible, 15000);
        $("[data-test-id='notification'] .notification__content").shouldHave(exactText("Встреча успешно забронирована на " + formatter.format(newDate)));
    }

    @Test
    void shouldFillWrongName() {
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(formatter.format(newDate));
        $("[data-test-id=name] input").setValue("Ivanov Иван");
        $("[data-test-id=phone] input").setValue("+79988888888");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=name].input_invalid").shouldHave(exactText("Фамилия и имя Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldNotFillCity() {
        $("[data-test-id=city] input").setValue("");
        $("[data-test-id=date] input").doubleClick().sendKeys(formatter.format(newDate));
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79999999999");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=city].input_invalid").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldNotFillDate() {
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "1", Keys.BACK_SPACE));
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79999999999");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=date]").shouldHave(exactText("Неверно введена дата"));
    }

    @Test
    void shouldNotFillName() {
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(formatter.format(newDate));
        $("[data-test-id=name] input").setValue("");
        $("[data-test-id=phone] input").setValue("+79999999999");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=name].input_invalid").shouldHave(exactText("Фамилия и имя Поле обязательно для заполнения"));
    }

    @Test
    void shouldNotFillPhone() {
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(formatter.format(newDate));
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=phone].input_invalid").shouldHave(exactText("Мобильный телефон Поле обязательно для заполнения"));
    }

    @Test
    void shouldNotSubmitAgreement() {
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(formatter.format(newDate));
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79999999999");
        $(".button").click();
        $("[data-test-id=agreement].input_invalid").shouldHave(exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }

    @Test
    void shouldFillWrongCity() {
        $("[data-test-id=city] input").setValue("Ленинград");
        $("[data-test-id=date] input").doubleClick().sendKeys(formatter.format(newDate));
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79999999999");
        $(".button").click();
        $("[data-test-id=city].input_invalid").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldFillWrongPhoneMoreThan11() {
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(formatter.format(newDate));
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+799999999990");
        $(".button").click();
        $("[data-test-id=phone].input_invalid").shouldHave(exactText("Мобильный телефон Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldFillWrongPhoneLessTHan11() {
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(formatter.format(newDate));
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+7999999999");
        $(".button").click();
        $("[data-test-id=phone].input_invalid").shouldHave(exactText("Мобильный телефон Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldFillWrongDate() {
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "1", Keys.BACK_SPACE));
        $("[data-test-id=date] input").setValue(LocalDate.now().plusDays(-3).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79999999999");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=date]").shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }
}
