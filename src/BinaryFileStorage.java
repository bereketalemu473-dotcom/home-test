import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Persists student records to a compact binary file.
 *
 * <ul>
 *   <li>Writing uses {@link DataOutputStream} (each field written with its
 *       primitive-specific method, e.g. {@code writeInt}, {@code writeUTF}).</li>
 *   <li>Reading uses {@link DataInputStream} and stops when an
 *       {@link EOFException} signals the end of the file.</li>
 * </ul>
 *
 * <p>The streams are wrapped in buffered streams for efficiency.</p>
 */
public class BinaryFileStorage {

    /** Saves all students to the given binary file. */
    public void save(List<Student> students, String path) throws IOException {
        try (DataOutputStream out = new DataOutputStream(
                new BufferedOutputStream(new FileOutputStream(path)))) {
            for (Student s : students) {
                out.writeInt(s.getId());
                out.writeUTF(s.getName());
                out.writeUTF(s.getDepartment());
                out.writeDouble(s.getGpa());
            }
        }
    }

    /** Loads all students from the given binary file. */
    public List<Student> load(String path) throws IOException {
        List<Student> students = new ArrayList<>();
        File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException("Binary file not found: " + path);
        }
        try (DataInputStream in = new DataInputStream(
                new BufferedInputStream(new FileInputStream(path)))) {
            // Keep reading records until we hit the end of the stream.
            while (true) {
                int id = in.readInt();           // throws EOFException at end
                String name = in.readUTF();
                String department = in.readUTF();
                double gpa = in.readDouble();
                students.add(new Student(id, name, department, gpa));
            }
        } catch (EOFException eof) {
            // Expected: we have read every record in the file.
        }
        return students;
    }
}
