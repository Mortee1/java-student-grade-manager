package com.marty.studentmanager.storage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StudentStorage {

    private static final String DELIMITER = ",";

    private final Path filePath;

    public StudentStorage(Path filePath) {
        if (filePath == null) {
            throw new IllegalArgumentException("filePath must not be null");
        }
        this.filePath = filePath;
    }

    public Map<String, Integer> load() {
        Map<String, Integer> result = new LinkedHashMap<>();

        if (Files.notExists(filePath)) {
            return result;
        }

        List<String> lines;
        try {
            lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("⚠️ Could not read file: " + filePath + " (" + e.getMessage() + ")");
            return result;
        }

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line == null) continue;

            String trimmed = line.trim();
            if (trimmed.isEmpty()) continue;

            String[] parts = trimmed.split(DELIMITER, 2);
            if (parts.length != 2) {
                warn(i, "missing comma", trimmed);
                continue;
            }

            String name = parts[0].trim();
            String gradeText = parts[1].trim();

            if (name.isEmpty()) {
                warn(i, "empty name", trimmed);
                continue;
            }

            int grade;
            try {
                grade = Integer.parseInt(gradeText);
            } catch (NumberFormatException e) {
                warn(i, "bad grade", trimmed);
                continue;
            }
            result.put(name, grade);
        }

        return result;
    }

    public void save(Map<String, Integer> students) {
        if (students == null) {
            System.err.println("⚠️ Could not save: students map is null");
            return;
        }

        List<String> lines = new ArrayList<>(students.size());
        for (Map.Entry<String, Integer> entry : students.entrySet()) {
            String name = entry.getKey();
            Integer grade = entry.getValue();

            if (name == null || grade == null) continue;

            String trimmedName = name.trim();
            if (trimmedName.isEmpty()) continue;

            lines.add(trimmedName + DELIMITER + grade);
        }

        try {
            Path parent = filePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            Files.write(
                    filePath,
                    lines,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
        } catch (IOException e) {
            System.err.println("⚠️ Could not save file: " + filePath + " (" + e.getMessage() + ")");
        }
    }

    private static void warn(int lineIndex, String reason, String content) {
        System.err.println("Skipping malformed line " + (lineIndex + 1) + " (" + reason + "): " + content);
    }
}
