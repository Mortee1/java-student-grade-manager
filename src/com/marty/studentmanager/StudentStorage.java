package com.marty.studentmanager;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.nio.file.Path;
import java.io.IOException;


public class StudentStorage{
    
    private final Path filePath;

    public StudentStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Map<String, Integer> load() {
        Map<String, Integer> result = new LinkedHashMap<>();

        // 1) Missing file = empty dataset (not an error)
        if (Files.notExists(filePath)) {
            return result;
        }

        // 2) Read all lines safely
        try {
            List<String> lines = Files.readAllLines(filePath);
            for (String line : lines){
                if(line == null) continue;

                String trimmed = line.trim();
                if(trimmed.isEmpty()) continue;

                String[] parts = trimmed.split(",", 2);
                if(parts.length != 2){
                    System.err.println("Skipping malformed line (missing comma): " + trimmed);
                    continue;
                }

                String name = parts[0].trim();
                String gradeText = parts[1].trim();

                if (name.isEmpty()) {
                    System.err.println("Skipping malformed line (empty name): " + trimmed);
                    continue;
                }
                try{
                    int grade = Integer.parseInt(gradeText);
                    result.put(name, grade);
                }catch(NumberFormatException e){
                    System.err.println("Skipping malformed line (bad grade): " + trimmed);
                }
            }
        } catch (IOException e) {
            // For a CLI app, don’t crash; just warn and continue with empty data
            System.err.println("⚠️ Could not read file: " + filePath + " (" + e.getMessage() + ")");
        }
        return result;
    }

    public void save(Map<String, Integer> students){
        List<String> lines = new ArrayList<>();

        for(Map.Entry<String, Integer> entry : students.entrySet()){
            String name = entry.getKey();
            Integer grade = entry.getValue();

            if(name == null || grade == null) continue;

            lines.add(name.trim() + "," + grade);
        }

        try{
            Path parent = filePath.getParent();
            if(parent != null){
                Files.createDirectories(parent);
            }

            Files.write(
                    filePath,
                    lines,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
        }catch(IOException e){
            System.err.println("Could not save file " + filePath + " (" + e.getMessage() + ")");
        }
    }
}