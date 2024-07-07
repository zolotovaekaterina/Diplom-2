import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class Steps {

    User staticUser = new User(Constants.email_for_test, Constants.password_static, Constants.name_fot_test);
    User user = new User(Constants.email, Constants.password, Constants.name);
    User userUpdated = new User(Constants.email, Constants.password, Constants.name);
    User emptyFieldName = new User(Constants.email, Constants.password, null);
    User emptyFieldPassword = new User(Constants.email, null, Constants.name);
    User emptyFieldEmail = new User(null, Constants.password, Constants.name);

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