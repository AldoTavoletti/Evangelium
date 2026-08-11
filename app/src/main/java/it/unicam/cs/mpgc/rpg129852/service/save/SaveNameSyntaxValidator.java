package it.unicam.cs.mpgc.rpg129852.service.save;

import it.unicam.cs.mpgc.rpg129852.util.SyntaxValidator;

/**
 * Concrete implementation of {@link SyntaxValidator} for save file names.
 * It enforces OS-level constraints to ensure the name can be safely written to the file system,
 * preventing the use of forbidden characters, reserved Windows names, and excessive lengths.
 */
public class SaveNameSyntaxValidator implements SyntaxValidator {

    private static final String ILLEGAL_CHARACTERS_REGEX = ".*[\\\\/:*?\"<>|].*";
    private static final int MAX_LENGTH = 30;

    @Override
    public void validate(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidSaveNameException("The save name cannot be blank.");
        }

        if (name.length() > MAX_LENGTH) {
            throw new InvalidSaveNameException("The save name cannot have more than " + MAX_LENGTH + " chars.");
        }

        if (name.matches(ILLEGAL_CHARACTERS_REGEX)) {
            throw new InvalidSaveNameException("The save name contains invalid symbols: \\ / : * ? \" < > |");
        }

        if (isReservedWindowsName(name)) {
            throw new InvalidSaveNameException("The save name is reserved for your system.");
        }
    }

    private boolean isReservedWindowsName(String name) {
        String upperName = name.toUpperCase();
        return upperName.equals("CON") || upperName.equals("PRN") ||
                upperName.equals("AUX") || upperName.equals("NUL") ||
                upperName.matches("COM[1-9]") || upperName.matches("LPT[1-9]");
    }
}