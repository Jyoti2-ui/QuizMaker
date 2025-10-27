package src.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Result class stores the outcome of a quiz attempt by a student.
 * Demonstrates Encapsulation and composition.
 * Implements Serializable for file handling support.
 */
public class Result implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Private fields - Encapsulation
    private String resultId;
    private String quizId;
    private String quizTitle;
    private String studentName;
    private String studentId;
    private Date attemptDate;
    private int totalMarks;
    private int marksObtained;
    private double percentage;
    private boolean passed;
    private int timeTaken; // in seconds
    private Map<Integer, String> studentAnswers; // Question index -> Student's answer
    private Map<Integer, Boolean> answerCorrectness; // Question index -> Is correct
    private List<Questions> questions; // Store questions for reference
    
    /**
     * Default constructor
     */
    public Result() {
        this.resultId = generateResultId();
        this.quizId = "";
        this.quizTitle = "";
        this.studentName = "";
        this.studentId = "";
        this.attemptDate = new Date();
        this.totalMarks = 0;
        this.marksObtained = 0;
        this.percentage = 0.0;
        this.passed = false;
        this.timeTaken = 0;
        this.studentAnswers = new HashMap<>();
        this.answerCorrectness = new HashMap<>();
        this.questions = new ArrayList<>();
    }
    
    /**
     * Parameterized constructor
     * @param quiz The quiz that was attempted
     * @param studentName The student's name
     * @param studentId The student's ID
     */
    public Result(Quiz quiz, String studentName, String studentId) {
        this();
        if (quiz != null) {
            this.quizId = quiz.getQuizId();
            this.quizTitle = quiz.getTitle();
            this.totalMarks = quiz.getTotalMarks();
            this.questions = new ArrayList<>(quiz.getQuestions());
        }
        this.studentName = studentName;
        this.studentId = studentId;
    }
    
    // Getters and Setters
    
    public String getResultId() {
        return resultId;
    }
    
    public String getQuizId() {
        return quizId;
    }
    
    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }
    
    public String getQuizTitle() {
        return quizTitle;
    }
    
    public void setQuizTitle(String quizTitle) {
        this.quizTitle = quizTitle;
    }
    
    public String getStudentName() {
        return studentName;
    }
    
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    
    public String getStudentId() {
        return studentId;
    }
    
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    
    public Date getAttemptDate() {
        return attemptDate;
    }
    
    public void setAttemptDate(Date attemptDate) {
        this.attemptDate = attemptDate;
    }
    
    public int getTotalMarks() {
        return totalMarks;
    }
    
    public void setTotalMarks(int totalMarks) {
        this.totalMarks = totalMarks;
    }
    
    public int getMarksObtained() {
        return marksObtained;
    }
    
    public void setMarksObtained(int marksObtained) {
        this.marksObtained = marksObtained;
    }
    
    public double getPercentage() {
        return percentage;
    }
    
    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
    
    public boolean isPassed() {
        return passed;
    }
    
    public void setPassed(boolean passed) {
        this.passed = passed;
    }
    
    public int getTimeTaken() {
        return timeTaken;
    }
    
    public void setTimeTaken(int timeTaken) {
        this.timeTaken = timeTaken;
    }
    
    public Map<Integer, String> getStudentAnswers() {
        return new HashMap<>(studentAnswers);
    }
    
    public Map<Integer, Boolean> getAnswerCorrectness() {
        return new HashMap<>(answerCorrectness);
    }
    
    public List<Questions> getQuestions() {
        return new ArrayList<>(questions);
    }
    
    public void setQuestions(List<Questions> questions) {
        this.questions = new ArrayList<>(questions);
    }
    
    // Answer Management Methods
    
    /**
     * Records a student's answer for a question
     * @param questionIndex The index of the question
     * @param answer The student's answer
     */
    public void recordAnswer(int questionIndex, String answer) {
        studentAnswers.put(questionIndex, answer);
    }
    
    /**
     * Gets the student's answer for a specific question
     * @param questionIndex The index of the question
     * @return The student's answer, or null if not answered
     */
    public String getAnswer(int questionIndex) {
        return studentAnswers.get(questionIndex);
    }
    
    /**
     * Checks if a question was answered
     * @param questionIndex The index of the question
     * @return true if answered, false otherwise
     */
    public boolean isQuestionAnswered(int questionIndex) {
        return studentAnswers.containsKey(questionIndex);
    }
    
    /**
     * Gets the number of answered questions
     * @return Number of answered questions
     */
    public int getAnsweredCount() {
        return studentAnswers.size();
    }
    
    /**
     * Gets the number of unanswered questions
     * @return Number of unanswered questions
     */
    public int getUnansweredCount() {
        return questions.size() - studentAnswers.size();
    }
    
    // Calculation Methods
    
    /**
     * Calculates the result by checking all answers
     * @param passingPercentage The passing percentage from the quiz
     */
    public void calculateResult(int passingPercentage) {
        marksObtained = 0;
        answerCorrectness.clear();
        
        for (int i = 0; i < questions.size(); i++) {
            Questions question = questions.get(i);
            String studentAnswer = studentAnswers.get(i);
            
            if (studentAnswer != null) {
                boolean isCorrect = question.checkAnswer(studentAnswer);
                answerCorrectness.put(i, isCorrect);
                
                if (isCorrect) {
                    marksObtained += question.getMarks();
                }
            } else {
                answerCorrectness.put(i, false);
            }
        }
        
        // Calculate percentage
        if (totalMarks > 0) {
            percentage = (double) marksObtained / totalMarks * 100;
        } else {
            percentage = 0.0;
        }
        
        // Determine pass/fail
        passed = percentage >= passingPercentage;
    }
    
    /**
     * Gets the number of correct answers
     * @return Number of correct answers
     */
    public int getCorrectAnswersCount() {
        int count = 0;
        for (Boolean isCorrect : answerCorrectness.values()) {
            if (isCorrect) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Gets the number of incorrect answers
     * @return Number of incorrect answers
     */
    public int getIncorrectAnswersCount() {
        return getAnsweredCount() - getCorrectAnswersCount();
    }
    
    /**
     * Checks if answer for a specific question was correct
     * @param questionIndex The index of the question
     * @return true if correct, false otherwise
     */
    public boolean isAnswerCorrect(int questionIndex) {
        Boolean isCorrect = answerCorrectness.get(questionIndex);
        return isCorrect != null && isCorrect;
    }
    
    // Display Methods
    
    /**
     * Displays the complete result with all details
     * @return A formatted string of the result
     */
    public String displayResult() {
        StringBuilder sb = new StringBuilder();
        sb.append("========== QUIZ RESULT ==========\n");
        sb.append("Quiz: ").append(quizTitle).append("\n");
        sb.append("Student: ").append(studentName);
        if (studentId != null && !studentId.isEmpty()) {
            sb.append(" (ID: ").append(studentId).append(")");
        }
        sb.append("\n");
        sb.append("Date: ").append(attemptDate).append("\n");
        sb.append("Time Taken: ").append(formatTime(timeTaken)).append("\n");
        sb.append("\n");
        sb.append("Score: ").append(marksObtained).append(" / ").append(totalMarks).append("\n");
        sb.append("Percentage: ").append(String.format("%.2f", percentage)).append("%\n");
        sb.append("Result: ").append(passed ? "PASSED" : "FAILED").append("\n");
        sb.append("\n");
        sb.append("Correct Answers: ").append(getCorrectAnswersCount()).append(" / ").append(questions.size()).append("\n");
        sb.append("Incorrect Answers: ").append(getIncorrectAnswersCount()).append("\n");
        sb.append("Unanswered: ").append(getUnansweredCount()).append("\n");
        sb.append("================================\n");
        
        return sb.toString();
    }
    
    /**
     * Displays detailed answer breakdown
     * @return A formatted string showing each question and answer
     */
    public String displayDetailedResult() {
        StringBuilder sb = new StringBuilder();
        sb.append(displayResult());
        sb.append("\n========== DETAILED BREAKDOWN ==========\n\n");
        
        for (int i = 0; i < questions.size(); i++) {
            Questions question = questions.get(i);
            String studentAnswer = studentAnswers.get(i);
            Boolean isCorrect = answerCorrectness.get(i);
            
            sb.append("Question ").append(i + 1).append(":\n");
            sb.append(question.displayQuestion());
            
            sb.append("Your Answer: ");
            if (studentAnswer != null) {
                sb.append(studentAnswer);
            } else {
                sb.append("[Not Answered]");
            }
            sb.append("\n");
            
            sb.append("Correct Answer: ").append(question.getCorrectAnswer()).append("\n");
            
            sb.append("Status: ");
            if (isCorrect != null && isCorrect) {
                sb.append("✓ CORRECT (").append(question.getMarks()).append(" marks)\n");
            } else {
                sb.append("✗ INCORRECT (0 marks)\n");
            }
            
            sb.append("\n");
        }
        
        sb.append("========================================\n");
        return sb.toString();
    }
    
    /**
     * Gets a summary of the result
     * @return A brief summary string
     */
    public String getSummary() {
        return String.format("%s - %s: %.2f%% (%d/%d) - %s",
                            quizTitle, studentName, percentage, 
                            marksObtained, totalMarks, 
                            passed ? "PASSED" : "FAILED");
    }
    
    /**
     * Gets grade based on percentage
     * @return Letter grade (A, B, C, D, F)
     */
    public String getGrade() {
        if (percentage >= 90) return "A";
        if (percentage >= 80) return "B";
        if (percentage >= 70) return "C";
        if (percentage >= 60) return "D";
        return "F";
    }
    
    // Utility Methods
    
    /**
     * Generates a unique result ID
     * @return A unique result identifier
     */
    private String generateResultId() {
        return "RESULT_" + System.currentTimeMillis();
    }
    
    /**
     * Formats time in seconds to readable format
     * @param seconds Time in seconds
     * @return Formatted time string (e.g., "5m 30s")
     */
    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        
        if (minutes > 0) {
            return String.format("%dm %ds", minutes, remainingSeconds);
        } else {
            return String.format("%ds", seconds);
        }
    }
    
    /**
     * Validates if the result is complete
     * @return true if valid, false otherwise
     */
    public boolean isValid() {
        return studentName != null && !studentName.isEmpty() 
                && quizId != null && !quizId.isEmpty()
                && totalMarks > 0;
    }
    
    @Override
    public String toString() {
        return String.format("Result[id=%s, student=%s, quiz=%s, score=%d/%d, percentage=%.2f%%, passed=%s]",
                            resultId, studentName, quizTitle, marksObtained, 
                            totalMarks, percentage, passed);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Result result = (Result) obj;
        return resultId.equals(result.resultId);
    }
    
    @Override
    public int hashCode() {
        return resultId.hashCode();
    }
}