package tests;

import io.qameta.allure.*;
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
 * Created by user nkorobicina on 14.10.2022.
 */
@Epic("User Edit cases")
@Feature("User edit")
public class UserEditTest extends BaseTestCase {

    @Test
    @Description("This test successfully create and edit user")
    @DisplayName("Test positive edit user")
    @Severity(SeverityLevel.CRITICAL)
    @Link("https://software-testing.ru/lms/mod/url/view.php?id=289472")
    @Story("Positive edit user")
    public void testEditJustCreatedTest() {
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

        //edit
        String newName = "Changed name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        apiCoreRequests.makePutRequest(
                "https://playground.learnqa.ru/api/user/" + userId,
                this.getHeader(responseGetAuth, "x-csrf-token"), this.getCookie(responseGetAuth, "auth_sid"),
                editData
        );
        //GET
        Response responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/" + userId,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));

        Assertions.assertJsonByName(responseUserData, "firstName", newName);

    }

    @Test
    @Description("This test try to edit user while not authorized")
    @DisplayName("Test negative edit user. Not Authorized")
    @Severity(SeverityLevel.NORMAL)
    @Link("https://software-testing.ru/lms/mod/assign/view.php?id=289483")
    @Story("Negative edit user")
    public void testEditNotAuthTest() {

        //edit
        String newName = "Changed name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response editUser = apiCoreRequests.makePutRequestNotAuth(
                "https://playground.learnqa.ru/api/user/" + "2",

                editData
        );

        Assertions.assertResponseCodeEquals(editUser, 400);
        Assertions.assertResponseTextEquals(editUser, "Auth token not supplied");

    }

    @Test
    @Description("This test successfully create and try to edit other user")
    @DisplayName("Test negative. Edit other user")
    @Story("Probably a bug")
    @Severity(SeverityLevel.NORMAL)
    @Link("https://software-testing.ru/lms/mod/assign/view.php?id=289483")
    public void testEditOtherUser() {
        //Generate user 1 for login
        Map<String, String> user1Data = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = apiCoreRequests
                .makePostRequestAndJsonPath("https://playground.learnqa.ru/api/user/", user1Data);

        String user1Id = responseCreateAuth.getString("id");

        //Generate user 2 for edit
        Map<String, String> user2Data = DataGenerator.getRegistrationData();

        JsonPath responseCreateUserForEdit = apiCoreRequests
                .makePostRequestAndJsonPath("https://playground.learnqa.ru/api/user/", user2Data);

        String user2Id = responseCreateUserForEdit.getString("id");

        //login user 1
        Map<String,String> authData = new HashMap<>();
        authData.put("email", user1Data.get("email"));
        authData.put("password", user1Data.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        //edit user 2
        String newName = "Changed name";
        Map<String, String> editData = new HashMap<>();
        editData.put("username", newName);

        Response responseEditUser = apiCoreRequests.makePutRequest(
                "https://playground.learnqa.ru/api/user/" + user2Id,
                this.getHeader(responseGetAuth, "x-csrf-token"), this.getCookie(responseGetAuth, "auth_sid"),
                editData
        );

        //GET logged in user
        Response responseUser1Data = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/" + user1Id,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));

        //меняется имя залогиненного юзера
        Assertions.assertJsonByName(responseUser1Data, "username", newName);

        //GET user which was tried to edit
        Response responseUser2Data = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/" + user2Id,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));

        //имя редактируемого юзера не меняется
        Assertions.assertJsonByName(responseUser2Data, "username", user2Data.get("username"));
    }

    @Test
    @Description("This test successfully create and try to edit userwith invalid email")
    @DisplayName("Test negative edit user. Invalid email")
    @Severity(SeverityLevel.NORMAL)
    @Link("https://software-testing.ru/lms/mod/assign/view.php?id=289483")
    @Story("Negative edit user")
    public void testEditWithInvalidEmail() {
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

        //edit
        String newEmail = DataGenerator.getRandomInvalidEmail();
        Map<String, String> editData = new HashMap<>();
        editData.put("email", newEmail);

        Response responseEditUser = apiCoreRequests.makePutRequest(
                "https://playground.learnqa.ru/api/user/" + userId,
                this.getHeader(responseGetAuth, "x-csrf-token"), this.getCookie(responseGetAuth, "auth_sid"),
                editData
        );


       Assertions.assertResponseTextEquals(responseEditUser, "Invalid email format");
       Assertions.assertResponseCodeEquals(responseEditUser, 400);


    }


    @Test
    @Description("This test successfully create and try to edit user with invalid firstName")
    @DisplayName("Test negative edit user. Invalid firstName")
    @Severity(SeverityLevel.NORMAL)
    @Link("https://software-testing.ru/lms/mod/assign/view.php?id=289483")
    @Story("Negative edit user")
    public void testEditWithInvalidFirstName() {
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

        //edit
        String newFirstName = "a";
        Map<String, String> editData = new HashMap<>();


        editData.put("firstName", newFirstName);

        Response responseEditUser = apiCoreRequests.makePutRequest(
                "https://playground.learnqa.ru/api/user/" + userId,
                this.getHeader(responseGetAuth, "x-csrf-token"), this.getCookie(responseGetAuth, "auth_sid"),
                editData
        );

        Assertions.assertResponseTextEquals(responseEditUser, "{\"error\":\"Too short value for field firstName\"}");
        Assertions.assertResponseCodeEquals(responseEditUser, 400);


    }
}
