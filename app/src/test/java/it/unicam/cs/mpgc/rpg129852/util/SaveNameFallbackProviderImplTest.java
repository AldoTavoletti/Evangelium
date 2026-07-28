package it.unicam.cs.mpgc.rpg129852.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SaveNameFallbackProviderImplTest {

    private SaveNameFallbackProviderImpl provider;

    @BeforeEach
    void setUp() {
        provider = new SaveNameFallbackProviderImpl();
    }

    @Test
    void provideFirstAvailable_whenListIsEmpty_returnsBaseName() {
        List<String> takenNames = Collections.emptyList();

        String result = provider.provideFirstAvailable(takenNames);

        assertEquals("untitled", result);
    }

    @Test
    void provideFirstAvailable_whenUnrelatedNamesAreTaken_returnsBaseName() {
        List<String> takenNames = List.of("Save1", "MyAdventure", "untitled(1)");

        String result = provider.provideFirstAvailable(takenNames);

        assertEquals("untitled", result);
    }

    @Test
    void provideFirstAvailable_whenBaseNameIsTaken_returnsCounterOne() {
        List<String> takenNames = List.of("untitled");

        String result = provider.provideFirstAvailable(takenNames);

        assertEquals("untitled(1)", result);
    }

    @Test
    void provideFirstAvailable_whenMultipleCountersAreTaken_returnsNextAvailable() {
        List<String> takenNames = List.of("untitled", "untitled(1)", "untitled(2)");

        String result = provider.provideFirstAvailable(takenNames);

        assertEquals("untitled(3)", result);
    }

    @Test
    void provideFirstAvailable_whenThereIsAGapInCounters_fillsTheGap() {
        List<String> takenNames = List.of("untitled", "untitled(2)", "untitled(3)");

        String result = provider.provideFirstAvailable(takenNames);

        assertEquals("untitled(1)", result);
    }
}