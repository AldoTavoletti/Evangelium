package it.unicam.cs.mpgc.rpg129852.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SaveNameSyntaxValidatorTest {

    private SaveNameSyntaxValidator validator;

    @BeforeEach
    void setUp() {
        validator = new SaveNameSyntaxValidator();
    }

    @ParameterizedTest
    @ValueSource(strings = {"ValidSave", "Save_01", "My Save Game", "12345", "Save.dat"})
    void validate_validName_doesNotThrow(String validName) {
        assertDoesNotThrow(() -> validator.validate(validName));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    void validate_nullOrBlankName_throwsException(String blankName) {
        assertThrows(IllegalArgumentException.class, () -> validator.validate(blankName));
    }

    @Test
    void validate_nameExceedingMaxLength_throwsException() {
        String longName = "A".repeat(31);

        assertThrows(IllegalArgumentException.class, () -> validator.validate(longName));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Save\\1", "Save/1", "Save:1", "Save*1",
            "Save?1", "Save\"1", "Save<1", "Save>1", "Save|1"
    })
    void validate_nameWithIllegalCharacters_throwsException(String invalidName) {
        assertThrows(IllegalArgumentException.class, () -> validator.validate(invalidName));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "CON", "con", "PRN", "AUX", "NUL",
            "COM1", "COM9", "com5", "LPT1", "lpt9"
    })
    void validate_reservedWindowsNames_throwsException(String reservedName) {
        assertThrows(IllegalArgumentException.class, () -> validator.validate(reservedName));
    }
}