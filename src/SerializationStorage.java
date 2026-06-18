import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Persists student records using Java Object Serialization.
 *
 * <p>The entire {@code List<Student>} is written as one object graph with
 * {@link ObjectOutputStream} and read back with {@link ObjectInputStream}.
 * This works because {@link Student} implements {@link java.io.Serializable}.</p>
 */
public class SerializationStorage {

    /** Serializes the list of students to the given file. */
    public void save(List<Student> students, String path) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(path)))) {
            // ArrayList is itself Serializable, so the whole list is written at once.
            out.writeObject(new ArrayList<>(students));
        }
    }

    /** Deserializes the list of students from the given file. */
    @SuppressWarnings("unchecked")
    public List<Student> load(String path) throws IOException, ClassNotFoundException {
        File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException("Serialized file not found: " + path);
        }
        try (ObjectInputStream in = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(path)))) {
            Object obj = in.readObject();
            if (obj instanceof List<?>) {
                return new ArrayList<>((List<Student>) obj);
            }
            throw new IOException("File does not contain a valid student list.");
        }
    }
}
