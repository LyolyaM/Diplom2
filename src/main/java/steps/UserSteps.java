package steps;

import data.Urls;
import io.restassured.response.Response;
import model.User;
import data.TestData;
import io.qameta.allure.Step;


import java.util.Locale;

import static io.restassured.RestAssured.given;


public class UserSteps {


    // ============ API-запросы ============
    @Step("Зарегистрировать пользователя")
    public static Response registerUser(User user) {
        return given()
                .header("Content-Type", "application/json")
                .body(user)
                .when()
                .post(Urls.REGISTER);
    }
    @Step("Авторизовать пользователя")
    public static Response loginUser(User user) {
        return given()
                .header("Content-Type", "application/json")
                .body(user)
                .when()
                .post(Urls.LOGIN);
    }
    @Step("Обновить данные пользователя")
    public static Response updateUser(String accessToken, User user) {
        return given()
                .header("Content-Type", "application/json")
                .header("Authorization", accessToken)
                .body(user)
                .when()
                .patch(Urls.USER);
    }
    @Step("Получить данные текущего пользователя")
    public static Response getUser(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .when()
                .get(Urls.USER);
    }
    @Step("Выйти из системы (деавторизация)")
    public static Response logoutUser(String refreshToken) {
        return given()
                .header("Content-Type", "application/json")
                .body("{\"token\": \"" + refreshToken + "\"}")
                .when()
                .post(Urls.LOGOUT);
    }
    @Step("Удалить пользователя")
    public static Response deleteUser(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .when()
                .delete(Urls.USER);
    }

    // ============ Создание тестовых данных ============
    @Step("Создать данные уникального пользователя")
    public static User createUniqueUser() {
        return TestData.createUniqueUser();
    }
    @Step("Создать данные пользователя без Email")
    public static User createUserWithoutEmail() {
        return TestData.createUserWithoutEmail();
    }
    @Step("Создать данные пользователя без пароля")
    public static User createUserWithoutPassword() {
        return TestData.createUserWithoutPassword();
    }
    @Step("Создать данные пользователя без имени")
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


