package steps;

import data.Urls;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;


public class OrderSteps {

    // Получение ингредиентов

    public static Response getIngredients() {
        return given()
                .header("Content-Type", "application/json")
                .when()
                .get(Urls.INGREDIENTS);
    }

    // Создание заказа

    public static Response createOrder(String accessToken, List<String> ingredients) {
        Map<String, List<String>> body = new HashMap<>();
        body.put("ingredients", ingredients);

        return given()
                .header("Content-Type", "application/json")
                .header("Authorization", accessToken)
                .body(body)
                .when()
                .post(Urls.ORDERS);
    }

    public static Response createOrderWithoutToken(List<String> ingredients) {
        Map<String, List<String>> body = new HashMap<>();
        body.put("ingredients", ingredients);

        return given()
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .post(Urls.ORDERS);
    }

    public static Response createOrderWithInvalidHash(String accessToken) {
        List<String> invalidIngredients = List.of("invalidHash123");

        Map<String, List<String>> body = new HashMap<>();
        body.put("ingredients", invalidIngredients);

        return given()
                .header("Content-Type", "application/json")
                .header("Authorization", accessToken)
                .body(body)
                .when()
                .post(Urls.ORDERS);
    }

    public static Response createOrderWithoutIngredients(String accessToken) {
        Map<String, List<String>> body = new HashMap<>();
        body.put("ingredients", List.of());

        return given()
                .header("Content-Type", "application/json")
                .header("Authorization", accessToken)
                .body(body)
                .when()
                .post(Urls.ORDERS);
    }

    // Вспомогательные методы

    public static List<String> getValidIngredientIds() {
        return getIngredients()
                .then()
                .statusCode(200)
                .extract()
                .path("data._id");
    }

    public static List<String> getTwoValidIngredientIds() {
        List<String> ids = getValidIngredientIds();
        if (ids.size() < 2) {
            throw new RuntimeException("Not enough ingredients");
        }
        return ids.subList(0, 2);
    }
}