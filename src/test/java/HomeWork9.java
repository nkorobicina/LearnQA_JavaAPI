import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user nkorobicina on 15.09.2022.
 */
public class HomeWork9 {

    private ArrayList<String> getPasswords() throws IOException {
        //забираем из вики пароли
        Document doc = Jsoup.connect("https://en.wikipedia.org/wiki/List_of_the_most_common_passwords").get();
        Elements wikiTables = doc.getElementsByClass("wikitable");
        Elements strings = wikiTables.get(1).getElementsByTag("td");
        ArrayList<String> passwords = new ArrayList<>();

        for(int i = 0; i < strings.size(); i++)
        {
            if(String.valueOf(strings.get(i)).contains("left")){
                passwords.add(strings.get(i).text());

            }
        }
 //       System.out.println(passwords);
        return passwords;
    }

    @Test
    public void ex9SearchPassword() throws IOException {
        //забрали пароли из вики
        ArrayList<String> passwords = getPasswords();
        for(int i = 0; i < passwords.size(); i++)
        {
        Map<String, String> data = new HashMap<>();
        data.put("login", "super_admin");
        data.put("password", passwords.get(i));

        //пробуем залогиниться, получаем авторизационную куку
        Response tryToLogin = RestAssured
             .given()
             .body(data)
             .when()
             .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
             .andReturn();

        Map<String, String> cookie = new HashMap<>();

        cookie.put("auth_cookie", tryToLogin.getCookie("auth_cookie"));
        //проверяем куки
        Response checkCookie = RestAssured
             .given()
             .body(data)
             .cookies(cookie)
             .when()
             .post("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
             .andReturn();

        if(checkCookie.asString().contains("You are authorized")) {
             checkCookie.print();
             System.out.println(data.get("password"));
             break;
        }

    }
    System.out.println("Цикл закончен");




    }
}
