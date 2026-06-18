import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Utility class built around {@link java.io.File} that handles everything to do
 * with the file system itself:
 *
 * <ul>
 *   <li>Creating the required directories and data files automatically.</li>
 *   <li>Displaying file properties (name, path, size, last modified date).</li>
 *   <li>Creating timestamped backups using <b>buffered streams</b>.</li>
 * </ul>
 */
public class FileManager {

    /** Directory where the live records are stored. */
    public static final String DATA_DIR   = "data";
    /** Directory where backups are written. */
    public static final String BACKUP_DIR = "backup";

    public static final String TEXT_FILE   = DATA_DIR + File.separator + "students.txt";
    public static final String BINARY_FILE = DATA_DIR + File.separator + "students.dat";
    public static final String SERIAL_FILE = DATA_DIR + File.separator + "students.ser";

    private static final DateTimeFormatter STAMP =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    /**
     * Creates the {@code data} and {@code backup} directories (and the empty
     * data files) if they do not already exist. Uses the {@link File} class.
     */
    public void initStorage() {
        createDirectory(DATA_DIR);
        createDirectory(BACKUP_DIR);
        createFileIfMissing(TEXT_FILE);
        createFileIfMissing(BINARY_FILE);
        createFileIfMissing(SERIAL_FILE);
    }

    private void createDirectory(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                System.out.println("Created directory: " + dir.getAbsolutePath());
            } else {
                System.out.println("Could not create directory: " + dir.getAbsolutePath());
            }
        }
    }

    private void createFileIfMissing(String path) {
        File file = new File(path);
        try {
            if (!file.exists() && file.createNewFile()) {
                System.out.println("Created file: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println("Could not create file " + path + ": " + e.getMessage());
        }
    }

    /**
     * Prints the properties of a single file using the {@link File} class:
     * name, absolute path, size and last-modified date.
     */
    public void printFileProperties(String path) {
        File file = new File(path);
        System.out.println("------------------------------------");
        if (!file.exists()) {
            System.out.println("File does not exist: " + path);
            System.out.println("------------------------------------");
            return;
        }
        System.out.println("Name          : " + file.getName());
        System.out.println("Absolute path : " + file.getAbsolutePath());
        System.out.println("Size          : " + file.length() + " bytes");
        System.out.println("Last modified : " + new Date(file.lastModified()));
        System.out.println("Readable      : " + file.canRead());
        System.out.println("Writable      : " + file.canWrite());
        System.out.println("------------------------------------");
    }

    /** Prints the properties of all three data files. */
    public void printAllFileProperties() {
        printFileProperties(TEXT_FILE);
        printFileProperties(BINARY_FILE);
        printFileProperties(SERIAL_FILE);
    }

    /**
     * Creates a backup copy of the given source file inside the backup
     * directory, using {@link BufferedInputStream} / {@link BufferedOutputStream}
     * to copy the bytes efficiently. The backup filename carries a timestamp.
     *
     * @return the {@link File} that was written.
     */
    public File backup(String sourcePath) throws IOException {
        File source = new File(sourcePath);
        if (!source.exists()) {
            throw new IOException("Cannot back up; source file is missing: " + sourcePath);
        }

        String stamp = LocalDateTime.now().format(STAMP);
        String backupName = stripExtension(source.getName()) + "_backup_" + stamp
                + extension(source.getName());
        File target = new File(BACKUP_DIR, backupName);

        try (BufferedInputStream in =
                     new BufferedInputStream(new FileInputStream(source));
             BufferedOutputStream out =
                     new BufferedOutputStream(new FileOutputStream(target))) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
        return target;
    }

    private String stripExtension(String fileName) {
        int dot = fileName.lastIndexOf('.');
        return (dot == -1) ? fileName : fileName.substring(0, dot);
    }

    private String extension(String fileName) {
        int dot = fileName.lastIndexOf('.');
        return (dot == -1) ? "" : fileName.substring(dot);
    }
}
