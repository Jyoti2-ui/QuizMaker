package src.ui;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import src.model.*;
import src.util.Validator;

/**
 * QuizCreationUI provides user interface for creating and editing quizzes.
 * Handles all quiz creation interactions with the user.
 */
public class QuizCreationUI {
    
    /**
     * Creates a new quiz through interactive prompts
     * @param scanner Scanner object for user input
     * @return The created Quiz object, or null if cancelled
     */
    public static Quiz createNewQuiz(Scanner scanner) {
        System.out.println("\n═══════════════════════════════════════");
        System.out.println("        CREATE NEW QUIZ");
        System.out.println("═══════════════════════════════════════\n");
        
        // Get quiz title
        String title = getValidatedInput(scanner, "Enter quiz title: ", 
                                        input -> Validator.validateQuizTitle(input));
        if (title == null) return null;
        
        // Get quiz description
        System.out.print("Enter quiz description (optional): ");
        String description = scanner.nextLine().trim();
        
        // Get creator name
        String createdBy = getValidatedInput(scanner, "Enter your name: ",
                                            input -> Validator.validateName(input));
        if (createdBy == null) return null;
        
        // Get time limit
        Integer timeLimit = getIntInput(scanner, "Enter time limit in minutes (0 for no limit): ", 
                                       0, 300);
        if (timeLimit == null) return null;
        
        // Get passing percentage
        Integer passingPercentage = getIntInput(scanner, 
                                               "Enter passing percentage (0-100): ", 
                                               0, 100);
        if (passingPercentage == null) return null;
        
        // Create quiz object
        Quiz quiz = new Quiz(title, description, createdBy, timeLimit);
        quiz.setPassingPercentage(passingPercentage);
        
        System.out.println("\n✓ Quiz basics configured successfully!");
        
        // Add questions
        boolean addingQuestions = true;
        
        while (addingQuestions) {
            System.out.println("\n───────────────────────────────────────");
            System.out.println("Current quiz has " + quiz.getQuestionCount() + " question(s)");
            System.out.println("───────────────────────────────────────");
            
            System.out.println("\n1. Add Multiple Choice Question");
            System.out.println("2. Add True/False Question");
            System.out.println("3. Add Short Answer Question");
            System.out.println("4. Finish and Save Quiz");
            System.out.println("5. Cancel Quiz Creation");
            
            Integer choice = getIntInput(scanner, "\nEnter your choice: ", 1, 5);
            if (choice == null) continue;
            
            switch (choice) {
                case 1 -> {
                    Questions mcq = createMultipleChoiceQuestion(scanner);
                    if (mcq != null && quiz.addQuestion(mcq)) {
                        System.out.println("✓ Multiple choice question added!");
                    }
                }
                case 2 -> {
                    Questions tfq = createTrueFalseQuestion(scanner);
                    if (tfq != null && quiz.addQuestion(tfq)) {
                        System.out.println("✓ True/False question added!");
                    }
                }
                case 3 -> {
                    Questions saq = createShortAnswerQuestion(scanner);
                    if (saq != null && quiz.addQuestion(saq)) {
                        System.out.println("✓ Short answer question added!");
                    }
                }
                case 4 -> {
                    if (quiz.getQuestionCount() == 0) {
                        System.out.println("✗ Quiz must have at least one question!");
                    } else {
                        addingQuestions = false;
                    }
                }
                case 5 -> {
                    System.out.print("Are you sure you want to cancel? (yes/no): ");
                    String confirm = scanner.nextLine().trim().toLowerCase();
                    if (confirm.equals("yes") || confirm.equals("y")) {
                        return null;
                    }
                }
            }
        }
        
        return quiz;
    }
    
    /**
     * Edits an existing quiz
     * @param quiz The quiz to edit
     * @param scanner Scanner object for user input
     * @return The edited Quiz object, or null if cancelled
     */
    public static Quiz editQuiz(Quiz quiz, Scanner scanner) {
        if (quiz == null) return null;
        
        System.out.println("\n═══════════════════════════════════════");
        System.out.println("          EDIT QUIZ");
        System.out.println("═══════════════════════════════════════\n");
        
        System.out.println("Current Quiz: " + quiz.getTitle());
        System.out.println("Questions: " + quiz.getQuestionCount());
        System.out.println();
        
        boolean editing = true;
        
        while (editing) {
            System.out.println("\n1. Edit Quiz Details");
            System.out.println("2. Add New Question");
            System.out.println("3. Edit Question");
            System.out.println("4. Delete Question");
            System.out.println("5. View All Questions");
            System.out.println("6. Save and Exit");
            System.out.println("7. Cancel Changes");
            
            Integer choice = getIntInput(scanner, "\nEnter your choice: ", 1, 7);
            if (choice == null) continue;
            
            switch (choice) {
                case 1 -> editQuizDetails(quiz, scanner);
                case 2 -> addQuestionToQuiz(quiz, scanner);
                case 3 -> editQuestionInQuiz(quiz, scanner);
                case 4 -> deleteQuestionFromQuiz(quiz, scanner);
                case 5 -> System.out.println("\n" + quiz.displayAllQuestions());
                case 6 -> editing = false;
                case 7 -> {
                    System.out.print("Discard all changes? (yes/no): ");
                    String confirm = scanner.nextLine().trim().toLowerCase();
                    if (confirm.equals("yes") || confirm.equals("y")) {
                        return null;
                    }
                }
            }
        }
        
        return quiz;
    }
    
    /**
     * Edits quiz details (title, description, etc.)
     */
    private static void editQuizDetails(Quiz quiz, Scanner scanner) {
        System.out.println("\nEdit Quiz Details");
        System.out.println("1. Edit Title");
        System.out.println("2. Edit Description");
        System.out.println("3. Edit Time Limit");
        System.out.println("4. Edit Passing Percentage");
        System.out.println("5. Back");
        
        Integer choice = getIntInput(scanner, "\nEnter your choice: ", 1, 5);
        if (choice == null) return;
        
        switch (choice) {
            case 1 -> {
                String title = getValidatedInput(scanner, "Enter new title: ",
                        input -> Validator.validateQuizTitle(input));
                if (title != null) {
                    quiz.setTitle(title);
                    System.out.println("✓ Title updated!");
                }
            }
            case 2 -> {
                System.out.print("Enter new description: ");
                String desc = scanner.nextLine().trim();
                quiz.setDescription(desc);
                System.out.println("✓ Description updated!");
            }
            case 3 -> {
                Integer timeLimit = getIntInput(scanner, "Enter new time limit (minutes): ", 0, 300);
                if (timeLimit != null) {
                    quiz.setTimeLimit(timeLimit);
                    System.out.println("✓ Time limit updated!");
                }
            }
            case 4 -> {
                Integer percentage = getIntInput(scanner, "Enter new passing percentage: ", 0, 100);
                if (percentage != null) {
                    quiz.setPassingPercentage(percentage);
                    System.out.println("✓ Passing percentage updated!");
                }
            }
        }
    }
    
    /**
     * Adds a new question to existing quiz
     */
    private static void addQuestionToQuiz(Quiz quiz, Scanner scanner) {
        System.out.println("\nAdd Question");
        System.out.println("1. Multiple Choice");
        System.out.println("2. True/False");
        System.out.println("3. Short Answer");
        System.out.println("4. Cancel");
        
        Integer choice = getIntInput(scanner, "\nSelect question type: ", 1, 4);
        if (choice == null || choice == 4) return;
        
        Questions question = null;
        
        switch (choice) {
            case 1 -> question = createMultipleChoiceQuestion(scanner);
            case 2 -> question = createTrueFalseQuestion(scanner);
            case 3 -> question = createShortAnswerQuestion(scanner);
        }
        
        if (question != null && quiz.addQuestion(question)) {
            System.out.println("✓ Question added successfully!");
        } else {
            System.out.println("✗ Failed to add question.");
        }
    }
    
    /**
     * Edits a question in the quiz
     */
    private static void editQuestionInQuiz(Quiz quiz, Scanner scanner) {
        if (quiz.getQuestionCount() == 0) {
            System.out.println("No questions to edit.");
            return;
        }
        
        System.out.println("\nSelect question to edit:");
        for (int i = 0; i < quiz.getQuestionCount(); i++) {
            System.out.println((i + 1) + ". " + quiz.addQuestion(i).getQuestionText());
        }
        
        Integer choice = getIntInput(scanner, "\nEnter question number: ", 1, quiz.getQuestionCount());
        if (choice == null) return;
        
        Questions oldQuestion = quiz.addQuestion(choice - 1);
        Questions newQuestion = null;
        
        String type = oldQuestion.getQuestionType();
        
        System.out.println("\nEditing: " + oldQuestion.getQuestionText());
        System.out.println("Type: " + type);
        
        switch (type) {
            case "Multiple Choice" -> newQuestion = createMultipleChoiceQuestion(scanner);
            case "True/False" -> newQuestion = createTrueFalseQuestion(scanner);
            case "Short Answer" -> newQuestion = createShortAnswerQuestion(scanner);
            default -> {
            }
        }
        
        if (newQuestion != null && quiz.updateQuestion(choice - 1, newQuestion)) {
            System.out.println("✓ Question updated successfully!");
        } else {
            System.out.println("✗ Failed to update question.");
        }
    }
    
    /**
     * Deletes a question from the quiz
     */
    private static void deleteQuestionFromQuiz(Quiz quiz, Scanner scanner) {
        if (quiz.getQuestionCount() == 0) {
            System.out.println("No questions to delete.");
            return;
        }
        
        System.out.println("\nSelect question to delete:");
        for (int i = 0; i < quiz.getQuestionCount(); i++) {
            System.out.println((i + 1) + ". " + quiz.addQuestion(i).getQuestionText());
        }
        
        Integer choice = getIntInput(scanner, "\nEnter question number (0 to cancel): ", 
                                    0, quiz.getQuestionCount());
        if (choice == null || choice == 0) return;
        
        System.out.print("Are you sure you want to delete this question? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        
        if (confirm.equals("yes") || confirm.equals("y")) {
            if (quiz.addQuestion(choice - 1) != null) {
                System.out.println("✓ Question deleted successfully!");
            } else {
                System.out.println("✗ Failed to delete question.");
            }
        }
    }
    
    /**
     * Creates a multiple choice question
     * @param scanner Scanner object for user input
     * @return The created question, or null if cancelled
     */
    @SuppressWarnings("UseSpecificCatch")
    private static Questions createMultipleChoiceQuestion(Scanner scanner) {
        System.out.println("\n─── Multiple Choice Question ───");
        
        // Get question text
        String questionText = getValidatedInput(scanner, "Enter question: ",
                                               input -> Validator.validateQuestionText(input));
        if (questionText == null) return null;
        
        // Get marks
        Integer marks = getIntInput(scanner, "Enter marks for this question: ", 1, 100);
        if (marks == null) return null;
        
        // Get options
        List<String> options = new ArrayList<>();
        
        System.out.println("\nEnter answer options (minimum 2, maximum 10):");
        System.out.println("Type 'done' when finished adding options");
        
        int optionNum = 1;
        while (options.size() < 10) {
            System.out.print("Option " + optionNum + ": ");
            String option = scanner.nextLine().trim();
            
            if (option.equalsIgnoreCase("done")) {
                if (options.size() < 2) {
                    System.out.println("You must enter at least 2 options!");
                    continue;
                }
                break;
            }
            
            if (Validator.isNotEmpty(option)) {
                options.add(option);
                optionNum++;
            } else {
                System.out.println("Option cannot be empty!");
            }
        }
        
        // Display options
        System.out.println("\nOptions:");
        for (int i = 0; i < options.size(); i++) {
            System.out.println((i + 1) + ". " + options.get(i));
        }
        
        // Get correct answer
        Integer correctIndex = getIntInput(scanner, 
                                          "\nEnter the number of the correct answer: ", 
                                          1, options.size());
        if (correctIndex == null) return null;
        
        String correctAnswer = options.get(correctIndex - 1);
        
        // Create and return question using reflection to access package-private class
        try {
            Class<?> mcqClass = Class.forName("src.model.MultipleChoiceQuestion");
            java.lang.reflect.Constructor<?> constructor = mcqClass.getDeclaredConstructor(
                String.class, int.class, List.class, String.class);
            constructor.setAccessible(true);
            return (Questions) constructor.newInstance(questionText, marks, options, correctAnswer);
        } catch (Exception e) {
            System.err.println("Error creating question: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Creates a true/false question
     * @param scanner Scanner object for user input
     * @return The created question, or null if cancelled
     */
    @SuppressWarnings("UseSpecificCatch")
    private static Questions createTrueFalseQuestion(Scanner scanner) {
        System.out.println("\n─── True/False Question ───");
        
        // Get question text
        String questionText = getValidatedInput(scanner, "Enter question: ",
                                               input -> Validator.validateQuestionText(input));
        if (questionText == null) return null;
        
        // Get marks
        Integer marks = getIntInput(scanner, "Enter marks for this question: ", 1, 100);
        if (marks == null) return null;
        
        // Get correct answer
        System.out.println("\n1. True");
        System.out.println("2. False");
        
        Integer choice = getIntInput(scanner, "\nSelect the correct answer: ", 1, 2);
        if (choice == null) return null;
        
        String correctAnswer = (choice == 1) ? "True" : "False";
        
        // Create and return question using reflection
        try {
            Class<?> tfqClass = Class.forName("src.model.TrueFalseQuestion");
            java.lang.reflect.Constructor<?> constructor = tfqClass.getDeclaredConstructor(
                String.class, int.class, String.class);
            constructor.setAccessible(true);
            return (Questions) constructor.newInstance(questionText, marks, correctAnswer);
        } catch (Exception e) {
            System.err.println("Error creating question: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Creates a short answer question
     * @param scanner Scanner object for user input
     * @return The created question, or null if cancelled
     */
    private static Questions createShortAnswerQuestion(Scanner scanner) {
        System.out.println("\n─── Short Answer Question ───");
        
        // Get question text
        String questionText = getValidatedInput(scanner, "Enter question: ",
                                               input -> Validator.validateQuestionText(input));
        if (questionText == null) return null;
        
        // Get marks
        Integer marks = getIntInput(scanner, "Enter marks for this question: ", 1, 100);
        if (marks == null) return null;
        
        // Get correct answer
        String correctAnswer = getValidatedInput(scanner, "Enter the correct answer: ",
                                                input -> Validator.validateAnswer(input));
        if (correctAnswer == null) return null;
        
        // Ask about case sensitivity
        System.out.print("\nShould the answer be case-sensitive? (yes/no): ");
        String caseResponse = scanner.nextLine().trim().toLowerCase();
        boolean caseSensitive = caseResponse.equals("yes") || caseResponse.equals("y");
        
        // Create and return question using reflection
        try {
            Class<?> saqClass = Class.forName("src.model.ShortAnswerQuestion");
            java.lang.reflect.Constructor<?> constructor = saqClass.getDeclaredConstructor(
                String.class, int.class, String.class, boolean.class);
            constructor.setAccessible(true);
            return (Questions) constructor.newInstance(questionText, marks, correctAnswer, caseSensitive);
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            System.err.println("Error creating question: " + e.getMessage());
            return null;
        }
    }
    
    // ==================== Utility Methods ====================
    
    /**
     * Gets validated string input from user
     * @param scanner Scanner object
     * @param prompt Prompt message
     * @param validator Validation function that returns error message or null
     * @return Valid input string, or null if cancelled
     */
    private static String getValidatedInput(Scanner scanner, String prompt, 
                                           java.util.function.Function<String, String> validator) {
        int attempts = 0;
        final int MAX_ATTEMPTS = 3;
        
        while (attempts < MAX_ATTEMPTS) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            
            if (input.equalsIgnoreCase("cancel")) {
                System.out.println("Input cancelled.");
                return null;
            }
            
            String error = validator.apply(input);
            if (error == null) {
                return input;
            } else {
                System.out.println("✗ " + error);
                attempts++;
                
                if (attempts < MAX_ATTEMPTS) {
                    System.out.println("Please try again (type 'cancel' to cancel)");
                } else {
                    System.out.println("Too many invalid attempts. Operation cancelled.");
                    return null;
                }
            }
        }
        
        return null;
    }
    
    /**
     * Gets integer input from user with validation
     * @param scanner Scanner object
     * @param prompt Prompt message
     * @param min Minimum valid value
     * @param max Maximum valid value
     * @return Valid integer, or null if cancelled
     */
    private static Integer getIntInput(Scanner scanner, String prompt, int min, int max) {
        int attempts = 0;
        final int MAX_ATTEMPTS = 3;
        
        while (attempts < MAX_ATTEMPTS) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            
            if (input.equalsIgnoreCase("cancel")) {
                System.out.println("Input cancelled.");
                return null;
            }
            
            try {
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.println("✗ Please enter a number between " + min + " and " + max);
                }
            } catch (NumberFormatException e) {
                System.out.println("✗ Invalid number format. Please enter a valid integer.");
            }
            
            attempts++;
            if (attempts < MAX_ATTEMPTS) {
                System.out.println("Please try again (type 'cancel' to cancel)");
            } else {
                System.out.println("Too many invalid attempts. Operation cancelled.");
                return null;
            }
        }
        
        return null;
    }
}
