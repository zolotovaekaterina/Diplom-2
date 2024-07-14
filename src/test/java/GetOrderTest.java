import api.clients.OrderClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import models.Constants;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class GetOrderTest {
    OrderClient orderClient = new OrderClient();

    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.URL;
    }

    @Test
    @DisplayName("Получение заказов пользователя с авторизацией")
    public void getOrdersWithAuth() {
        given()
                .header("Content-type", "application/json")
                .auth().oauth2(orderClient.successLogin())
                .when()
                .get("/api/orders")
                .then().statusCode(200)
                .body("success", Matchers.is(true))
                .body("total", Matchers.notNullValue());
    }

    @Test
    @DisplayName("Получение заказов пользователя без авторизации")
    public void getOrdersWithOutAuth() {
        given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/orders")
                .then().statusCode(401)
                .body("success", Matchers.is(false))
                .body("message", Matchers.equalTo("You should be authorised"));
    }
}
