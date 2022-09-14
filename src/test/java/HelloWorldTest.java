import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

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

    @Test
    public void testEx4(){
        Response response = RestAssured.get("https://playground.learnqa.ru/api/get_text")
                .andReturn();
        response.print();
    }
}
