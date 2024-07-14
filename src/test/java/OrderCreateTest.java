import api.clients.OrderClient;
import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import models.Constants;
import models.Ingredients;
import models.User;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class OrderCreateTest {
    OrderClient orderClient = new OrderClient();
    static Faker faker = new Faker();

    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.URL;
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

    }


    @Test
    @DisplayName("Создание заказа с авторизацией")
    public void successCreationOfOrderTestWithAuth() {
        List<String> listOfOrders = orderClient.getListOfIngredients().getBody().path("data._id");
        Ingredients jsonOne = new Ingredients(listOfOrders.subList(1, 3));
        User usr = new User(faker.name().firstName() + "@ya.ru", faker.name().username() + "one", faker.name().username() + "two");
        String str = orderClient.getToken(usr).extract().path("accessToken").toString();
        given()
                .header("Authorization", str
                )
                .header("Content-type", "application/json")
                .body(jsonOne)
                .when()
                .post("/api/orders")
                .then().statusCode(200)
                .body("success", Matchers.is(true));
        given().header("Authorization",
                str).delete("/api/auth/user");
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void failedCreationOfOrderTestWithoutAuth() {
        List<String> listOfOrders = orderClient.getListOfIngredients().getBody().path("data._id");
        Ingredients jsonOne = new Ingredients(listOfOrders.subList(2, 4));
        given()
                .header("Content-type", "application/json")
                .body(jsonOne)
                .when()
                .post("/api/orders")
                .then().statusCode(200)
                .body("success", Matchers.is(true));
    }

    @Test
    @DisplayName("Создание заказа без ингридиентов")
    public void creationOfOrderTestWithAuthAndWithEmptyIngredients() {
        User usn = new User(faker.name().firstName() + "@gmail.ru", faker.name().username() + "passw", faker.name().username() + "vad");
        String auth = orderClient.getToken(usn).extract().path("accessToken").toString();
        Ingredients jsonOne = new Ingredients();
        given()
                .header("Authorization",
                        auth)
                .header("Content-type", "application/json")
                .body(jsonOne)
                .when()
                .post("/api/orders")
                .then().statusCode(400)
                .body("success", Matchers.is(false));
        given().header("Authorization",
                auth).delete("/api/auth/user");
    }


    @Test
    @DisplayName("Создание заказа с неверным хэшем ингридиентов")
    public void creationOfOrderTestWithAuthAndWithWrongIngredients() {
        User usz = new User(faker.name().firstName() + "@rmb.ru", faker.name().username() + "passt", faker.name().username() + "var");
        String authToken = orderClient.getToken(usz).extract().path("accessToken").toString();
        List<String> ingredients = new ArrayList<>();
        ingredients.add("61c0c5a71d1f82001bdaaa6d1");
        Ingredients json = new Ingredients(ingredients);
        given()
                .header("Authorization",
                        authToken.replaceAll("Bearer ", ""))
                .header("Content-type", "application/json")
                .when()
                .body(json)
                .when()
                .post("/api/orders")
                .then().statusCode(500);
        given().header("Authorization",
                authToken).delete("/api/auth/user");
    }
}
