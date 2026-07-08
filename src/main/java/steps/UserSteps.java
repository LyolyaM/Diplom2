package steps;

import data.Urls;
import io.restassured.response.Response;
import model.User;
import data.TestData;


import java.util.Locale;

import static io.restassured.RestAssured.given;


public class UserSteps {


    // ============ API-запросы ============

    public static Response registerUser(User user) {
        return given()
                .header("Content-Type", "application/json")
                .body(user)
                .when()
                .post(Urls.REGISTER);
    }

    public static Response loginUser(User user) {
        return given()
                .header("Content-Type", "application/json")
                .body(user)
                .when()
                .post(Urls.LOGIN);
    }

    public static Response updateUser(String accessToken, User user) {
        return given()
                .header("Content-Type", "application/json")
                .header("Authorization", accessToken)
                .body(user)
                .when()
                .patch(Urls.USER);
    }

    public static Response getUser(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .when()
                .get(Urls.USER);
    }

    public static Response logoutUser(String refreshToken) {
        return given()
                .header("Content-Type", "application/json")
                .body("{\"token\": \"" + refreshToken + "\"}")
                .when()
                .post(Urls.LOGOUT);
    }

    public static Response deleteUser(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .when()
                .delete(Urls.USER);
    }

    // ============ Создание тестовых данных ============

    public static User createUniqueUser() {
        return TestData.createUniqueUser();
    }

    public static User createUserWithoutEmail() {
        return TestData.createUserWithoutEmail();
    }

    public static User createUserWithoutPassword() {
        return TestData.createUserWithoutPassword();
    }

    public static User createUserWithoutName() {
        return TestData.createUserWithoutName();
    }

    // ============ Вспомогательные методы для тестов ============

    public static String getAccessToken(Response response) {
        return response.then().extract().path("accessToken");
    }

    public static String getRefreshToken(Response response) {
        return response.then().extract().path("refreshToken");
    }

    public static User getUserFromResponse(Response response) {
        return response.then().extract().path("user");
    }
    }


