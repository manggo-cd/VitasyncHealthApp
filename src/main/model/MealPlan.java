package model;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a meal plan composed of multiple meals.
 * Provides methods to add meals and calculate total macronutrients.
 */
public class MealPlan {
    private List<Meal> meals;

    /**
     * Constructs an empty MealPlan.
     * Modifies: Initializes the meals list.
     * Effects: Creates a new meal plan.
     */
    public MealPlan() {
        meals = new ArrayList<>();
    }

    /**
     * Adds a meal to the plan.
     * Requires: meal is non-null.
     * Modifies: the meals list.
     * Effects: Appends the meal.
     * @param meal the meal to add.
     */
    public void addMeal(Meal meal) {
        if (meal == null) {
            throw new IllegalArgumentException("Meal cannot be null.");
        }
        meals.add(meal);
    }

    /**
     * Returns the list of meals in the plan.
     * @return a copy of the meals list.
     */
    public List<Meal> getMeals() {
        return new ArrayList<>(meals);
    }

    /**
     * Calculates the total protein in the meal plan.
     * @return total protein.
     */
    public int getTotalProtein() {
        return meals.stream().mapToInt(Meal::getProtein).sum();
    }

    /**
     * Calculates the total carbohydrates in the meal plan.
     * @return total carbs.
     */
    public int getTotalCarbs() {
        return meals.stream().mapToInt(Meal::getCarbs).sum();
    }

    /**
     * Calculates the total fat in the meal plan.
     * @return total fat.
     */
    public int getTotalFat() {
        return meals.stream().mapToInt(Meal::getFat).sum();
    }

    /**
     * Returns this MealPlan's meals as a JSON array.
     * Each Meal must define a toJson() method.
     */
    public JSONArray toJson() {
        JSONArray array = new JSONArray();
        for (Meal m : meals) {
            array.put(m.toJson());
            // Ensure Meal.java has public JSONObject toJson()
        }
        return array;
    }
}
