package se.yrgo.libraryapp.validators;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

public class RealNameTest {
    @Test
    void rejectsBadWordsFromFile() throws IOException {
        Set<String> badWords = new HashSet<>();

        try (InputStream is = getClass().getClassLoader().getResourceAsStream("bad_words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                badWords.add(line);
            }
        }
        for (String word : badWords) {
            assertThat(RealName.validate(word)).isFalse();
        }
    }

    @ParameterizedTest
    @ValueSource(strings={"malin sundberg",
                        "ture petersson",
                        "sture beastgren"})
    @EmptySource
    void validRealName(String realNameString) {
        assertThat(RealName.validate(realNameString)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings={"Slutpig sundberg",
                            "hec beast berg",
                            "ture SOUPMAN"})
    void invalidRealName(String badWordNameString) {
        assertThat(RealName.validate(badWordNameString)).isFalse();
    }
}
