package model;

import org.json.JSONObject;

/**
 * Represents a meal with macronutrient details.
 */
public class Meal {
    private String name;
    private int protein;
    private int carbs;
    private int fat;

    /**
     * Constructs a Meal with a name and macronutrient values.
     * Requires: name is non-null/non-empty, and protein, carbs, fat are non-negative.
     * @param name the meal name.
     * @param protein grams of protein.
     * @param carbs grams of carbohydrates.
     * @param fat grams of fat.
     */
    public Meal(String name, int protein, int carbs, int fat) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Meal name cannot be null or empty.");
        }
        if (protein < 0 || carbs < 0 || fat < 0) {
            throw new IllegalArgumentException("Macronutrients cannot be negative.");
        }
        this.name = name;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
    }

    /**
     * Returns the name of the meal.
     * @return the meal name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the amount of protein.
     * @return protein in grams.
     */
    public int getProtein() {
        return protein;
    }

    /**
     * Returns the amount of carbohydrates.
     * @return carbohydrates in grams.
     */
    public int getCarbs() {
        return carbs;
    }

    /**
     * Returns the amount of fat.
     * @return fat in grams.
     */
    public int getFat() {
        return fat;
    }

    /**
     * Returns this Meal as a JSON object.
     * Effects: Converts meal details into a JSONObject.
     */
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("protein", protein);
        json.put("carbs", carbs);
        json.put("fat", fat);
        return json;
    }
}
