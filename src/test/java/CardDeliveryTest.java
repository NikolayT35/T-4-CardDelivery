import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class CardDeliveryTest {

    LocalDate today = LocalDate.now();
    LocalDate newDate = today.plusDays(3);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Test
    void shouldSubmitRequest() {
        open("http://localhost:9999");
        SelenideElement form = $(".form");
        form.$("[data-test-id=city] input").setValue("Москва");
        form.$("[data-test-id=date] input").doubleClick().sendKeys(formatter.format(newDate));
        form.$("[data-test-id=name] input").setValue("Иванов Иван");
        form.$("[data-test-id=phone] input").setValue("+79999999999");
        form.$("[data-test-id=agreement]").click();
        form.$(".button").click();
      $(withText("Успешно!")).waitUntil(visible, 15000);
       $("[data-test-id='notification'] .notification__content").shouldHave(exactText("Встреча успешно забронирована на 13.11.2020")).waitUntil(exist, 20000);
    }
}

