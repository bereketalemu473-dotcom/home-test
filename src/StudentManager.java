import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * In-memory store that holds the working set of {@link Student} records and
 * provides the core CRUD operations:
 *
 * <ul>
 *   <li>Add Student</li>
 *   <li>Search Student by ID</li>
 *   <li>Update Student Information</li>
 *   <li>Delete Student</li>
 *   <li>Display all Students</li>
 * </ul>
 *
 * <p>The persistence layer (text / binary / serialization) reads from and
 * writes to this manager, keeping I/O concerns separate from business logic.</p>
 */
public class StudentManager {

    private final List<Student> students = new ArrayList<>();

    /**
     * Adds a new student.
     *
     * @throws IllegalArgumentException if a student with the same id already exists.
     */
    public void addStudent(Student student) {
        if (findById(student.getId()).isPresent()) {
            throw new IllegalArgumentException(
                    "A student with ID " + student.getId() + " already exists.");
        }
        students.add(student);
    }

    /** Internal helper that returns the matching student if present. */
    private Optional<Student> findById(int id) {
        return students.stream()
                .filter(s -> s.getId() == id)
                .findFirst();
    }

    /**
     * Searches for a student by id.
     *
     * @throws StudentNotFoundException if no such student exists.
     */
    public Student searchById(int id) throws StudentNotFoundException {
        return findById(id).orElseThrow(() -> new StudentNotFoundException(id));
    }

    /**
     * Updates the editable fields of an existing student. Passing {@code null}
     * for a String field or a negative value for gpa leaves that field
     * unchanged, so the caller can update only what is needed.
     *
     * @throws StudentNotFoundException if no such student exists.
     */
    public void updateStudent(int id, String name, String department, double gpa)
            throws StudentNotFoundException {
        Student student = searchById(id);
        if (name != null && !name.isBlank())            student.setName(name);
        if (department != null && !department.isBlank()) student.setDepartment(department);
        if (gpa >= 0)                                    student.setGpa(gpa);
    }

    /**
     * Deletes the student with the given id.
     *
     * @throws StudentNotFoundException if no such student exists.
     */
    public void deleteStudent(int id) throws StudentNotFoundException {
        Student student = searchById(id);
        students.remove(student);
    }

    /** @return an unmodifiable view of all students. */
    public List<Student> getAllStudents() {
        return Collections.unmodifiableList(students);
    }

    /** Replaces the entire working set (used when loading records from disk). */
    public void replaceAll(List<Student> loaded) {
        students.clear();
        students.addAll(loaded);
    }

    public int size() {
        return students.size();
    }

    public boolean isEmpty() {
        return students.isEmpty();
    }
}
