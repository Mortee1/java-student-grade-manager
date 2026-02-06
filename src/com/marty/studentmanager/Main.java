package com.marty.studentmanager;

import com.marty.studentmanager.manager.StudentManager;
import com.marty.studentmanager.storage.StudentStorage;

import java.nio.file.Path;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static final Scanner input = new Scanner(System.in);

    private static final int MIN_GRADE = 0;
    private static final int MAX_GRADE = 100;

    private static final Path DATA_FILE = Path.of("data", "students.txt");

    public static void main(String[] args) {
        StudentManager studentManager = new StudentManager();
        StudentStorage studentStorage = new StudentStorage(DATA_FILE);

        studentManager.loadStudents(studentStorage.load());
        System.out.println("Loaded " + studentManager.getAllStudents().size() + " students.");

        while (true) {
            printMenu();
            int choice = readIntInRange("Select an option: ", 0, 5);

            if (choice == 0) {
                System.out.println("Shutting down...");
                break;
            }

            switch (choice) {
                case 1 -> addStudentFlow(studentManager, studentStorage);
                case 2 -> removeStudentFlow(studentManager, studentStorage);
                case 3 -> updateGradeFlow(studentManager, studentStorage);
                case 4 -> listAllStudentsFlow(studentManager);
                case 5 -> searchStudentFlow(studentManager);
                default -> System.out.println("Invalid option.");
            }

            pressEnterToContinue();
        }
    }

    private static void addStudentFlow(StudentManager studentManager, StudentStorage studentStorage) {
        printSection("Add a student");

        String name = readNonEmptyString("Enter student name: ");
        int grade = readIntInRange("Enter student grade (0–100): ", MIN_GRADE, MAX_GRADE);

        try {
            studentManager.addStudent(name, grade);
            System.out.println("✅ Student saved: " + name.trim() + " (" + grade + ")");
            studentStorage.save(studentManager.getAllStudents());
        } catch (IllegalArgumentException e) {
            System.out.println("⚠️ " + e.getMessage());
        }
    }

    private static void removeStudentFlow(StudentManager studentManager, StudentStorage studentStorage) {
        printSection("Remove a student");

        String name = readNonEmptyString("Enter student name: ");

        try {
            boolean removed = studentManager.removeStudent(name);
            if (removed) {
                System.out.println("✅ Student removed: " + name.trim());
                studentStorage.save(studentManager.getAllStudents());
            } else {
                System.out.println("⚠️ Student not found: " + name.trim());
            }
        } catch (IllegalArgumentException e) {
            System.out.println("⚠️ " + e.getMessage());
        }
    }

    private static void updateGradeFlow(StudentManager studentManager, StudentStorage studentStorage) {
        printSection("Update student grade");

        String name = readNonEmptyString("Enter student name: ");
        int grade = readIntInRange("Enter updated grade (0–100): ", MIN_GRADE, MAX_GRADE);

        try {
            boolean updated = studentManager.updateGrade(name, grade);
            if (updated) {
                System.out.println("✅ Grade updated: " + name.trim() + " → " + grade);
                studentStorage.save(studentManager.getAllStudents());
            } else {
                System.out.println("⚠️ Student not found: " + name.trim());
            }
        } catch (IllegalArgumentException e) {
            System.out.println("⚠️ " + e.getMessage());
        }
    }

    private static void listAllStudentsFlow(StudentManager studentManager) {
        printSection("List all students");

        Map<String, Integer> students = studentManager.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        System.out.println("Student List (" + students.size() + ")");
        System.out.println("-------------------");

        students.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(String.CASE_INSENSITIVE_ORDER))
                .forEach(e -> System.out.println("Name: " + e.getKey() + ", Grade: " + e.getValue()));
    }

    private static void searchStudentFlow(StudentManager studentManager) {
        printSection("Search for a student");

        String name = readNonEmptyString("Enter student name: ");

        try {
            Integer grade = studentManager.getGrade(name);
            if (grade == null) {
                System.out.println("⚠️ Student not found: " + name.trim());
            } else {
                System.out.println("✅ Student Information");
                System.out.println("Name : " + name.trim());
                System.out.println("Grade: " + grade);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("⚠️ " + e.getMessage());
        }
    }

    private static void printMenu() {
        System.out.println("\nStudent Grade Manager");
        System.out.println("---------------------");
        System.out.println("""
                [1] Add student
                [2] Remove student
                [3] Update grade
                [4] List all students
                [5] Search student
                [0] Exit
                """);
        System.out.println("---------------------");
    }

    private static void printSection(String title) {
        System.out.println("\n" + title);
        System.out.println("-".repeat(title.length()));
    }

    private static void pressEnterToContinue() {
        System.out.print("\nPress Enter to continue...");
        input.nextLine();
    }

    private static String readNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = input.nextLine().trim();
            if (!value.isEmpty()) return value;
            System.out.println("Invalid input. Please enter a non-empty value.");
        }
    }

    private static int readIntInRange(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String raw = input.nextLine().trim();

            try {
                int value = Integer.parseInt(raw);
                if (value < min || value > max) {
                    System.out.println("Invalid number. Enter a value between " + min + " and " + max + ".");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }
}
