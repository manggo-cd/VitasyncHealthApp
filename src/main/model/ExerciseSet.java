package model;

import org.json.JSONObject;

/**
 * Represents a set of an exercise with a target number of reps.
 * Allows marking each rep as completed.
 */
public class ExerciseSet {
    private int targetReps;
    private int completedReps;

    /**
     * Constructs an ExerciseSet with a given target number of reps.
     * Requires: targetReps > 0.
     * Modifies: Sets completedReps to 0.
     * Effects: Creates an exercise set with the specified target.
     * @param targetReps the target number of reps.
     */
    public ExerciseSet(int targetReps) {
        if (targetReps <= 0) {
            throw new IllegalArgumentException("Target reps must be greater than 0.");
        }
        this.targetReps = targetReps;
        this.completedReps = 0;
    }

    /**
     * Marks one rep as completed.
     * Modifies: Increments the completed rep count if it is less than the target.
     * Effects: Checks off one rep.
     */
    public void checkOffRep() {
        if (completedReps < targetReps) {
            completedReps++;
        }
    }

    /**
     * Returns the number of completed reps.
     * @return the count of completed reps.
     */
    public int getCompletedReps() {
        return completedReps;
    }

    /**
     * Returns the target number of reps.
     * @return the target rep count.
     */
    public int getTargetReps() {
        return targetReps;
    }

    /**
     * Checks if the set is fully completed.
     * @return true if completedReps equals targetReps; false otherwise.
     */
    public boolean isCompleted() {
        return completedReps == targetReps;
    }
    
    /**
     * Returns this ExerciseSet as a JSON object.
     * Effects: Converts the target and completed reps into a JSONObject.
     * @return a JSONObject representation of this ExerciseSet.
     */
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("targetReps", targetReps);
        json.put("completedReps", completedReps);
        return json;
    }
}
