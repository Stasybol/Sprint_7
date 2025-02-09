import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.services.qa.scooter.client.ScooterServiceClient;
import ru.praktikum.services.qa.scooter.data.DataSetCourier;
import ru.praktikum.services.qa.scooter.model.Courier;
import ru.praktikum.services.qa.scooter.model.Credentials;
import ru.praktikum.services.qa.scooter.model.IdCourier;
import static org.hamcrest.CoreMatchers.equalTo;
import static ru.praktikum.services.qa.scooter.constant.Url.BASE_URI;
import static org.apache.http.HttpStatus.*;


public class CreateCourierTest {
    private Courier courier;
    private ScooterServiceClient client;

    @Before
    public void before() {
          client = new ScooterServiceClient(BASE_URI);
    }

    @Test
    @DisplayName("Создание курьера при заполнении всех полей")
    @Description("Тест проверяет, что курьер создается при заполнении полей: login, password и firstName")
    public void allValidFields(){
        courier = DataSetCourier.dataSetValid();
        ValidatableResponse response = client.createCourier(courier);
        response.assertThat().statusCode(SC_CREATED).body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Создание курьера при заполнении логина и пароля")
    @Description("Тест проверяет, что курьер создается при заполнении полей: login и password")
    public void withoutFirstName(){
        courier = DataSetCourier.dataSetWithoutFirstName();
        ValidatableResponse response = client.createCourier(courier);
        response.assertThat().statusCode(SC_CREATED).body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Ошибка при создании курьера без логина")
    @Description("Тест проверяет, что курьер НЕ создается без поля login")
    public void withoutLogin(){
        courier = DataSetCourier.dataSetWithoutLogin();
        ValidatableResponse response = client.createCourier(courier);
        response.assertThat().statusCode(SC_BAD_REQUEST).body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Ошибка при создании курьера без пароля")
    @Description("Тест проверяет, что курьер НЕ создается без поля password")
    public void withoutPassword(){
        courier = DataSetCourier.dataSetWithoutPassword();
        ValidatableResponse response = client.createCourier(courier);
        response.assertThat().statusCode(SC_BAD_REQUEST).body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Ошибка при создании двух одинаковых курьеров")
    @Description("Тест проверяет, что курьер НЕ создается с логином и паролем, которые уже есть")
    public void withExistingLogin(){
        courier = DataSetCourier.dataSetValid();
        ValidatableResponse response = client.createCourier(courier);
        ValidatableResponse responseRepeat = client.createCourier(courier);
        responseRepeat.assertThat().statusCode(SC_CONFLICT).body("message", equalTo("Этот логин уже используется"));
    }

    @After
    public void after(){
        Credentials credentials = Credentials.fromCourier(courier);
        ValidatableResponse response = client.login(credentials);
        int idCourier = response.extract().as(IdCourier.class).getId();
        if (idCourier != 0) {
            ValidatableResponse delete = client.deleteCourier(idCourier);
            delete.assertThat().statusCode(SC_OK).body("ok", equalTo(true));
        }
    }
}
