package com.marty.studentmanager.Main;

import com.marty.studentmanager.manager.StudentManager;
import com.marty.studentmanager.storage.StudentStorage;

import java.util.Map;
import java.util.Scanner;
import java.nio.file.Path;

public class Main {

    private static final Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        StudentManager studentManager = new StudentManager();
        StudentStorage studentStorage = new StudentStorage(Path.of("students.txt"));

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
            }

            pressEnterToContinue();
        }

        input.close();
    }

    // --------- Flows (each case becomes a small, readable function) ---------

    private static void addStudentFlow(StudentManager studentManager, StudentStorage studentStorage) {
        printSection("Add a student");
        String name = readNonEmptyString("Enter student name: ");
        int grade = readIntInRange("Enter student grade (0–100): ", 0, 100);

        studentManager.addStudent(name, grade);
        System.out.println("✅ Student added: " + name + " (" + grade + ")");

        studentStorage.save(studentManager.getAllStudents());
    }

    private static void removeStudentFlow(StudentManager studentManager, StudentStorage studentStorage) {
        printSection("Remove a student");
        String name = readNonEmptyString("Enter student name: ");

        boolean removed = studentManager.removeStudent(name);
        if (removed) {
            System.out.println("✅ Student removed: " + name);
            studentStorage.save(studentManager.getAllStudents());
        } else {
            System.out.println("⚠️ Student not found: " + name);
        }
    }

    private static void updateGradeFlow(StudentManager studentManager, StudentStorage studentStorage) {
        printSection("Update student grade");
        String name = readNonEmptyString("Enter student name: ");
        int grade = readIntInRange("Enter updated grade (0–100): ", 0, 100);

        boolean updated = studentManager.updateGrade(name, grade);
        if (updated) {
            System.out.println("✅ Grade updated: " + name + " → " + grade);
            studentStorage.save(studentManager.getAllStudents());
        } else {
            System.out.println("⚠️ Student not found: " + name);
        }
    }

    private static void listAllStudentsFlow(StudentManager studentManager) {
        printSection("List all students");

        Map<String, Integer> students = studentManager.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        System.out.println("Student List:");
        System.out.println("-------------");
        // Optional: sort by name for nicer output
        students.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(String.CASE_INSENSITIVE_ORDER))
                .forEach(e -> System.out.println("Name: " + e.getKey() + ", Grade: " + e.getValue()));
    }

    private static void searchStudentFlow(StudentManager studentManager) {
        printSection("Search for a student");
        String name = readNonEmptyString("Enter student name: ");

        Integer grade = studentManager.getGrade(name);
        if (grade == null) {
            System.out.println("⚠️ Student not found: " + name);
        } else {
            System.out.println("✅ Student Information");
            System.out.println("Name : " + name);
            System.out.println("Grade: " + grade);
        }
    }

    // --------- UI Helpers ---------

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

    // --------- Input Helpers ---------

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
