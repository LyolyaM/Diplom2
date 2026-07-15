import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import model.User;
import org.junit.Test;
import steps.OrderSteps;
import steps.UserSteps;

import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

@Epic("Управление заказами")
@Feature("Создание заказа")

public class OrderTest extends BaseApiTest {

    // Получение ингредиентов

    @Test
    @Story("Получение ингредиентов")
    @DisplayName("Получение списка ингредиентов")
    @Description("Проверка успешного получения списка ингредиентов")
    public void getIngredientsTest() {
        OrderSteps.getIngredients()
                .then()
                .log().all()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("data", notNullValue());
    }

    // Создание заказа с авторизацией

    @Test
    @Story("Создание заказа")
    @DisplayName("Создание заказа с авторизацией и ингредиентами")
    @Description("Проверка успешного создания заказа авторизованным пользователем")
    public void createOrderWithAuthTest() {
        // 1. Создаём и регистрируем пользователя
        User user = UserSteps.createUniqueUser();
        UserSteps.registerUser(user);

        // 2. Логинимся и получаем accessToken
        accessToken = UserSteps.loginUser(user)
                .then()
                .statusCode(SC_OK)
                .extract()
                .path("accessToken");

        // 3. Получаем два валидных ID ингредиентов
        List<String> ingredients = OrderSteps.getTwoValidIngredientIds();

        // 4. Создаём заказ
        OrderSteps.createOrder(accessToken, ingredients)
                .then()
                .log().all()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("order.number", notNullValue())
                .body("name", notNullValue());
    }

    //  Создание заказа без авторизации, здесь очистка не нужна

    @Test
    @Story("Создание заказа")
    @DisplayName("Создание заказа без авторизации")
    @Description("Проверка создания заказа неавторизованным пользователем")
    public void createOrderWithoutAuthTest() {
        List<String> ingredients = OrderSteps.getTwoValidIngredientIds();

        OrderSteps.createOrderWithoutToken(ingredients)
                .then()
                .log().all()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("order.number", notNullValue())
                .body("name", notNullValue());
    }

    //  Создание заказа без ингредиентов

    @Test
    @Story("Создание заказа с ошибкой")
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Проверка ошибки при создании заказа без ингредиентов")
    public void createOrderWithoutIngredientsTest() {
        User user = UserSteps.createUniqueUser();
        UserSteps.registerUser(user);

        accessToken = UserSteps.loginUser(user)
                .then()
                .statusCode(SC_OK)
                .extract()
                .path("accessToken");

        OrderSteps.createOrderWithoutIngredients(accessToken)
                .then()
                .log().all()
                .statusCode(SC_BAD_REQUEST);

    }

    // Создание заказа с неверным хешем ингредиентов

    @Test
    @Story("Создание заказа с ошибкой")
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Description("Проверка ошибки при создании заказа с невалидным хешем ингредиента")
    public void createOrderWithInvalidHashTest() {
        User user = UserSteps.createUniqueUser();
        UserSteps.registerUser(user);

        accessToken = UserSteps.loginUser(user)
                .then()
                .statusCode(SC_OK)
                .extract()
                .path("accessToken");

        OrderSteps.createOrderWithInvalidHash(accessToken)
                .then()
                .log().all()
                .statusCode(SC_INTERNAL_SERVER_ERROR);

    }

    //  Создание заказа с одним ингредиентом

    @Test
    @Story("Создание заказа")
    @DisplayName("Создание заказа с одним ингредиентом")
    @Description("Проверка создания заказа с одним ингредиентом")
    public void createOrderWithOneIngredientTest() {
        User user = UserSteps.createUniqueUser();
        UserSteps.registerUser(user);

        accessToken = UserSteps.loginUser(user)
                .then()
                .statusCode(SC_OK)
                .extract()
                .path("accessToken");

        List<String> ingredients = OrderSteps.getValidIngredientIds();
        List<String> oneIngredient = ingredients.subList(0, 1);

        OrderSteps.createOrder(accessToken, oneIngredient)
                .then()
                .log().all()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("order.number", notNullValue())
                .body("name", notNullValue());
    }
}
