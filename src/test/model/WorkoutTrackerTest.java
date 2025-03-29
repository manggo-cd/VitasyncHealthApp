package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class WorkoutTrackerTest {

    private WorkoutTracker tracker;
    private Workout workout1;
    private Workout workout2;

    @BeforeEach
    void setUp() {
        tracker = new WorkoutTracker();
        workout1 = new Workout(LocalDate.of(2025, 1, 1));
        workout2 = new Workout(LocalDate.of(2025, 1, 2));
    }

    @Test
    void testConstructor() {
        // Initially, the tracker should have no workouts.
        assertTrue(tracker.getWorkouts().isEmpty());
    }

    @Test
    void testAddWorkout() {
        // Add the first workout and verify it's added correctly.
        tracker.addWorkout(workout1);
        List<Workout> workouts = tracker.getWorkouts();
        assertEquals(1, workouts.size());
        assertEquals(LocalDate.of(2025, 1, 1), workouts.get(0).getDate());

        // Add a second workout and verify the list size and order.
        tracker.addWorkout(workout2);
        workouts = tracker.getWorkouts();
        assertEquals(2, workouts.size());
        assertEquals(LocalDate.of(2025, 1, 2), workouts.get(1).getDate());
    }

    @Test
    void testAddWorkoutNull() {
        // Attempting to add a null workout should throw an IllegalArgumentException.
        assertThrows(IllegalArgumentException.class, () -> tracker.addWorkout(null));
    }

    @Test
    void testGetWorkoutsReturnsCopy() {
        // Ensure getWorkouts returns a copy so that modifications do not affect the internal list.
        tracker.addWorkout(workout1);
        List<Workout> workouts = tracker.getWorkouts();
        workouts.clear(); // Clearing the returned list
        // The tracker's internal list should remain unchanged.
        assertEquals(1, tracker.getWorkouts().size());
    }
}
