package tests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user nkorobicina on 14.10.2022.
 */
@Epic("Get user cases")
@Feature("Get user")
public class UserGetTest extends BaseTestCase {

    @Test
    @Description("This test get user without authorization")
    @DisplayName("Test positive get user without authorization")
    @Severity(SeverityLevel.NORMAL)
    @Link("https://software-testing.ru/lms/mod/url/view.php?id=289471")
    @Story("Positive get user")
    public void testGetUserDataNotAuth() {
        Response responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/2");

        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasNotField(responseUserData, "firstName");
        Assertions.assertJsonHasNotField(responseUserData, "lastName");
        Assertions.assertJsonHasNotField(responseUserData, "email");
    }

    @Test
    @Description("This test get the same")
    @DisplayName("Test positive. Same user")
    @Severity(SeverityLevel.CRITICAL)
    @Link("https://software-testing.ru/lms/mod/url/view.php?id=289471")
    @Story("Positive get user")
    public void testGetUserDetailsAuthAsSameUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        Response responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/2", header, cookie);

        String[] expectedFields = {"username", "firstName", "lastName", "email"};
        Assertions.assertJsonHasFields(responseUserData, expectedFields);
    }

    @Test
    @Description("This test authorize and get another user")
    @DisplayName("Test positive. Get other user")
    @Severity(SeverityLevel.CRITICAL)
    @Link("https://software-testing.ru/lms/mod/assign/view.php?id=2894821")
    @Story("Positive get user")
    public void testGetOtherUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        Response responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/1", header, cookie);

        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasNotField(responseUserData, "firstName");
        Assertions.assertJsonHasNotField(responseUserData, "lastName");
        Assertions.assertJsonHasNotField(responseUserData, "email");
    }
}
