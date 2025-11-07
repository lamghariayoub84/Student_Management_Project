package view;
import exception.InvalidCourseException;
import model.Course;
import model.Student;
import service.StatisticsService;
import service.StudentService;
import strategy.AverageCalculationStrategy;
import strategy.BestStudentStrategy;
import strategy.FailingStudentsStrategy;

import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {

    private StudentService studentService = new StudentService();
    private StatisticsService statisticsService = new StatisticsService();
    private Scanner scanner = new Scanner(System.in);

    public void start() {
        int choice = -1;
        do {
            System.out.println("\n===== Student Management Menu =====");
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Add Courses for a Student");
            System.out.println("6. Update Courses for a Student");
            System.out.println("7. Remove Courses for a Student");
            System.out.println("8. Calculate Average per Student");
            System.out.println("9. Find Best Student");
            System.out.println("10. List Failing Students (Average < 10)\"");
            System.out.println("11. Show All Statistics");
            System.out.println("0. Exit");

            boolean validInput = false;
            while (!validInput) {
                System.out.print("Enter choice: ");
                try {
                    choice = scanner.nextInt();
                    scanner.nextLine();
                    validInput = true;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number only!");
                    scanner.nextLine();
                }
            }

            switch (choice) {
                case 1 -> studentService.addStudent();
                case 2 -> studentService.viewStudents();
                case 3 -> studentService.updateStudent();
                case 4 -> studentService.deleteStudent();
                //case 5-> manageCoursesMenu();
                case 5 -> addCourseToStudent();
                case 6 -> updateCourse();
                case 7 -> removeCourse();
                case 8 -> calculateAverageNotePerStudent();
                case 9 -> findBestStudent();
                case 10 -> listFailingStudents();
                case 11 -> showAllStatistics();
                case 0 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid choice.");
            }

        } while (choice != 0);
    }


    private void manageCoursesMenu() {
        System.out.print("Enter student ID: ");
        int studentId = scanner.nextInt();
        scanner.nextLine();
        Student student = studentService.getStudentById(studentId);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        int choice;
        do {
            System.out.println("\n--- Manage Courses for " + student.getName() + " ---");
            System.out.println("1. Add Course");
            System.out.println("2. Update Course");
            System.out.println("3. Remove Course");
            System.out.println("4. View Courses");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Course name: ");
                    String name = scanner.nextLine();
                    System.out.print("Course note (0-20): ");
                    float note = scanner.nextFloat();
                    scanner.nextLine();
                    try {
                        Course course = new Course.CourseBuilder().withName(name).withNote(note).build();
                        student.addCourse(course);
                    } catch (InvalidCourseException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 2 -> {
                    System.out.print("Course name to update: ");
                    String name = scanner.nextLine();
                    System.out.print("New note: ");
                    float newNote = scanner.nextFloat();
                    scanner.nextLine();
                    student.updateCourse(name, newNote);
                }
                case 3 -> {
                    System.out.print("Course name to remove: ");
                    String name = scanner.nextLine();
                    student.removeCourse(name);
                }
                case 4 -> {
                    System.out.println("Courses for " + student.getName() + ":");
                    for (Course c : student.getCourses()) {
                        System.out.println(c);
                    }
                }
                case 0 -> System.out.println("Returning to Main Menu...");
                default -> System.out.println("Invalid choice.");
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

    //Store student data in a file
    private void storefileMenu() {
        File file = new File("students.txt");
        ArrayList<Student> studentArrayList = new ArrayList<>();

        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file);
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                studentArrayList = (ArrayList<Student>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }


        studentArrayList.addAll(studentService.getAllStudents());
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(studentArrayList);
            System.out.println("Successfully stored students.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readerfileMenu() {
        File file = new File("students.txt");
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

    public void addCourseToStudent() {
        File file = new File("students.txt");
        ArrayList<Student> studentArrayList = new ArrayList<>();

        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file);
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                studentArrayList = (ArrayList<Student>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Erreur lors de la lecture du fichier : " + e.getMessage());
                return;
            }
        }

        if (studentArrayList.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        int id = -1;
        boolean validInput = false;
        while (!validInput) {
            System.out.print("Enter student ID: ");
            try {
                id = scanner.nextInt();
                scanner.nextLine();
                validInput = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Enter numbers only!");
                scanner.nextLine();
            }
        }


        Student targetStudent = null;
        for (Student s : studentArrayList) {
            if (s.getId() == id) {
                targetStudent = s;
                break;
            }
        }

        if (targetStudent == null) {
            System.out.println("Student not found.");
            return;
        }


        System.out.print("Enter course name: ");
        String courseName = scanner.nextLine();
        float note = -1;
        validInput = false;
        while (!validInput) {
            System.out.print("Enter course note (0-20): ");
            try {
                note = scanner.nextFloat();
                scanner.nextLine();
                validInput = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Enter a number for the note!");
                scanner.nextLine();
            }
        }

        try {
            Course newCourse = new Course.CourseBuilder()
                    .withName(courseName)
                    .withNote(note)
                    .build();

            targetStudent.getCourses().add(newCourse); // Assure-toi que Student a ArrayList<Course> courses


            try (FileOutputStream fos = new FileOutputStream(file);
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(studentArrayList);
                System.out.println("Course added successfully to student " + targetStudent.getName());
            } catch (IOException e) {
                System.out.println("Erreur lors de la sauvegarde : " + e.getMessage());
            }

        } catch (InvalidCourseException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    public void updateCourse() {
        File file = new File("students.txt");
        ArrayList<Student> studentArrayList = new ArrayList<>();


        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file);
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                studentArrayList = (ArrayList<Student>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Erreur lors de la lecture du fichier : " + e.getMessage());
                return;
            }
        }

        if (studentArrayList.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        Scanner scanner = new Scanner(System.in);


        int id = -1;
        boolean validInput = false;
        while (!validInput) {
            System.out.print("Enter student ID: ");
            try {
                id = scanner.nextInt();
                scanner.nextLine();
                validInput = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Enter numbers only!");
                scanner.nextLine();
            }
        }


        Student targetStudent = null;
        for (Student s : studentArrayList) {
            if (s.getId() == id) {
                targetStudent = s;
                break;
            }
        }

        if (targetStudent == null) {
            System.out.println("Student not found.");
            return;
        }

        if (targetStudent.getCourses().isEmpty()) {
            System.out.println("This student has no courses yet.");
            return;
        }


        System.out.print("Enter course name to update: ");
        String courseName = scanner.nextLine();

        Course courseToUpdate = null;
        for (Course c : targetStudent.getCourses()) {
            if (c.getName().equalsIgnoreCase(courseName)) {
                courseToUpdate = c;
                break;
            }
        }

        if (courseToUpdate == null) {
            System.out.println("Course not found for this student.");
            return;
        }


        System.out.print("Enter new course name (leave empty to keep current): ");
        String newName = scanner.nextLine();
        if (!newName.isBlank()) {
            courseToUpdate.setName(newName);
        }


        float newNote = -1;
        validInput = false;
        while (!validInput) {
            System.out.print("Enter new note (0-20, leave empty to keep current): ");
            String noteInput = scanner.nextLine();
            if (noteInput.isBlank()) {
                validInput = true; // garder note actuelle
            } else {
                try {
                    newNote = Float.parseFloat(noteInput);
                    if (newNote < 0 || newNote > 20) {
                        System.out.println("Note must be between 0 and 20!");
                    } else {
                        courseToUpdate.setNote(newNote);
                        validInput = true;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number, try again.");
                }
            }
        }


        for (int i = 0; i < studentArrayList.size(); i++) {
            if (studentArrayList.get(i).getId() == targetStudent.getId()) {
                studentArrayList.set(i, targetStudent);
                break;
            }
        }

        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(studentArrayList);
            System.out.println("Course updated successfully for student " + targetStudent.getName());
        } catch (IOException e) {
            System.out.println("Erreur lors de la sauvegarde : " + e.getMessage());
        }
    }
    public void removeCourse() {
        File file = new File("students.txt");
        ArrayList<Student> studentArrayList = new ArrayList<>();

        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file);
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                studentArrayList = (ArrayList<Student>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Erreur lors de la lecture du fichier : " + e.getMessage());
                return;
            }
        }

        if (studentArrayList.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        Scanner scanner = new Scanner(System.in);


        int id = -1;
        boolean validInput = false;
        while (!validInput) {
            System.out.print("Enter student ID: ");
            try {
                id = scanner.nextInt();
                scanner.nextLine();
                validInput = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Enter numbers only!");
                scanner.nextLine();
            }
        }


        Student targetStudent = null;
        for (Student s : studentArrayList) {
            if (s.getId() == id) {
                targetStudent = s;
                break;
            }
        }

        if (targetStudent == null) {
            System.out.println("Student not found.");
            return;
        }

        if (targetStudent.getCourses().isEmpty()) {
            System.out.println("This student has no courses yet.");
            return;
        }


        System.out.print("Enter course name to remove: ");
        String courseName = scanner.nextLine();

        boolean removed = targetStudent.getCourses().removeIf(c -> c.getName().equalsIgnoreCase(courseName));

        if (!removed) {
            System.out.println("Course not found for this student.");
            return;
        }


        for (int i = 0; i < studentArrayList.size(); i++) {
            if (studentArrayList.get(i).getId() == targetStudent.getId()) {
                studentArrayList.set(i, targetStudent);
                break;
            }
        }

        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(studentArrayList);
            System.out.println("Course removed successfully from student " + targetStudent.getName());
        } catch (IOException e) {
            System.out.println("Erreur lors de la sauvegarde : " + e.getMessage());
        }
    }
    public void calculateAverageNotePerStudent() {
        File file = new File("students.txt");
        ArrayList<Student> studentArrayList = new ArrayList<>();

        // Charger les étudiants depuis le fichier
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file);
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                studentArrayList = (ArrayList<Student>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Erreur lors de la lecture du fichier : " + e.getMessage());
                return;
            }
        }

        // Vérifier si la liste est vide
        if (studentArrayList.isEmpty()) {
            System.out.println("Aucun étudiant trouvé dans le fichier.");
            return;
        }

        // Calculer et afficher la moyenne de chaque étudiant
        for (Student student : studentArrayList) {
            List<Course> courses = student.getCourses();
            if (courses == null || courses.isEmpty()) {
                System.out.println(student.getName() + " n’a aucun cours enregistré.");
                continue;
            }

            float totalNotes = 0;
            for (Course c : courses) {
                totalNotes += c.getNote();
            }

            float moyenne = totalNotes / courses.size();
            System.out.println(student.getName() + " (" + student.getId() + ") → Moyenne : " + moyenne);
        }
    }

    public void findBestStudent() {
        File file = new File("students.txt");
        ArrayList<Student> studentArrayList = new ArrayList<>();

        // Charger les étudiants depuis le fichier
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file);
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                studentArrayList = (ArrayList<Student>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Erreur lors de la lecture du fichier : " + e.getMessage());
                return;
            }
        }

        if (studentArrayList.isEmpty()) {
            System.out.println("Aucun étudiant trouvé dans le fichier.");
            return;
        }

        Student bestStudent = null;
        float bestAverage = -1;

        // Calculer la moyenne de chaque étudiant et trouver le meilleur
        for (Student student : studentArrayList) {
            List<Course> courses = student.getCourses();
            if (courses == null || courses.isEmpty()) {
                continue; // ignorer les étudiants sans cours
            }

            float total = 0;
            for (Course c : courses) {
                total += c.getNote();
            }
            float avg = total / courses.size();

            if (avg > bestAverage) {
                bestAverage = avg;
                bestStudent = student;
            }
        }

        if (bestStudent != null) {
            System.out.println("Best Student");
            System.out.println("Name: " + bestStudent.getName());
            System.out.println("ID: " + bestStudent.getId());
            System.out.println("Average Note: " + bestAverage);
        } else {
            System.out.println("Aucun étudiant avec des cours trouvés.");
        }
    }
    public void listFailingStudents() {
        File file = new File("students.txt");
        ArrayList<Student> studentArrayList = new ArrayList<>();

        // Charger les étudiants depuis le fichier
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file);
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                studentArrayList = (ArrayList<Student>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Erreur lors de la lecture du fichier : " + e.getMessage());
                return;
            }
        }

        if (studentArrayList.isEmpty()) {
            System.out.println("Aucun étudiant trouvé dans le fichier.");
            return;
        }

        boolean hasFailing = false;

        System.out.println("\n⚠️ Liste des étudiants ayant une moyenne < 10 ⚠️");
        for (Student student : studentArrayList) {
            List<Course> courses = student.getCourses();
            if (courses == null || courses.isEmpty()) {
                continue; // ignorer les étudiants sans cours
            }

            float total = 0;
            for (Course c : courses) {
                total += c.getNote();
            }
            float average = total / courses.size();

            if (average < 10) {
                hasFailing = true;
                System.out.println("→ " + student.getName() + " (ID: " + student.getId() + ") - Moyenne: " + average);
            }
        }

        if (!hasFailing) {
            System.out.println("Tous les étudiants ont une moyenne supérieure ou égale à 10 ✅");
        }
    }
    public void showAllStatistics() {
        File file = new File("students.txt");
        ArrayList<Student> students = new ArrayList<>();

        // Charger les étudiants depuis le fichier
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file);
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                students = (ArrayList<Student>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Erreur lors de la lecture du fichier : " + e.getMessage());
                return;
            }
        }

        if (students.isEmpty()) {
            System.out.println("⚠️ Aucun étudiant trouvé dans le fichier.");
            return;
        }

        int totalStudents = students.size();
        int failingStudents = 0;
        float totalAllAverages = 0;
        Student bestStudent = null;
        float bestAverage = 0;

        for (Student student : students) {
            List<Course> courses = student.getCourses();
            if (courses == null || courses.isEmpty()) {
                continue;
            }

            float total = 0;
            for (Course c : courses) {
                total += c.getNote();
            }

            float average = total / courses.size();
            totalAllAverages += average;

            if (average < 10) {
                failingStudents++;
            }

            if (average > bestAverage) {
                bestAverage = average;
                bestStudent = student;
            }
        }

        float classAverage = totalAllAverages / totalStudents;

        System.out.println("\n📊===== STATISTIQUES GÉNÉRALES =====📊");
        System.out.println("👩‍🎓 Nombre total d'étudiants : " + totalStudents);
        System.out.println("📈 Moyenne générale de la classe : " + String.format("%.2f", classAverage));
        System.out.println("⚠️ Étudiants ayant une moyenne < 10 : " + failingStudents);

        if (bestStudent != null) {
            System.out.println("🏅 Meilleur étudiant : " + bestStudent.getName() +
                    " (ID: " + bestStudent.getId() + ") - Moyenne : " +
                    String.format("%.2f", bestAverage));
        } else {
            System.out.println("Aucun étudiant avec des cours trouvés.");
        }

        System.out.println("=====================================\n");
    }





}
