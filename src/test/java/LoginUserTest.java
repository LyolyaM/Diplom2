import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import model.User;
import org.junit.Test;
import steps.UserSteps;
import io.restassured.response.Response;
import org.junit.Before;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

@Epic("Управление пользователями")
@Feature("Авторизация пользователя")

public class LoginUserTest extends BaseApiTest{
    @Before
    @Override
    public void setUp() {
        super.setUp(); // Вызываем настройку RestAssured из BaseApiTest

        // Регистрируем пользователя для тестов и сохраняем его в переменные базового класса
        createdUser = UserSteps.createUniqueUser();
        Response registerResponse = UserSteps.registerUser(createdUser);
        accessToken = UserSteps.getAccessToken(registerResponse);
    }

    @Test
    @Story("Вход в систему")
    @DisplayName("Вход под существующим пользователем")
    @Description("Проверка успешной авторизации зарегистрированного пользователя")
    public void loginExistingUserTest() {
        // Используем созданного в setUp() пользователя createdUser
        UserSteps.loginUser(createdUser)
                .then()
                .log().all()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .body("user.email", equalTo(createdUser.getEmail().toLowerCase()))
                .body("user.name", equalTo(createdUser.getName()));
    }


    @Test
    @Story("Вход с ошибкой")
    @DisplayName("Вход с неверным паролем")
    @Description("Проверка ошибки при входе с неправильным паролем")
    public void loginWithWrongPasswordTest() {
        User wrongUser = User.builder()
                .email(createdUser.getEmail())
                .password("wrongPassword123")
                .build();

        UserSteps.loginUser(wrongUser)
                .then()
                .log().all()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @Story("Вход с ошибкой")
    @DisplayName("Вход с неверным логином")
    @Description("Проверка ошибки при входе с неправильным email")
    public void loginWithWrongEmailTest() {
        User wrongUser = User.builder()
                .email("wrong@email.com")
                .password(createdUser.getPassword())
                .build();

        UserSteps.loginUser(wrongUser)
                .then()
                .log().all()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @Story("Вход с ошибкой")
    @DisplayName("Вход с неверным логином и паролем")
    @Description("Проверка ошибки при входе с неправильными email и паролем")
    public void loginWithWrongEmailAndPasswordTest() {
        // Здесь мы ВООБЩЕ НЕ регистрируем пользователя на сервере, просто шлем фейковые данные
        User wrongUser = User.builder()
                .email("wrong@email.com")
                .password("wrongPassword123")
                .build();

        UserSteps.loginUser(wrongUser)
                .then()
                .log().all()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @Story("Вход с ошибкой")
    @DisplayName("Вход без пароля")
    @Description("Проверка ошибки при входе без пароля")
    public void loginWithoutPasswordTest() {
        User wrongUser = User.builder()
                .email(createdUser.getEmail())
                .build();  // без пароля

        UserSteps.loginUser(wrongUser)
                .then()
                .log().all()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @Story("Вход с ошибкой")
    @DisplayName("Вход без email")
    @Description("Проверка ошибки при входе без email")
    public void loginWithoutEmailTest() {
        User wrongUser = User.builder()
                .password(createdUser.getPassword())
                .build();  //  без email

        UserSteps.loginUser(wrongUser)
                .then()
                .log().all()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @Story("Вход с ошибкой")
    @DisplayName("Вход с пустым телом запроса")
    @Description("Проверка ошибки при пустом теле запроса")
    public void loginWithEmptyBodyTest() { //Пользователя не создаём, очистка не требуется
        User emptyUser = User.builder().build();
        UserSteps.loginUser(emptyUser)
                .then()
                .log().all()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}


