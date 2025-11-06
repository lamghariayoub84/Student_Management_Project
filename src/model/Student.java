package model;

import java.util.ArrayList;
import java.util.List;

public class Student {
    private int id;
    private String name;
    private String email;

    //Tâche 2
    private List<Course> courses;

    public Student(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.courses = new ArrayList<>();
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<Course> getCourses() { return courses; }

    @Override
    public String toString() {
        return "ID: " + id + " | Name: " + name + " | Email: " + email + " | Courses: " + courses;
    }

    /**
     * Add a course to the student's list of courses.
     * @param course The Course object to add
     */
    public void addCourse(Course course) {
        courses.add(course);
        System.out.println("Course added: " + course);
    }


    /**
     * Update the note of an existing course in the student's list.
     * @param courseName The name of the course to update
     * @param newNote The new note to set (0-20)
     * @return true if the course was found and updated, false otherwise
     */
    public boolean updateCourse(String courseName, float newNote) {
        for (Course c : courses) {
            if (c.getName().equalsIgnoreCase(courseName)) {
                c.setNote(newNote);
                System.out.println("Course updated: " + c);
                return true;
            }
        }
        System.out.println("Course not found: " + courseName);
        return false;
    }


    /**
     * Remove a course from the student's list by its name.
     * @param courseName The name of the course to remove
     * @return true if the course was found and removed, false otherwise
     */
    public boolean removeCourse(String courseName) {
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getName().equalsIgnoreCase(courseName)) {
                System.out.println("Course removed: " + courses.get(i));
                courses.remove(i);
                return true;
            }
        }
        System.out.println("Course not found: " + courseName);
        return false;
    }
}
