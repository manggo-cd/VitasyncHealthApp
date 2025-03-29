package ui;

import model.VitaSyncData;
import model.Workout;
import model.Exercise;
import model.ExerciseSet;
import model.Meal;
import model.Recipe;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * VitaSyncApp is the console-based user interface for the VitaSync application.
 * It allows users to log workout sessions, view workout history, manage meal plans,
 * interact with a recipe library, and save/load the entire application state.
 *
 * Requires: System.in is available.
 * Modifies: Displays output to System.out and reads from System.in.
 * Effects: Provides a working console-based interface to perform user story actions.
 */
public class VitaSyncApp {

    private Scanner scanner;
    private VitaSyncData vitaSyncData; // encapsulates workoutTracker, mealPlan, recipeLibrary

    /**
     * Constructs a new VitaSyncApp and initializes the model components.
     */
    public VitaSyncApp() {
        scanner = new Scanner(System.in);
        // Create a top-level state object with a default name.
        vitaSyncData = new VitaSyncData("My VitaSync Data");
    }

    /**
     * Starts the VitaSync application.
     *
     * Requires: User inputs valid choices.
     * Modifies: State of the application and outputs to System.out.
     * Effects: Runs the application until the user exits.
     */
    public void start() {
        boolean running = true;
        while (running) {
            displayMainMenu();
            String choice = scanner.nextLine();
            running = processMainMenuChoice(choice);
        }
    }

    /**
     * Displays the main menu options.
     *
     * Effects: Outputs the menu to System.out.
     */
    private void displayMainMenu() {
        System.out.println(" _    ___ __        _____                 \n"
                          + "| |  / (_) /_____ _/ ___/__  ______  _____\n" 
                          + "| | / / / __/ __ `/\\__ \\/ / / / __ \\/ ___/\n" 
                          + "| |/ / / /_/ /_/ /___/ / /_/ / / / / /__  \n" 
                          + "|___/_/\\__/\\__,_//____/\\__, /_/ /_/\\___/  \n"
                          + "                      /____/ \n");
        System.out.println("1. Log a Workout Session");
        System.out.println("2. View Workout History");
        System.out.println("3. Add a Meal to Meal Plan");
        System.out.println("4. View Daily Macros");
        System.out.println("5. Recipe Library");
        System.out.println("6. Save Data");
        System.out.println("7. Load Data");
        System.out.println("8. Quit");
        System.out.print("Enter your choice: ");
    }

    /**
     * Processes the main menu choice.
     *
     * Effects: Executes the corresponding action.
     * Returns false if the user chooses to quit.
     */
    private boolean processMainMenuChoice(String choice) {
        if ("8".equals(choice)) {
            exitApplication();
            return false;
        } else {
            performMainMenuAction(choice);
            return true;
        }
    }

    private void performMainMenuAction(String choice) {
        Runnable action = getMainMenuAction(choice);
        action.run();
    }
    
    private Runnable getMainMenuAction(String choice) {
        switch (choice) {
            case "1":
                return () -> handleWorkoutSession();
            case "2":
                return () -> viewWorkoutHistory();
            case "3":
                return () -> addMeal();
            case "4":
                return () -> viewDailyMacros();
            case "5":
                return () -> recipeLibraryMenu();
            case "6":
                return () -> saveData();
            case "7":
                return () -> loadData();
            default:
                return () -> System.out.println("Invalid choice. Please select an option from 1 to 8.");
        }
    }
    

    /**
     * Prints the exit message.
     *
     * Effects: Outputs an exit message to System.out.
     */
    private void exitApplication() {
        System.out.println("Exiting VitaSync. Goodbye!");
    }

    /**
     * Handles logging a workout session.
     *
     * Effects: Adds a new workout to the tracker.
     */
    private void handleWorkoutSession() {
        try {
            LocalDate date = readWorkoutDate();
            Workout workout = new Workout(date);
            addExercisesToWorkout(workout);
            vitaSyncData.getWorkoutTracker().addWorkout(workout);
            System.out.println("Workout logged successfully.");
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid number entered. Please try again.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Reads a workout date from the user.
     *
     * Effects: Returns a LocalDate parsed from user input.
     */
    private LocalDate readWorkoutDate() {
        System.out.print("Enter workout date (yyyy-MM-dd): ");
        String dateInput = scanner.nextLine();
        return LocalDate.parse(dateInput);
    }

    /**
     * Adds exercises to the given workout.
     *
     * Effects: Modifies the workout by adding exercises.
     */
    private void addExercisesToWorkout(Workout workout) {
        boolean addingExercises = true;
        while (addingExercises) {
            System.out.print("Enter exercise name (or blank to finish): ");
            String exerciseName = scanner.nextLine();
            if (exerciseName.isEmpty()) {
                addingExercises = false;
            } else {
                Exercise exercise = new Exercise(exerciseName);
                addSetsToExercise(exercise);
                workout.addExercise(exercise);
            }
        }
    }

    /**
     * Adds sets to the given exercise.
     *
     * Effects: Modifies the exercise by adding sets.
     */
    private void addSetsToExercise(Exercise exercise) {
        System.out.print("Enter number of sets for this exercise: ");
        int numSets = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < numSets; i++) {
            System.out.print("Enter target reps for set " + (i + 1) + ": ");
            int targetReps = Integer.parseInt(scanner.nextLine());
            ExerciseSet set = new ExerciseSet(targetReps);
            while (!set.isCompleted()) {
                set.checkOffRep();
            }
            exercise.addSet(set);
        }
    }

    /**
     * Displays the workout history.
     *
     * Effects: Outputs the details of each workout.
     */
    private void viewWorkoutHistory() {
        System.out.println("\n--- Workout History ---");
        if (vitaSyncData.getWorkoutTracker().getWorkouts().isEmpty()) {
            System.out.println("No workouts logged yet.");
        } else {
            for (Workout workout : vitaSyncData.getWorkoutTracker().getWorkouts()) {
                System.out.println("Date: " + workout.getDate());
                for (Exercise exercise : workout.getExercises()) {
                    System.out.println("  Exercise: " + exercise.getName());
                    int setNumber = 1;
                    for (ExerciseSet set : exercise.getSets()) {
                        System.out.println("    Set " + setNumber + ": " 
                                + set.getCompletedReps() + "/" + set.getTargetReps() + " reps completed");
                        setNumber++;
                    }
                }
            }
        }
    }

    /**
     * Prompts the user to add a meal.
     *
     * Effects: Adds a meal to the meal plan.
     */
    private void addMeal() {
        try {
            System.out.print("Enter meal name: ");
            String mealName = scanner.nextLine();
            System.out.print("Enter protein amount (grams): ");
            int protein = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter carbohydrate amount (grams): ");
            int carbs = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter fat amount (grams): ");
            int fat = Integer.parseInt(scanner.nextLine());
            Meal meal = new Meal(mealName, protein, carbs, fat);
            vitaSyncData.getMealPlan().addMeal(meal);
            System.out.println("Meal added to plan.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Please try again.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Displays the daily macronutrients.
     *
     * Effects: Outputs total protein, carbs, and fat.
     */
    private void viewDailyMacros() {
        System.out.println("\n--- Daily Macronutrients ---");
        System.out.println("Total Protein: " + vitaSyncData.getMealPlan().getTotalProtein() + "g");
        System.out.println("Total Carbs: " + vitaSyncData.getMealPlan().getTotalCarbs() + "g");
        System.out.println("Total Fat: " + vitaSyncData.getMealPlan().getTotalFat() + "g");
    }

    /**
     * Displays the recipe library menu and processes choices.
     *
     * Effects: Enables the user to manage the recipe library.
     */
    private void recipeLibraryMenu() {
        boolean inRecipeMenu = true;
        while (inRecipeMenu) {
            System.out.println("\n--- Recipe Library ---");
            System.out.println("1. View All Recipes");
            System.out.println("2. Add New Recipe");
            System.out.println("3. Edit Recipe");
            System.out.println("4. Delete Recipe");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();
            inRecipeMenu = processRecipeMenuChoice(choice);
        }
    }

    /**
     * Processes a choice from the recipe library menu.
     *
     * Effects: Executes the corresponding recipe library operation.
     * Returns false if the user chooses to return to the main menu.
     */
    private boolean processRecipeMenuChoice(String choice) {
        switch (choice) {
            case "1":
                viewAllRecipes();
                break;
            case "2":
                addNewRecipe();
                break;
            case "3":
                editRecipe();
                break;
            case "4":
                deleteRecipe();
                break;
            case "5":
                return false;
            default:
                System.out.println("Invalid choice. Please select an option from 1 to 5.");
                break;
        }
        return true;
    }

    /**
     * Displays all recipes in the recipe library.
     *
     * Effects: Outputs recipe details to System.out.
     */
    private void viewAllRecipes() {
        System.out.println("\n--- All Recipes ---");
        List<Recipe> recipes = vitaSyncData.getRecipeLibrary().getAllRecipes();
        if (recipes.isEmpty()) {
            System.out.println("No recipes saved yet.");
        } else {
            for (Recipe recipe : recipes) {
                System.out.println("Name: " + recipe.getName());
                System.out.println("Ingredients: " + recipe.getIngredients());
                System.out.println("Instructions: " + recipe.getInstructions());
                System.out.println("------------------------------");
            }
        }
    }

    /**
     * Prompts the user for recipe details and adds a new recipe.
     *
     * Effects: Adds the new recipe to the recipe library.
     */
    private void addNewRecipe() {
        try {
            System.out.print("Enter recipe name: ");
            String name = scanner.nextLine();
            System.out.print("Enter ingredients (comma-separated): ");
            String ingredientsLine = scanner.nextLine();
            List<String> ingredients = new ArrayList<>();
            for (String ing : ingredientsLine.split(",")) {
                ingredients.add(ing.trim());
            }
            System.out.print("Enter preparation instructions: ");
            String instructions = scanner.nextLine();
            Recipe recipe = new Recipe(name, ingredients, instructions);
            if (vitaSyncData.getRecipeLibrary().addRecipe(recipe)) {
                System.out.println("Recipe added successfully.");
            } else {
                System.out.println("Failed to add recipe.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Prompts the user for a recipe name and new details to edit an existing recipe.
     *
     * Effects: Updates the specified recipe if inputs are valid.
     */
    private void editRecipe() {
        System.out.print("Enter the name of the recipe to edit: ");
        String name = scanner.nextLine();
        System.out.print("Enter new ingredients (comma-separated): ");
        String ingredientsLine = scanner.nextLine();
        List<String> ingredients = new ArrayList<>();
        for (String ing : ingredientsLine.split(",")) {
            ingredients.add(ing.trim());
        }
        System.out.print("Enter new preparation instructions: ");
        String instructions = scanner.nextLine();
        boolean success = vitaSyncData.getRecipeLibrary().editRecipe(name, ingredients, instructions);
        if (success) {
            System.out.println("Recipe updated successfully.");
        } else {
            System.out.println("Failed to update recipe. Please check if the recipe exists and your inputs are valid.");
        }
    }

    /**
     * Prompts the user for a recipe name to delete and attempts removal.
     *
     * Effects: Updates the recipe library accordingly.
     */
    private void deleteRecipe() {
        System.out.print("Enter the name of the recipe to delete: ");
        String name = scanner.nextLine();
        boolean success = vitaSyncData.getRecipeLibrary().deleteRecipe(name);
        if (success) {
            System.out.println("Recipe deleted successfully.");
        } else {
            System.out.println("Failed to delete recipe. Please check if the recipe exists.");
        }
    }

    /**
     * Saves the current VitaSyncData state to file.
     *
     * Effects: Writes the current state to a JSON file.
     */
    private void saveData() {
        JsonWriter writer = new JsonWriter("./data/vitaSyncData.json");
        try {
            writer.open();
            writer.write(vitaSyncData);
            writer.close();
            System.out.println("Data successfully saved to ./data/vitaSyncData.json");
        } catch (FileNotFoundException e) {
            System.out.println("Error: Unable to open file for writing.");
        }
    }

    /**
     * Loads the VitaSyncData state from file.
     *
     * Effects: Reads the state from a JSON file and updates the current state.
     */
    private void loadData() {
        JsonReader reader = new JsonReader("./data/vitaSyncData.json");
        try {
            vitaSyncData = reader.read();
            System.out.println("Data successfully loaded from ./data/vitaSyncData.json");
        } catch (IOException e) {
            System.out.println("Error: Unable to read file.");
        }
    }
}
