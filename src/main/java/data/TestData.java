package data;
import com.github.javafaker.Faker;
import model.User;

import java.util.Locale;

public class TestData {
    private static final Faker faker = new Faker(new Locale("ru"));

    // ============ Генерация отдельных полей ============

    public static String generateEmail() {
        return faker.internet().emailAddress();
    }

    public static String generatePassword() {
        return faker.internet().password(6, 12);
    }

    public static String generateName() {
        return faker.name().fullName();
    }

    public static String generateDigits(int count) {
        return faker.regexify("[0-9]{" + count + "}");
    }

    // ============ Создание пользователей ============

    public static User createUniqueUser() {
        return User.builder()
                .email(generateEmail())
                .password(generatePassword())
                .name(generateName())
                .build();
    }

    public static User createUserWithoutEmail() {
        return User.builder()
                .password(generatePassword())
                .name(generateName())
                .build();
    }

    public static User createUserWithoutPassword() {
        return User.builder()
                .email(generateEmail())
                .name(generateName())
                .build();
    }

    public static User createUserWithoutName() {
        return User.builder()
                .email(generateEmail())
                .password(generatePassword())
                .build();
    }

}
