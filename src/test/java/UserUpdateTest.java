import api.clients.UserClient;
import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import models.Constants;
import models.User;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class UserUpdateTest {
    UserClient userClient = new UserClient();

    static Faker faker = new Faker();

    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.URL;
    }

    @Test
    @DisplayName("Изменение данных пользователя(email, pass, name)")
        public void successUpdateInfoTest() {
        User user = new User(faker.name().firstName()+"@mail.ru", faker.name().username()+"One", faker.name().username()+"Four");
        User userUpd = new User(faker.name().firstName()+"@gmail.com", faker.name().username()+"Two", faker.name().username()+"Five");
        String auth = userClient.getToken(user).extract().path("accessToken").toString();
        given()
                .header("Content-type", "application/json")
                .header("Authorization",
                        auth)
                .body(userUpd)
                .when()
                .patch("/api/auth/user")
                .then().statusCode(200)
                .body("success", Matchers.is(true));
        given().header("Authorization",
                auth).delete("/api/auth/user");
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    public void failedUpdateInfoTest() {
        given()
                .header("Content-type", "application/json")
                .body(userClient.userUpdated)
                .when()
                .patch("/api/auth/user")
                .then().statusCode(401)
                .body("success", Matchers.is(false))
                .body("message", Matchers.equalTo("You should be authorised"));
    }
}
