package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

class RecipeTest {

    private Recipe recipe;

    @BeforeEach
    void setUp() {
        recipe = new Recipe(
            "Alfredo Pasta",
            Arrays.asList("pasta", "cream", "parmesan cheese"),
            "Boil pasta. Make sauce with cream and cheese. Combine."
        );
    }

    @Test
    void testConstructor() {
        assertEquals("Alfredo Pasta", recipe.getName());
        assertEquals(3, recipe.getIngredients().size());
        assertTrue(recipe.getIngredients().contains("pasta"));
        assertEquals("Boil pasta. Make sauce with cream and cheese. Combine.", recipe.getInstructions());
    }

    @Test
    void testConstructorInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> new Recipe("", Arrays.asList("pasta"), "Do stuff"));
        assertThrows(IllegalArgumentException.class, () -> new Recipe(null, Arrays.asList("pasta"), "Do stuff"));
    }

    @Test
    void testConstructorNullIngredientsOrInstructions() {
        assertThrows(IllegalArgumentException.class, () -> new Recipe("Pasta", null, "Boil water"));
        assertThrows(IllegalArgumentException.class, () -> new Recipe("Pasta", Arrays.asList("pasta"), null));
    }
}
