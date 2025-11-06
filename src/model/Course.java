package model;

import exception.InvalidCourseException;

/**
 * Class representing a Course with a name and a note (0-20).
 * Provides a Builder pattern to create Course instances safely with validation.
 */
public class Course {

    private String name;
    private float note;

    // Private constructor to enforce the use of Builder
    private Course(){}

    /**
     * Builder class for Course
     * Allows setting name and note, and validates them when building
     */
    public static class CourseBuilder{

        private String name;
        private float note;

        // Set the name of the course
        public CourseBuilder withName(String name)
        {
            this.name = name;
            return this;
        }

        // Set the note of the course
        public CourseBuilder withNote(float note)
        {
            this.note = note;
            return this;
        }

        /**
         * Build the Course instance
         * @return Course object
         * @throws InvalidCourseException if name is empty or note is out of range
         */
        public Course build() throws InvalidCourseException {
            if(name == null || name.isEmpty())
            {
                throw new InvalidCourseException("Course name cannot be empty!");
            }
            if(note < 0 || note > 20)
            {
                throw new InvalidCourseException("Note must be between 0 and 20!");
            }
            Course course = new Course();
            course.name = name;
            course.note = note;
            return course;
        }
    }

    // Getter for name
    public String getName()
    {
        return this.name;
    }

    // Getter for note
    public float getNote()
    {
        return this.note;
    }

    // Setter for  name
    public void setName(String name)
    {
        this.name = name;
    }

    // Setter for note
    public void setNote(float note)
    {
        this.note = note;
    }

    // Returns a string representation of the course
    @Override
    public String toString()
    {
        return "Course : " + this.name + " ----> " + this.note;
    }

    /**
     * Equals method to compare courses by name (case-insensitive)
     */
    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        Course other = (Course) obj;
        return this.name != null && this.name.equalsIgnoreCase(other.name);
    }

    /**
     * Hash code based on the course name (case-insensitive)
     */
    @Override
    public int hashCode() {
        return name == null ? 0 : name.toLowerCase().hashCode();
    }

}