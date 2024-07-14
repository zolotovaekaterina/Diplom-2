import api.clients.UserClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import models.Constants;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

public class UserCreateTest {

    UserClient userClient = new UserClient();

    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.URL;
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Test
    @DisplayName("Успешное создание пользователя")
    public void successCreationOfUser() {
        userClient.creationOfUser(userClient.user).then().statusCode(200)
                .and().body("accessToken", Matchers.notNullValue())
                .and().body("success", Matchers.is(true))
                .and().body("user.email", Matchers.notNullValue())
                .and().body("user.name", Matchers.notNullValue())
                .and().body("refreshToken", Matchers.notNullValue());
    }

    @Test
    @DisplayName("Создание уже существующего пользователя")
    public void duplicateCheckOfUserCreation() {
        userClient.creationOfUser(userClient.staticUser);
        userClient.creationOfUser(userClient.staticUser).then().statusCode(403)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    public void emptyFieldNameCheckOfUserCreation() {
        userClient.creationOfUser(userClient.emptyFieldName).then().statusCode(403)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    public void emptyFieldPasswordCheckOfUserCreation() {
        userClient.creationOfUser(userClient.emptyFieldPassword).then().statusCode(403)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без email")
    public void emptyFieldEmailCheckOfUserCreation() {
        userClient.creationOfUser(userClient.emptyFieldEmail).then().statusCode(403)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.equalTo("Email, password and name are required fields"));
    }

}
