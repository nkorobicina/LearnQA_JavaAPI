import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * Created by user nkorobicina on 14.09.2022.
 */
public class HomeWork7 {

    @Test
    public void ex7LongRedirect(){
        ArrayList<String> locationList = new ArrayList<>();
        int answerCode = 301;
        String keyUrl = "https://playground.learnqa.ru/api/long_redirect";
        Response response;
        int i = 0;
        while(answerCode == 301) {
            response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .get(keyUrl)
                    .andReturn();
            answerCode = response.getStatusCode();
            keyUrl = response.getHeader("location");
            if(keyUrl != null)
            {
                locationList.add(keyUrl);
                i++;
            }
        }

       if(answerCode != 200) {
            System.out.println("На последнем редиректе не получен код 200, код ответа: " + answerCode);
        }
        else
        {
            System.out.println("На последнем редиректе получен код 200, количество итераций: " + i);
        }

        System.out.println(locationList);
    }
}
