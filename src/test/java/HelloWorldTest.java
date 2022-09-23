import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by user nkorobicina on 08.09.2022.
 */
public class HelloWorldTest {

    @Test
    public void testRestAssured(){
        Map<String, Object> body = new HashMap<>();
        body.put("param1","value1");
        body.put("param2","value2");

        Response response = RestAssured
                .given()
                .body(body)
                .post("https://playground.learnqa.ru/api/check_type")
                .andReturn();
        int statusCode = response.getStatusCode();
        System.out.println(statusCode);
        response.print();

    }

    @ParameterizedTest
    @ValueSource(strings = {"", "John", "Pete"})
    public void testRestAssured2(String name) {
        Map<String, String> queryParams = new HashMap<>();

        if(name.length() > 0){
            queryParams.put("name", name);
        }
        JsonPath response = RestAssured
                .given()
                .queryParams(queryParams)
                .get("https://playground.learnqa.ru/api/hello")
                .jsonPath();
        String answer = response.getString("answer");
        String expectedName = (name.length() > 0) ? name : "someone";
        assertEquals("Hello, " + expectedName, answer, "The answer is not expected");
    }

    @Test
    public void testHelloMethodWithoutName(){
        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/hello")
                .jsonPath();
        String answer = response.getString("answer");
        assertEquals("Hello, someone", answer, "The answer is not expected");
    }

    @Test
    public void testHelloMethodWithName(){
        String name = "Username";
        JsonPath response = RestAssured
                .given()
                .queryParam("name", name)
                .get("https://playground.learnqa.ru/api/hello")
                .jsonPath();
        String answer = response.getString("answer");
        assertEquals("Hello, " + name, answer, "The answer is not expected");
    }

    @Test
    public void testEx4(){
        Response response = RestAssured.get("https://playground.learnqa.ru/api/get_text")
                .andReturn();
        response.print();
    }
}
