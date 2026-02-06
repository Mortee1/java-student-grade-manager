package com.marty.studentmanager;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

public class StudentManager{

    private final HashMap <String, Integer> students = new HashMap<>();

    public void addStudent(String name, int grade){
        students.put(name, grade);
    }

    public boolean removeStudent(String name){
        return students.remove(name) != null;
    }

    public boolean updateGrade(String name, int grade){
        if(students.containsKey(name)){
            students.put(name, grade);
            return true;
        }else{
            return false;
        }
    }

    public Integer getGrade(String name){
        return students.get(name);
    }

    public Map<String, Integer> getAllStudents(){
        return Collections.unmodifiableMap(students);
    }

    public void loadStudents(Map<String, Integer> loadedStudents){
        students.clear();
        students.putAll(loadedStudents);
    }

}