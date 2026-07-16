import data.Urls;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.After;
import org.junit.Before;
import model.User;
import steps.UserSteps;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;

public class BaseApiTest {
    protected RequestSpecification requestSpec;
    protected String accessToken;      // ← для хранения токена
    protected User createdUser;        // ← для хранения созданного пользователя

    @Before
    public  void setUp() {
        RestAssured.baseURI = Urls.BASE_URL;
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }

    protected RequestSpecification getBaseRequest() {
        return given()
                .spec(requestSpec)
                .log().all();
    }
    @After
    public void tearDown() {
        // Если в тесте был создан пользователь и получен токен - удаляем его
        if (accessToken != null && !accessToken.isEmpty()) {
            UserSteps.deleteUser(accessToken)
                    .then()
                    .statusCode(SC_ACCEPTED);
            System.out.println(" Пользователь удален после теста");
        }
        // Сбрасываем данные для следующего теста
        accessToken = null;
        createdUser = null;
    }
}
