package it.unicam.cs.mpgc.rpg129852.util;

import it.unicam.cs.mpgc.rpg129852.persistence.AvailableSavesProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class SaveNameResolverImplTest {

    private StubAvailableSavesProvider savesProvider;
    private FakeSyntaxValidator syntaxValidator;
    private StubFallbackProvider fallbackProvider;
    private SaveNameResolverImpl resolver;

    private String resolvedName;
    private Exception thrownException;

    @BeforeEach
    void setUp() {
        savesProvider = new StubAvailableSavesProvider();
        syntaxValidator = new FakeSyntaxValidator();
        fallbackProvider = new StubFallbackProvider();
        resolver = new SaveNameResolverImpl(savesProvider, syntaxValidator, fallbackProvider);

        resolvedName = null;
        thrownException = null;
    }

    @Test
    void validProposedName_whenNotTaken_returnsProposedName() {
        givenTakenNames("Save1", "Save2");

        whenResolvingName("MyNewSave", false);

        thenResolvedNameIs("MyNewSave");
        thenNoExceptionIsThrown();
    }

    @Test
    void emptyProposedName_usesFallbackProvider() {
        givenTakenNames("Save1");
        givenFallbackWillReturn("untitled");

        whenResolvingName("   ", false);

        thenResolvedNameIs("untitled");
        thenNoExceptionIsThrown();
    }

    @Test
    void nullProposedName_usesFallbackProvider() {
        givenFallbackWillReturn("untitled");

        whenResolvingName(null, false);

        thenResolvedNameIs("untitled");
        thenNoExceptionIsThrown();
    }

    @Test
    void takenProposedName_withoutForceOverwrite_throwsIllegalStateException() {
        givenTakenNames("ExistingSave");

        whenResolvingName("ExistingSave", false);

        thenExceptionIsThrown(IllegalStateException.class);
    }

    @Test
    void takenProposedName_withForceOverwrite_returnsProposedName() {
        givenTakenNames("ExistingSave");

        whenResolvingName("ExistingSave", true);

        thenResolvedNameIs("ExistingSave");
        thenNoExceptionIsThrown();
    }

    @Test
    void invalidSyntaxName_propagatesIllegalArgumentException() {
        givenSyntaxValidatorWillThrow();

        whenResolvingName("Invalid*Name", false);

        thenExceptionIsThrown(IllegalArgumentException.class);
    }

    private void givenTakenNames(String... names) {
        savesProvider.availableSaves = Arrays.asList(names);
    }

    private void givenFallbackWillReturn(String fallbackName) {
        fallbackProvider.fallbackName = fallbackName;
    }

    private void givenSyntaxValidatorWillThrow() {
        syntaxValidator.shouldThrow = true;
    }

    private void whenResolvingName(String proposedName, boolean forceOverwrite) {
        try {
            resolvedName = resolver.resolveFinalName(proposedName, forceOverwrite);
        } catch (Exception e) {
            thrownException = e;
        }
    }

    private void thenResolvedNameIs(String expectedName) {
        assertNotNull(resolvedName, "Ci si aspettava un nome risolto, ma e' stata lanciata un'eccezione o il nome e' null.");
        assertEquals(expectedName, resolvedName);
    }

    private void thenNoExceptionIsThrown() {
        assertNull(thrownException, "Non ci si aspettava alcuna eccezione, ma e' stata lanciata: " + thrownException);
    }

    private void thenExceptionIsThrown(Class<? extends Exception> expectedExceptionClass) {
        assertNotNull(thrownException, "Ci si aspettava un'eccezione, ma non e' stata lanciata.");
        assertInstanceOf(expectedExceptionClass, thrownException,
                "L'eccezione lanciata non e' del tipo atteso.");
    }

    class StubAvailableSavesProvider implements AvailableSavesProvider {
        public List<String> availableSaves = List.of();

        @Override
        public List<String> getAvailableSaves() {
            return availableSaves;
        }
    }

    class FakeSyntaxValidator implements SyntaxValidator {
        public boolean shouldThrow = false;

        @Override
        public void validate(String name) {
            if (shouldThrow) {
                throw new IllegalArgumentException("Errore di sintassi simulato.");
            }
        }
    }

    class StubFallbackProvider implements SaveNameFallbackProvider {
        public String fallbackName = "untitled";

        @Override
        public String provideFirstAvailable(List<String> takenNames) {
            return fallbackName;
        }
    }
}