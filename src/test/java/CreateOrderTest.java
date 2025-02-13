import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.praktikum.services.qa.scooter.client.ScooterServiceClient;
import ru.praktikum.services.qa.scooter.model.Order;
import ru.praktikum.services.qa.scooter.model.TrackOrder;
import java.util.List;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static ru.praktikum.services.qa.scooter.constant.Url.BASE_URI;


@RunWith(Parameterized.class)
public class CreateOrderTest {
    private ScooterServiceClient client;
    private ValidatableResponse response;

    private String firstName;
    private String lastName;
    private String address;
    private int metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    private List<String> color;

    public CreateOrderTest(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters(name = "Значение поля color: {0}")
    public static Object[][] orderDataTest() {
        return new Object[][]{
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of("BLACK", "GREY")},
                {List.of()}
        };
    }

    @Before
    public void before() {
        this.firstName = "Angel";
        this.lastName = "Smith";
        this.address = "Street World, 143";
        this.metroStation = 5;
        this.phone = "+7 800 000 22 33";
        this.rentTime = 2;
        this.deliveryDate = "2025-01-01";
        this.comment = "Scooter season is open!";
        client = new ScooterServiceClient(BASE_URI);
    }

    @Test
    @DisplayName("Проверка цвета самоката")
    @Description("Тест проверяет, что заказ создается при разных значениях поля color")
    public void testFieldColor(){
        Order order = new Order(firstName,lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        response = client.createOrders(order);
        response.assertThat().statusCode(SC_CREATED).body("track", notNullValue());
    }

    @After
    public void after() {
        int track = response.extract().as(TrackOrder.class).getTrack();
        ValidatableResponse cancel = client.cancelOrder(track);
        cancel.assertThat().statusCode(SC_OK).body("ok", equalTo(true));
    }
}
