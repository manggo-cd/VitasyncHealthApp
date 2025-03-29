package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ExerciseSetTest {

    private ExerciseSet exerciseSet;

    @BeforeEach
    void setUp() {
        exerciseSet = new ExerciseSet(5);
    }

    @Test
    void testConstructor() {
        assertEquals(5, exerciseSet.getTargetReps());
        assertEquals(0, exerciseSet.getCompletedReps());
        assertFalse(exerciseSet.isCompleted());
    }

    @Test
    void testConstructorInvalidReps() {
        assertThrows(IllegalArgumentException.class, () -> new ExerciseSet(0));
        assertThrows(IllegalArgumentException.class, () -> new ExerciseSet(-1));
    }

    @Test
    void testCheckOffRep() {
        exerciseSet.checkOffRep();
        assertEquals(1, exerciseSet.getCompletedReps());
        assertFalse(exerciseSet.isCompleted());

        for (int i = 0; i < 4; i++) {
            exerciseSet.checkOffRep();
        }
        assertEquals(5, exerciseSet.getCompletedReps());
        assertTrue(exerciseSet.isCompleted());

        // Extra check-off shouldn't increase count
        exerciseSet.checkOffRep();
        assertEquals(5, exerciseSet.getCompletedReps());
    }
}
