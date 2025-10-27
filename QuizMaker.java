package src.main;

import src.main.Main;

/**
 * QuizMaker class - Alternative entry point that delegates to Main class.
 * This provides a simpler way to run the application.
 */
public class QuizMaker {
    
    public static void main(String[] args) {
        // Delegate to the actual Main class
        Main.main(args);
    }
}