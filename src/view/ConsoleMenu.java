package view;

import model.Student;
import service.StudentService;
import exception.StudentNotFoundException;

import java.util.Scanner;

public class ConsoleMenu {
    private Scanner scanner = new Scanner(System.in);
    private StudentService studentService = new StudentService();

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

    public void start() {
        int choice;

        do {
            System.out.println("\n===== Student Management Menu =====");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Find Student by ID");
            System.out.println("0. Exit");
            choice = readValidInt("Enter choice: ");

            switch (choice) {
                case 1:
                    int idAdd = readValidInt("Enter ID: ");
                    try {
                        studentService.findStudentById(idAdd);
                        System.out.println("ID déjà utilisé.");
                    } catch (StudentNotFoundException e) {
                        System.out.print("Enter Name: ");
                        String nameAdd = scanner.nextLine();
                        System.out.print("Enter Email: ");
                        String emailAdd = scanner.nextLine();
                        Student newStudent = new Student(idAdd, nameAdd, emailAdd);
                        studentService.addStudent(newStudent);
                    }
                    break;

                case 2:
                    studentService.viewAllStudents();
                    break;

                case 3:
                    int idUpdate = readValidInt("Enter ID to update: ");
                    try {
                        studentService.findStudentById(idUpdate);
                        System.out.print("Enter New Name: ");
                        String nameUpdate = scanner.nextLine();
                        System.out.print("Enter New Email: ");
                        String emailUpdate = scanner.nextLine();
                        Student updatedStudent = new Student(idUpdate, nameUpdate, emailUpdate);
                        studentService.updateStudent(idUpdate, updatedStudent);
                    } catch (StudentNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 4:
                    int idDelete = readValidInt("Enter ID to delete: ");
                    try {
                        studentService.deleteStudent(idDelete);
                    } catch (StudentNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 5:
                    int idFind = readValidInt("Enter ID to find: ");
                    try {
                        Student student = studentService.findStudentById(idFind);
                        System.out.println("Étudiant trouvé : " + student.getName() + " (" + student.getEmail() + ")");
                    } catch (StudentNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 0:
                    System.out.println("Au revoir !");
                    break;

                default:
                    System.out.println("Choix invalide.");
            }

        } while (choice != 0);
    }
}
