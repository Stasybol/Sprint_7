package ru.praktikum.services.qa.scooter.client;

import static io.restassured.RestAssured.given;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.praktikum.services.qa.scooter.model.Courier;
import ru.praktikum.services.qa.scooter.model.Credentials;
import ru.praktikum.services.qa.scooter.model.Order;

public class ScooterServiceClient {

    private String baseURI;

    public ScooterServiceClient(String baseURI) {
        this.baseURI = baseURI;
    }

    @Step("Создание курьера")
    public ValidatableResponse createCourier(Courier courier) {
        return given()
                .log()
                .all()
                .baseUri(baseURI)
                .header("Content-Type", "application/json")
                .body(courier)
                .post("/api/v1/courier")
                .then()
                .log()
                .all();
    }

    @Step("Авторизация курьера")
    public ValidatableResponse login(Credentials credentials) {
        return given()
                .log()
                .all()
                .baseUri(baseURI)
                .header("Content-Type", "application/json")
                .body(credentials)
                .post("/api/v1/courier/login")
                .then()
                .log()
                .all();
    }

    @Step("Получение списка заказов")
    public ValidatableResponse getOrders() {
        return given()
                .log()
                .all()
                .baseUri(baseURI)
                .header("Content-Type", "application/json")
                .get("/api/v1/orders")
                .then()
                .log()
                .all();
    }

    @Step("Создание заказа")
    public ValidatableResponse createOrders(Order order){
        return given()
                .log()
                .all()
                .baseUri(baseURI)
                .header("Content-Type", "application/json")
                .body(order)
                .post("/api/v1/orders")
                .then()
                .log()
                .all();
    }

    @Step("Удаление курьера")
    public ValidatableResponse deleteCourier(int id) {
        return given()
                .log()
                .all()
                .baseUri(baseURI)
                .header("Content-Type", "application/json")
                .delete("/api/v1/courier/" + id)
                .then()
                .log()
                .all();

    }

    @Step("Отмена заказа")
    public ValidatableResponse cancelOrder(int track){
        return given()
                .log()
                .all()
                .baseUri(baseURI)
                .header("Content-Type", "application/json")
                .queryParam("track", track)
                .put("/api/v1/orders/cancel")
                .then()
                .log()
                .all();
    }
}


