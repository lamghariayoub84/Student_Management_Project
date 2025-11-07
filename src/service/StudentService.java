package service;

import model.Course;
import model.Student;

import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class StudentService {
    private List<Student> students = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);

    // Ajouter un étudiant

    public void addStudent() {
        int id;
        String name, email;


        while (true) {
            System.out.print("Enter ID: ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("ID cannot be empty. Please enter a number.");
                continue;
            }

            if (!input.matches("\\d+")) {
                System.out.println(" Invalid ID. Please enter numbers only.");
                continue;
            }

            id = Integer.parseInt(input);
            break;
        }

        File file = new File("C:/Users/AYOUB/Desktop/Project mouad/Student Management app/students.txt");
        ArrayList<Student> studentArrayList = new ArrayList<>();


        if (file.exists() && file.length() > 0) {
            try (FileInputStream fis = new FileInputStream(file);
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                studentArrayList = (ArrayList<Student>) ois.readObject();
            } catch (EOFException e) {
                // fichier vide → on ignore
                studentArrayList = new ArrayList<>();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println(" Error reading file: " + e.getMessage());
            }
        }


        boolean exists = studentArrayList.stream().anyMatch(s -> s.getId() == id);
        if (exists) {
            System.out.println("A student with ID " + id + " already exists!");
            return;
        }


        while (true) {
            System.out.print("Enter Name: ");
            name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Name cannot be empty.");
            } else {
                break;
            }
        }


        while (true) {
            System.out.print("Enter Email: ");
            email = scanner.nextLine().trim();
            if (email.isEmpty()) {
                System.out.println("Email cannot be empty.");
                continue;
            }
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                System.out.println("Invalid email format.");
            } else {
                break;
            }
        }


        Student newStudent = new Student(id, name, email);
        studentArrayList.add(newStudent);


        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(studentArrayList);
            System.out.println("Student added successfully and saved to file!");
        } catch (IOException e) {
            System.out.println(" Error while saving student: " + e.getMessage());
        }
    }

    // Afficher tous les étudiants
    public void viewStudents() {
//        if (students.isEmpty()) {
//            System.out.println("No students found.");
//        } else {
//            for (Student s : students) {
//                System.out.println(s);
//            }
//        }
        File file = new File("C:/Users/AYOUB/Desktop/Project mouad/Student Management app/students.txt");
        if (!file.exists()) {
            System.out.println("No file found.");
            return;
        }
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            ArrayList<Student> studentArrayList = (ArrayList<Student>) ois.readObject();
            for (Student s : studentArrayList) {
                System.out.println(s);
            }
            System.out.println("Reader successfully read students.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    public void updateStudent() {
        File file = new File("C:/Users/AYOUB/Desktop/Project mouad/Student Management app/students.txt");
        ArrayList<Student> studentArrayList = new ArrayList<>();
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file);
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                studentArrayList = (ArrayList<Student>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }


        if (studentArrayList.isEmpty()) {
            System.out.println("No students found.");
            return;
        }


        int id = -1;
        boolean validInput = false;
        while (!validInput) {
            System.out.print("Enter ID of student to update: ");
            try {
                id = scanner.nextInt();
                scanner.nextLine();
                validInput = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number only!");
                scanner.nextLine();
            }
        }

        boolean found = false;
        for (Student s : studentArrayList) {
            if (s.getId() == id) {
                System.out.println("Current name: " + s.getName());
                System.out.print("Enter new name (leave empty to keep the same): ");
                String newName = scanner.nextLine();
                if (!newName.isBlank()) {
                    s.setName(newName);
                }

                System.out.println("Current email: " + s.getEmail());
                System.out.print("Enter new email (leave empty to keep the same): ");
                String newEmail = scanner.nextLine();
                if (!newEmail.isBlank()) {
                    s.setEmail(newEmail);
                }

                found = true;
                break;
            }
        }


        if (found) {
            try (FileOutputStream fos = new FileOutputStream(file);
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(studentArrayList);
                System.out.println("Student updated successfully!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Student not found.");
        }
    }



    // Supprimer un étudiant
    public void deleteStudent() {
        File file = new File("C:/Users/AYOUB/Desktop/Project mouad/Student Management app/students.txt");
        ArrayList<Student> studentList = new ArrayList<>();


        if (file.exists() && file.length() > 0) {
            try (FileInputStream fis = new FileInputStream(file);
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                studentList = (ArrayList<Student>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No students found in file.");
            return;
        }


        int id = -1;
        boolean validInput = false;
        while (!validInput) {
            System.out.print("Enter ID of student to delete: ");
            try {
                String input = scanner.nextLine().trim();


                if (input.isEmpty()) {
                    System.out.println("Input cannot be empty. Please enter a valid ID.");
                    continue;
                }


                id = Integer.parseInt(input);
                validInput = true;

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number only!");
            }
        }


        boolean found = false;
        for (Student s : studentList) {
            if (s.getId() == id) {
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Student with ID " + id + " not found.");
            return;
        }


        int finalId = id;
        boolean removed = studentList.removeIf(s -> s.getId() == finalId);


        if (removed) {
            try (FileOutputStream fos = new FileOutputStream(file);
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(studentList);
                System.out.println("Student deleted successfully and file updated.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    /**
     * Retrieve a student by their ID from the list of students.
     * @param id The ID of the student to search for
     * @return The Student object if found, otherwise null
     */
    public Student getStudentById(int id) {
        for (Student s : students) {
            if (s.getId() == id) return s;
        }
        return null;
    }

    /**
     * Add a course to a specific student identified by studentId.
     * @param studentId The ID of the student
     * @param course The Course object to add
     */
    public void addCourseToStudent(int studentId, Course course) {
        Student s = getStudentById(studentId);
        if(s != null) s.addCourse(course);
        else System.out.println("Student not found.");
    }

    /**
     * Update a course's note for a specific student.
     * @param studentId The ID of the student
     * @param courseName The name of the course to update
     * @param newNote The new note value
     */
    public void updateStudentCourse(int studentId, String courseName, float newNote) {
        Student s = getStudentById(studentId);
        if(s != null) s.updateCourse(courseName, newNote);
        else System.out.println("Student not found.");
    }

    /**
     * Remove a course from a specific student by course name.
     * @param studentId The ID of the student
     * @param courseName The name of the course to remove
     */
    public void removeStudentCourse(int studentId, String courseName) {
        Student s = getStudentById(studentId);
        if(s != null) s.removeCourse(courseName);
        else System.out.println("Student not found.");
    }

    /**
     * Get all students
     * @return List of all students
     */
    public List<Student> getAllStudents() {
        return students;
    }

}
