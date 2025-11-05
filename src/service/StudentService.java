package service;

import model.Course;
import model.Student;
import exception.StudentNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentService {
    private Map<Integer, Student> studentMap = new HashMap<>();
    private List<Student> students = new ArrayList<>();

    public void addStudent(Student student) {
        if (studentMap.containsKey(student.getId())) {
            System.out.println("ID déjà utilisé. Étudiant non ajouté.");
            return;
        }
        studentMap.put(student.getId(), student);
        System.out.println("Étudiant ajouté avec succès.");
    }

    public Student findStudentById(int id) {
        Student student = studentMap.get(id);
        if (student == null) {
            throw new StudentNotFoundException("Étudiant avec ID " + id + " introuvable.");
        }
        return student;
    }

    public void updateStudent(int id, Student updatedStudent) {
        if (!studentMap.containsKey(id)) {
            throw new StudentNotFoundException("Impossible de mettre à jour : ID " + id + " introuvable.");
        }
        studentMap.put(id, updatedStudent);
        System.out.println("Étudiant mis à jour.");
    }

    public void deleteStudent(int id) {
        if (!studentMap.containsKey(id)) {
            throw new StudentNotFoundException("Impossible de supprimer : ID " + id + " introuvable.");
        }
        studentMap.remove(id);
        System.out.println("Étudiant supprimé.");
    }

    public void viewAllStudents() {
        if (studentMap.isEmpty()) {
            System.out.println("Aucun étudiant enregistré.");
            return;
        }
        for (Student student : studentMap.values()) {
            System.out.println("ID: " + student.getId() + ", Nom: " + student.getName() + ", Email: " + student.getEmail());
        }
    }

    public Student getStudentById(int id) {
        return studentMap.get(id);
    }

    public void addCourseToStudent(int studentId, Course course) {
        Student s = getStudentById(studentId);
        if(s != null) s.addCourse(course);
        else System.out.println("Student not found.");
    }

    public void updateStudentCourse(int studentId, String courseName, float newNote) {
        Student s = getStudentById(studentId);
        if(s != null) s.updateCourse(courseName, newNote);
        else System.out.println("Student not found.");
    }

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
