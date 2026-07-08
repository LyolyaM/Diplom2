import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import model.User;
import org.junit.Test;
import steps.UserSteps;

import static org.hamcrest.Matchers.*;
import static org.apache.http.HttpStatus.*;

@Epic("Управление пользователями")
@Feature("Регистрация пользователя")

public class CreateUserTest extends BaseApiTest{
    @Test
    @Story("Создание нового пользователя")
    @DisplayName("Создание уникального пользователя")
    @Description("Проверка успешной регистрации нового пользователя с уникальными данными")
    public void createUniqueUserTest() {
        // Создаём пользователя через UserSteps
        User user = UserSteps.createUniqueUser();

        UserSteps.registerUser(user)
                .then()
                .statusCode(SC_OK )
                .body("success", equalTo(true))
                .body("user.email", equalTo(user.getEmail().toLowerCase()))
                .body("user.name", equalTo(user.getName()))
                .body("accessToken", notNullValue());
    }

    @Test
    @Story("Создание пользователя с конфликтом")
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    @Description("Проверка ошибки при попытке создать существующего пользователя с уже существующим email")
    public void createAlreadyRegisteredUserTest() {
        User user = UserSteps.createUniqueUser();
        UserSteps.registerUser(user); // первый раз

        UserSteps.registerUser(user) // второй раз
                .then()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @Story("Создание пользователя с ошибкой валидации")
    @DisplayName("Создание пользователя без email")
    @Description("Проверка ошибки при регистрации без обязательного поля email")
    public void createUserWithoutEmailTest() {
        User user = UserSteps.createUserWithoutEmail();

        UserSteps.registerUser(user)
                .then()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @Story("Создание пользователя с ошибкой валидации")
    @DisplayName("Создание пользователя без password")
    @Description("Проверка ошибки при регистрации без обязательного поля password")
    public void createUserWithoutPasswordTest() {
        User user = UserSteps.createUserWithoutPassword();

        UserSteps.registerUser(user)
                .then()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @Story("Создание пользователя с ошибкой валидации")
    @DisplayName("Создание пользователя без name")
    @Description("Проверка ошибки при регистрации без обязательного поля name")
    public void createUserWithoutNameTest() {
        User user = UserSteps.createUserWithoutName();

        UserSteps.registerUser(user)
                .then()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

}
