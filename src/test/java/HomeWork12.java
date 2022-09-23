import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by user nkorobicina on 23.09.2022.
 */
public class HomeWork12 {

    @Test
    public void homeWork12() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();

        assertEquals("Some secret value", response.getHeader("x-secret-homework-header"));
    }
}
