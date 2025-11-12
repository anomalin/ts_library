package se.yrgo.libraryapp.validators;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

public class UsernameTest {
    @ParameterizedTest
    @ValueSource(strings={"bosse", "bossE--", "boss", "bo@0rm", "bo1234", "89839boobobobobobobobobobobobobobo"})
    void validUsername(String nameString) {
        boolean result = Username.validate(nameString);
        assertThat(result).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings={"name with space", "   ", "??jfk", "bos", "båsse"})
    @NullAndEmptySource
    void invalidUsername(String nameString) {
        boolean result = Username.validate(nameString);
        assertThat(result).isFalse();
    }




}
