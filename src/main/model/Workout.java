package model;

import org.json.JSONArray;
import org.json.JSONObject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a workout session on a specific date.
 * Contains a list of exercises that were performed during the session.
 */
public class Workout {
    private LocalDate date;
    private List<Exercise> exercises;

    /**
     * Constructs a new Workout for the given date.
     * Requires: date is non-null.
     * Modifies: Initializes the exercises list.
     * Effects: Creates a workout session for the given date.
     * @param date the date of the workout session.
     */
    public Workout(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null.");
        }
        this.date = date;
        this.exercises = new ArrayList<>();
    }

    /**
     * Adds an exercise to this workout session.
     * Requires: exercise is non-null.
     * Modifies: the exercises list.
     * Effects: the exercise is appended to the list.
     * @param exercise the exercise to add.
     */
    public void addExercise(Exercise exercise) {
        if (exercise == null) {
            throw new IllegalArgumentException("Exercise cannot be null.");
        }
        exercises.add(exercise);
    }

    /**
     * Returns the list of exercises in this workout.
     * @return a copy of the exercises list.
     */
    public List<Exercise> getExercises() {
        return new ArrayList<>(exercises);
    }

    /**
     * Returns the date of this workout session.
     * @return the workout date.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns this Workout as a JSON object.
     * Effects: Converts the workout date and exercises into JSON.
     */
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("date", date.toString());
        JSONArray exercisesArray = new JSONArray();
        for (Exercise ex : exercises) {
            exercisesArray.put(ex.toJson());
        }
        json.put("exercises", exercisesArray);
        return json;
    }
}
