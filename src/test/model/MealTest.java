package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MealTest {

    private Meal meal;

    @BeforeEach
    void setUp() {
        meal = new Meal("Oatmeal", 10, 30, 5);
    }

    @Test
    void testConstructor() {
        assertEquals("Oatmeal", meal.getName());
        assertEquals(10, meal.getProtein());
        assertEquals(30, meal.getCarbs());
        assertEquals(5, meal.getFat());
    }

    @Test
    void testConstructorInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> new Meal("", 10, 10, 10));
        assertThrows(IllegalArgumentException.class, () -> new Meal(null, 10, 10, 10));
    }

    @Test
    void testConstructorNegativeMacros() {
        assertThrows(IllegalArgumentException.class, () -> new Meal("Test Meal", -1, 10, 10));
        assertThrows(IllegalArgumentException.class, () -> new Meal("Test Meal", 10, -5, 10));
        assertThrows(IllegalArgumentException.class, () -> new Meal("Test Meal", 10, 10, -3));
    }
}
