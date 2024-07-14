package api.clients;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import models.Constants;
import models.Tokens;
import models.User;

import static io.restassured.RestAssured.given;

public class UserClient {
    public User staticUser = new User(Constants.email_for_test, Constants.password_static, Constants.name_fot_test);
    public User user = new User(Constants.email, Constants.password, Constants.name);
    public User userUpdated = new User(Constants.email, Constants.password, Constants.name);
    public User emptyFieldName = new User(Constants.email, Constants.password, null);
    public User emptyFieldPassword = new User(Constants.email, null, Constants.name);
    public User emptyFieldEmail = new User(null, Constants.password, Constants.name);

    @Step("Создание пользователя")
    public Response creationOfUser(User user) {
        return
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(user)
                        .when()
                        .post("/api/auth/register");
    }

    @Step("Авторизация пользователя")
    public Response authOfUser(User user) {
        return
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(user)
                        .when()
                        .post("/api/auth/login");
    }


    @Step("Создание пользователя и получение токена")
    public ValidatableResponse getToken(User user) {
        return RestAssured.given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post("/api/auth/register").then();

    }


}
