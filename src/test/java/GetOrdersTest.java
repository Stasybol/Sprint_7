import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.services.qa.scooter.client.ScooterServiceClient;
import ru.praktikum.services.qa.scooter.model.Order;
import ru.praktikum.services.qa.scooter.model.TrackOrder;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class GetOrdersTest {
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru/";
    ScooterServiceClient client;
    ValidatableResponse response;

    @Before
    public void before() {
        client = new ScooterServiceClient(BASE_URI);
        Order order = new Order("Angel", "Smith", "street World, 143", 5, "+7 800 000 22 33", 2, "2025-01-01", "Scooter season is open!", List.of("BLACK"));
        response = client.createOrders(order);
        Assume.assumeTrue(response.extract().statusCode() == 201);
    }

    @Test
    @DisplayName("Проверка на получение в ответ НЕ пустого списка заказов")
    public void getOrders() {
        ValidatableResponse responseOrders = client.getOrders();
        responseOrders.assertThat().body("orders", notNullValue()).statusCode(200);
    }

    @After
    public void after() {
        int track = response.extract().as(TrackOrder.class).getTrack();
        ValidatableResponse cancel = client.cancelOrder(track);
        cancel.assertThat().body("ok", equalTo(true)).statusCode(200);
    }
}
