# VitaSync- A Personalized Health and Fitness Application

## A Fitness-Workout Tracker & Macro Planner/ Meal Library Combined.

**VitaSync** is a Java-based application that merges workout tracking with meal planning into one cohesive interface. The workout tracker component helps you log exercise sessions, record sets and reps, and monitor progress over time, while the meal planner calculates daily macros and tracks nutrition for each meal. However, unlike most traditional macro-trackers, VitaSync seeks to integrate a **Recipe Library** where users are able to add and filter recipes by ingredients, and save recipes for future use. By integrating these two pillars of fitness, the app provides a **holistic approach** for users aiming to organize and optimize their health goals **more effectively**.

This project is tailored for *anyone* committed to improving their well-being—from casual gym-goers to competitive athletes. I find it particularly interesting because it combines my passion for software development with my personal interest in data-driven health management. By exploring features like logging, analytics, and macro calculations, I’ll strengthen my Java skills and create a practical tool that can help others maintain consistent, informed lifestyle habits.

## User Stories

- “As a user, I want to be able to log a completed workout session (with date, exercises, sets, and reps) so that I can keep track of my exercise history.”

- “As a user, I want to be able to view a list of all my past workouts in the workout history, including the date and exercises, so that I can see what I’ve accomplished over time.”

- “As a user, I want to be able to add a meal to my plan, specifying its name and macronutrients (protein, carbs, fat), so that I can keep an organized record of what I plan to eat.”

- “As a user, I want to be able to see the total protein, carbs, and fat in my planned meals for the day, so that I can ensure I’m meeting my dietary targets.”

- “As a user, I want to have the option to save the entire state of my application (including my workout history, meal plan, and recipe library) to a file, so that I can preserve my data between sessions.”

- “As a user, I want to have the option to load my entire application state from a file when I start the application, so that I can resume exactly where I left off.”

// CITATION: CPSC210 JSONSERIALIZATION REPOSITORY for the help on Saving and Loading features, specifically with the persistence package and tests and methodology

# Instructions for End User

- **Launching the Application:**  
  Simply run the VitaSyncGUI application. When launched, an animated splash screen with your banner image (`vitasyncbanner.png`) and a progress bar will appear. Once the loading completes, the main application window will be displayed with a custom background image (`background.jpg`).

- **Navigating the Main Window:**  
  The main window is organized into three tabs:
  - **Workouts Tab:**  
    - Click **"Add Workout"** to log a new workout session. You will be prompted to enter the workout date (in `yyyy-MM-dd` format) and then to add one or more exercises (with sets and target reps).  
    - All logged workouts are displayed in the text area below, showing the date, exercises, and sets completed.
  
  - **Meals Tab:**  
    - Click **"Add Meal"** to add a meal. You will be prompted for the meal name and its macronutrient details (protein, carbs, fat).  
    - Click **"View Daily Macros"** to display a summary of the total protein, carbs, and fat from all added meals.  
    - The tab shows a list of all meals with their respective macronutrient values.
  
  - **Recipes Tab:**  
    - Use the **"View All Recipes"** button to see a list of all saved recipes (displaying name, ingredients, and instructions).  
    - Click **"Add Recipe"** to add a new recipe. You will be prompted to enter the recipe name, a comma-separated list of ingredients, and the preparation instructions.  
    - Use **"Edit Recipe"** to modify an existing recipe (by entering its name and the new details).  
    - Use **"Delete Recipe"** to remove a recipe (by entering its name).

- **Saving and Loading Data:**  
  - In the **File** menu, select **"Save Data"** to write your current workouts, meals, and recipes to a JSON file.  
  - Select **"Load Data"** from the File menu to reload the saved state into the application.

- **Visual Components:**  
  - The application features a modern, attractive design with a custom background image behind all tabs.  
  - An animated splash screen (with a progress bar) is shown on startup, giving a visually engaging introduction to the application.

