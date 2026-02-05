package com.marty.studentmanager;

import java.util.*;

public class Main {

    private final static Scanner input = new Scanner(System.in);

    public static void main (String[] args){

        StudentManager studentManager = new StudentManager();

        while(true){
            printMenu();
            int choice = readMenuChoice();

            if(choice == 0){
                System.out.println("Shutting down...");
                break;
            }

            try{
                switch(choice){
                    case 1 -> {
                        System.out.println("------------------");
                        System.out.println("Add a student");
                        System.out.println("------------------");

                        System.out.print("Enter student name: ");
                        String name = input.nextLine().trim();

                        if(name.isEmpty()){
                            System.out.println("Name cannot be empty");
                            break;
                        }

                        int grade;
                        try{
                            System.out.print("Enter student grade: ");
                            grade = Integer.parseInt(input.nextLine());

                            if(grade < 0 || grade > 100){
                                System.out.println("Grade must be between 0 and 100.");
                                break;
                            }

                        }catch(NumberFormatException e){
                            System.out.println("Invalid grade. Please input a number.");
                            break;
                        }

                        studentManager.addStudent(name, grade);
                        System.out.println("Student has been added successfully!");
                    }case 2 -> {
                        System.out.println("-----------------");
                        System.out.println("Remove a student");
                        System.out.println("-----------------");

                        System.out.print("Enter student name: ");
                        String rName = input.nextLine().trim();

                        if(rName.isEmpty()){
                            System.out.println("Please enter a name.");
                            break;
                        }
                        boolean checkIfRemoved = studentManager.removeStudent(rName);
                        if(checkIfRemoved){
                            System.out.println("Student has been removed.");
                        }else {
                            System.out.println("Student does not exist.");
                        }
                    }case 3 -> {
                        System.out.println("--------------------");
                        System.out.println("Update student grade");
                        System.out.println("--------------------");

                        System.out.print("Enter student name: ");
                        String uName = input.nextLine().trim();

                        if(uName.isEmpty()){
                            System.out.println("Please enter a name.");
                            break;
                        }

                        int uGrade;
                        try{
                            System.out.print("Enter updated grade: ");
                            uGrade = Integer.parseInt(input.nextLine());
                            if(uGrade < 0 || uGrade > 100){
                                System.out.println("Grade must be between 0 and 100.");
                            }
                            boolean checkIfUpdated = studentManager.updateGrade(uName, uGrade);
                            if(checkIfUpdated){
                                System.out.println("-----------------------");
                                System.out.println("Grade has been updated.");
                                System.out.println("Updated Info:" +
                                        "\n Name: " + uName +
                                        "\n Grade: " + uGrade);
                            }else{
                                System.out.println("Student does not exist.");
                            }
                        }catch(NumberFormatException e){
                            System.out.println("Invalid grade. Please enter a number.");
                        }

                    }case 4 -> {
                        Map <String, Integer> students = studentManager.getAllStudents();

                        if(students.isEmpty()){
                            System.out.println("No students found.");
                        }else{
                            System.out.println("Student List:");
                            System.out.println("-------------");
                            for(Map.Entry<String, Integer> entry : students.entrySet()){
                                System.out.println("Name: " + entry.getKey() + ", Grade: " + entry.getValue());
                            }
                        }
                    }case 5 -> {
                        System.out.println("--------------------");
                        System.out.println("Search for a student");
                        System.out.println("--------------------");

                        System.out.print("Enter student name: ");
                        String sName = input.nextLine().trim();

                        if(sName.isEmpty()){
                            System.out.println("Please enter a name.");
                            break;
                        }

                        Integer grade = studentManager.getGrade(sName);
                        if(grade == null) {
                            System.out.println("Student cannot be found.");
                        }else{
                            System.out.println("Student Information");
                            System.out.println("Name: " + sName);
                            System.out.println("Grade: " + grade);
                        }
                    }
                }

            }catch(IllegalArgumentException e){
                System.out.println("System Error: " + e.getMessage());
            }
        }

        input.close();
    }

    //Helper methods

    private static void printMenu(){

        System.out.println("Student Grade Manager");
        System.out.println("---------------------");
        System.out.println("""
                [1] Add student
                [2] Remove student
                [3] Update grade
                [4] List all students
                [5] Search student
                [0] Exit""");
        System.out.println("---------------------");
    }

    private static int readMenuChoice(){
        while(true){
            try{
                System.out.print("Select an option: ");
                int choice = Integer.parseInt(input.nextLine());

                if(choice < 0 || choice > 5){
                    System.out.println("Selected option is unavailable. Try again.");
                    continue;
                }
                return choice;
            }catch(NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }


    }
}