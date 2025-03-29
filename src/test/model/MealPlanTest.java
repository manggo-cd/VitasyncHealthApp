package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MealPlanTest {

    private MealPlan mealPlan;
    private Meal breakfast;
    private Meal lunch;

    @BeforeEach
    void setUp() {
        mealPlan = new MealPlan();
        breakfast = new Meal("Oatmeal", 10, 30, 5);
        lunch = new Meal("Salad", 5, 10, 2);
    }

    @Test
    void testConstructor() {
        assertTrue(mealPlan.getMeals().isEmpty());
    }

    @Test
    void testAddMeal() {
        mealPlan.addMeal(breakfast);
        assertEquals(1, mealPlan.getMeals().size());
        assertEquals("Oatmeal", mealPlan.getMeals().get(0).getName());
    }

    @Test
    void testAddMealNull() {
        assertThrows(IllegalArgumentException.class, () -> mealPlan.addMeal(null));
    }

    @Test
    void testGetTotals() {
        mealPlan.addMeal(breakfast); // 10p, 30c, 5f
        mealPlan.addMeal(lunch);     // 5p, 10c, 2f
        assertEquals(15, mealPlan.getTotalProtein());
        assertEquals(40, mealPlan.getTotalCarbs());
        assertEquals(7, mealPlan.getTotalFat());
    }
}
