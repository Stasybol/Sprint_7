import io.qameta.allure.Description;
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
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static ru.praktikum.services.qa.scooter.constant.Url.BASE_URI;


public class GetOrdersTest {
    ScooterServiceClient client;
    ValidatableResponse response;

    @Before
    public void before() {
        client = new ScooterServiceClient(BASE_URI);
        Order order = new Order("Angel", "Smith", "street World, 143", 5, "+7 800 000 22 33", 2, "2025-01-01", "Scooter season is open!", List.of("BLACK"));
        response = client.createOrders(order);
        Assume.assumeTrue(response.extract().statusCode() == SC_CREATED);
    }

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Тест проверяет, что при запросе возвращается НЕ пустой список заказов")
    public void getOrders() {
        ValidatableResponse responseOrders = client.getOrders();
        responseOrders.assertThat().statusCode(SC_OK).body("orders", notNullValue());
    }

    @After
    public void after() {
        int track = response.extract().as(TrackOrder.class).getTrack();
        ValidatableResponse cancel = client.cancelOrder(track);
        cancel.assertThat().statusCode(SC_OK).body("ok", equalTo(true));
    }
}
