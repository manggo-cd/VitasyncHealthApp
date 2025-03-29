package model;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;

/**
 * Represents a recipe with a name, list of ingredients, and preparation instructions.
 */
public class Recipe {
    private String name;
    private List<String> ingredients;
    private String instructions;

    /**
     * Constructs a Recipe with the specified parameters.
     * Requires: name is non-null/non-empty, ingredients and instructions are non-null.
     * @param name the recipe name.
     * @param ingredients the list of ingredients.
     * @param instructions the preparation instructions.
     */
    public Recipe(String name, List<String> ingredients, String instructions) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Recipe name cannot be null or empty.");
        }
        if (ingredients == null) {
            throw new IllegalArgumentException("Ingredients cannot be null.");
        }
        if (instructions == null) {
            throw new IllegalArgumentException("Instructions cannot be null.");
        }
        this.name = name;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    /**
     * Returns the name of the recipe.
     * @return the recipe name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the list of ingredients.
     * @return a list of ingredients.
     */
    public List<String> getIngredients() {
        return ingredients;
    }

    /**
     * Returns the preparation instructions.
     * @return the instructions.
     */
    public String getInstructions() {
        return instructions;
    }

    /**
     * Returns this Recipe as a JSON object.
     * Effects: Converts recipe data into a JSONObject.
     */
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("instructions", instructions);
        JSONArray ingArray = new JSONArray();
        for (String ing : ingredients) {
            ingArray.put(ing);
        }
        json.put("ingredients", ingArray);
        return json;
    }
}
