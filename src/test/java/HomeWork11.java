import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by user nkorobicina on 23.09.2022.
 */
public class HomeWork11 {

    @Test
    public void testHomeworkCookie(){
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();

        assertEquals("hw_value", response.getCookie("HomeWork"));
    }
}
