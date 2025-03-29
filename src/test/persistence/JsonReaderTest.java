package persistence;

import model.VitaSyncData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;


import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest {

    private VitaSyncData vsData;

    @BeforeEach
    void setUp() {
        // vsData will be created by the reader
    }

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/nonExistentFile.json");
        try {
            vsData = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass; expected exception
        }
    }

    @Test
    void testReaderEmptyData() {
        // testLoadEmpty.json should contain a JSON object with an empty state.
        JsonReader reader = new JsonReader("./data/testLoadEmpty.json");
        try {
            vsData = reader.read();
            // For example, if testLoadEmpty.json contains:
            // { "name": "My VitaSync Data", "workouts": [], "meals": [], "recipes": [] }
            assertEquals("My VitaSync Data", vsData.getName());
            assertTrue(vsData.getWorkoutTracker().getWorkouts().isEmpty());
            assertTrue(vsData.getMealPlan().getMeals().isEmpty());
            assertTrue(vsData.getRecipeLibrary().getAllRecipes().isEmpty());
        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }

    @Test
    void testReaderTypicalData() {
        // testLoadTypical.json should contain a JSON object with typical data.
        JsonReader reader = new JsonReader("./data/testLoadTypical.json");
        try {
            vsData = reader.read();
            // Example expected values:
            assertEquals("My VitaSync Data", vsData.getName());
            // Expect one workout with date "2025-01-01"
            assertEquals(1, vsData.getWorkoutTracker().getWorkouts().size());
            assertEquals("2025-01-01", vsData.getWorkoutTracker().getWorkouts().get(0).getDate().toString());
            // Expect one meal named "Oatmeal" with protein 10, carbs 30, fat 5
            assertEquals(1, vsData.getMealPlan().getMeals().size());
            assertEquals("Oatmeal", vsData.getMealPlan().getMeals().get(0).getName());
            // Expect one recipe named "Alfredo Pasta"
            assertEquals(1, vsData.getRecipeLibrary().getAllRecipes().size());
            assertEquals("Alfredo Pasta", vsData.getRecipeLibrary().getAllRecipes().get(0).getName());
        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }
}

