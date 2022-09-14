import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

/**
 * Created by user nkorobicina on 14.09.2022.
 */
public class HomeWork5 {


    @Test
    public void ex5ParsingJson(){

        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();

        String message2 = response.get("messages.message[1]");
        System.out.println(message2);

    }
}
