package model;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Tracks all workout sessions.
 */
public class WorkoutTracker {
    private List<Workout> workouts;

    /**
     * Constructs an empty WorkoutTracker.
     * Modifies: Initializes the workouts list.
     * Effects: Creates a new tracker with no workouts.
     */
    public WorkoutTracker() {
        workouts = new ArrayList<>();
    }

    /**
     * Adds a workout session to the tracker.
     * Requires: workout is non-null.
     * Modifies: the workouts list.
     * Effects: Appends the workout.
     * @param workout the workout session to add.
     */
    public void addWorkout(Workout workout) {
        if (workout == null) {
            throw new IllegalArgumentException("Workout cannot be null.");
        }
        workouts.add(workout);
    }

    /**
     * Returns a list of all recorded workouts.
     * @return a copy of the workouts list.
     */
    public List<Workout> getWorkouts() {
        return new ArrayList<>(workouts);
    }

    /**
     * Returns this WorkoutTracker's workouts as a JSON array.
     * Each Workout must also define a toJson() method.
     */
    public JSONArray toJson() {
        JSONArray array = new JSONArray();
        for (Workout w : workouts) {
            array.put(w.toJson()); 
            // Make sure Workout.java has a public JSONObject toJson() method
        }
        return array;
    }
}
