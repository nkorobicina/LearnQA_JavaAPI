package lib;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by user nkorobicina on 14.10.2022.
 */
public class DataGenerator {
    public static String getRandomEmail() {
        String timestamp = new SimpleDateFormat("yyyMMddHHmmss").format(new java.util.Date());
        return "learnqa" + timestamp + "@example.com";
    }

    public static String getRandomInvalidEmail() {
        String timestamp = new SimpleDateFormat("yyyMMddHHmmss").format(new java.util.Date());
        return "learnqa" + timestamp + "example.com";
    }

    public static Map<String, String> getRegistrationData() {
        Map<String, String> data = new HashMap<>();
        data.put("email", DataGenerator.getRandomEmail());
        data.put("password", "123");
        data.put("username", "learnqa");
        data.put("firstName", "learnqa");
        data.put("lastName", "learnqa");

        return data;
    }

    public static Map<String, String> getRegistrationData(Map<String, String> nonDefaultValues) {
        Map<String, String> defaultValues = DataGenerator.getRegistrationData();

        Map<String, String> userData = new HashMap<>();
        String[] keys = {"email", "password", "username", "firstName", "lastName"};
        for(String key: keys) {
            if(nonDefaultValues.containsKey(key)) {
                userData.put(key, nonDefaultValues.get(key));
            } else {
                userData.put(key, defaultValues.get(key));
            }
        }
        return userData;
    }

    public static Map<String, String> getRegistrationDataWithoutOneField(String fieldkey) {
        Map<String, String> defaultValues = DataGenerator.getRegistrationData();

        Map<String, String> userData = new HashMap<>();
        String[] keys = {"email", "password", "username", "firstName", "lastName"};
        for(String key: keys) {
            if(!key.equals(fieldkey)) {
                userData.put(key, defaultValues.get(key));
            }
        }

        return userData;
    }

    public static Map<String, String> getRegistrationDataWithShortName(String fieldkey) {
        Map<String, String> shortName = new HashMap<>();
        shortName.put(fieldkey, "a");
        return getRegistrationData(shortName);

    }

    public static Map<String, String> getRegistrationDataWithLongName(String fieldKey) {
        Random random = new Random();
        String name = random
                .ints(97, 123)
                .limit(251)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        Map<String, String> longName = new HashMap<>();
        longName.put(fieldKey, name);
        return getRegistrationData(longName);
    }


}
