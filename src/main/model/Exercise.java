package model;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an exercise in a workout session.
 * Consists of a name and a list of exercise sets.
 */
public class Exercise {
    private String name;
    private List<ExerciseSet> sets;

    /**
     * Constructs an Exercise with the specified name.
     * Requires: name is non-null and non-empty.
     * Modifies: Initializes the sets list.
     * Effects: Creates an exercise with the given name.
     * @param name the name of the exercise.
     */
    public Exercise(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Exercise name cannot be null or empty.");
        }
        this.name = name;
        this.sets = new ArrayList<>();
    }

    /**
     * Adds a set to this exercise.
     * Requires: set is non-null.
     * Modifies: the sets list.
     * Effects: Appends the exercise set.
     * @param set the exercise set to add.
     */
    public void addSet(ExerciseSet set) {
        if (set == null) {
            throw new IllegalArgumentException("Exercise set cannot be null.");
        }
        sets.add(set);
    }

    /**
     * Returns the name of the exercise.
     * @return the exercise name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the list of sets in this exercise.
     * @return a copy of the exercise sets.
     */
    public List<ExerciseSet> getSets() {
        return new ArrayList<>(sets);
    }
    
    /**
     * Returns this Exercise as a JSON object.
     * Effects: Converts the exercise name and its sets into a JSONObject.
     * @return a JSONObject representation of this Exercise.
     */
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        JSONArray setsArray = new JSONArray();
        for (ExerciseSet set : sets) {
            setsArray.put(set.toJson());
        }
        json.put("sets", setsArray);
        return json;
    }
}
