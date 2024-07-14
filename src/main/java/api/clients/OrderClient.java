package api.clients;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import models.Constants;
import models.Tokens;
import models.User;

import static io.restassured.RestAssured.given;

public class OrderClient {
    public User staticUser = new User(Constants.email_for_test, Constants.password_static, Constants.name_fot_test);


    @Step("Создание пользователя и получение токена")
    public ValidatableResponse getToken(User user) {
        return RestAssured.given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post("/api/auth/register").then();

    }

    @Step("Авторизация пользователя и получение токена")
    public String successLogin() {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(staticUser)
                .when()
                .post("/api/auth/login").as(Tokens.class).getAccessToken().replaceAll("Bearer ", "");
    }

    @Step("Получение списка ингредиентов")
    public Response getListOfIngredients() {
        return given()
                .header("Content-type", "application/json")
                .and()
                .get("/api/ingredients");
    }


}
