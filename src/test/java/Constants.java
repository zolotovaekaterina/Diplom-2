import com.github.javafaker.Faker;

public class Constants {
    static Faker faker = new Faker();
    public static final String URL = "https://stellarburgers.nomoreparties.site/";

    public static final String email_for_test = "test.ya.ya09-ya@ya.ru";
    public static final String name_fot_test = "log1n007";
    public static final String password_static = "Pas5w0rd";

    public static final String email = faker.name().firstName()+"@ya.ru";
    public static final String password = faker.name().lastName()+"pass";
    public static final String name = faker.name().username();

}
