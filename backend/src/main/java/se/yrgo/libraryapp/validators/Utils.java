package se.yrgo.libraryapp.validators;

import java.util.Map;

/**
 * Utility functions for validation classes.
 * 
 */
class Utils {
    private Utils() {}

    /**
     * Remove any letters outside of the Swedish Latin alphabet from a string, but keep any whitespace.
     * Will return the string as all lowercase.
     * 
     * @param str the string to filter
     * @return a string containing only Swedish Latin letters and whitespace
     */
    static String onlyLettersAndWhitespace(String str) {
        return str.toLowerCase().chars().filter(cp -> (cp >= 'a' && cp <= 'z') || 
        cp == 'å' || cp == 'ä' || cp == 'ö'|| 
        Character.isWhitespace(cp))
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    /**
     * Converts any "leet speak" letters into their alphabetic equivalent (i.e. 4 to a etc.) and 
     * then removes any letters that are not alphabetic (but not whitespace). Will return the string
     * as all lowecase.
     * 
     * @param str the string to clean
     * @return a string without non alphabetic characters (except whitespace)
     */
    static String cleanAndUnLeet(String str) {
        final var leetMap = Map.of('1', 'l', '3', 'e', '4', 'a', '5', 's', '6', 'g', '7', 't', '8', 'b', '0', 'o');

        final StringBuilder res = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            char newChar = leetMap.getOrDefault(ch, ch);
            res.append(newChar);
        }

        return onlyLettersAndWhitespace(res.toString());
    }
}
