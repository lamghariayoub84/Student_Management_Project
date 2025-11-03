package service;

import model.Student;
import exception.StudentNotFoundException;

import java.util.HashMap;
import java.util.Map;

public class StudentService {
    private Map<Integer, Student> studentMap = new HashMap<>();

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
}
