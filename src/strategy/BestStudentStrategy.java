package strategy;

import model.Student;
import java.util.List;

/**
 * Strategy to find and display the student with the best average
 */
public class BestStudentStrategy implements StatisticsStrategy {

    @Override
    public void execute(List<Student> students) {
        if (students.isEmpty()) {
            System.out.println("No students available.");
            return;
        }

        Student bestStudent = null;
        float bestAverage = -1;

        for (Student s : students) {
            float average = AverageCalculationStrategy.calculateAverage(s);
            if (average > bestAverage) {
                bestAverage = average;
                bestStudent = s;
            }
        }

        System.out.println("\n===== Best Student =====");
        if (bestStudent != null && bestAverage >= 0) {
            System.out.printf("Student: %s | Average: %.2f/20%n", bestStudent.getName(), bestAverage);
        } else {
            System.out.println("No student has courses enrolled.");
        }
    }
}