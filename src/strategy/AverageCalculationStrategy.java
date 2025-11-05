package strategy;

import model.Course;
import model.Student;
import java.util.List;

/**
 * Strategy to calculate and display the average note for each student
 */
public class AverageCalculationStrategy implements StatisticsStrategy {

    @Override
    public void execute(List<Student> students) {
        if (students.isEmpty()) {
            System.out.println("No students available.");
            return;
        }

        System.out.println("\n===== Average Notes per Student =====");
        for (Student s : students) {
            float average = calculateAverage(s);
            if (average >= 0) {
                System.out.printf("Student: %s | Average: %.2f/20%n", s.getName(), average);
            } else {
                System.out.printf("Student: %s | No courses enrolled%n", s.getName());
            }
        }
    }

    /**
     * Calculate the average note for a student
     * @param student The student
     * @return The average note, or -1 if no courses
     */
    public static float calculateAverage(Student student) {
        List<Course> courses = student.getCourses();
        if (courses.isEmpty()) {
            return -1;
        }

        float sum = 0;
        for (Course c : courses) {
            sum += c.getNote();
        }
        return sum / courses.size();
    }
}