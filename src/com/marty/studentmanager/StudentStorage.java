package com.marty.studentmanager;

import java.nio.file.Path;
import java.util.Map;
import java.util.LinkedHashMap;

public class StudentStorage {

    private final Path filePath;

    public StudentStorage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads students from disk.
     * - If the file does not exist, returns an empty map.
     * - Malformed lines are skipped safely.
     */
    public Map<String, Integer> load() {
        // TODO Day 8 Step 1: implement safe load
        return new LinkedHashMap<>();
    }

    /**
     * Saves students to disk.
     * - Writes the whole dataset (atomic-ish approach, simple and safe enough for CLI).
     */
    public void save(Map<String, Integer> students) {
        // TODO Day 8 Step 2: implement safe save
    }

    // ---- Helpers (you’ll implement these step-by-step later) ----

    /**
     * Attempts to parse one line in the format: Name,Grade
     * Returns null (or empty) if invalid.
     */
    // private StudentRecord parseLine(String line) { ... }

    /**
     * Formats one student entry into a single line: Name,Grade
     */
    // private String formatLine(String name, int grade) { ... }

    // Optional: record for internal parsing clarity (Java 16+)
    // private record StudentRecord(String name, int grade) {}
}
