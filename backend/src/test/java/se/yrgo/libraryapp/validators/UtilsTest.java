package se.yrgo.libraryapp.validators;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.assertj.core.api.Assertions.assertThat;

public class UtilsTest {
    @ParameterizedTest
    @CsvSource({"p4ssword, password",
                "L33t__, leet",
                "m4lin, malin",
                "800t, boot",
                "5ommar 7id, sommar tid"})
    void cleanFromLeetCharacterChange(String actual, String expected) {
        assertThat(Utils.cleanAndUnLeet(actual)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({"b0sse!, bsse",
                "STA@p, stap",
                "Hej!! Hopp, hej hopp"})
    void cleanRemoveCharacters(String actual, String expected) {
        assertThat(Utils.onlyLettersAndWhitespace(actual)).isEqualTo(expected);
    }

    @Test
    void validStringReturnsUnchanged() {
        assertThat(Utils.cleanAndUnLeet("password")).isEqualTo("password");
    }
}
