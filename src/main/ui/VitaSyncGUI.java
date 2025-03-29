package ui;

import model.VitaSyncData;
import model.Workout;
import model.Exercise;
import model.ExerciseSet;
import model.Meal;
import model.Recipe;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * VitaSyncGUI is a Swing-based GUI that replicates the console functionality of the VitaSync application.
 *
 * It provides tabs for managing workouts, meals, and recipes, along with Save/Load functionality.
 * An animated splash screen is displayed at startup.
 *
 * Requires: Model and persistence classes must be properly implemented; required images must be available.
 * Modifies: The GUI window and internal state of VitaSyncData.
 * Effects: Provides a fully functional, modern-looking GUI for VitaSync.
 */
public class VitaSyncGUI extends JFrame {

    private static final String DATA_FILE_PATH = "./data/vitaSyncData.json";
    private static final String BG_IMAGE_PATH = "./data/background.png";
    private static final Font CUSTOM_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Color FOREGROUND_COLOR = new Color(20, 20, 20);
    private static final Color BTN_BG_COLOR = new Color(220, 220, 250);

    private VitaSyncData vitaSyncData;
    private JTabbedPane tabbedPane;
    private JTextArea workoutArea;
    private JTextArea mealArea;
    private JTextArea recipeArea;

    /**
     * Constructs the VitaSyncGUI.
     *
     * Requires: None.
     * Modifies: Initializes the internal state and GUI components.
     * Effects: Creates and configures the main application window.
     */
    public VitaSyncGUI() {
        setLookAndFeelNimbus();
        vitaSyncData = new VitaSyncData("My VitaSync Data");
        initUI();
    }

    /**
     * Sets the Nimbus look and feel for a modern appearance.
     *
     * Requires: Nimbus L&F is installed.
     * Modifies: UIManager settings.
     * Effects: Changes the application's look and feel to Nimbus.
     */
    private void setLookAndFeelNimbus() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Fallback to default L&F if Nimbus is unavailable.
        }
    }

    /**
     * Initializes the main UI components.
     *
     * Requires: Background image and proper layout managers.
     * Modifies: Sets up the content pane, menu bar, and tabbed pane.
     * Effects: Builds and lays out the GUI.
     */
    private void initUI() {
        setTitle("VitaSync Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        BackgroundPanel mainPanel = createBackgroundPanel();
        setContentPane(mainPanel);

        createMenuBar();

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(CUSTOM_FONT);
        tabbedPane.addTab("Workouts", createWorkoutsTab());
        tabbedPane.addTab("Meals", createMealsTab());
        tabbedPane.addTab("Recipes", createRecipesTab());
        tabbedPane.addTab("Gregor", createGregorTab());

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
    }

    /**
     * Creates a BackgroundPanel using the specified background image.
     *
     * Requires: BG_IMAGE_PATH is valid.
     * Modifies: Creates a new BackgroundPanel.
     * Effects: Returns a panel that displays the background image.
     *
     * @return the BackgroundPanel
     */
    private BackgroundPanel createBackgroundPanel() {
        ImageIcon bgIcon = new ImageIcon(BG_IMAGE_PATH);
        BackgroundPanel panel = new BackgroundPanel(bgIcon.getImage());
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        return panel;
    }

    /**
     * Creates the menu bar with Save and Load options.
     *
     * Requires: None.
     * Modifies: Sets the JFrame's menu bar.
     * Effects: Adds a File menu with Save Data and Load Data items.
     */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setFont(CUSTOM_FONT);

        JMenuItem saveItem = new JMenuItem("Save Data");
        styleMenuItem(saveItem);
        saveItem.addActionListener(e -> saveData());

        JMenuItem loadItem = new JMenuItem("Load Data");
        styleMenuItem(loadItem);
        loadItem.addActionListener(e -> loadData());

        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    // =========================
    //       WORKOUTS TAB
    // =========================

    /**
     * Creates the Workouts tab panel.
     *
     * Requires: None.
     * Modifies: Creates and returns a JPanel.
     * Effects: Provides a tab with a button for adding workouts and a text area for display.
     *
     * @return the Workouts tab panel
     */
    private JPanel createWorkoutsTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        panel.add(createWorkoutsTopPanel(), BorderLayout.NORTH);
        panel.add(createWorkoutsCenterArea(), BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates the top panel of the Workouts tab.
     *
     * Requires: None.
     * Modifies: Creates a JPanel with a button.
     * Effects: Returns a panel with an "Add Workout" button.
     *
     * @return the top panel for workouts
     */
    private JPanel createWorkoutsTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.setOpaque(false);

        JButton addWorkoutButton = new JButton("Add Workout");
        styleButton(addWorkoutButton);
        addWorkoutButton.addActionListener(e -> handleAddWorkout());
        topPanel.add(addWorkoutButton);

        return topPanel;
    }

    /**
     * Creates the center area for the Workouts tab.
     *
     * Requires: None.
     * Modifies: Instantiates the workoutArea.
     * Effects: Returns a JScrollPane containing the workoutArea.
     *
     * @return the center area scroll pane
     */
    private JScrollPane createWorkoutsCenterArea() {
        workoutArea = new JTextArea();
        styleTextArea(workoutArea);
        return new JScrollPane(workoutArea);
    }

    /**
     * Handles adding a new workout by prompting the user and updating the display.
     *
     * Requires: User input is valid.
     * Modifies: vitaSyncData and workoutArea.
     * Effects: Adds a workout and refreshes the workouts display.
     */
    private void handleAddWorkout() {
        try {
            Workout workout = promptUserForWorkout();
            if (workout != null) {
                vitaSyncData.getWorkoutTracker().addWorkout(workout);
                JOptionPane.showMessageDialog(this, "Workout logged successfully.");
                refreshWorkoutsDisplay();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    /**
     * Prompts the user for workout details and returns a Workout object.
     *
     * Requires: User enters a valid date and exercise details.
     * Modifies: Creates a new Workout instance.
     * Effects: Returns a constructed Workout, or null if cancelled.
     *
     * @return the constructed Workout or null
     */
    private Workout promptUserForWorkout() {
        String dateStr = JOptionPane.showInputDialog(this, "Enter workout date (yyyy-MM-dd):");
        if (isEmpty(dateStr)) {
            return null;
        }
        LocalDate date = LocalDate.parse(dateStr);
        Workout workout = new Workout(date);

        boolean addingExercises = true;
        while (addingExercises) {
            String exName = JOptionPane.showInputDialog(this,
                    "Enter exercise name (or blank to finish):");
            if (exName == null || exName.trim().isEmpty()) {
                addingExercises = false;
            } else {
                Exercise exercise = new Exercise(exName);
                addSetsToExercise(exercise);
                workout.addExercise(exercise);
            }
        }
        return workout;
    }

    /**
     * Prompts the user for set details and adds them to the given exercise.
     *
     * Requires: User enters a valid number of sets and target reps.
     * Modifies: The provided Exercise object.
     * Effects: Adds one or more ExerciseSet objects to the exercise.
     *
     * @param exercise the Exercise to add sets to
     */
    private void addSetsToExercise(Exercise exercise) {
        try {
            String numSetsStr = JOptionPane.showInputDialog(this, "Enter number of sets:");
            if (isEmpty(numSetsStr)) {
                return;
            }
            int numSets = Integer.parseInt(numSetsStr);
            for (int i = 0; i < numSets; i++) {
                String targetRepsStr = JOptionPane.showInputDialog(this,
                        "Enter target reps for set " + (i + 1) + ":");
                if (isEmpty(targetRepsStr)) {
                    continue;
                }
                int targetReps = Integer.parseInt(targetRepsStr);
                ExerciseSet set = new ExerciseSet(targetReps);
                while (!set.isCompleted()) {
                    set.checkOffRep();
                }
                exercise.addSet(set);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding sets: " + e.getMessage());
        }
    }

    /**
     * Refreshes the workoutArea to display current workouts.
     *
     * Requires: vitaSyncData is up-to-date.
     * Modifies: workoutArea text.
     * Effects: Updates the displayed list of workouts.
     */
    private void refreshWorkoutsDisplay() {
        StringBuilder sb = new StringBuilder();
        List<Workout> workouts = vitaSyncData.getWorkoutTracker().getWorkouts();
        if (workouts.isEmpty()) {
            sb.append("No workouts logged yet.\n");
        } else {
            for (Workout w : workouts) {
                sb.append("Date: ").append(w.getDate()).append("\n");
                for (Exercise ex : w.getExercises()) {
                    sb.append("  Exercise: ").append(ex.getName()).append("\n");
                    int setNumber = 1;
                    for (ExerciseSet s : ex.getSets()) {
                        sb.append("    Set ").append(setNumber).append(": ")
                          .append(s.getCompletedReps()).append("/")
                          .append(s.getTargetReps()).append(" reps completed\n");
                        setNumber++;
                    }
                }
                sb.append("\n");
            }
        }
        workoutArea.setText(sb.toString());
    }

    // =========================
    //         MEALS TAB
    // =========================

    /**
     * Creates the Meals tab panel.
     *
     * Requires: None.
     * Modifies: Creates a new JPanel for meals.
     * Effects: Returns a panel with a top panel (buttons) and a text area.
     *
     * @return the Meals tab panel
     */
    private JPanel createMealsTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        panel.add(createMealsTopPanel(), BorderLayout.NORTH);
        mealArea = new JTextArea();
        styleTextArea(mealArea);
        panel.add(new JScrollPane(mealArea), BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates the top panel of the Meals tab.
     *
     * Requires: None.
     * Modifies: Creates a panel with "Add Meal" and "View Daily Macros" buttons.
     * Effects: Returns the Meals top panel.
     *
     * @return the Meals top panel
     */
    private JPanel createMealsTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.setOpaque(false);

        JButton addMealButton = new JButton("Add Meal");
        styleButton(addMealButton);
        addMealButton.addActionListener(e -> handleAddMeal());
        topPanel.add(addMealButton);

        JButton macrosButton = new JButton("View Daily Macros");
        styleButton(macrosButton);
        macrosButton.addActionListener(e -> showDailyMacros());
        topPanel.add(macrosButton);

        return topPanel;
    }

    /**
     * Handles adding a new meal.
     *
     * Requires: User input is valid.
     * Modifies: vitaSyncData and mealArea.
     * Effects: Adds a meal and refreshes the meal display.
     */
    private void handleAddMeal() {
        try {
            Meal meal = promptUserForMeal();
            if (meal != null) {
                vitaSyncData.getMealPlan().addMeal(meal);
                JOptionPane.showMessageDialog(this, "Meal added successfully.");
                refreshMealsDisplay();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding meal: " + e.getMessage());
        }
    }

    /**
     * Prompts the user for meal details and returns a Meal object.
     *
     * Requires: User enters valid meal details.
     * Modifies: Creates a new Meal instance.
     * Effects: Returns a Meal, or null if cancelled.
     *
     * @return the constructed Meal or null
     */
    private Meal promptUserForMeal() {
        String mealName = JOptionPane.showInputDialog(this, "Enter meal name:");
        if (isEmpty(mealName)) {
            return null;
        }
        String proteinStr = JOptionPane.showInputDialog(this, "Enter protein (grams):");
        String carbsStr = JOptionPane.showInputDialog(this, "Enter carbs (grams):");
        String fatStr = JOptionPane.showInputDialog(this, "Enter fat (grams):");
        if (isEmpty(proteinStr) || isEmpty(carbsStr) || isEmpty(fatStr)) {
            return null;
        }
        int protein = Integer.parseInt(proteinStr);
        int carbs = Integer.parseInt(carbsStr);
        int fat = Integer.parseInt(fatStr);
        return new Meal(mealName, protein, carbs, fat);
    }

    /**
     * Refreshes the mealArea to display current meals.
     *
     * Requires: vitaSyncData is up-to-date.
     * Modifies: mealArea text.
     * Effects: Updates the displayed list of meals.
     */
    private void refreshMealsDisplay() {
        StringBuilder sb = new StringBuilder();
        List<Meal> meals = vitaSyncData.getMealPlan().getMeals();
        if (meals.isEmpty()) {
            sb.append("No meals added yet.\n");
        } else {
            for (Meal m : meals) {
                sb.append(m.getName()).append(" (Protein: ")
                  .append(m.getProtein()).append(", Carbs: ")
                  .append(m.getCarbs()).append(", Fat: ")
                  .append(m.getFat()).append(")\n");
            }
        }
        mealArea.setText(sb.toString());
    }

    /**
     * Displays daily macros in a dialog.
     *
     * Requires: MealPlan data is up-to-date.
     * Modifies: None.
     * Effects: Shows a message dialog with daily macronutrient totals.
     */
    private void showDailyMacros() {
        int totalProtein = vitaSyncData.getMealPlan().getTotalProtein();
        int totalCarbs = vitaSyncData.getMealPlan().getTotalCarbs();
        int totalFat = vitaSyncData.getMealPlan().getTotalFat();
        String msg = "Total Protein: " + totalProtein + "g\n"
                   + "Total Carbs: " + totalCarbs + "g\n"
                   + "Total Fat: " + totalFat + "g";
        JOptionPane.showMessageDialog(this, msg, "Daily Macros", JOptionPane.INFORMATION_MESSAGE);
    }

    // =========================
    //       RECIPES TAB
    // =========================

    private JPanel createRecipesTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        panel.add(buildRecipesTopPanel(), BorderLayout.NORTH);
        recipeArea = new JTextArea();
        styleTextArea(recipeArea);
        panel.add(new JScrollPane(recipeArea), BorderLayout.CENTER);

        return panel;
    }

    /**
     * Builds the top panel for the Recipes tab with buttons.
     *
     * Requires: None.
     * Modifies: Creates a JPanel with recipe management buttons.
     * Effects: Returns the top panel.
     *
     * @return the top panel for recipes
     */
    private JPanel buildRecipesTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.setOpaque(false);

        JButton viewAllButton = new JButton("View All Recipes");
        styleButton(viewAllButton);
        viewAllButton.addActionListener(e -> viewAllRecipes());

        JButton addButton = new JButton("Add Recipe");
        styleButton(addButton);
        addButton.addActionListener(e -> addNewRecipe());

        JButton editButton = new JButton("Edit Recipe");
        styleButton(editButton);
        editButton.addActionListener(e -> editRecipe());

        JButton deleteButton = new JButton("Delete Recipe");
        styleButton(deleteButton);
        deleteButton.addActionListener(e -> deleteRecipe());

        topPanel.add(viewAllButton);
        topPanel.add(addButton);
        topPanel.add(editButton);
        topPanel.add(deleteButton);

        return topPanel;
    }

    /**
     * Displays all recipes in the recipeArea.
     *
     * Requires: RecipeLibrary is up-to-date.
     * Modifies: recipeArea text.
     * Effects: Updates the displayed list of recipes.
     */
    private void viewAllRecipes() {
        List<Recipe> recipes = vitaSyncData.getRecipeLibrary().getAllRecipes();
        if (recipes.isEmpty()) {
            recipeArea.setText("No recipes saved yet.\n");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("--- All Recipes ---\n\n");
            for (Recipe r : recipes) {
                sb.append("Name: ").append(r.getName()).append("\n");
                sb.append("Ingredients: ").append(r.getIngredients()).append("\n");
                sb.append("Instructions: ").append(r.getInstructions()).append("\n");
                sb.append("------------------------------\n");
            }
            recipeArea.setText(sb.toString());
        }
    }

    /**
     * Adds a new recipe by prompting the user.
     *
     * Requires: User input is valid.
     * Modifies: vitaSyncData and recipeArea.
     * Effects: Adds a recipe and refreshes the recipes display.
     */
    private void addNewRecipe() {
        Recipe recipe = buildRecipeFromUser();
        if (recipe == null) {
            return;
        }
        boolean success = vitaSyncData.getRecipeLibrary().addRecipe(recipe);
        if (success) {
            JOptionPane.showMessageDialog(this, "Recipe added successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add recipe.");
        }
        viewAllRecipes();
    }

    /**
     * Prompts the user for recipe details and returns a Recipe.
     *
     * Requires: User input is valid.
     * Modifies: Creates a new Recipe instance.
     * Effects: Returns the constructed Recipe or null if cancelled.
     *
     * @return the constructed Recipe, or null
     */
    private Recipe buildRecipeFromUser() {
        String name = prompt("Enter recipe name:");
        if (isEmpty(name)) {
            return null;
        }
        String ingredientsLine = prompt("Enter ingredients (comma-separated):");
        if (isEmpty(ingredientsLine)) {
            return null;
        }
        String instructions = prompt("Enter preparation instructions:");
        if (isEmpty(instructions)) {
            return null;
        }
        List<String> ingredients = Arrays.asList(ingredientsLine.split("\\s*,\\s*"));
        try {
            return new Recipe(name, ingredients, instructions);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Edits an existing recipe by prompting for new details.
     *
     * Requires: Recipe with the given name exists.
     * Modifies: Updates the recipe in vitaSyncData.
     * Effects: Refreshes the recipes display after editing.
     */
    private void editRecipe() {
        String name = prompt("Enter the name of the recipe to edit:");
        if (isEmpty(name)) {
            return;
        }
        String ingredientsLine = prompt("Enter new ingredients (comma-separated):");
        if (ingredientsLine == null) {
            return;
        }
        List<String> newIngredients = Arrays.asList(ingredientsLine.split("\\s*,\\s*"));
        String instructions = prompt("Enter new preparation instructions:");
        if (instructions == null) {
            return;
        }
        boolean success = vitaSyncData.getRecipeLibrary().editRecipe(name, newIngredients, instructions);
        if (success) {
            JOptionPane.showMessageDialog(this, "Recipe updated successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update recipe. Check if it exists.");
        }
        viewAllRecipes();
    }

    /**
     * Deletes a recipe by prompting the user for the recipe name.
     *
     * Requires: Recipe with the given name exists.
     * Modifies: Removes the recipe from vitaSyncData.
     * Effects: Refreshes the recipes display.
     */
    private void deleteRecipe() {
        String name = prompt("Enter the name of the recipe to delete:");
        if (isEmpty(name)) {
            return;
        }
        boolean success = vitaSyncData.getRecipeLibrary().deleteRecipe(name);
        if (success) {
            JOptionPane.showMessageDialog(this, "Recipe deleted successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete recipe. Check if it exists.");
        }
        viewAllRecipes();
    }

    // =========================
    //        GREGOR TAB
    // =========================

        /**
     * Creates the "Gregor" tab panel which displays an image of Gregor.
     *
     * Requires: The image file exists at the specified path.
     * Modifies: Creates a new JPanel.
     * Effects: Returns a panel displaying the Gregor image centered.
     *
     * @return the Gregor tab panel
     */
    private JPanel createGregorTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false); // allow background to show through if desired
        ImageIcon gregorIcon = new ImageIcon("./data/gregor.jpg");
        JLabel gregorLabel = new JLabel(gregorIcon);
        gregorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(gregorLabel, BorderLayout.CENTER);
        return panel;
    }

    // =========================
    //        SAVE/LOAD
    // =========================

    /**
     * Saves the current VitaSyncData state to file.
     *
     * Requires: DATA_FILE_PATH is valid.
     * Modifies: Writes data to a file.
     * Effects: Persists the application state.
     */
    private void saveData() {
        JsonWriter writer = new JsonWriter(DATA_FILE_PATH);
        try {
            writer.open();
            writer.write(vitaSyncData);
            writer.close();
            JOptionPane.showMessageDialog(this, "Data successfully saved to " + DATA_FILE_PATH);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Error: Unable to open file for writing.");
        }
    }

    /**
     * Loads the VitaSyncData state from file and refreshes the UI.
     *
     * Requires: DATA_FILE_PATH points to a valid JSON file.
     * Modifies: Updates vitaSyncData and refreshes display components.
     * Effects: Loads the application state from file.
     */
    private void loadData() {
        JsonReader reader = new JsonReader(DATA_FILE_PATH);
        try {
            vitaSyncData = reader.read();
            JOptionPane.showMessageDialog(this, "Data loaded successfully from " + DATA_FILE_PATH);
            refreshWorkoutsDisplay();
            refreshMealsDisplay();
            viewAllRecipes();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error: Unable to read file.");
        }
    }

    // =========================
    //         UTILITY
    // =========================

    /**
     * Prompts the user with a message and returns the input.
     *
     * Requires: message is non-null.
     * Modifies: None.
     * Effects: Displays an input dialog.
     *
     * @param message the prompt message
     * @return the user input string
     */
    private String prompt(String message) {
        return JOptionPane.showInputDialog(this, message);
    }

    /**
     * Checks if a string is null or empty after trimming.
     *
     * Requires: None.
     * Modifies: None.
     * Effects: Returns a boolean.
     *
     * @param s the string to check
     * @return true if null or empty, false otherwise
     */
    private boolean isEmpty(String s) {
        return (s == null || s.trim().isEmpty());
    }

    /**
     * Applies custom styling to a menu item.
     *
     * Requires: item is non-null.
     * Modifies: The menu item appearance.
     * Effects: Sets the font, background, and foreground.
     *
     * @param item the JMenuItem to style
     */
    private void styleMenuItem(JMenuItem item) {
        item.setFont(CUSTOM_FONT);
        item.setBackground(BTN_BG_COLOR);
        item.setForeground(FOREGROUND_COLOR);
    }

    /**
     * Applies custom styling to a button.
     *
     * Requires: btn is non-null.
     * Modifies: The button appearance.
     * Effects: Sets the font, background, and foreground.
     *
     * @param btn the JButton to style
     */
    private void styleButton(JButton btn) {
        btn.setFont(CUSTOM_FONT);
        btn.setBackground(BTN_BG_COLOR);
        btn.setForeground(FOREGROUND_COLOR);
    }

    /**
     * Applies custom styling to a text area.
     *
     * Requires: area is non-null.
     * Modifies: The text area appearance.
     * Effects: Sets the font, foreground color, and transparency.
     *
     * @param area the JTextArea to style
     */
    private void styleTextArea(JTextArea area) {
        area.setFont(CUSTOM_FONT);
        area.setForeground(FOREGROUND_COLOR);
        area.setOpaque(false);
    }

    /**
     * Main method that runs the splash screen first and then the GUI.
     *
     * Requires: FancySplashScreen and related classes are available.
     * Modifies: Launches the application.
     * Effects: Displays an animated splash screen, then shows the main GUI.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FancySplashScreen splash = new FancySplashScreen("./data/vitasyncbanner.png");
            splash.startAnimation();
        });
    }
}
