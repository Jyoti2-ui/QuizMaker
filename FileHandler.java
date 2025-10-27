package src.util;

import src.model.Quiz;
import src.model.Result;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * FileHandler class manages file operations for Quiz and Result objects.
 * Handles serialization and deserialization of quiz data.
 * Demonstrates file I/O operations and exception handling.
 */
public class FileHandler {
    
    // Directory paths for storing data
    private static final String QUIZ_DIRECTORY = "data/quizzes/";
    private static final String RESULT_DIRECTORY = "data/results/";
    private static final String FILE_EXTENSION = ".quiz";
    private static final String RESULT_EXTENSION = ".result";
    
    /**
     * Static initialization block to create necessary directories
     */
    static {
        createDirectories();
    }
    
    /**
     * Creates necessary directories if they don't exist
     */
    private static void createDirectories() {
        File quizDir = new File(QUIZ_DIRECTORY);
        File resultDir = new File(RESULT_DIRECTORY);
        
        if (!quizDir.exists()) {
            quizDir.mkdirs();
        }
        
        if (!resultDir.exists()) {
            resultDir.mkdirs();
        }
    }
    
    // ==================== Quiz Operations ====================
    
    /**
     * Saves a quiz to a file
     * @param quiz The quiz to save
     * @return true if successful, false otherwise
     */
    public static boolean saveQuiz(Quiz quiz) {
        if (quiz == null || !quiz.isValid()) {
            System.err.println("Cannot save invalid quiz");
            return false;
        }
        
        String filename = QUIZ_DIRECTORY + sanitizeFilename(quiz.getTitle()) + FILE_EXTENSION;
        
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filename))) {
            oos.writeObject(quiz);
            System.out.println("Quiz saved successfully: " + filename);
            return true;
        } catch (IOException e) {
            System.err.println("Error saving quiz: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Saves a quiz with a specific filename
     * @param quiz The quiz to save
     * @param filename The filename to use
     * @return true if successful, false otherwise
     */
    public static boolean saveQuizAs(Quiz quiz, String filename) {
        if (quiz == null || !quiz.isValid()) {
            System.err.println("Cannot save invalid quiz");
            return false;
        }
        
        String fullPath = QUIZ_DIRECTORY + sanitizeFilename(filename) + FILE_EXTENSION;
        
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(fullPath))) {
            oos.writeObject(quiz);
            System.out.println("Quiz saved successfully: " + fullPath);
            return true;
        } catch (IOException e) {
            System.err.println("Error saving quiz: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Loads a quiz from a file
     * @param filename The name of the file (without extension)
     * @return The loaded Quiz object, or null if error occurs
     */
    public static Quiz loadQuiz(String filename) {
        String fullPath = QUIZ_DIRECTORY + sanitizeFilename(filename) + FILE_EXTENSION;
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(fullPath))) {
            Quiz quiz = (Quiz) ois.readObject();
            System.out.println("Quiz loaded successfully: " + filename);
            return quiz;
        } catch (FileNotFoundException e) {
            System.err.println("Quiz file not found: " + filename);
            return null;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading quiz: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Loads a quiz from a full file path
     * @param filepath The complete path to the quiz file
     * @return The loaded Quiz object, or null if error occurs
     */
    public static Quiz loadQuizFromPath(String filepath) {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filepath))) {
            Quiz quiz = (Quiz) ois.readObject();
            System.out.println("Quiz loaded successfully from: " + filepath);
            return quiz;
        } catch (FileNotFoundException e) {
            System.err.println("Quiz file not found: " + filepath);
            return null;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading quiz: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Deletes a quiz file
     * @param filename The name of the file to delete (without extension)
     * @return true if successful, false otherwise
     */
    public static boolean deleteQuiz(String filename) {
        String fullPath = QUIZ_DIRECTORY + sanitizeFilename(filename) + FILE_EXTENSION;
        File file = new File(fullPath);
        
        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                System.out.println("Quiz deleted successfully: " + filename);
            } else {
                System.err.println("Failed to delete quiz: " + filename);
            }
            return deleted;
        } else {
            System.err.println("Quiz file not found: " + filename);
            return false;
        }
    }
    
    /**
     * Gets a list of all saved quiz filenames
     * @return List of quiz filenames (without extensions)
     */
    public static List<String> getAllQuizFilenames() {
        List<String> filenames = new ArrayList<>();
        File directory = new File(QUIZ_DIRECTORY);
        
        File[] files = directory.listFiles((dir, name) -> name.endsWith(FILE_EXTENSION));
        
        if (files != null) {
            for (File file : files) {
                String name = file.getName();
                // Remove the file extension
                String nameWithoutExt = name.substring(0, name.length() - FILE_EXTENSION.length());
                filenames.add(nameWithoutExt);
            }
        }
        
        return filenames;
    }
    
    /**
     * Gets a list of all saved quiz objects
     * @return List of Quiz objects
     */
    public static List<Quiz> getAllQuizzes() {
        List<Quiz> quizzes = new ArrayList<>();
        List<String> filenames = getAllQuizFilenames();
        
        for (String filename : filenames) {
            Quiz quiz = loadQuiz(filename);
            if (quiz != null) {
                quizzes.add(quiz);
            }
        }
        
        return quizzes;
    }
    
    /**
     * Checks if a quiz file exists
     * @param filename The name of the file to check (without extension)
     * @return true if file exists, false otherwise
     */
    public static boolean quizExists(String filename) {
        String fullPath = QUIZ_DIRECTORY + sanitizeFilename(filename) + FILE_EXTENSION;
        return new File(fullPath).exists();
    }
    
    // ==================== Result Operations ====================
    
    /**
     * Saves a result to a file
     * @param result The result to save
     * @return true if successful, false otherwise
     */
    public static boolean saveResult(Result result) {
        if (result == null) {
            System.err.println("Cannot save null result");
            return false;
        }
        
        // Generate filename based on student name and timestamp
        String filename = RESULT_DIRECTORY + sanitizeFilename(result.getStudentName()) 
                        + "_" + System.currentTimeMillis() + RESULT_EXTENSION;
        
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filename))) {
            oos.writeObject(result);
            System.out.println("Result saved successfully: " + filename);
            return true;
        } catch (IOException e) {
            System.err.println("Error saving result: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Loads a result from a file
     * @param filename The name of the file
     * @return The loaded Result object, or null if error occurs
     */
    public static Result loadResult(String filename) {
        String fullPath = RESULT_DIRECTORY + filename;
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(fullPath))) {
            Result result = (Result) ois.readObject();
            System.out.println("Result loaded successfully: " + filename);
            return result;
        } catch (FileNotFoundException e) {
            System.err.println("Result file not found: " + filename);
            return null;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading result: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Gets a list of all saved result filenames
     * @return List of result filenames
     */
    public static List<String> getAllResultFilenames() {
        List<String> filenames = new ArrayList<>();
        File directory = new File(RESULT_DIRECTORY);
        
        File[] files = directory.listFiles((dir, name) -> name.endsWith(RESULT_EXTENSION));
        
        if (files != null) {
            for (File file : files) {
                filenames.add(file.getName());
            }
        }
        
        return filenames;
    }
    
    /**
     * Gets a list of all saved result objects
     * @return List of Result objects
     */
    public static List<Result> getAllResults() {
        List<Result> results = new ArrayList<>();
        List<String> filenames = getAllResultFilenames();
        
        for (String filename : filenames) {
            Result result = loadResult(filename);
            if (result != null) {
                results.add(result);
            }
        }
        
        return results;
    }
    
    /**
     * Gets all results for a specific quiz
     * @param quizTitle The title of the quiz
     * @return List of Result objects for that quiz
     */
    public static List<Result> getResultsForQuiz(String quizTitle) {
        List<Result> quizResults = new ArrayList<>();
        List<Result> allResults = getAllResults();
        
        for (Result result : allResults) {
            if (result.getQuizId().equals(quizTitle)) {
                quizResults.add(result);
            }
        }
        
        return quizResults;
    }
    
    /**
     * Gets all results for a specific student
     * @param studentName The name of the student
     * @return List of Result objects for that student
     */
    public static List<Result> getResultsForStudent(String studentName) {
        List<Result> studentResults = new ArrayList<>();
        List<Result> allResults = getAllResults();
        
        for (Result result : allResults) {
            if (result.getStudentName().equalsIgnoreCase(studentName)) {
                studentResults.add(result);
            }
        }
        
        return studentResults;
    }
    
    /**
     * Deletes a result file
     * @param filename The name of the file to delete
     * @return true if successful, false otherwise
     */
    public static boolean deleteResult(String filename) {
        String fullPath = RESULT_DIRECTORY + filename;
        File file = new File(fullPath);
        
        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                System.out.println("Result deleted successfully: " + filename);
            } else {
                System.err.println("Failed to delete result: " + filename);
            }
            return deleted;
        } else {
            System.err.println("Result file not found: " + filename);
            return false;
        }
    }
    
    // ==================== Utility Methods ====================
    
    /**
     * Sanitizes a filename by removing invalid characters
     * @param filename The filename to sanitize
     * @return A sanitized filename
     */
    private static String sanitizeFilename(String filename) {
        if (filename == null || filename.trim().isEmpty()) {
            return "unnamed_" + System.currentTimeMillis();
        }
        
        // Remove invalid filename characters
        return filename.replaceAll("[^a-zA-Z0-9.-]", "_");
    }
    
    /**
     * Gets the full path for a quiz file
     * @param filename The filename (without extension)
     * @return The full file path
     */
    public static String getQuizFilePath(String filename) {
        return QUIZ_DIRECTORY + sanitizeFilename(filename) + FILE_EXTENSION;
    }
    
    /**
     * Gets the full path for a result file
     * @param filename The filename (with extension)
     * @return The full file path
     */
    public static String getResultFilePath(String filename) {
        return RESULT_DIRECTORY + filename;
    }
    
    /**
     * Clears all quiz files (use with caution!)
     * @return Number of files deleted
     */
    public static int clearAllQuizzes() {
        int count = 0;
        List<String> filenames = getAllQuizFilenames();
        
        for (String filename : filenames) {
            if (deleteQuiz(filename)) {
                count++;
            }
        }
        
        System.out.println("Cleared " + count + " quiz files");
        return count;
    }
    
    /**
     * Clears all result files (use with caution!)
     * @return Number of files deleted
     */
    public static int clearAllResults() {
        int count = 0;
        List<String> filenames = getAllResultFilenames();
        
        for (String filename : filenames) {
            if (deleteResult(filename)) {
                count++;
            }
        }
        
        System.out.println("Cleared " + count + " result files");
        return count;
    }
    
    /**
     * Exports a quiz to a specific location
     * @param quiz The quiz to export
     * @param exportPath The destination path
     * @return true if successful, false otherwise
     */
    public static boolean exportQuiz(Quiz quiz, String exportPath) {
        if (quiz == null || !quiz.isValid()) {
            System.err.println("Cannot export invalid quiz");
            return false;
        }
        
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(exportPath))) {
            oos.writeObject(quiz);
            System.out.println("Quiz exported successfully to: " + exportPath);
            return true;
        } catch (IOException e) {
            System.err.println("Error exporting quiz: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Imports a quiz from a specific location
     * @param importPath The source path
     * @return The imported Quiz object, or null if error occurs
     */
    public static Quiz importQuiz(String importPath) {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(importPath))) {
            Quiz quiz = (Quiz) ois.readObject();
            System.out.println("Quiz imported successfully from: " + importPath);
            return quiz;
        } catch (FileNotFoundException e) {
            System.err.println("Import file not found: " + importPath);
            return null;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error importing quiz: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}