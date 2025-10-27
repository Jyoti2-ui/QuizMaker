package src.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Quiz class represents a complete quiz with multiple questions.
 * Demonstrates Encapsulation and composition (has-a relationship with Questions).
 * Implements Serializable for file handling support.
 */
public class Quiz implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Private fields - Encapsulation
    private String quizId;
    private String title;
    private String description;
    private String createdBy;
    private Date dateCreated;
    private Date lastModified;
    private int timeLimit; // in minutes, 0 means no time limit
    private boolean isActive;
    private List<Questions> questions;
    private int passingPercentage;
    
    /**
     * Default constructor
     */
    public Quiz() {
        this.quizId = generateQuizId();
        this.title = "Untitled Quiz";
        this.description = "";
        this.createdBy = "Anonymous";
        this.dateCreated = new Date();
        this.lastModified = new Date();
        this.timeLimit = 0;
        this.isActive = true;
        this.questions = new ArrayList<>();
        this.passingPercentage = 50;
    }
    
    /**
     * Parameterized constructor
     * @param title The title of the quiz
     * @param description The description of the quiz
     */
    public Quiz(String title, String description) {
        this();
        this.title = title;
        this.description = description;
    }
    
    /**
     * Full parameterized constructor
     * @param title The title of the quiz
     * @param description The description of the quiz
     * @param createdBy The creator's name
     * @param timeLimit Time limit in minutes
     */
    public Quiz(String title, String description, String createdBy, int timeLimit) {
        this();
        this.title = title;
        this.description = description;
        this.createdBy = createdBy;
        this.timeLimit = timeLimit;
    }
    
    // Getters and Setters (Encapsulation)
    
    public String getQuizId() {
        return quizId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        if (title != null && !title.trim().isEmpty()) {
            this.title = title;
            this.lastModified = new Date();
        }
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
        this.lastModified = new Date();
    }
    
    public String getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    public Date getDateCreated() {
        return dateCreated;
    }
    
    public Date getLastModified() {
        return lastModified;
    }
    
    public int getTimeLimit() {
        return timeLimit;
    }
    
    public void setTimeLimit(int timeLimit) {
        if (timeLimit >= 0) {
            this.timeLimit = timeLimit;
            this.lastModified = new Date();
        }
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
        this.lastModified = new Date();
    }
    
    public int getPassingPercentage() {
        return passingPercentage;
    }
    
    public void setPassingPercentage(int passingPercentage) {
        if (passingPercentage >= 0 && passingPercentage <= 100) {
            this.passingPercentage = passingPercentage;
            this.lastModified = new Date();
        }
    }
    
    /**
     * Gets an unmodifiable list of questions
     * @return List of questions
     */
    public List<Questions> getQuestions() {
        return new ArrayList<>(questions);
    }
    
    /**
     * Gets a specific question by index
     * @param index The index of the question
     * @return The question at the specified index, or null if index is invalid
     */
    public Questions getQuestion(int index) {
        if (index >= 0 && index < questions.size()) {
            return questions.get(index);
        }
        return null;
    }
    
    // Question Management Methods
    
    /**
     * Adds a question to the quiz
     * @param question The question to add
     * @return true if question was added successfully, false otherwise
     */
    public boolean addQuestion(Questions question) {
        if (question != null && question.isValid()) {
            boolean added = questions.add(question);
            if (added) {
                this.lastModified = new Date();
            }
            return added;
        }
        return false;
    }
    
    /**
     * Adds a question at a specific position
     * @param index The position to insert the question
     * @param question The question to add
     * @return true if question was added successfully, false otherwise
     */
    public boolean addQuestionAt(int index, Questions question) {
        if (question != null && question.isValid() && index >= 0 && index <= questions.size()) {
            questions.add(index, question);
            this.lastModified = new Date();
            return true;
        }
        return false;
    }
    
    /**
     * Removes a question from the quiz
     * @param question The question to remove
     * @return true if question was removed successfully, false otherwise
     */
    public boolean removeQuestion(Questions question) {
        boolean removed = questions.remove(question);
        if (removed) {
            this.lastModified = new Date();
        }
        return removed;
    }
    
    /**
     * Removes a question at a specific index
     * @param index The index of the question to remove
     * @return The removed question, or null if index is invalid
     */
    public Questions removeQuestionAt(int index) {
        if (index >= 0 && index < questions.size()) {
            Questions removed = questions.remove(index);
            this.lastModified = new Date();
            return removed;
        }
        return null;
    }
    
    /**
     * Updates a question at a specific index
     * @param index The index of the question to update
     * @param newQuestion The new question to replace the old one
     * @return true if question was updated successfully, false otherwise
     */
    public boolean updateQuestion(int index, Questions newQuestion) {
        if (index >= 0 && index < questions.size() && newQuestion != null && newQuestion.isValid()) {
            questions.set(index, newQuestion);
            this.lastModified = new Date();
            return true;
        }
        return false;
    }
    
    /**
     * Clears all questions from the quiz
     */
    public void clearQuestions() {
        questions.clear();
        this.lastModified = new Date();
    }
    
    /**
     * Gets the total number of questions in the quiz
     * @return The number of questions
     */
    public int getQuestionCount() {
        return questions.size();
    }
    
    /**
     * Checks if the quiz has any questions
     * @return true if quiz has at least one question, false otherwise
     */
    public boolean hasQuestions() {
        return !questions.isEmpty();
    }
    
    /**
     * Shuffles the order of questions in the quiz
     */
    public void shuffleQuestions() {
        Collections.shuffle(questions);
        this.lastModified = new Date();
    }
    
    // Calculation Methods
    
    /**
     * Calculates the total marks for the quiz
     * @return The sum of all question marks
     */
    public int getTotalMarks() {
        int total = 0;
        for (Questions question : questions) {
            total += question.getMarks();
        }
        return total;
    }
    
    /**
     * Calculates the passing marks based on passing percentage
     * @return The minimum marks required to pass
     */
    public int getPassingMarks() {
        return (int) Math.ceil((getTotalMarks() * passingPercentage) / 100.0);
    }
    
    /**
     * Gets the average marks per question
     * @return The average marks, or 0 if no questions
     */
    public double getAverageMarksPerQuestion() {
        if (questions.isEmpty()) {
            return 0.0;
        }
        return (double) getTotalMarks() / questions.size();
    }
    
    // Question Type Statistics
    
    /**
     * Counts questions by type
     * @param questionType The type of question to count
     * @return The number of questions of the specified type
     */
    public int countQuestionsByType(String questionType) {
        int count = 0;
        for (Questions question : questions) {
            if (question.getQuestionType().equalsIgnoreCase(questionType)) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Gets all questions of a specific type
     * @param questionType The type of questions to retrieve
     * @return List of questions of the specified type
     */
    public List<Questions> getQuestionsByType(String questionType) {
        List<Questions> result = new ArrayList<>();
        for (Questions question : questions) {
            if (question.getQuestionType().equalsIgnoreCase(questionType)) {
                result.add(question);
            }
        }
        return result;
    }
    
    // Validation Methods
    
    /**
     * Validates if the quiz is ready to be attempted
     * @return true if quiz is valid, false otherwise
     */
    public boolean isValid() {
        if (title == null || title.trim().isEmpty()) {
            return false;
        }
        if (questions.isEmpty()) {
            return false;
        }
        for (Questions question : questions) {
            if (!question.isValid()) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Gets a list of validation errors
     * @return List of error messages, empty if valid
     */
    public List<String> getValidationErrors() {
        List<String> errors = new ArrayList<>();
        
        if (title == null || title.trim().isEmpty()) {
            errors.add("Quiz title is required");
        }
        
        if (questions.isEmpty()) {
            errors.add("Quiz must have at least one question");
        } else {
            for (int i = 0; i < questions.size(); i++) {
                Questions q = questions.get(i);
                if (!q.isValid()) {
                    errors.add("Question " + (i + 1) + " is invalid");
                }
            }
        }
        
        return errors;
    }
    
    // Display Methods
    
    /**
     * Displays all questions in the quiz
     * @return A formatted string of all questions
     */
    public String displayAllQuestions() {
        if (questions.isEmpty()) {
            return "No questions in this quiz.";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Quiz: ").append(title).append("\n");
        sb.append("Description: ").append(description).append("\n");
        sb.append("Total Questions: ").append(questions.size()).append("\n");
        sb.append("Total Marks: ").append(getTotalMarks()).append("\n");
        sb.append("Passing Marks: ").append(getPassingMarks()).append("\n");
        if (timeLimit > 0) {
            sb.append("Time Limit: ").append(timeLimit).append(" minutes\n");
        }
        sb.append("\n");
        
        for (int i = 0; i < questions.size(); i++) {
            sb.append("Question ").append(i + 1).append(":\n");
            sb.append(questions.get(i).displayQuestion());
            sb.append("\n");
        }
        
        return sb.toString();
    }
    
    /**
     * Gets quiz summary information
     * @return A summary string with key quiz details
     */
    public String getQuizSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("Title: ").append(title).append("\n");
        sb.append("Created by: ").append(createdBy).append("\n");
        sb.append("Date Created: ").append(dateCreated).append("\n");
        sb.append("Total Questions: ").append(questions.size()).append("\n");
        sb.append("Total Marks: ").append(getTotalMarks()).append("\n");
        sb.append("Passing Percentage: ").append(passingPercentage).append("%\n");
        
        if (timeLimit > 0) {
            sb.append("Time Limit: ").append(timeLimit).append(" minutes\n");
        }
        
        sb.append("Multiple Choice Questions: ").append(countQuestionsByType("Multiple Choice")).append("\n");
        sb.append("True/False Questions: ").append(countQuestionsByType("True/False")).append("\n");
        sb.append("Short Answer Questions: ").append(countQuestionsByType("Short Answer")).append("\n");
        
        return sb.toString();
    }
    
    /**
     * Creates a copy of this quiz
     * @return A new Quiz object with the same data
     */
    public Quiz clone() {
        Quiz clonedQuiz = new Quiz(this.title, this.description, this.createdBy, this.timeLimit);
        clonedQuiz.passingPercentage = this.passingPercentage;
        clonedQuiz.isActive = this.isActive;
        
        for (Questions question : this.questions) {
            clonedQuiz.addQuestion(question.clone());
        }
        
        return clonedQuiz;
    }
    
    // Utility Methods
    
    /**
     * Generates a unique quiz ID
     * @return A unique quiz identifier
     */
    private String generateQuizId() {
        return "QUIZ_" + System.currentTimeMillis();
    }
    
    /**
     * Gets the estimated time to complete the quiz
     * @param secondsPerQuestion Average time per question
     * @return Estimated time in minutes
     */
    public int getEstimatedTime(int secondsPerQuestion) {
        return (questions.size() * secondsPerQuestion) / 60;
    }
    
    /**
     * Checks if quiz has a time limit
     * @return true if time limit is set, false otherwise
     */
    public boolean hasTimeLimit() {
        return timeLimit > 0;
    }
    
    @Override
    public String toString() {
        return String.format("Quiz[id=%s, title=%s, questions=%d, totalMarks=%d]",
                            quizId, title, questions.size(), getTotalMarks());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Quiz quiz = (Quiz) obj;
        return quizId.equals(quiz.quizId);
    }
    
    @Override
    public int hashCode() {
        return quizId.hashCode();
    }
}