package se.yrgo.libraryapp.validators;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UsernameTest {
    @Test
    void correctUsername() {
        boolean result = Username.validate("bosse");
        assertThat(result).isTrue();
    }

    @Test
    void incorrectUsername() {
        boolean result = Username.validate("name with space");
        assertThat(result).isFalse();
    }


}
