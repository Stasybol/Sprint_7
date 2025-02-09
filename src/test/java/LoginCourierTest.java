import io.qameta.allure.Description;
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
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static ru.praktikum.services.qa.scooter.constant.Url.BASE_URI;


public class LoginCourierTest {
    private Courier courier;
    private ScooterServiceClient client;

    @Before
    public void before() {
        client = new ScooterServiceClient(BASE_URI);
        courier = DataSetCourier.dataSetValid();
        ValidatableResponse response = client.createCourier(courier);
        Assume.assumeTrue(response.extract().statusCode() == SC_CREATED);
    }

    @Test
    @DisplayName("Успешная авторизация курьера при вводе валидных логина и пароля")
    @Description("Тест проверяет, что авторизация происходит при вводе валидных значений в полях login и password")
    public void validFields(){
        Credentials credentials = Credentials.fromCourier(courier);
        ValidatableResponse response = client.login(credentials);
        response.assertThat().statusCode(SC_OK).body("id", notNullValue());
    }

    @Test
    @DisplayName("Ошибка авторизации курьера при вводе НЕвалидного логина")
    @Description("Тест проверяет, что авторизация НЕ происходит при вводе НЕвалидного поля login и валидного password")
    public void notValidLogin() {
        Credentials credentials = Credentials.otherLoginCourier(courier, "Stasy");
        ValidatableResponse response = client.login(credentials);
        response.assertThat().statusCode(SC_NOT_FOUND).body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Ошибка авторизации курьера при вводе НЕвалидного пароля")
    @Description("Тест проверяет, что авторизация НЕ происходит при вводе валидного поля login и НЕвалидного password")
    public void notValidPassword() {
        Credentials credentials = Credentials.otherPasswordCourier(courier, "123456");
        ValidatableResponse response = client.login(credentials);
        response.assertThat().statusCode(SC_NOT_FOUND).body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Ошибка авторизации курьера при вводе без поля логина")
    @Description("Тест проверяет, что авторизация НЕ происходит при вводе пустого поля login")
    public void withoutLogin() {
        Credentials credentials = Credentials.otherLoginCourier(courier, "");
        ValidatableResponse response = client.login(credentials);
        response.assertThat().statusCode(SC_BAD_REQUEST).body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Ошибка авторизации курьера при вводе без поля пароль")
    @Description("Тест проверяет, что авторизация НЕ происходит при вводе пустого поля password")
    public void withoutPassword() {
        Credentials credentials = Credentials.otherPasswordCourier(courier, "");
        ValidatableResponse response = client.login(credentials);
        response.assertThat().statusCode(SC_BAD_REQUEST).body("message", equalTo("Недостаточно данных для входа"));
    }

    @After
    public void after(){
        Credentials credentials = Credentials.fromCourier(courier);
        ValidatableResponse response = client.login(credentials);
        int idCourier = response.extract().as(IdCourier.class).getId();
        ValidatableResponse delete = client.deleteCourier(idCourier);
        delete.assertThat().statusCode(SC_OK).body("ok", equalTo(true));
    }
}
