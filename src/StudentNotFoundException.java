/**
 * Checked exception thrown when an operation references a student id that does
 * not exist in the current record set (search / update / delete).
 */
public class StudentNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    public StudentNotFoundException(int id) {
        super("No student found with ID: " + id);
    }
}
