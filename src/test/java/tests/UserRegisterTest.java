package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user nkorobicina on 14.10.2022.
 */
@Epic("User registration cases")
@Feature("Registration")
public class UserRegisterTest extends BaseTestCase {

    @Test
    @Description("This test try to register user with existing email")
    @DisplayName("Test negative registration. Existing email")
    public void testCreateUserWithExistingEmail() {
        String email = "vinkotov@example.com";
        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");
    }

    @Test
    @Description("This test successfully register user")
    @DisplayName("Test positive registration")
    public void testCreateUserSuccessfully() {
        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
        Assertions.assertJsonHasField(responseCreateAuth, "id");
    }

    @Test
    @Description("This test try to register user with invalid email")
    @DisplayName("Test negative registration. Invalid email")
    public void testCreateUserWithInvalidEmail() {
        Map<String, String> invalidEmail = new HashMap<>();
        invalidEmail.put("email", DataGenerator.getRandomInvalidEmail());
        Map<String, String> userData = DataGenerator.getRegistrationData(invalidEmail);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Invalid email format");
    }

    @Description("This test try to register user without one of field")
    @DisplayName("Test negative registration. Invalid set of fields")
    @ParameterizedTest
    @ValueSource(strings = {"email", "password", "username", "firstName", "lastName"})
    public void testCreateUserWithInvalidSetOfFields(String fieldkey) {
        Map<String, String> userData = DataGenerator.getRegistrationDataWithoutOneField(fieldkey);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The following required params are missed: " + fieldkey);
    }

    @Description("This test try to register user with short name")
    @DisplayName("Test negative registration. Short name")
    @ParameterizedTest
    @ValueSource(strings = {"username", "firstName", "lastName"})
    public void testCreateUserWithShortName(String name) {
        Map<String, String> userData = DataGenerator.getRegistrationDataWithShortName(name);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of '" + name + "' field is too short");
    }

    @Description("This test try to register user with too long name")
    @DisplayName("Test negative registration. Long name")
    @ParameterizedTest
    @ValueSource(strings = {"username", "firstName", "lastName"})
    public void testCreateUserWithLongName(String name) {
        Map<String, String> userData = DataGenerator.getRegistrationDataWithLongName(name);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);


        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of '" + name + "' field is too long");
    }
}
