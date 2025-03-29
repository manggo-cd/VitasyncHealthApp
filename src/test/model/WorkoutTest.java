package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class WorkoutTest {

    private Workout workout;
    private Exercise exercise;

    @BeforeEach
    void setUp() {
        workout = new Workout(LocalDate.of(2025, 1, 1));
        exercise = new Exercise("Push-Ups");
    }

    @Test
    void testConstructor() {
        assertEquals(LocalDate.of(2025, 1, 1), workout.getDate());
        assertTrue(workout.getExercises().isEmpty());
    }

    @Test
    void testConstructorNullDate() {
        assertThrows(IllegalArgumentException.class, () -> new Workout(null));
    }

    @Test
    void testAddExercise() {
        workout.addExercise(exercise);
        assertEquals(1, workout.getExercises().size());
        assertEquals("Push-Ups", workout.getExercises().get(0).getName());
    }

    @Test
    void testAddExerciseNull() {
        assertThrows(IllegalArgumentException.class, () -> workout.addExercise(null));
    }
}
