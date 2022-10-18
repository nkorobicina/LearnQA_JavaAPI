package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user nkorobicina on 18.10.2022.
 */
@Epic("User Delete cases")
@Feature("User delete")
public class UserDeleteTest extends BaseTestCase {

    @Test
    @Description("This test try to delete protected user")
    @DisplayName("Test negative. Delete protected user")
    public void testDeleteProtectedUser() {
        Map<String,String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        Response responseDeleteUser = apiCoreRequests
                .makeDeleteRequest
                        ("https://playground.learnqa.ru/api/user/2",
                                responseGetAuth.getHeader("x-csrf-token"),
                                responseGetAuth.getCookie("auth_sid")
                        );

        System.out.println(responseDeleteUser.asString());
        System.out.println(responseDeleteUser.statusCode());

        Assertions.assertResponseCodeEquals(responseDeleteUser, 400);
        Assertions.assertResponseTextEquals(responseDeleteUser, "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");

    }

    @Test
    @Description("This test successfully create user and delete")
    @DisplayName("Test positive delete user")
    public void testDeleteUser() {
        //Generate user
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = apiCoreRequests
                .makePostRequestAndJsonPath("https://playground.learnqa.ru/api/user/", userData);

        String userId = responseCreateAuth.getString("id");

        //login
        Map<String,String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        //delete user
        apiCoreRequests
                .makeDeleteRequest
                        ("https://playground.learnqa.ru/api/user/" + userId,
                                responseGetAuth.getHeader("x-csrf-token"),
                                responseGetAuth.getCookie("auth_sid")
                        );


        //GET
        Response responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/" + userId,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));


        Assertions.assertResponseCodeEquals(responseUserData, 404);
        Assertions.assertResponseTextEquals(responseUserData, "User not found");

    }

    @Test
    @Description("This test successfully create user, create second user and delete")
    @DisplayName("Test negative. Delete other user")
    public void testDeleteOtherUser() {
        //Generate user 1 for login
        Map<String, String> user1Data = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = apiCoreRequests
                .makePostRequestAndJsonPath("https://playground.learnqa.ru/api/user/", user1Data);

        String user1Id = responseCreateAuth.getString("id");

        //login by user 1
        Map<String,String> authData = new HashMap<>();
        authData.put("email", user1Data.get("email"));
        authData.put("password", user1Data.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        //Generate user 2 for delete
        Map<String, String> user2Data = DataGenerator.getRegistrationData();

        JsonPath responseCreateUserForDelete = apiCoreRequests
                .makePostRequestAndJsonPath("https://playground.learnqa.ru/api/user/", user2Data);

        String user2Id = responseCreateUserForDelete.getString("id");



        //delete user 2
        Response responseDeleteUser = apiCoreRequests
                .makeDeleteRequest
                        ("https://playground.learnqa.ru/api/user/" + user2Id,
                                responseGetAuth.getHeader("x-csrf-token"),
                                responseGetAuth.getCookie("auth_sid")
                        );



        //GET user which tried to delete
        Response responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/" + user2Id,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));

        //Пользователь не удалился
        Assertions.assertResponseCodeEquals(responseUserData, 200);


        //GET logged in user
        responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/" + user1Id,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));


        //Залогиненный пользователь удалился
        Assertions.assertResponseCodeEquals(responseUserData, 404);
        Assertions.assertResponseTextEquals(responseUserData, "User not found");

    }
}
