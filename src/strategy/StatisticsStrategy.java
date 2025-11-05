package strategy;

import model.Student;
import java.util.List;

/**
 * Strategy interface for different statistics calculations
 */
public interface StatisticsStrategy {
    void execute(List<Student> students);
}