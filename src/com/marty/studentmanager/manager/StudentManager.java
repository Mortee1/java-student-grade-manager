package com.marty.studentmanager.manager;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class StudentManager {

    private static final int MIN_GRADE = 0;
    private static final int MAX_GRADE = 100;

    private final Map<String, Integer> students = new HashMap<>();

    public void addStudent(String name, int grade) {
        String key = normalizeName(name);
        validateGrade(grade);
        students.put(key, grade);
    }

    public boolean removeStudent(String name) {
        String key = normalizeName(name);
        return students.remove(key) != null;
    }

    public boolean updateGrade(String name, int grade) {
        String key = normalizeName(name);
        validateGrade(grade);
        return students.replace(key, grade) != null;
    }

    public Integer getGrade(String name) {
        String key = normalizeName(name);
        return students.get(key);
    }

    public Map<String, Integer> getAllStudents() {
        return Collections.unmodifiableMap(students);
    }

    public void loadStudents(Map<String, Integer> loadedStudents) {
        students.clear();
        if (loadedStudents == null || loadedStudents.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Integer> entry : loadedStudents.entrySet()) {
            String name = entry.getKey();
            Integer grade = entry.getValue();

            if (name == null || grade == null) continue;

            String key;
            try {
                key = normalizeName(name);
            } catch (IllegalArgumentException e) {
                continue;
            }

            if (grade < MIN_GRADE || grade > MAX_GRADE) {
                continue;
            }

            students.put(key, grade);
        }
    }

    private static String normalizeName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Student name must not be null");
        }
        String trimmed = name.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Student name must not be blank");
        }
        return trimmed;
    }

    private static void validateGrade(int grade) {
        if (grade < MIN_GRADE || grade > MAX_GRADE) {
            throw new IllegalArgumentException(
                    "Grade must be between " + MIN_GRADE + " and " + MAX_GRADE + ": " + grade
            );
        }
    }
}
