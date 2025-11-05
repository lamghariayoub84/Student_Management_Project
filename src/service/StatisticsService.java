package service;

import model.Student;
import strategy.StatisticsStrategy;
import java.util.List;

/**
 * Service class to manage statistics calculations using Strategy pattern
 */
public class StatisticsService {

    private StatisticsStrategy strategy;

    /**
     * Set the strategy to use for statistics calculation
     * @param strategy The statistics strategy
     */
    public void setStrategy(StatisticsStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Execute the current strategy on the list of students
     * @param students The list of students
     */
    public void executeStrategy(List<Student> students) {
        if (strategy == null) {
            System.out.println("No strategy set.");
            return;
        }
        strategy.execute(students);
    }
}