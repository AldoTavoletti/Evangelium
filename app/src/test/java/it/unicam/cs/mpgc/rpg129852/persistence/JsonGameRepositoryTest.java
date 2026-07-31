package it.unicam.cs.mpgc.rpg129852.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unicam.cs.mpgc.rpg129852.model.DiscipleData;
import it.unicam.cs.mpgc.rpg129852.model.Game;
import it.unicam.cs.mpgc.rpg129852.model.GameState;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonGameRepositoryTest {

    @TempDir
    Path tempDir;

    static Gson gson;

    @BeforeAll
    static void setUpGson(){
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    private JsonGameRepository repository;
    private Game loadedGame;
    private List<String> availableSaves;

    @BeforeEach
    void setUp() {
        repository = new JsonGameRepository(tempDir, gson);
        loadedGame = null;
        availableSaves = null;
    }

    @Test
    void save_createsJsonFileWithCorrectName() {
        Game gameToSave = createDummyGame("HeroSave");

        whenSaving(gameToSave);

        thenFileExists("HeroSave.json");
    }

    @Test
    void load_existingSave_returnsCorrectGame() {
        Game gameToSave = createDummyGame("HeroSave");
        givenAnExistingSaveFile(gameToSave);

        whenLoading("HeroSave");

        thenGameIsLoadedSuccessfully("HeroSave");
    }

    @Test
    void load_missingSave_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> whenLoading("MissingSave"));
    }

    @Test
    void delete_existingSave_removesFile() {
        Game gameToSave = createDummyGame("HeroSave");
        givenAnExistingSaveFile(gameToSave);

        whenDeleting("HeroSave");

        thenFileDoesNotExist("HeroSave.json");
    }

    @Test
    void delete_missingSave_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> whenDeleting("MissingSave"));
    }

    @Test
    void getAvailableSaves_returnsOnlyJsonFileNamesWithoutExtension() throws IOException {
        givenAnExistingSaveFile(createDummyGame("Save1"));
        givenAnExistingSaveFile(createDummyGame("Save2"));
        givenAnUnrelatedFile("image.png");
        givenAnUnrelatedFile("config.txt");

        whenGettingAvailableSaves();

        thenAvailableSavesAre("Save1", "Save2");
    }

    private Game createDummyGame(String saveName) {
        DiscipleData data = new DiscipleData("Arthur", "Knight", "Blue");
        GameState state = new GameState(data);
        return new Game(saveName, state);
    }

    private void givenAnExistingSaveFile(Game game) {
        repository.save(game);
    }

    private void givenAnUnrelatedFile(String fileName) throws IOException {
        Files.createFile(tempDir.resolve(fileName));
    }

    private void whenSaving(Game game) {
        repository.save(game);
    }

    private void whenLoading(String saveName) {
        loadedGame = repository.load(saveName);
    }

    private void whenDeleting(String saveName) {
        repository.delete(saveName);
    }

    private void whenGettingAvailableSaves() {
        availableSaves = repository.getAvailableSaves();
    }

    private void thenFileExists(String fileName) {
        Path filePath = tempDir.resolve(fileName);
        assertTrue(Files.exists(filePath), "Il file non e' stato creato sul disco.");
    }

    private void thenFileDoesNotExist(String fileName) {
        Path filePath = tempDir.resolve(fileName);
        assertFalse(Files.exists(filePath), "Il file non e' stato eliminato dal disco.");
    }

    private void thenGameIsLoadedSuccessfully(String expectedSaveName) {
        assertNotNull(loadedGame, "Il gioco caricato non dovrebbe essere null.");
        assertEquals(expectedSaveName, loadedGame.getSaveName());
    }

    private void thenAvailableSavesAre(String... expectedNames) {
        assertNotNull(availableSaves);
        assertEquals(expectedNames.length, availableSaves.size(), "Il numero di salvataggi trovati non e' corretto.");
        for (String expectedName : expectedNames) {
            assertTrue(availableSaves.contains(expectedName), "La lista non contiene il salvataggio: " + expectedName);
        }
    }
}