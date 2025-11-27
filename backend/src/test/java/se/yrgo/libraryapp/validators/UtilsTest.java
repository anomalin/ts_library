package se.yrgo.libraryapp.validators;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;

import static org.assertj.core.api.Assertions.assertThat;

public class UtilsTest {
    @ParameterizedTest
    @CsvSource({ "p4ssword, password",
            "L33t__, leet",
            "m4lin, malin",
            "800t, boot",
            "5ommar 7id, sommar tid" })
    void cleanFromLeetCharacterChange(String actual, String expected) {
        assertThat(Utils.cleanAndUnLeet(actual)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({ "b0sse!, bsse",
            "STA@p, stap",
            "Hej!! Hopp, hej hopp" })
    void cleanRemoveCharacters(String actual, String expected) {
        assertThat(Utils.onlyLettersAndWhitespace(actual)).isEqualTo(expected);
    }

    @Test
    void validStringReturnsUnchanged() {
        assertThat(Utils.cleanAndUnLeet("password")).isEqualTo("password");
    }

    @Property
    void shouldRemoveNonAlphabetic(@ForAll String text) {
        String test = Utils.onlyLettersAndWhitespace(text);
        char[] letters = test.toCharArray();
        for (char letter : letters) {
            assertThat(Character.isLowerCase(letter) || Character.isWhitespace(letter))
                    .as("Illegal char: '%s' from input '%s'", letter, text)
                    .isTrue();
        }
    }

    @Property
    void leetCleanSwitchesCharacters(@ForAll("leetStrings") String text) {
        String cleaned = Utils.cleanAndUnLeet(text);
        for (char c : cleaned.toCharArray()) {
            assertThat(Character.isLowerCase(c) || Character.isWhitespace(c))
                    .as("Illegal char: '%s' from input '%s'", c, text)
                    .isTrue();
        }
    }

    @Provide
    Arbitrary<String> leetStrings() {
        return Arbitraries.strings()
                .withChars(
                        "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789\t\n");
    }
}
