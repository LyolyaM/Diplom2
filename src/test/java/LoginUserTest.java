import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import model.User;
import org.junit.Test;
import steps.UserSteps;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

@Epic("Управление пользователями")
@Feature("Авторизация пользователя")

public class LoginUserTest extends BaseApiTest{
    @Test
    @Story("Вход в систему")
    @DisplayName("Вход под существующим пользователем")
    @Description("Проверка успешной авторизации зарегистрированного пользователя")
    public void loginExistingUserTest() {
        // 1. Создаём пользователя
        User user = UserSteps.createUniqueUser();
        UserSteps.registerUser(user);

        // 2. Логинимся
        UserSteps.loginUser(user)
                .then()
                .log().all()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .body("user.email", equalTo(user.getEmail().toLowerCase()))
                .body("user.name", equalTo(user.getName()));
    }


    @Test
    @Story("Вход с ошибкой")
    @DisplayName("Вход с неверным паролем")
    @Description("Проверка ошибки при входе с неправильным паролем")
    public void loginWithWrongPasswordTest() {
        // 1. Создаём пользователя
        User user = UserSteps.createUniqueUser();
        UserSteps.registerUser(user);

        // 2. Логинимся с НЕПРАВИЛЬНЫМ паролем
        User wrongUser = User.builder()
                .email(user.getEmail())
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
        // 1. Создаём пользователя
        User user = UserSteps.createUniqueUser();
        UserSteps.registerUser(user);

        // 2. Логинимся с НЕПРАВИЛЬНЫМ email
        User wrongUser = User.builder()
                .email("wrong@email.com")
                .password(user.getPassword())
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
        User user = UserSteps.createUniqueUser();
        UserSteps.registerUser(user);

        User wrongUser = User.builder()
                .email(user.getEmail())
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
        User user = UserSteps.createUniqueUser();
        UserSteps.registerUser(user);

        User wrongUser = User.builder()
                .password(user.getPassword())
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
    public void loginWithEmptyBodyTest() {
        User emptyUser = User.builder().build();
        UserSteps.loginUser(emptyUser)
                .then()
                .log().all()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}


