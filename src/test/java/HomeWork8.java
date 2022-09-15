import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import static java.lang.Thread.sleep;

/**
 * Created by user nkorobicina on 15.09.2022.
 */
public class HomeWork8 {

    @Test
    public void ex8Tokens() throws InterruptedException {

        JsonPath jsonTask = RestAssured
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();

        String token = jsonTask.get("token");
        String seconds = jsonTask.get("seconds").toString();

        JsonPath checkStatus = RestAssured
                .given()
                .queryParam("token", token)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();

        Object status = checkStatus.get("status");
        if ("No job linked to this token".equals(status)) {
            System.out.println("Задача не найдена");
        } else if ("Job is NOT ready".equals(status)) {
            System.out.println("Задача не завершена, включаем ожидание");
            sleep(Long.parseLong(seconds + "000"));
            checkStatus = RestAssured
                    .given()
                    .queryParam("token", token)
                    .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                    .jsonPath();
            status = checkStatus.get("status");
            if ("No job linked to this token".equals(status)) {
                System.out.println("Задача не найдена");
            } else if ("Job is NOT ready".equals(status)) {
                System.out.println("Задача не завершена после ожидания");
            } else {
                System.out.println("Задача завершена, результат: " + checkStatus.get("status"));
            }
        } else {
            System.out.println("Задача завершена без ожидания, результат: " + checkStatus.get("status"));
        }
    }
}
