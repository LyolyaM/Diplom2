import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import model.User;
import org.junit.Test;
import steps.OrderSteps;
import steps.UserSteps;
import org.junit.Before;
import java.util.List;
import io.restassured.response.Response;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

@Epic("Управление заказами")
@Feature("Создание заказа")

public class OrderTest extends BaseApiTest {
    @Before
    @Override
    public void setUp() {
        super.setUp(); // Настраиваем RestAssured из BaseApiTest

        // Создаем, регистрируем пользователя и логинимся, чтобы получить accessToken для тестов
        createdUser = UserSteps.createUniqueUser();
        UserSteps.registerUser(createdUser);

        Response loginResponse = UserSteps.loginUser(createdUser);
        accessToken = UserSteps.getAccessToken(loginResponse);
    }

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

        //  Получаем два валидных ID ингредиентов
        List<String> ingredients = OrderSteps.getTwoValidIngredientIds();

        //  Создаём заказ,с токеном, полученным в setUp()
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
