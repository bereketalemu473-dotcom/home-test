import java.io.Serializable;

/**
 * Model class that represents a single student record.
 *
 * <p>Implements {@link Serializable} so that whole {@code Student} objects can
 * be written to / read from disk using {@link java.io.ObjectOutputStream} and
 * {@link java.io.ObjectInputStream}.</p>
 */
public class Student implements Serializable {

    /** Version id used by Java's serialization mechanism. */
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String department;
    private double gpa;

    public Student(int id, String name, String department, double gpa) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.gpa = gpa;
    }

    // ----- Getters -------------------------------------------------------
    public int getId()            { return id; }
    public String getName()       { return name; }
    public String getDepartment() { return department; }
    public double getGpa()        { return gpa; }

    // ----- Setters -------------------------------------------------------
    public void setName(String name)             { this.name = name; }
    public void setDepartment(String department) { this.department = department; }
    public void setGpa(double gpa)               { this.gpa = gpa; }

    /**
     * @return a single line, table-style description of the student suitable
     *         for printing to the console.
     */
    @Override
    public String toString() {
        return String.format("%-6d | %-20s | %-15s | %5.2f",
                id, name, department, gpa);
    }
}
