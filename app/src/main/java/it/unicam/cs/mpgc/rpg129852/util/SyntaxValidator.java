package it.unicam.cs.mpgc.rpg129852.util;

/**
 * Defines a contract for validating the syntax of a given string.
 *
 * This is typically used to ensure that user inputs, such as file names
 * or profile names, conform to specific rules, length constraints,
 * or file system restrictions.
 */
public interface SyntaxValidator {

    /**
     * Validates the provided string against predefined syntax rules.
     *
     * @param str the string to validate, which can be null or empty.
     * @throws IllegalArgumentException if the string violates the required syntax rules,
     *                                  contains forbidden characters, or exceeds length limits.
     */
    public void validate(String str);
}