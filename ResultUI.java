package src.ui;

import java.util.List;
import java.util.Scanner;
import src.model.Result;

/**
 * ResultUI provides user interface for displaying quiz results.
 * Handles result visualization and statistics.
 */
public class ResultUI {
    
    /**
     * Displays a result summary
     * @param result The result to display
     */
    public static void displayResult(Result result) {
        if (result == null) {
            System.out.println("✗ No result to display.");
            return;
        }
        
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         QUIZ RESULT                    ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        System.out.println(result.displayResult());
        
        displayPerformanceAnalysis(result);
    }
    
    /**
     * Displays detailed result with all questions and answers
     * @param result The result to display
     */
    public static void displayDetailedResult(Result result) {
        if (result == null) {
            System.out.println("✗ No result to display.");
            return;
        }
        
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      DETAILED QUIZ RESULT              ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        System.out.println(result.displayDetailedResult());
    }
    
    /**
     * Displays a list of results
     * @param results List of results to display
     */
    public static void displayResultsList(List<Result> results) {
        if (results == null || results.isEmpty()) {
            System.out.println("No results to display.");
            return;
        }
        
        System.out.println("\n═══════════════════════════════════════════════════════════════════════");
        System.out.printf("%-25s %-20s %-10s %-10s %-10s%n", 
                         "QUIZ", "STUDENT", "SCORE", "PERCENT", "STATUS");
        System.out.println("═══════════════════════════════════════════════════════════════════════");
        
        for (Result result : results) {
            String quizTitle = truncate(result.getQuizTitle(), 24);
            String studentName = truncate(result.getStudentName(), 19);
            String score = result.getMarksObtained() + "/" + result.getTotalMarks();
            String percent = String.format("%.1f%%", result.getPercentage());
            String status = result.isPassed() ? "PASS" : "FAIL";
            
            System.out.printf("%-25s %-20s %-10s %-10s %-10s%n",
                            quizTitle, studentName, score, percent, status);
        }
        
        System.out.println("═══════════════════════════════════════════════════════════════════════");
        System.out.println("Total Results: " + results.size());
    }
    
    /**
     * Displays performance analysis for a result
     * @param result The result to analyze
     */
    private static void displayPerformanceAnalysis(Result result) {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      PERFORMANCE ANALYSIS              ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        // Grade
        System.out.println("Grade: " + result.getGrade());
        
        // Performance bars
        System.out.println("\nScore Distribution:");
        displayProgressBar("Correct", result.getCorrectAnswersCount(), 
                          result.getQuestions().size());
        displayProgressBar("Incorrect", result.getIncorrectAnswersCount(), 
                          result.getQuestions().size());
        displayProgressBar("Unanswered", result.getUnansweredCount(), 
                          result.getQuestions().size());
        
        // Performance message
        System.out.println("\n" + getPerformanceMessage(result.getPercentage()));
    }
    
    /**
     * Displays a progress bar
     * @param label Label for the bar
     * @param value Current value
     * @param max Maximum value
     */
    private static void displayProgressBar(String label, int value, int max) {
        int barLength = 30;
        int filled = max > 0 ? (value * barLength) / max : 0;
        
        System.out.print(String.format("%-12s [", label + ":"));
        
        for (int i = 0; i < barLength; i++) {
            if (i < filled) {
                System.out.print("█");
            } else {
                System.out.print("░");
            }
        }
        
        System.out.println("] " + value + "/" + max);
    }
    
    /**
     * Gets a performance message based on percentage
     * @param percentage The percentage score
     * @return Motivational message
     */
    private static String getPerformanceMessage(double percentage) {
        if (percentage >= 95) {
            return "🌟 Outstanding! Perfect performance!";
        } else if (percentage >= 85) {
            return "🎉 Excellent work! You've mastered this topic!";
        } else if (percentage >= 75) {
            return "👍 Great job! Keep up the good work!";
        } else if (percentage >= 65) {
            return "✓ Good effort! You're on the right track!";
        } else if (percentage >= 50) {
            return "📚 Not bad! Review the material and try again!";
        } else {
            return "💪 Keep practicing! You'll improve with more study!";
        }
    }
    
    /**
     * Displays statistics for multiple results
     * @param results List of results
     */
    public static void displayStatistics(List<Result> results) {
        if (results == null || results.isEmpty()) {
            System.out.println("No results available for statistics.");
            return;
        }
        
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         STATISTICS                     ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        // Calculate statistics
        int totalAttempts = results.size();
        int passed = 0;
        int failed = 0;
        double totalPercentage = 0;
        double highestScore = 0;
        double lowestScore = 100;
        
        for (Result result : results) {
            if (result.isPassed()) {
                passed++;
            } else {
                failed++;
            }
            
            double percentage = result.getPercentage();
            totalPercentage += percentage;
            
            if (percentage > highestScore) {
                highestScore = percentage;
            }
            
            if (percentage < lowestScore) {
                lowestScore = percentage;
            }
        }
        
        double averageScore = totalPercentage / totalAttempts;
        double passRate = (passed * 100.0) / totalAttempts;
        
        // Display statistics
        System.out.println("Total Attempts: " + totalAttempts);
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        System.out.println("Pass Rate: " + String.format("%.1f%%", passRate));
        System.out.println("\nAverage Score: " + String.format("%.2f%%", averageScore));
        System.out.println("Highest Score: " + String.format("%.2f%%", highestScore));
        System.out.println("Lowest Score: " + String.format("%.2f%%", lowestScore));
        
        // Score distribution
        System.out.println("\nScore Distribution:");
        displayScoreDistribution(results);
    }
    
    /**
     * Displays score distribution chart
     * @param results List of results
     */
    private static void displayScoreDistribution(List<Result> results) {
        int[] ranges = new int[5]; // 0-20, 21-40, 41-60, 61-80, 81-100
        
        for (Result result : results) {
            double percentage = result.getPercentage();
            
            if (percentage <= 20) {
                ranges[0]++;
            } else if (percentage <= 40) {
                ranges[1]++;
            } else if (percentage <= 60) {
                ranges[2]++;
            } else if (percentage <= 80) {
                ranges[3]++;
            } else {
                ranges[4]++;
            }
        }
        
        String[] labels = {"0-20%", "21-40%", "41-60%", "61-80%", "81-100%"};
        
        for (int i = 0; i < ranges.length; i++) {
            displayProgressBar(labels[i], ranges[i], results.size());
        }
    }
    
    /**
     * Displays comparison between results
     * @param result1 First result
     * @param result2 Second result
     */
    public static void compareResults(Result result1, Result result2) {
        if (result1 == null || result2 == null) {
            System.out.println("✗ Cannot compare: Invalid results.");
            return;
        }
        
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      RESULTS COMPARISON                ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        System.out.println(String.format("%-30s %-15s %-15s", 
                                        "", "Result 1", "Result 2"));
        System.out.println("─────────────────────────────────────────────────────────────");
        
        System.out.println(String.format("%-30s %-15s %-15s", 
                                        "Student:", 
                                        result1.getStudentName(), 
                                        result2.getStudentName()));
        
        System.out.println(String.format("%-30s %-15s %-15s", 
                                        "Quiz:", 
                                        truncate(result1.getQuizTitle(), 14), 
                                        truncate(result2.getQuizTitle(), 14)));
        
        System.out.println(String.format("%-30s %-15s %-15s", 
                                        "Score:", 
                                        result1.getMarksObtained() + "/" + result1.getTotalMarks(),
                                        result2.getMarksObtained() + "/" + result2.getTotalMarks()));
        
        System.out.println(String.format("%-30s %-15s %-15s", 
                                        "Percentage:", 
                                        String.format("%.2f%%", result1.getPercentage()),
                                        String.format("%.2f%%", result2.getPercentage())));
        
        System.out.println(String.format("%-30s %-15s %-15s", 
                                        "Status:", 
                                        result1.isPassed() ? "PASSED" : "FAILED",
                                        result2.isPassed() ? "PASSED" : "FAILED"));
        
        System.out.println(String.format("%-30s %-15s %-15s", 
                                        "Grade:", 
                                        result1.getGrade(),
                                        result2.getGrade()));
        
        System.out.println("─────────────────────────────────────────────────────────────");
        
        // Performance comparison
        double diff = result2.getPercentage() - result1.getPercentage();
        if (Math.abs(diff) < 0.1) {
            System.out.println("\nPerformance is approximately the same.");
        } else if (diff > 0) {
            System.out.println(String.format("\nResult 2 is %.2f%% better than Result 1", diff));
        } else {
            System.out.println(String.format("\nResult 1 is %.2f%% better than Result 2", Math.abs(diff)));
        }
    }
    
    /**
     * Displays result with option to view details
     * @param result The result to display
     * @param scanner Scanner for user input
     */
    public static void displayResultWithOptions(Result result, Scanner scanner) {
        displayResult(result);
        
        System.out.println("\nOptions:");
        System.out.println("1. View Detailed Answers");
        System.out.println("2. Return to Main Menu");
        
        System.out.print("\nEnter your choice: ");
        String input = scanner.nextLine().trim();
        
        if (input.equals("1")) {
            displayDetailedResult(result);
            System.out.print("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }
    
    /**
     * Exports result to text format
     * @param result The result to export
     * @return Formatted text representation
     */
    public static String exportResultAsText(Result result) {
        if (result == null) {
            return "No result to export.";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("════════════════════════════════════════════════════════════\n");
        sb.append("                    QUIZ RESULT EXPORT\n");
        sb.append("════════════════════════════════════════════════════════════\n\n");
        sb.append(result.displayDetailedResult());
        sb.append("\n════════════════════════════════════════════════════════════\n");
        sb.append("Generated on: ").append(new java.util.Date()).append("\n");
        sb.append("════════════════════════════════════════════════════════════\n");
        
        return sb.toString();
    }
    
    // ==================== Utility Methods ====================
    
    /**
     * Truncates a string to specified length
     * @param str String to truncate
     * @param maxLength Maximum length
     * @return Truncated string
     */
    private static String truncate(String str, int maxLength) {
        if (str == null) {
            return "";
        }
        
        if (str.length() <= maxLength) {
            return str;
        }
        
        return str.substring(0, maxLength - 3) + "...";
    }
}
