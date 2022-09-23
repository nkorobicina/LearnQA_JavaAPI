import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by user nkorobicina on 23.09.2022.
 */
public class HomeWork10 {

    @ParameterizedTest
    @ValueSource(strings = {"15symbols------", "morethan15symbols", "less15"})
    public void testNegativeAuthUser(String inputValue){

        assertTrue(inputValue.length() > 15, "Value less than 15 symbols or equals to 15 symbols: " + inputValue);

    }
}
