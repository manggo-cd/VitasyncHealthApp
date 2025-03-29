package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RecipeLibraryTest {

    private RecipeLibrary recipeLibrary;
    private Recipe alfredoPasta;
    private Recipe chickenSalad;

    @BeforeEach
    void setUp() {
        recipeLibrary = new RecipeLibrary();
        alfredoPasta = new Recipe(
            "Alfredo Pasta",
            Arrays.asList("pasta", "cream", "parmesan cheese"),
            "Boil pasta. Make sauce with cream and cheese. Combine."
        );
        chickenSalad = new Recipe(
            "Chicken Salad",
            Arrays.asList("chicken", "lettuce", "tomato", "dressing"),
            "Cook chicken. Mix with veggies and dressing."
        );
    }

    @Test
    void testAddRecipe() {
        assertTrue(recipeLibrary.getAllRecipes().isEmpty());
        boolean added = recipeLibrary.addRecipe(alfredoPasta);
        assertTrue(added);
        assertEquals(1, recipeLibrary.getAllRecipes().size());
    }

    @Test
    void testAddRecipeNull() {
        boolean added = recipeLibrary.addRecipe(null);
        assertFalse(added);
        assertTrue(recipeLibrary.getAllRecipes().isEmpty());
    }

    @Test
    void testGetRecipeByName() {
        recipeLibrary.addRecipe(alfredoPasta);
        recipeLibrary.addRecipe(chickenSalad);

        Optional<Recipe> found = recipeLibrary.getRecipeByName("Alfredo Pasta");
        assertTrue(found.isPresent());
        assertEquals("Alfredo Pasta", found.get().getName());

        Optional<Recipe> notFound = recipeLibrary.getRecipeByName("Not Real");
        assertFalse(notFound.isPresent());
    }

    @Test
    void testGetRecipeByNameNullEmpty() {
        // We expect an empty Optional if the name is invalid
        assertFalse(recipeLibrary.getRecipeByName(null).isPresent());
        assertFalse(recipeLibrary.getRecipeByName("").isPresent());
    }

    @Test
    void testFilterRecipesByIngredient() {
        recipeLibrary.addRecipe(alfredoPasta);
        recipeLibrary.addRecipe(chickenSalad);

        List<Recipe> filtered = recipeLibrary.filterRecipesByIngredient("pasta");
        assertEquals(1, filtered.size());
        assertEquals("Alfredo Pasta", filtered.get(0).getName());
    }

    @Test
    void testFilterRecipesByIngredientNullEmpty() {
        // We expect an empty list if ingredient is invalid
        recipeLibrary.addRecipe(alfredoPasta);
        assertTrue(recipeLibrary.filterRecipesByIngredient(null).isEmpty());
        assertTrue(recipeLibrary.filterRecipesByIngredient("").isEmpty());
    }

    @Test
    void testDeleteRecipe() {
        recipeLibrary.addRecipe(alfredoPasta);
        recipeLibrary.addRecipe(chickenSalad);

        boolean deleted = recipeLibrary.deleteRecipe("Alfredo Pasta");
        assertTrue(deleted);
        assertEquals(1, recipeLibrary.getAllRecipes().size());

        boolean notDeleted = recipeLibrary.deleteRecipe("Not Real");
        assertFalse(notDeleted);
        assertEquals(1, recipeLibrary.getAllRecipes().size());
    }

    @Test
    void testDeleteRecipeNullEmpty() {
        // Should return false
        recipeLibrary.addRecipe(alfredoPasta);
        assertFalse(recipeLibrary.deleteRecipe(null));
        assertFalse(recipeLibrary.deleteRecipe(""));
        assertEquals(1, recipeLibrary.getAllRecipes().size());
    }

    @Test
    void testEditRecipeSuccess() {
        recipeLibrary.addRecipe(alfredoPasta);
        List<String> newIngredients = Arrays.asList("pasta", "cream", "garlic");
        String newInstructions = "Updated instructions here.";

        boolean edited = recipeLibrary.editRecipe("Alfredo Pasta", newIngredients, newInstructions);
        assertTrue(edited);

        // Check that the updated recipe replaced the old one
        Optional<Recipe> updated = recipeLibrary.getRecipeByName("Alfredo Pasta");
        assertTrue(updated.isPresent());
        assertEquals(3, updated.get().getIngredients().size());
        assertEquals("Updated instructions here.", updated.get().getInstructions());
    }

    @Test
    void testEditRecipeNonexistent() {
        // If we try to edit a recipe that doesn't exist, it should return false
        boolean edited = recipeLibrary.editRecipe("Nonexistent", Arrays.asList("ing"), "Steps");
        assertFalse(edited);
    }

    @Test
    void testEditRecipeNullName() {
        recipeLibrary.addRecipe(alfredoPasta);
        // If name is null or empty, we do not proceed
        boolean editedNull = recipeLibrary.editRecipe(null, Arrays.asList("ing"), "Steps");
        assertFalse(editedNull);

        boolean editedEmpty = recipeLibrary.editRecipe("", Arrays.asList("ing"), "Steps");
        assertFalse(editedEmpty);
    }

    @Test
    void testEditRecipeNullIngredientsOrInstructions() {
        recipeLibrary.addRecipe(alfredoPasta);

        // This will cause the constructor to throw inside editRecipe, which is caught
        // The method should return false
        boolean editedNullIngredients = recipeLibrary.editRecipe("Alfredo Pasta", null, "Steps");
        assertFalse(editedNullIngredients);

        boolean editedNullInstructions = recipeLibrary.editRecipe("Alfredo Pasta", Arrays.asList("ing"), null);
        assertFalse(editedNullInstructions);

        // Original recipe should still be there
        Optional<Recipe> stillOld = recipeLibrary.getRecipeByName("Alfredo Pasta");
        assertTrue(stillOld.isPresent());
        assertTrue(stillOld.get().getIngredients().contains("cream"));
    }
}
