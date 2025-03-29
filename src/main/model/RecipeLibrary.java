package model;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Manages a collection of recipes.
 */
public class RecipeLibrary {
    private List<Recipe> recipes;

    /**
     * Constructs an empty RecipeLibrary.
     */
    public RecipeLibrary() {
        recipes = new ArrayList<>();
    }

    /**
     * Adds a recipe to the library. Returns false if recipe is null.
     *
     * @param recipe the recipe to add
     * @return true if successfully added, false otherwise
     */
    public boolean addRecipe(Recipe recipe) {
        if (recipe == null) {
            return false;
        }
        recipes.add(recipe);
        return true;
    }

    /**
     * Returns a list of all recipes in the library.
     */
    public List<Recipe> getAllRecipes() {
        return new ArrayList<>(recipes);
    }

    /**
     * Searches for a recipe by its name (case-insensitive). Returns empty if
     * name is null or empty.
     */
    public Optional<Recipe> getRecipeByName(String name) {
        if (name == null || name.isEmpty()) {
            return Optional.empty();
        }
        return recipes.stream()
                      .filter(r -> r.getName().equalsIgnoreCase(name))
                      .findFirst();
    }

    /**
     * Filters recipes by a specified ingredient. Returns an empty list if
     * ingredient is null or empty.
     */
    public List<Recipe> filterRecipesByIngredient(String ingredient) {
        if (ingredient == null || ingredient.isEmpty()) {
            return new ArrayList<>();
        }
        return recipes.stream()
                      .filter(r -> r.getIngredients().contains(ingredient))
                      .collect(Collectors.toList());
    }

    /**
     * Deletes a recipe from the library by its name (case-insensitive).
     * Returns false if the name is null/empty or if no recipe is found.
     */
    public boolean deleteRecipe(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }
        Optional<Recipe> toDelete = getRecipeByName(name);
        if (toDelete.isPresent()) {
            recipes.remove(toDelete.get());
            return true;
        }
        return false;
    }

    /**
     * Edits an existing recipe. Returns false if:
     *  - name is null/empty
     *  - the recipe does not exist in the library
     *  - creating the new recipe fails (e.g., null ingredients)
     *
     * We use a try/catch around the Recipe constructor so that if the constructor
     * throws IllegalArgumentException (e.g., for null ingredients or instructions),
     * we simply return false rather than propagate an exception to the test.
     */
    public boolean editRecipe(String name, List<String> newIngredients, String newInstructions) {
        if (name == null || name.isEmpty()) {
            return false;
        }
        Optional<Recipe> oldRecipe = getRecipeByName(name);
        if (!oldRecipe.isPresent()) {
            return false;
        }
        recipes.remove(oldRecipe.get());
        try {
            Recipe updated = new Recipe(name, newIngredients, newInstructions);
            recipes.add(updated);
            return true;
        } catch (IllegalArgumentException e) {
            recipes.add(oldRecipe.get());
            return false;
        }
    }

    /**
     * Returns this RecipeLibrary's recipes as a JSON array.
     * Each Recipe must define a toJson() method.
     */
    public JSONArray toJson() {
        JSONArray array = new JSONArray();
        for (Recipe r : recipes) {
            array.put(r.toJson());
            // Make sure Recipe.java has public JSONObject toJson()
        }
        return array;
    }
}
