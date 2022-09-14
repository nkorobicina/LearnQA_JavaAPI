import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;


/**
 * Created by user nkorobicina on 14.09.2022.
 */
public class HomeWork6 {

    @Test
    public void ex6Redirect() {

        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();


        System.out.println(response.getHeader("location"));
    }

}
