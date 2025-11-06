package strategy;

import model.Student;
import java.util.ArrayList;
import java.util.List;

/**
 * Strategy to list students with average below 10 (failing students)
 */
public class FailingStudentsStrategy implements StatisticsStrategy {

    private static final float PASSING_THRESHOLD = 10.0f;

    @Override
    public void execute(List<Student> students) {
        if (students.isEmpty()) {
            System.out.println("No students available.");
            return;
        }

        List<Student> failingStudents = new ArrayList<>();

        for (Student s : students) {
            float average = AverageCalculationStrategy.calculateAverage(s);
            if (average >= 0 && average < PASSING_THRESHOLD) {
                failingStudents.add(s);
            }
        }

        System.out.println("\n===== Failing Students (Average < 10) =====");
        if (failingStudents.isEmpty()) {
            System.out.println("No failing students found.");
        } else {
            for (Student s : failingStudents) {
                float average = AverageCalculationStrategy.calculateAverage(s);
                System.out.printf("Student: %s | Average: %.2f/20%n", s.getName(), average);
            }
        }
    }
}