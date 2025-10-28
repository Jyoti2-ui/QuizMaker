package src.ui;

import java.util.Scanner;
import src.model.Questions;
import src.model.Quiz;
import src.model.Result;

/**
 * QuizAttemptUI provides user interface for attempting quizzes.
 * Handles quiz-taking interactions and timer management.
 */
public class QuizAttemptUI {
    
    /**
     * Manages the quiz attempt process
     * @param quiz The quiz to attempt
     * @param scanner Scanner object for user input
     * @return Result object with quiz results, or null if cancelled
     */
    public static Result attemptQuiz(Quiz quiz, Scanner scanner) {
        if (quiz == null || !quiz.isValid()) {
            System.out.println("✗ Invalid quiz. Cannot attempt.");
            return null;
        }
        
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         QUIZ ATTEMPT                   ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        // Get student information
        System.out.print("Enter your name: ");
        String studentName = scanner.nextLine().trim();
        
        if (studentName.isEmpty()) {
            System.out.println("✗ Name cannot be empty.");
            return null;
        }
        
        System.out.print("Enter your student ID (optional): ");
        String studentId = scanner.nextLine().trim();
        
        // Create result object
        Result result = new Result(quiz, studentName, studentId);
        
        // Display quiz instructions
        displayQuizInstructions(quiz);
        
        System.out.print("\nReady to start? Press Enter to begin...");
        scanner.nextLine();
        
        // Start timer
        long startTime = System.currentTimeMillis();
        
        // Attempt each question
        for (int i = 0; i < quiz.getQuestionCount(); i++) {
            Questions question = quiz.addQuestion(i);
            
            clearScreen();
            System.out.println("╔════════════════════════════════════════╗");
            System.out.println("║  Question " + (i + 1) + " of " + quiz.getQuestionCount() + "                         ║");
            System.out.println("╚════════════════════════════════════════╝\n");
            
            // Display question
            System.out.println(question.displayQuestion());
            
            // Get answer based on question type
            String answer = getAnswerForQuestion(question, scanner);
            
            if (answer != null) {
                result.recordAnswer(i, answer);
            }
            
            // Check time limit
            if (quiz.hasTimeLimit()) {
                long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
                long timeLimit = quiz.getTimeLimit() * 60L;
                
                if (elapsedTime >= timeLimit) {
                    System.out.println("\n⏰ Time's up! Quiz will be submitted automatically.");
                    break;
                }
            }
        }
        
        // Calculate time taken
        long endTime = System.currentTimeMillis();
        int timeTaken = (int) ((endTime - startTime) / 1000);
        result.setTimeTaken(timeTaken);
        
        // Calculate result
        result.calculateResult(quiz.getPassingPercentage());
        
        System.out.println("\n✓ Quiz completed!");
        System.out.println("Time taken: " + formatTime(timeTaken));
        
        return result;
    }
    
    /**
     * Displays quiz instructions
     * @param quiz The quiz being attempted
     */
    private static void displayQuizInstructions(Quiz quiz) {
        System.out.println("\n═══════════════════════════════════════");
        System.out.println("            INSTRUCTIONS");
        System.out.println("═══════════════════════════════════════");
        System.out.println("Quiz: " + quiz.getTitle());
        System.out.println("Total Questions: " + quiz.getQuestionCount());
        System.out.println("Total Marks: " + quiz.getTotalMarks());
        System.out.println("Passing Marks: " + quiz.getPassingMarks() + 
                          " (" + quiz.getPassingPercentage() + "%)");
        
        if (quiz.hasTimeLimit()) {
            System.out.println("Time Limit: " + quiz.getTimeLimit() + " minutes");
        } else {
            System.out.println("Time Limit: No limit");
        }
        
        System.out.println("\nInstructions:");
        System.out.println("• Read each question carefully");
        System.out.println("• Type your answer and press Enter");
        System.out.println("• For Multiple Choice: Enter option letter or full text");
        System.out.println("• For True/False: Enter 'True' or 'False'");
        System.out.println("• You cannot go back to previous questions");
        System.out.println("• Type 'skip' to skip a question");
        System.out.println("═══════════════════════════════════════");
    }
    
    /**
     * Gets answer for a specific question
     * @param question The question being answered
     * @param scanner Scanner object for input
     * @return The student's answer, or null if skipped
     */
    private static String getAnswerForQuestion(Questions question, Scanner scanner) {
        String questionType = question.getQuestionType();
        
        switch (questionType) {
            case "Multiple Choice" -> {
                return getMultipleChoiceAnswer(question, scanner);
            }
            case "True/False" -> {
                return getTrueFalseAnswer(scanner);
            }
            case "Short Answer" -> {
                return getShortAnswer(scanner);
            }
            default -> {
            }
        }
        
        return null;
    }
    
    /**
     * Gets answer for multiple choice question
     * @param question The question
     * @param scanner Scanner object
     * @return The selected answer
     */
    private static String getMultipleChoiceAnswer(Questions question, Scanner scanner) {
        while (true) {
            System.out.print("\nYour answer (enter option letter or number, or 'skip'): ");
            String input = scanner.nextLine().trim();
            
            if (input.equalsIgnoreCase("skip")) {
                System.out.println("Question skipped.");
                return null;
            }
            
            if (input.isEmpty()) {
                System.out.println("✗ Answer cannot be empty. Please try again.");
                continue;
            }
            
            // Check if input is a letter (A, B, C, etc.)
            if (input.length() == 1 && Character.isLetter(input.charAt(0))) {
                char letter = Character.toUpperCase(input.charAt(0));
                int index = letter - 'A';
                
                if (index >= 0 && index < question.getOptions().size()) {
                    return question.getOptions().get(index);
                } else {
                    System.out.println("✗ Invalid option. Please try again.");
                    continue;
                }
            }
            
            // Check if input is a number
            try {
                int number = Integer.parseInt(input);
                if (number >= 1 && number <= question.getOptions().size()) {
                    return question.getOptions().get(number - 1);
                } else {
                    System.out.println("✗ Invalid option number. Please try again.");
                }
            } catch (NumberFormatException e) {
                // Not a number, treat as full text answer
                return input;
            }
        }
    }
    
    /**
     * Gets answer for true/false question
     * @param scanner Scanner object
     * @return "True" or "False", or null if skipped
     */
    private static String getTrueFalseAnswer(Scanner scanner) {
        while (true) {
            System.out.print("\nYour answer (True/False or 'skip'): ");
            String input = scanner.nextLine().trim();
            
            if (input.equalsIgnoreCase("skip")) {
                System.out.println("Question skipped.");
                return null;
            }
            
            if (input.isEmpty()) {
                System.out.println("✗ Answer cannot be empty. Please try again.");
                continue;
            }
            
            String normalized = input.toLowerCase();
            switch (normalized) {
                case "true", "t", "1", "a" -> {
                    return "True";
                }
                case "false", "f", "0", "b" -> {
                    return "False";
                }
                default -> System.out.println("✗ Invalid answer. Please enter True or False.");
            }
        }
    }
    
    /**
     * Gets answer for short answer question
     * @param scanner Scanner object
     * @return The answer text, or null if skipped
     */
    private static String getShortAnswer(Scanner scanner) {
        while (true) {
            System.out.print("\nYour answer (or 'skip'): ");
            String input = scanner.nextLine().trim();
            
            if (input.equalsIgnoreCase("skip")) {
                System.out.println("Question skipped.");
                return null;
            }
            
            if (input.isEmpty()) {
                System.out.println("✗ Answer cannot be empty. Please try again.");
                continue;
            }
            
            return input;
        }
    }
    
    /**
     * Formats time in seconds to readable format
     * @param seconds Time in seconds
     * @return Formatted time string
     */
    private static String formatTime(int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int remainingSeconds = seconds % 60;
        
        if (hours > 0) {
            return String.format("%dh %dm %ds", hours, minutes, remainingSeconds);
        } else if (minutes > 0) {
            return String.format("%dm %ds", minutes, remainingSeconds);
        } else {
            return String.format("%ds", seconds);
        }
    }
    
    /**
     * Clears the console screen (simulated)
     */
    private static void clearScreen() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
}
