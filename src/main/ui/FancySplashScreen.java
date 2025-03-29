package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Displays an animated splash screen with a progress bar.
 * 
 * Requires: A valid splash image path must be provided.
 * Modifies: This window's state (progress bar and visibility).
 * Effects: Animates a progress bar from 0 to 100, then launches the main application GUI.
 */
public class FancySplashScreen extends JWindow {

    private JProgressBar progressBar;
    private Timer timer;
    private int progressValue;

    private static final int SPLASH_WIDTH = 500;
    private static final int SPLASH_HEIGHT = 300;
    private static final int TIMER_DELAY = 50;    // ms per tick
    private static final int TOTAL_STEPS = 100;   // progress from 0 to 100

    /**
     * Constructs a FancySplashScreen with the given splash image.
     * 
     * Requires: splashImagePath is non-null and points to a valid image file.
     * Modifies: Initializes this window's size, content pane, and progress bar.
     * Effects: Sets up the animated splash screen.
     *
     * @param splashImagePath path to the splash image
     */
    public FancySplashScreen(String splashImagePath) {
        setSize(SPLASH_WIDTH, SPLASH_HEIGHT);
        setLocationRelativeTo(null);
        
        JPanel content = buildContentPanel(splashImagePath);
        setContentPane(content);
        
        progressBar = buildProgressBar();
        content.add(progressBar, BorderLayout.SOUTH);
        
        setupTimer();
    }
    
    /**
     * Builds the content panel with the splash image.
     * 
     * Requires: splashImagePath is valid.
     * Modifies: Creates a new JPanel.
     * Effects: Returns a panel with the splash image centered.
     *
     * @param splashImagePath path to the splash image
     * @return the content panel
     */
    private JPanel buildContentPanel(String splashImagePath) {
        JPanel panel = new JPanel(new BorderLayout());
        ImageIcon icon = new ImageIcon(splashImagePath);
        JLabel imageLabel = new JLabel(icon);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(imageLabel, BorderLayout.CENTER);
        return panel;
    }
    
    /**
     * Creates the progress bar.
     * 
     * Requires: TOTAL_STEPS is properly defined.
     * Modifies: Creates a new JProgressBar.
     * Effects: Returns a progress bar with range 0 to TOTAL_STEPS.
     *
     * @return the progress bar
     */
    private JProgressBar buildProgressBar() {
        JProgressBar bar = new JProgressBar(0, TOTAL_STEPS);
        bar.setStringPainted(true);
        return bar;
    }
    
    /**
     * Sets up the timer to animate the progress bar.
     * 
     * Requires: TIMER_DELAY and TOTAL_STEPS are properly defined.
     * Modifies: Initializes the timer and progressValue.
     * Effects: Increments progressValue until TOTAL_STEPS, then stops and launches the main app.
     */
    private void setupTimer() {
        progressValue = 0;
        timer = new Timer(TIMER_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                progressValue++;
                progressBar.setValue(progressValue);
                if (progressValue >= TOTAL_STEPS) {
                    timer.stop();
                    dispose();
                    launchMainApp();
                }
            }
        });
    }
    
    /**
     * Starts the animation by making the splash screen visible and starting the timer.
     * 
     * Requires: Timer has been set up.
     * Modifies: This window's visibility.
     * Effects: Displays the splash screen and begins the animation.
     */
    public void startAnimation() {
        setVisible(true);
        timer.start();
    }
    
    /**
     * Launches the main application GUI after the splash screen is finished.
     * 
     * Requires: SwingUtilities is available.
     * Modifies: Creates a new instance of the main GUI.
     * Effects: Schedules the main application GUI to be displayed.
     */
    private void launchMainApp() {
        SwingUtilities.invokeLater(() -> {
            VitaSyncGUI mainApp = new VitaSyncGUI();
            mainApp.setVisible(true);
        });
    }

    /**
     * Main method to run the splash screen.
     * 
     * Requires: args is not used.
     * Modifies: Launches the splash screen.
     * Effects: Starts the splash screen animation.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        FancySplashScreen splash = new FancySplashScreen("./data/vitasyncbanner.png");
        splash.startAnimation();
    }
}
