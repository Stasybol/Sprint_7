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


public class CreateCourierTest {
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru/";
    private Courier courier;
    private ScooterServiceClient client;


    @Before
    public void before() {
        client = new ScooterServiceClient(BASE_URI);
    }

    @Test
    @DisplayName("Курьер создается при заполнении поля логин, пароль, имя")
    public void allValidFields(){
        courier = DataSetCourier.dataSetValid();
        ValidatableResponse response = client.createCourier(courier);
        response.assertThat().body("ok", equalTo(true)).statusCode(201);
    }

    @Test
    @DisplayName("Курьер создается при заполнении поля логин и пароль")
    public void withoutFirstName(){
        courier = DataSetCourier.dataSetWithoutFirstName();
        ValidatableResponse response = client.createCourier(courier);
        response.assertThat().body("ok", equalTo(true)).statusCode(201);
    }

    @Test
    @DisplayName("Курьер НЕ создается без поля логин")
    public void withoutLogin(){
        courier = DataSetCourier.dataSetWithoutLogin();
        ValidatableResponse response = client.createCourier(courier);
        response.assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи")).statusCode(400);
    }

    @Test
    @DisplayName("Курьер НЕ создается без поля пароль")
    public void withoutPassword(){
        courier = DataSetCourier.dataSetWithoutPassword();
        ValidatableResponse response = client.createCourier(courier);
        response.assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи")).statusCode(400);
    }

    @Test
    @DisplayName("Два одиноковых курьера НЕ создаются")
    public void withExistingLogin(){
        courier = DataSetCourier.dataSetValid();
        ValidatableResponse response = client.createCourier(courier);
        ValidatableResponse responseRepeat = client.createCourier(courier);
        responseRepeat.assertThat().body("message", equalTo("Этот логин уже используется")).statusCode(409);
    }

    @After
    public void after(){
        Credentials credentials = Credentials.fromCourier(courier);
        ValidatableResponse response = client.login(credentials);
        int idCourier = response.extract().as(IdCourier.class).getId();
        if (idCourier != 0) {
            ValidatableResponse delete = client.deleteCourier(idCourier);
            delete.assertThat().body("ok", equalTo(true)).statusCode(200);
        }
    }
}
