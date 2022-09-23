import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by user nkorobicina on 23.09.2022.
 */
public class TestHomeWork13 {

    @ParameterizedTest
    @CsvSource({
            "Mobile, No, Android, Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30",
            "Mobile, Chrome, iOS, Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1",
            "Googlebot, Unknown, Unknown, Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)",
            "Web, Chrome, No, Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0",
            "Mobile, No, iPhone, Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1"

    })
    public void homeWork13(String platform, String browser, String device, String userAgent) {

        JsonPath checkUserAgent = RestAssured
                .given()
                .header("user-agent", userAgent)
                .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
                .jsonPath();

        String platformFromRequest = checkUserAgent.get("platform");
        String browserFromRequest = checkUserAgent.get("browser");
        String deviceFromRequest = checkUserAgent.get("device");
        System.out.println("\nGot values: platform: " + platformFromRequest + "; browser: " + browserFromRequest + "; device: " + deviceFromRequest +
                "\nExpected values:\n" +
                "platform: " + platform + "; browser: " + browser + "; device: " + device +
                "\n\nUser Agent: " + userAgent);

        assertTrue((platform.equals(platformFromRequest) && (device.equals(deviceFromRequest) && (browser.equals(browserFromRequest)))),
                "Wrong answer.");
    }
}
