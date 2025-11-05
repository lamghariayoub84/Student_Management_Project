package view;

import exception.InvalidCourseException;
import exception.StudentNotFoundException;
import model.Course;
import model.Student;
import service.StatisticsService;
import service.StudentService;
import strategy.AverageCalculationStrategy;
import strategy.BestStudentStrategy;
import strategy.FailingStudentsStrategy;

import java.util.Scanner;

public class ConsoleMenu {

    private Scanner scanner = new Scanner(System.in);
    private StudentService studentService = new StudentService();
    private StatisticsService statisticsService = new StatisticsService();

    // Lire un int valide
    private int readValidInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Entrée invalide. Veuillez entrer un nombre entier.");
            }
        }
    }

    // Lire un float valide
    private float readFloat(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Float.parseFloat(input);
            } catch (NumberFormatException e) {
                System.out.println("Entrée invalide. Veuillez entrer un nombre décimal.");
            }
        }
    }

    // Menu principal
    public void start() {
        int choice;
        do {
            System.out.println("\n===== Student Management Menu =====");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Manage Courses for a Student");
            System.out.println("6. Statistics Menu");
            System.out.println("0. Exit");
            choice = readValidInt("Enter choice: ");

            switch (choice) {
                case 1:
                    addStudentFlow();
                    break;
                case 2:
                    studentService.viewAllStudents();
                    break;
                case 3:
                    updateStudentFlow();
                    break;
                case 4:
                    deleteStudentFlow();
                    break;
                case 5:
                    manageCoursesFlow();
                    break;
                case 6: statisticsMenu();
                break;
                case 0:
                    System.out.println("Au revoir !");
                    break;
                default:
                    System.out.println("Choix invalide.");
                    break;
            }
        } while (choice != 0);
    }

    // Ajouter un étudiant
    private void addStudentFlow() {
        int id = readValidInt("Enter ID: ");
        try {
            studentService.findStudentById(id);
            System.out.println("ID déjà utilisé !");
        } catch (StudentNotFoundException e) {
            System.out.print("Enter Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter Email: ");
            String email = scanner.nextLine();
            Student student = new Student(id, name, email);
            studentService.addStudent(student);
            System.out.println("Étudiant ajouté avec succès !");
        }
    }

    // Mettre à jour un étudiant
    private void updateStudentFlow() {
        int id = readValidInt("Enter ID to update: ");
        try {
            Student student = studentService.findStudentById(id);
            System.out.print("Enter New Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter New Email: ");
            String email = scanner.nextLine();
            Student updated = new Student(id, name, email);
            studentService.updateStudent(id, updated);
            System.out.println("Étudiant mis à jour !");
        } catch (StudentNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    // Supprimer un étudiant
    private void deleteStudentFlow() {
        int id = readValidInt("Enter ID to delete: ");
        try {
            studentService.deleteStudent(id);
            System.out.println("Étudiant supprimé !");
        } catch (StudentNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    // Gestion des cours d’un étudiant
    private void manageCoursesFlow() {
        int id = readValidInt("Enter student ID: ");
        try {
            studentService.findStudentById(id);
        } catch (StudentNotFoundException e) {
            System.out.println(e.getMessage());
            return;
        }

        int choice;
        do {
            System.out.println("\n--- Manage Courses for Student ID " + id + " ---");
            System.out.println("1. Add Course");
            System.out.println("2. Update Course");
            System.out.println("3. Remove Course");
            System.out.println("4. View Courses");
            System.out.println("0. Back to Main Menu");
            choice = readValidInt("Enter choice: ");

            switch (choice) {
                case 1: {
                    System.out.print("Course name: ");
                    String name = scanner.nextLine();
                    float note = readFloat("Course note (0-20): ");
                    try {
                        Course course = new Course.CourseBuilder()
                                .withName(name)
                                .withNote(note)
                                .build();
                        studentService.addCourseToStudent(id, course);
                        System.out.println("Cours ajouté !");
                    } catch (InvalidCourseException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case 2: {
                    System.out.print("Course name to update: ");
                    String name = scanner.nextLine();
                    float newNote = readFloat("New note: ");
                    studentService.updateStudentCourse(id, name, newNote);
                    break;
                }
                case 3: {
                    System.out.print("Course name to remove: ");
                    String name = scanner.nextLine();
                    studentService.removeStudentCourse(id, name);
                    break;
                }
                case 4: {
                    Student student = studentService.getStudentById(id);
                    System.out.println("Courses for " + student.getName() + ":");
                    for (Course c : student.getCourses()) {
                        System.out.println(c);
                    }
                    break;
                }
                case 0:
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }

        } while (choice != 0);
    }


    //Statistics Menu
    private void statisticsMenu() {
        int choice;
        do {
            System.out.println("\n===== Statistics Menu =====");
            System.out.println("1. Calculate Average per Student");
            System.out.println("2. Find Best Student");
            System.out.println("3. List Failing Students (Average < 10)");
            System.out.println("4. Show All Statistics");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    statisticsService.setStrategy(new AverageCalculationStrategy());
                    statisticsService.executeStrategy(studentService.getAllStudents());
                }
                case 2 -> {
                    statisticsService.setStrategy(new BestStudentStrategy());
                    statisticsService.executeStrategy(studentService.getAllStudents());
                }
                case 3 -> {
                    statisticsService.setStrategy(new FailingStudentsStrategy());
                    statisticsService.executeStrategy(studentService.getAllStudents());
                }
                case 4 -> {
                    statisticsService.setStrategy(new AverageCalculationStrategy());
                    statisticsService.executeStrategy(studentService.getAllStudents());

                    statisticsService.setStrategy(new BestStudentStrategy());
                    statisticsService.executeStrategy(studentService.getAllStudents());

                    statisticsService.setStrategy(new FailingStudentsStrategy());
                    statisticsService.executeStrategy(studentService.getAllStudents());
                }
                case 0 -> System.out.println("Returning to Main Menu...");
                default -> System.out.println("Invalid choice.");
            }
        } while (choice != 0);
    }
}


