package src.util;

import src.model.Questions;
import src.model.Quiz;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Validator class provides validation methods for various inputs in the Quiz Maker application.
 * Demonstrates input validation and error handling best practices.
 */
public class Validator {
    
    // Regular expression patterns for validation
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    private static final Pattern NAME_PATTERN = Pattern.compile(
        "^[A-Za-z\\s]{2,50}$"
    );
    
    private static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile(
        "^[A-Za-z0-9\\s]{1,100}$"
    );
    
    // Validation constants
    private static final int MIN_QUESTION_LENGTH = 5;
    private static final int MAX_QUESTION_LENGTH = 500;
    private static final int MIN_ANSWER_LENGTH = 1;
    private static final int MAX_ANSWER_LENGTH = 200;
    private static final int MIN_MARKS = 1;
    private static final int MAX_MARKS = 100;
    private static final int MIN_OPTIONS = 2;
    private static final int MAX_OPTIONS = 10;
    private static final int MIN_TIME_LIMIT = 0;
    private static final int MAX_TIME_LIMIT = 300; // 5 hours
    
    // ==================== String Validation ====================
    
    /**
     * Validates if a string is not null and not empty
     * @param str The string to validate
     * @return true if valid, false otherwise
     */
    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }
    
    /**
     * Validates if a string is not null, not empty, and within length limits
     * @param str The string to validate
     * @param minLength Minimum length
     * @param maxLength Maximum length
     * @return true if valid, false otherwise
     */
    public static boolean isValidLength(String str, int minLength, int maxLength) {
        if (!isNotEmpty(str)) {
            return false;
        }
        int length = str.trim().length();
        return length >= minLength && length <= maxLength;
    }
    
    /**
     * Validates if a string contains only alphanumeric characters and spaces
     * @param str The string to validate
     * @return true if valid, false otherwise
     */
    public static boolean isAlphanumeric(String str) {
        return isNotEmpty(str) && ALPHANUMERIC_PATTERN.matcher(str).matches();
    }
    
    // ==================== Name Validation ====================
    
    /**
     * Validates a person's name
     * @param name The name to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidName(String name) {
        return isNotEmpty(name) && NAME_PATTERN.matcher(name).matches();
    }
    
    /**
     * Validates a person's name with error message
     * @param name The name to validate
     * @return Error message if invalid, null if valid
     */
    public static String validateName(String name) {
        if (!isNotEmpty(name)) {
            return "Name cannot be empty";
        }
        if (name.trim().length() < 2) {
            return "Name must be at least 2 characters long";
        }
        if (name.trim().length() > 50) {
            return "Name must not exceed 50 characters";
        }
        if (!NAME_PATTERN.matcher(name).matches()) {
            return "Name can only contain letters and spaces";
        }
        return null; // Valid
    }
    
    // ==================== Email Validation ====================
    
    /**
     * Validates an email address
     * @param email The email to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        return isNotEmpty(email) && EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * Validates an email address with error message
     * @param email The email to validate
     * @return Error message if invalid, null if valid
     */
    public static String validateEmail(String email) {
        if (!isNotEmpty(email)) {
            return "Email cannot be empty";
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return "Invalid email format";
        }
        return null; // Valid
    }
    
    // ==================== Number Validation ====================
    
    /**
     * Validates if a number is within a range
     * @param value The value to validate
     * @param min Minimum value (inclusive)
     * @param max Maximum value (inclusive)
     * @return true if valid, false otherwise
     */
    public static boolean isInRange(int value, int min, int max) {
        return value >= min && value <= max;
    }
    
    /**
     * Validates if a number is positive
     * @param value The value to validate
     * @return true if positive, false otherwise
     */
    public static boolean isPositive(int value) {
        return value > 0;
    }
    
    /**
     * Validates if a number is non-negative
     * @param value The value to validate
     * @return true if non-negative, false otherwise
     */
    public static boolean isNonNegative(int value) {
        return value >= 0;
    }
    
    /**
     * Parses a string to integer with validation
     * @param str The string to parse
     * @return The parsed integer, or null if invalid
     */
    public static Integer parseInteger(String str) {
        if (!isNotEmpty(str)) {
            return null;
        }
        try {
            return Integer.parseInt(str.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    /**
     * Validates integer input with error message
     * @param str The string to validate
     * @param min Minimum value
     * @param max Maximum value
     * @param fieldName Name of the field being validated
     * @return Error message if invalid, null if valid
     */
    public static String validateInteger(String str, int min, int max, String fieldName) {
        if (!isNotEmpty(str)) {
            return fieldName + " cannot be empty";
        }
        
        Integer value = parseInteger(str);
        if (value == null) {
            return fieldName + " must be a valid number";
        }
        
        if (!isInRange(value, min, max)) {
            return fieldName + " must be between " + min + " and " + max;
        }
        
        return null; // Valid
    }
    
    // ==================== Question Validation ====================
    
    /**
     * Validates question text
     * @param questionText The question text to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidQuestionText(String questionText) {
        return isValidLength(questionText, MIN_QUESTION_LENGTH, MAX_QUESTION_LENGTH);
    }
    
    /**
     * Validates question text with error message
     * @param questionText The question text to validate
     * @return Error message if invalid, null if valid
     */
    public static String validateQuestionText(String questionText) {
        if (!isNotEmpty(questionText)) {
            return "Question text cannot be empty";
        }
        if (questionText.trim().length() < MIN_QUESTION_LENGTH) {
            return "Question must be at least " + MIN_QUESTION_LENGTH + " characters long";
        }
        if (questionText.trim().length() > MAX_QUESTION_LENGTH) {
            return "Question must not exceed " + MAX_QUESTION_LENGTH + " characters";
        }
        if (!questionText.trim().endsWith("?") && !questionText.trim().endsWith(".")) {
            // Warning, not an error
            return null;
        }
        return null; // Valid
    }
    
    /**
     * Validates answer text
     * @param answer The answer to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidAnswer(String answer) {
        return isValidLength(answer, MIN_ANSWER_LENGTH, MAX_ANSWER_LENGTH);
    }
    
    /**
     * Validates answer text with error message
     * @param answer The answer to validate
     * @return Error message if invalid, null if valid
     */
    public static String validateAnswer(String answer) {
        if (!isNotEmpty(answer)) {
            return "Answer cannot be empty";
        }
        if (answer.trim().length() > MAX_ANSWER_LENGTH) {
            return "Answer must not exceed " + MAX_ANSWER_LENGTH + " characters";
        }
        return null; // Valid
    }
    
    /**
     * Validates marks for a question
     * @param marks The marks value to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidMarks(int marks) {
        return isInRange(marks, MIN_MARKS, MAX_MARKS);
    }
    
    /**
     * Validates marks with error message
     * @param marks The marks to validate
     * @return Error message if invalid, null if valid
     */
    public static String validateMarks(int marks) {
        if (!isInRange(marks, MIN_MARKS, MAX_MARKS)) {
            return "Marks must be between " + MIN_MARKS + " and " + MAX_MARKS;
        }
        return null; // Valid
    }
    
    /**
     * Validates a list of options for multiple choice questions
     * @param options The list of options to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidOptions(List<String> options) {
        if (options == null || options.size() < MIN_OPTIONS || options.size() > MAX_OPTIONS) {
            return false;
        }
        
        for (String option : options) {
            if (!isNotEmpty(option)) {
                return false;
            }
        }
        
        // Check for duplicate options
        for (int i = 0; i < options.size(); i++) {
            for (int j = i + 1; j < options.size(); j++) {
                if (options.get(i).trim().equalsIgnoreCase(options.get(j).trim())) {
                    return false; // Duplicate found
                }
            }
        }
        
        return true;
    }
    
    /**
     * Validates options with error message
     * @param options The list of options to validate
     * @return Error message if invalid, null if valid
     */
    public static String validateOptions(List<String> options) {
        if (options == null || options.isEmpty()) {
            return "Options list cannot be empty";
        }
        
        if (options.size() < MIN_OPTIONS) {
            return "Must have at least " + MIN_OPTIONS + " options";
        }
        
        if (options.size() > MAX_OPTIONS) {
            return "Cannot have more than " + MAX_OPTIONS + " options";
        }
        
        for (int i = 0; i < options.size(); i++) {
            if (!isNotEmpty(options.get(i))) {
                return "Option " + (i + 1) + " cannot be empty";
            }
        }
        
        // Check for duplicates
        for (int i = 0; i < options.size(); i++) {
            for (int j = i + 1; j < options.size(); j++) {
                if (options.get(i).trim().equalsIgnoreCase(options.get(j).trim())) {
                    return "Duplicate option found: " + options.get(i);
                }
            }
        }
        
        return null; // Valid
    }
    
    /**
     * Validates if correct answer is in the options list
     * @param correctAnswer The correct answer
     * @param options The list of options
     * @return true if valid, false otherwise
     */
    public static boolean isAnswerInOptions(String correctAnswer, List<String> options) {
        if (!isNotEmpty(correctAnswer) || options == null) {
            return false;
        }
        
        for (String option : options) {
            if (option.trim().equalsIgnoreCase(correctAnswer.trim())) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Validates a complete Question object
     * @param question The question to validate
     * @return List of validation errors (empty if valid)
     */
    public static List<String> validateQuestion(Questions question) {
        List<String> errors = new ArrayList<>();
        
        if (question == null) {
            errors.add("Question object is null");
            return errors;
        }
        
        // Validate question text
        String textError = validateQuestionText(question.getQuestionText());
        if (textError != null) {
            errors.add(textError);
        }
        
        // Validate marks
        String marksError = validateMarks(question.getMarks());
        if (marksError != null) {
            errors.add(marksError);
        }
        
        // Validate correct answer
        String answerError = validateAnswer(question.getCorrectAnswer());
        if (answerError != null) {
            errors.add(answerError);
        }
        
        // Validate options for multiple choice questions
        if (question.getQuestionType().equals("Multiple Choice")) {
            String optionsError = validateOptions(question.getOptions());
            if (optionsError != null) {
                errors.add(optionsError);
            } else if (!isAnswerInOptions(question.getCorrectAnswer(), question.getOptions())) {
                errors.add("Correct answer must be one of the options");
            }
        }
        
        return errors;
    }
    
    // ==================== Quiz Validation ====================
    
    /**
     * Validates quiz title
     * @param title The title to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidQuizTitle(String title) {
        return isValidLength(title, 3, 100);
    }
    
    /**
     * Validates quiz title with error message
     * @param title The title to validate
     * @return Error message if invalid, null if valid
     */
    public static String validateQuizTitle(String title) {
        if (!isNotEmpty(title)) {
            return "Quiz title cannot be empty";
        }
        if (title.trim().length() < 3) {
            return "Quiz title must be at least 3 characters long";
        }
        if (title.trim().length() > 100) {
            return "Quiz title must not exceed 100 characters";
        }
        return null; // Valid
    }
    
    /**
     * Validates quiz description
     * @param description The description to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidQuizDescription(String description) {
        // Description is optional, but if provided, should not exceed 500 chars
        if (description == null) {
            return true;
        }
        return description.length() <= 500;
    }
    
    /**
     * Validates time limit
     * @param timeLimit The time limit in minutes
     * @return true if valid, false otherwise
     */
    public static boolean isValidTimeLimit(int timeLimit) {
        return isInRange(timeLimit, MIN_TIME_LIMIT, MAX_TIME_LIMIT);
    }
    
    /**
     * Validates time limit with error message
     * @param timeLimit The time limit to validate
     * @return Error message if invalid, null if valid
     */
    public static String validateTimeLimit(int timeLimit) {
        if (!isInRange(timeLimit, MIN_TIME_LIMIT, MAX_TIME_LIMIT)) {
            return "Time limit must be between " + MIN_TIME_LIMIT + " and " + MAX_TIME_LIMIT + " minutes";
        }
        return null; // Valid
    }
    
    /**
     * Validates passing percentage
     * @param percentage The passing percentage to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPassingPercentage(int percentage) {
        return isInRange(percentage, 0, 100);
    }
    
    /**
     * Validates passing percentage with error message
     * @param percentage The percentage to validate
     * @return Error message if invalid, null if valid
     */
    public static String validatePassingPercentage(int percentage) {
        if (!isInRange(percentage, 0, 100)) {
            return "Passing percentage must be between 0 and 100";
        }
        return null; // Valid
    }
    
    /**
     * Validates a complete Quiz object
     * @param quiz The quiz to validate
     * @return List of validation errors (empty if valid)
     */
    public static List<String> validateQuiz(Quiz quiz) {
        List<String> errors = new ArrayList<>();
        
        if (quiz == null) {
            errors.add("Quiz object is null");
            return errors;
        }
        
        // Validate title
        String titleError = validateQuizTitle(quiz.getTitle());
        if (titleError != null) {
            errors.add(titleError);
        }
        
        // Validate that quiz has questions
        if (quiz.getQuestionCount() == 0) {
            errors.add("Quiz must have at least one question");
        }
        
        // Validate time limit
        String timeLimitError = validateTimeLimit(quiz.getTimeLimit());
        if (timeLimitError != null) {
            errors.add(timeLimitError);
        }
        
        // Validate passing percentage
        String percentageError = validatePassingPercentage(quiz.getPassingPercentage());
        if (percentageError != null) {
            errors.add(percentageError);
        }
        
        // Validate each question
        for (int i = 0; i < quiz.getQuestionCount(); i++) {
            Questions question = quiz.getQuestion(i);
            List<String> questionErrors = validateQuestion(question);
            
            for (String error : questionErrors) {
                errors.add("Question " + (i + 1) + ": " + error);
            }
        }
        
        return errors;
    }
    
    // ==================== True/False Validation ====================
    
    /**
     * Validates a True/False answer
     * @param answer The answer to validate
     * @return true if valid (True, False, T, F, 1, 0), false otherwise
     */
    public static boolean isValidTrueFalseAnswer(String answer) {
        if (!isNotEmpty(answer)) {
            return false;
        }
        
        String normalized = answer.trim().toLowerCase();
        return normalized.equals("true") || normalized.equals("false") ||
               normalized.equals("t") || normalized.equals("f") ||
               normalized.equals("1") || normalized.equals("0");
    }
    
    /**
     * Validates True/False answer with error message
     * @param answer The answer to validate
     * @return Error message if invalid, null if valid
     */
    public static String validateTrueFalseAnswer(String answer) {
        if (!isNotEmpty(answer)) {
            return "Answer cannot be empty";
        }
        
        if (!isValidTrueFalseAnswer(answer)) {
            return "Answer must be True or False";
        }
        
        return null; // Valid
    }
    
    // ==================== Utility Methods ====================
    
    /**
     * Checks if a string contains only whitespace
     * @param str The string to check
     * @return true if string is only whitespace, false otherwise
     */
    public static boolean isWhitespaceOnly(String str) {
        return str != null && str.trim().isEmpty() && !str.isEmpty();
    }
    
    /**
     * Sanitizes input by trimming and removing extra spaces
     * @param input The input to sanitize
     * @return Sanitized input
     */
    public static String sanitizeInput(String input) {
        if (input == null) {
            return "";
        }
        return input.trim().replaceAll("\\s+", " ");
    }
    
    /**
     * Validates if a list is not null and not empty
     * @param list The list to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidList(List<?> list) {
        return list != null && !list.isEmpty();
    }
    
    /**
     * Creates a summary of validation errors
     * @param errors List of error messages
     * @return Formatted error summary
     */
    public static String formatErrors(List<String> errors) {
        if (errors == null || errors.isEmpty()) {
            return "No errors";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Validation Errors (").append(errors.size()).append("):\n");
        
        for (int i = 0; i < errors.size(); i++) {
            sb.append(i + 1).append(". ").append(errors.get(i)).append("\n");
        }
        
        return sb.toString();
    }
}