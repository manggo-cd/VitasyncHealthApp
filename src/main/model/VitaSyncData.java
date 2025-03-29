package model;

import org.json.JSONObject;
import persistence.Writable;

/**
 * Represents the entire VitaSync state with references to
 * workoutTracker, mealPlan, and recipeLibrary.
 */
public class VitaSyncData implements Writable {
    private String name;
    private WorkoutTracker workoutTracker;
    private MealPlan mealPlan;
    private RecipeLibrary recipeLibrary;

    public VitaSyncData(String name) {
        this.name = name;
        this.workoutTracker = new WorkoutTracker();
        this.mealPlan = new MealPlan();
        this.recipeLibrary = new RecipeLibrary();
    }

    public String getName() {
        return name;
    }

    public WorkoutTracker getWorkoutTracker() {
        return workoutTracker;
    }

    public MealPlan getMealPlan() {
        return mealPlan;
    }

    public RecipeLibrary getRecipeLibrary() {
        return recipeLibrary;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        // These methods must exist in your sub-classes and return a JSONArray
        json.put("workouts", workoutTracker.toJson());
        json.put("meals", mealPlan.toJson());
        json.put("recipes", recipeLibrary.toJson());
        return json;
    }
}
