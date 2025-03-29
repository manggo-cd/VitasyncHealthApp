package persistence;

import model.*;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.time.LocalDate;

// CITATION: CPSC210 JSONSERIALIZATION REPOSITORY for the help

public class JsonReader {
    private String source;

    public JsonReader(String source) {
        this.source = source;
    }

    public VitaSyncData read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseVitaSyncData(jsonObject);
    }

    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }
        return contentBuilder.toString();
    }

    private VitaSyncData parseVitaSyncData(JSONObject jsonObject) {
        // parse "name" field
        String name = jsonObject.getString("name");
        VitaSyncData vsData = new VitaSyncData(name);

        // parse "workouts" array
        JSONArray workoutsArray = jsonObject.getJSONArray("workouts");
        parseWorkouts(workoutsArray, vsData.getWorkoutTracker());

        // parse "meals" array
        JSONArray mealsArray = jsonObject.getJSONArray("meals");
        parseMeals(mealsArray, vsData.getMealPlan());

        // parse "recipes" array
        JSONArray recipesArray = jsonObject.getJSONArray("recipes");
        parseRecipes(recipesArray, vsData.getRecipeLibrary());

        return vsData;
    }

    private void parseWorkouts(JSONArray workoutsArray, WorkoutTracker tracker) {
        for (Object obj : workoutsArray) {
            JSONObject workoutsJson = (JSONObject) obj;
            LocalDate date = LocalDate.parse(workoutsJson.getString("date"));
            Workout w = new Workout(date);

            JSONArray exercisesArray = workoutsJson.getJSONArray("exercises");
            for (Object exerciseObj : exercisesArray) {
                JSONObject exJson = (JSONObject) exerciseObj;
                Exercise ex = new Exercise(exJson.getString("name"));

                JSONArray setsArray = exJson.getJSONArray("sets");
                for (Object setsObj : setsArray) {
                    JSONObject setJson = (JSONObject) setsObj;
                    ExerciseSet set = new ExerciseSet(setJson.getInt("targetReps"));
                    // If "completedReps" is stored, apply it:
                    int completed = setJson.getInt("completedReps");
                    for (int i = 0; i < completed; i++) {
                        set.checkOffRep();
                    }
                    ex.addSet(set);
                }
                w.addExercise(ex);
            }

            tracker.addWorkout(w);
        }
    }

    private void parseMeals(JSONArray mealsArray, MealPlan mealPlan) {
        for (Object obj : mealsArray) {
            JSONObject mealsJson = (JSONObject) obj;
            String name = mealsJson.getString("name");
            int protein = mealsJson.getInt("protein");
            int carbs = mealsJson.getInt("carbs");
            int fat = mealsJson.getInt("fat");
            Meal meal = new Meal(name, protein, carbs, fat);
            mealPlan.addMeal(meal);
        }
    }

    private void parseRecipes(JSONArray recipesArray, RecipeLibrary library) {
        for (Object obj : recipesArray) {
            JSONObject recipesJson = (JSONObject) obj;
            String name = recipesJson.getString("name");
            String instructions = recipesJson.getString("instructions");
            JSONArray ingArray = recipesJson.getJSONArray("ingredients");
            List<String> ingredients = new ArrayList<>();
            for (Object ing : ingArray) {
                ingredients.add((String) ing);
            }
            Recipe recipe = new Recipe(name, ingredients, instructions);
            library.addRecipe(recipe);
        }
    }
}
