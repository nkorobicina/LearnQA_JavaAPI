import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

/**
 * Created by user nkorobicina on 08.09.2022.
 */
public class HelloWorldTest {

    @Test
    public void testHelloWorld(){
        System.out.println("Hello from Nataly");
    }

    @Test
    public void testEx4(){
        Response response = RestAssured.get("https://playground.learnqa.ru/api/get_text")
                .andReturn();
        response.print();
    }
}
