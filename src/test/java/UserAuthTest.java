import api.clients.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import models.Constants;
import org.junit.Before;
import org.junit.Test;
import org.hamcrest.Matchers;

public class UserAuthTest {

    UserClient userClient = new UserClient();

    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.URL;
    }


    @Test
    @DisplayName("Авторизация пользователя")
    public void successLogin() {
        userClient.authOfUser(userClient.staticUser).then().statusCode(200)
                .and().body("success", Matchers.is(true))
                .and().body("accessToken", Matchers.notNullValue())
                .and().body("refreshToken", Matchers.notNullValue());
    }

    @Test
    @DisplayName("Авторизация несуществующего пользователя")
    public void failedLogin() {
        userClient.authOfUser(userClient.user).then().statusCode(401)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.equalTo("email or password are incorrect"));
    }
}
