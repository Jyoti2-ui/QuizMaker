package src.main;

import javax.swing.SwingUtilities;
import src.ui.QuizCreationUI;

/**
 * Entry point of the Quiz Maker application.
 * This class initializes and launches the main UI using Swing.
 */
public class Main {
    public static void main(String[] args) {
        // Run UI on Event Dispatch Thread for thread safety
        SwingUtilities.invokeLater(() -> {
            try {
                new QuizCreationUI().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
