package persistence;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest {

    private VitaSyncData vsData;

    @BeforeEach
    void setUp() {
        vsData = new VitaSyncData("My VitaSync Data");
    }

    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/\0illegalFileName.json");
            writer.open();
            fail("FileNotFoundException expected");
        } catch (FileNotFoundException e) {
            // expected
        }
    }

    @Test
    void testWriterEmptyData() {
        JsonWriter writer = new JsonWriter("./data/testWriterEmpty.json");
        try {
            writer.open();
            writer.write(vsData);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmpty.json");
            VitaSyncData readData = reader.read();
            assertEquals("My VitaSync Data", readData.getName());
            assertTrue(readData.getWorkoutTracker().getWorkouts().isEmpty());
            assertTrue(readData.getMealPlan().getMeals().isEmpty());
            assertTrue(readData.getRecipeLibrary().getAllRecipes().isEmpty());
        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }

    @Test
    void testWriterTypicalData() {
        populateVsDataWithTypicalData();
        writeAndVerify("./data/testWriterTypical.json");
    }

    // HELPER METHODS:

    // Break out the creation of a "typical" VitaSyncData into a separate method
    private void populateVsDataWithTypicalData() {
        // 1) Create a workout
        Workout workout = new Workout(LocalDate.of(2025, 1, 1));
        Exercise exercise = new Exercise("Push Ups");
        ExerciseSet set = new ExerciseSet(10);
        for (int i = 0; i < 10; i++) {
            set.checkOffRep();
        }
        exercise.addSet(set);
        workout.addExercise(exercise);
        vsData.getWorkoutTracker().addWorkout(workout);

        // 2) Create a meal
        Meal meal = new Meal("Oatmeal", 10, 30, 5);
        vsData.getMealPlan().addMeal(meal);

        // 3) Create a recipe
        Recipe recipe = new Recipe("Alfredo Pasta",
                Arrays.asList("pasta", "cream", "cheese"),
                "Boil pasta. Make sauce. Combine.");
        vsData.getRecipeLibrary().addRecipe(recipe);
    }

    // Break out the writing/reading and assertions into another method
    private void writeAndVerify(String filePath) {
        JsonWriter writer = new JsonWriter(filePath);
        try {
            writer.open();
            writer.write(vsData);
            writer.close();

            JsonReader reader = new JsonReader(filePath);
            VitaSyncData readData = reader.read();
            assertEquals("My VitaSync Data", readData.getName());
            assertEquals(1, readData.getWorkoutTracker().getWorkouts().size());
            assertEquals("2025-01-01",
                    readData.getWorkoutTracker().getWorkouts().get(0).getDate().toString());
            assertEquals(1, readData.getMealPlan().getMeals().size());
            assertEquals("Oatmeal", readData.getMealPlan().getMeals().get(0).getName());
            assertEquals(1, readData.getRecipeLibrary().getAllRecipes().size());
            assertEquals("Alfredo Pasta",
                    readData.getRecipeLibrary().getAllRecipes().get(0).getName());
        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }
}
