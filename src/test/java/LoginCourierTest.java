import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.services.qa.scooter.client.ScooterServiceClient;
import ru.praktikum.services.qa.scooter.data.DataSetCourier;
import ru.praktikum.services.qa.scooter.model.Courier;
import ru.praktikum.services.qa.scooter.model.Credentials;
import ru.praktikum.services.qa.scooter.model.IdCourier;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginCourierTest {
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru/";
    private Courier courier;
    private ScooterServiceClient client;

    @Before
    public void before() {
        client = new ScooterServiceClient(BASE_URI);
        courier = DataSetCourier.dataSetValid();
        ValidatableResponse response = client.createCourier(courier);
        Assume.assumeTrue(response.extract().statusCode() == 201);
    }

    @Test
    @DisplayName("Успешная авторизация курьера при вводе валидных логина и пароля")
    public void validFields(){
        Credentials credentials = Credentials.fromCourier(courier);
        ValidatableResponse response = client.login(credentials);
        response.assertThat().body("id", notNullValue()).statusCode(200);
    }

    @Test
    @DisplayName("Курьер НЕ авторизован при вводе НЕвалидного логина")
    public void notValidLogin() {
        Credentials credentials = Credentials.otherLoginCourier(courier, "Stasy");
        ValidatableResponse response = client.login(credentials);
        response.assertThat().body("message", equalTo("Учетная запись не найдена")).statusCode(404);
    }

    @Test
    @DisplayName("Курьер НЕ авторизован при вводе НЕвалидного пароля")
    public void notValidPassword() {
        Credentials credentials = Credentials.otherPasswordCourier(courier, "123456");
        ValidatableResponse response = client.login(credentials);
        response.assertThat().body("message", equalTo("Учетная запись не найдена")).statusCode(404);
    }

    @Test
    @DisplayName("Курьер НЕ авторизован без поля логин")
    public void withoutLogin() {
        Credentials credentials = Credentials.otherLoginCourier(courier, "");
        ValidatableResponse response = client.login(credentials);
        response.assertThat().body("message", equalTo("Недостаточно данных для входа")).statusCode(400);
    }

    @Test
    @DisplayName("Курьер НЕ авторизован без поля пароль")
    public void withoutPassword() {
        Credentials credentials = Credentials.otherPasswordCourier(courier, "");
        ValidatableResponse response = client.login(credentials);
        response.assertThat().body("message", equalTo("Недостаточно данных для входа")).statusCode(400);
    }

    @After
    public void after(){
        Credentials credentials = Credentials.fromCourier(courier);
        ValidatableResponse response = client.login(credentials);
        int idCourier = response.extract().as(IdCourier.class).getId();
        ValidatableResponse delete = client.deleteCourier(idCourier);
        delete.assertThat().body("ok", equalTo(true)).statusCode(200);
    }
}
