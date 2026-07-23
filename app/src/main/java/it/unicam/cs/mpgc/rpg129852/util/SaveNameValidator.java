package it.unicam.cs.mpgc.rpg129852.util;

public class SaveNameValidator implements NameValidator {

    private static final String ILLEGAL_CHARACTERS_REGEX = ".*[\\\\/:*?\"<>|].*";
    private static final int MAX_LENGTH = 30;

    @Override
    public void validate(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Il nome del salvataggio non può essere vuoto.");
        }

        if (name.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Il nome del salvataggio non può superare i " + MAX_LENGTH + " caratteri.");
        }

        if (name.matches(ILLEGAL_CHARACTERS_REGEX)) {
            throw new IllegalArgumentException("Il nome contiene caratteri non validi per il file system: \\ / : * ? \" < > |");
        }

        if (isReservedWindowsName(name)) {
            throw new IllegalArgumentException("Il nome scelto è riservato dal sistema e non può essere utilizzato.");
        }
    }

    private boolean isReservedWindowsName(String name) {
        String upperName = name.toUpperCase();
        return upperName.equals("CON") || upperName.equals("PRN") ||
                upperName.equals("AUX") || upperName.equals("NUL") ||
                upperName.matches("COM[1-9]") || upperName.matches("LPT[1-9]");
    }
}