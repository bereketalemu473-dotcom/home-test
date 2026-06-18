import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Persists student records to a human-readable text file.
 *
 * <ul>
 *   <li>Writing is done with {@link PrintWriter}.</li>
 *   <li>Reading is done with {@link Scanner}.</li>
 * </ul>
 *
 * <p>Each record is stored on its own line using a pipe ('|') delimiter so the
 * file stays readable, e.g.:
 * <pre>101|Abel Tesfaye|Computer Science|3.75</pre></p>
 */
public class TextFileStorage {

    private static final String DELIMITER = "|";
    /** Pre-compiled regex form of the delimiter for splitting. */
    private static final String SPLIT_REGEX = "\\|";

    /** Saves all students to the given text file. */
    public void save(List<Student> students, String path) throws IOException {
        // try-with-resources guarantees the writer is closed even on error.
        try (PrintWriter writer = new PrintWriter(path)) {
            for (Student s : students) {
                writer.println(String.join(DELIMITER,
                        String.valueOf(s.getId()),
                        s.getName(),
                        s.getDepartment(),
                        String.valueOf(s.getGpa())));
            }
        }
    }

    /** Loads all students from the given text file. */
    public List<Student> load(String path) throws FileNotFoundException {
        List<Student> students = new ArrayList<>();
        File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException("Text file not found: " + path);
        }
        try (Scanner scanner = new Scanner(file)) {
            int lineNo = 0;
            while (scanner.hasNextLine()) {
                lineNo++;
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue; // skip blank lines
                }
                String[] parts = line.split(SPLIT_REGEX, -1);
                if (parts.length != 4) {
                    System.out.println("  [warning] Skipping malformed line "
                            + lineNo + ": " + line);
                    continue;
                }
                try {
                    int id = Integer.parseInt(parts[0].trim());
                    double gpa = Double.parseDouble(parts[3].trim());
                    students.add(new Student(id, parts[1], parts[2], gpa));
                } catch (NumberFormatException e) {
                    System.out.println("  [warning] Skipping line " + lineNo
                            + " (bad number): " + line);
                }
            }
        }
        return students;
    }
}
