package ui;

import javax.swing.*;
import java.awt.*;

/**
 * A custom JPanel that draws a background image behind its child components.
 *
 * Requires: A valid Image object must be provided.
 * Modifies: This panel's painting behavior.
 * Effects: Draws the background image scaled to the panel size.
 */
public class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    /**
     * Constructs a BackgroundPanel with the given image.
     *
     * Requires: backgroundImage is non-null.
     * Modifies: Sets this.backgroundImage.
     * Effects: Creates a panel that will paint the provided image as its background.
     *
     * @param backgroundImage the image to draw as the panel background
     */
    public BackgroundPanel(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
        setLayout(new BorderLayout());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
