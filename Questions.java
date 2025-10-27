package src.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for all question types in the Quiz Maker application.
 * Demonstrates Abstraction and Encapsulation principles.
 * Implements Serializable for file handling support.
 */
public abstract class Questions implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Protected fields - accessible to subclasses (Encapsulation)
    protected String questionText;
    protected int marks;
    protected String correctAnswer;
    
    /**
     * Default constructor
     */
    public Questions() {
        this.questionText = "";
        this.marks = 1;
        this.correctAnswer = "";
    }
    
    /**
     * Parameterized constructor
     * @param questionText The text of the question
     * @param marks The marks assigned to this question
     * @param correctAnswer The correct answer for this question
     */
    public Questions(String questionText, int marks, String correctAnswer) {
        this.questionText = questionText;
        this.marks = marks;
        this.correctAnswer = correctAnswer;
    }
    
    // Getters and Setters (Encapsulation)
    
    /**
     * Gets the question text
     * @return The question text
     */
    public String getQuestionText() {
        return questionText;
    }
    
    /**
     * Sets the question text
     * @param questionText The question text to set
     */
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }
    
    /**
     * Gets the marks for this question
     * @return The marks value
     */
    public int getMarks() {
        return marks;
    }
    
    /**
     * Sets the marks for this question
     * @param marks The marks to set (must be positive)
     */
    public void setMarks(int marks) {
        if (marks > 0) {
            this.marks = marks;
        } else {
            this.marks = 1; // Default to 1 if invalid
        }
    }
    
    /**
     * Gets the correct answer
     * @return The correct answer
     */
    public String getCorrectAnswer() {
        return correctAnswer;
    }
    
    /**
     * Sets the correct answer
     * @param correctAnswer The correct answer to set
     */
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
    
    // Abstract methods - must be implemented by subclasses (Abstraction)
    
    /**
     * Abstract method to get the type of question
     * @return A string representing the question type (e.g., "Multiple Choice", "True/False")
     */
    public abstract String getQuestionType();
    
    /**
     * Abstract method to check if the user's answer is correct
     * @param userAnswer The answer provided by the user
     * @return true if the answer is correct, false otherwise
     */
    public abstract boolean checkAnswer(String userAnswer);
    
    /**
     * Abstract method to get all available options for the question
     * @return List of answer options
     */
    public abstract List<String> getOptions();
    
    /**
     * Abstract method to display question details
     * @return A formatted string representation of the question
     */
    public abstract String displayQuestion();
    
    /**
     * Validates if the question is properly configured
     * @return true if question is valid, false otherwise
     */
    public boolean isValid() {
        return questionText != null && !questionText.trim().isEmpty() 
                && marks > 0 
                && correctAnswer != null && !correctAnswer.trim().isEmpty();
    }
    
    /**
     * Creates a copy of the question
     * @return A new Questions object with the same data
     */
    public abstract Questions clone();
    
    @Override
    public String toString() {
        return String.format("[%s] %s (Marks: %d)", 
                            getQuestionType(), questionText, marks);
    }
}


/**
 * Multiple Choice Question class - demonstrates Inheritance and Polymorphism
 */
class MultipleChoiceQuestion extends Questions {
    
    private static final long serialVersionUID = 1L;
    
    private List<String> options;
    
    /**
     * Default constructor
     */
    public MultipleChoiceQuestion() {
        super();
        this.options = new ArrayList<>();
    }
    
    /**
     * Parameterized constructor
     * @param questionText The question text
     * @param marks The marks for this question
     * @param options List of answer options
     * @param correctAnswer The correct answer (must be one of the options)
     */
    public MultipleChoiceQuestion(String questionText, int marks, 
                                  List<String> options, String correctAnswer) {
        super(questionText, marks, correctAnswer);
        this.options = new ArrayList<>(options);
    }
    
    /**
     * Gets the list of options
     * @return List of answer options
     */
    @Override
    public List<String> getOptions() {
        return new ArrayList<>(options);
    }
    
    /**
     * Sets the options for this question
     * @param options List of options to set
     */
    public void setOptions(List<String> options) {
        this.options = new ArrayList<>(options);
    }
    
    /**
     * Adds a single option to the list
     * @param option The option to add
     */
    public void addOption(String option) {
        if (option != null && !option.trim().isEmpty()) {
            this.options.add(option);
        }
    }
    
    /**
     * Removes an option from the list
     * @param option The option to remove
     */
    public void removeOption(String option) {
        this.options.remove(option);
    }
    
    @Override
    public String getQuestionType() {
        return "Multiple Choice";
    }
    
    @Override
    public boolean checkAnswer(String userAnswer) {
        if (userAnswer == null || correctAnswer == null) {
            return false;
        }
        return userAnswer.trim().equalsIgnoreCase(correctAnswer.trim());
    }
    
    @Override
    public String displayQuestion() {
        StringBuilder sb = new StringBuilder();
        sb.append("Q. ").append(questionText).append("\n");
        sb.append("Marks: ").append(marks).append("\n");
        sb.append("Options:\n");
        
        char optionLabel = 'A';
        for (String option : options) {
            sb.append("  ").append(optionLabel++).append(") ").append(option).append("\n");
        }
        
        return sb.toString();
    }
    
    @Override
    public boolean isValid() {
        return super.isValid() && options != null && options.size() >= 2
                && options.contains(correctAnswer);
    }
    
    @Override
    public Questions clone() {
        return new MultipleChoiceQuestion(this.questionText, this.marks, 
                                         this.options, this.correctAnswer);
    }
}


/**
 * True/False Question class - demonstrates Inheritance and Polymorphism
 */
class TrueFalseQuestion extends Questions {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Default constructor
     */
    public TrueFalseQuestion() {
        super();
    }
    
    /**
     * Parameterized constructor
     * @param questionText The question text
     * @param marks The marks for this question
     * @param correctAnswer The correct answer ("True" or "False")
     */
    public TrueFalseQuestion(String questionText, int marks, String correctAnswer) {
        super(questionText, marks, correctAnswer);
        // Validate and normalize the answer
        if (correctAnswer != null) {
            this.correctAnswer = normalizeAnswer(correctAnswer);
        }
    }
    
    /**
     * Normalizes the answer to "True" or "False"
     * @param answer The answer to normalize
     * @return "True" or "False"
     */
    private String normalizeAnswer(String answer) {
        if (answer == null) {
            return "True";
        }
        String normalized = answer.trim().toLowerCase();
        if (normalized.equals("true") || normalized.equals("t") || normalized.equals("1")) {
            return "True";
        } else {
            return "False";
        }
    }
    
    @Override
    public String getQuestionType() {
        return "True/False";
    }
    
    @Override
    public boolean checkAnswer(String userAnswer) {
        if (userAnswer == null) {
            return false;
        }
        String normalizedUser = normalizeAnswer(userAnswer);
        return normalizedUser.equals(correctAnswer);
    }
    
    @Override
    public List<String> getOptions() {
        List<String> options = new ArrayList<>();
        options.add("True");
        options.add("False");
        return options;
    }
    
    @Override
    public String displayQuestion() {
        StringBuilder sb = new StringBuilder();
        sb.append("Q. ").append(questionText).append("\n");
        sb.append("Marks: ").append(marks).append("\n");
        sb.append("Options:\n");
        sb.append("  A) True\n");
        sb.append("  B) False\n");
        return sb.toString();
    }
    
    @Override
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = normalizeAnswer(correctAnswer);
    }
    
    @Override
    public boolean isValid() {
        return super.isValid() && 
               (correctAnswer.equals("True") || correctAnswer.equals("False"));
    }
    
    @Override
    public Questions clone() {
        return new TrueFalseQuestion(this.questionText, this.marks, this.correctAnswer);
    }
}


/**
 * Short Answer Question class - for text-based answers
 */
class ShortAnswerQuestion extends Questions {
    
    private static final long serialVersionUID = 1L;
    
    private boolean caseSensitive;
    
    /**
     * Default constructor
     */
    public ShortAnswerQuestion() {
        super();
        this.caseSensitive = false;
    }
    
    /**
     * Parameterized constructor
     * @param questionText The question text
     * @param marks The marks for this question
     * @param correctAnswer The correct answer
     * @param caseSensitive Whether the answer matching should be case-sensitive
     */
    public ShortAnswerQuestion(String questionText, int marks, 
                              String correctAnswer, boolean caseSensitive) {
        super(questionText, marks, correctAnswer);
        this.caseSensitive = caseSensitive;
    }
    
    public boolean isCaseSensitive() {
        return caseSensitive;
    }
    
    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }
    
    @Override
    public String getQuestionType() {
        return "Short Answer";
    }
    
    @Override
    public boolean checkAnswer(String userAnswer) {
        if (userAnswer == null || correctAnswer == null) {
            return false;
        }
        
        String user = userAnswer.trim();
        String correct = correctAnswer.trim();
        
        if (caseSensitive) {
            return user.equals(correct);
        } else {
            return user.equalsIgnoreCase(correct);
        }
    }
    
    @Override
    public List<String> getOptions() {
        // Short answer questions don't have predefined options
        return new ArrayList<>();
    }
    
    @Override
    public String displayQuestion() {
        StringBuilder sb = new StringBuilder();
        sb.append("Q. ").append(questionText).append("\n");
        sb.append("Marks: ").append(marks).append("\n");
        sb.append("Type: Short Answer");
        if (caseSensitive) {
            sb.append(" (Case Sensitive)");
        }
        sb.append("\n");
        return sb.toString();
    }
    
    @Override
    public Questions clone() {
        return new ShortAnswerQuestion(this.questionText, this.marks, 
                                      this.correctAnswer, this.caseSensitive);
    }
}