package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ExerciseTest {

    private Exercise exercise;
    private ExerciseSet set1;
    private ExerciseSet set2;

    @BeforeEach
    void setUp() {
        exercise = new Exercise("Push-Ups");
        set1 = new ExerciseSet(10);
        set2 = new ExerciseSet(8);
    }

    @Test
    void testConstructor() {
        assertEquals("Push-Ups", exercise.getName());
        assertTrue(exercise.getSets().isEmpty());
    }

    @Test
    void testConstructorInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> new Exercise(""));
        assertThrows(IllegalArgumentException.class, () -> new Exercise(null));
    }

    @Test
    void testAddSet() {
        exercise.addSet(set1);
        assertEquals(1, exercise.getSets().size());
        assertEquals(10, exercise.getSets().get(0).getTargetReps());
        exercise.addSet(set2);
        assertEquals(2, exercise.getSets().size());
    }

    @Test
    void testAddSetNull() {
        assertThrows(IllegalArgumentException.class, () -> exercise.addSet(null));
    }
}
