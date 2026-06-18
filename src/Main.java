import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Console entry point for the Student Record Management System.
 *
 * <p>Presents a menu that exercises every required feature: CRUD operations,
 * three persistence formats (text / binary / serialization), a statistics
 * report, file-property inspection and buffered backups — all wrapped in
 * proper exception handling.</p>
 */
public class Main {

    private final Scanner in = new Scanner(System.in);
    private final StudentManager manager = new StudentManager();
    private final TextFileStorage textStorage = new TextFileStorage();
    private final BinaryFileStorage binaryStorage = new BinaryFileStorage();
    private final SerializationStorage serialStorage = new SerializationStorage();
    private final ReportGenerator reportGenerator = new ReportGenerator();
    private final FileManager fileManager = new FileManager();

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        // Make sure the data/ and backup/ folders + files exist before we start.
        fileManager.initStorage();
        System.out.println("\nWelcome to the Student Record Management System");

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter your choice: ");
            System.out.println();
            switch (choice) {
                case 1  -> addStudent();
                case 2  -> searchStudent();
                case 3  -> updateStudent();
                case 4  -> deleteStudent();
                case 5  -> displayAll();
                case 6  -> System.out.println(reportGenerator.generate(manager.getAllStudents()));
                case 7  -> saveText();
                case 8  -> loadText();
                case 9  -> saveBinary();
                case 10 -> loadBinary();
                case 11 -> saveSerialized();
                case 12 -> loadSerialized();
                case 13 -> fileManager.printAllFileProperties();
                case 14 -> createBackup();
                case 0  -> running = false;
                default -> System.out.println("Invalid choice. Please pick a number from the menu.");
            }
        }
        System.out.println("Goodbye!");
    }

    private void printMenu() {
        System.out.println("""

                =========== MAIN MENU ===========
                 1.  Add Student
                 2.  Search Student by ID
                 3.  Update Student
                 4.  Delete Student
                 5.  Display All Students
                 6.  Generate Report
                 7.  Save to Text File        (PrintWriter)
                 8.  Load from Text File      (Scanner)
                 9.  Save to Binary File      (DataOutputStream)
                 10. Load from Binary File    (DataInputStream)
                 11. Save with Serialization  (ObjectOutputStream)
                 12. Load with Serialization  (ObjectInputStream)
                 13. Show File Properties
                 14. Create Backup            (Buffered Streams)
                 0.  Exit
                =================================""");
    }

    // ----- CRUD operations ----------------------------------------------

    private void addStudent() {
        try {
            int id = readInt("Student ID: ");
            String name = readLine("Name: ");
            String department = readLine("Department: ");
            double gpa = readGpa("GPA (0.0 - 4.0): ");
            manager.addStudent(new Student(id, name, department, gpa));
            System.out.println("Student added successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void searchStudent() {
        int id = readInt("Enter Student ID to search: ");
        try {
            Student student = manager.searchById(id);
            printHeader();
            System.out.println(student);
        } catch (StudentNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateStudent() {
        int id = readInt("Enter Student ID to update: ");
        try {
            // Show the current record first.
            Student current = manager.searchById(id);
            printHeader();
            System.out.println(current);
            System.out.println("Leave a field blank to keep its current value.");

            String name = readLine("New Name: ");
            String department = readLine("New Department: ");
            String gpaInput = readLine("New GPA: ");
            double gpa = gpaInput.isBlank() ? -1 : Double.parseDouble(gpaInput);

            manager.updateStudent(id,
                    name.isBlank() ? null : name,
                    department.isBlank() ? null : department,
                    gpa);
            System.out.println("Student updated successfully.");
        } catch (StudentNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error: GPA must be a number. Update aborted.");
        }
    }

    private void deleteStudent() {
        int id = readInt("Enter Student ID to delete: ");
        try {
            manager.deleteStudent(id);
            System.out.println("Student deleted successfully.");
        } catch (StudentNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void displayAll() {
        List<Student> students = manager.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students to display.");
            return;
        }
        printHeader();
        students.forEach(System.out::println);
        System.out.println("Total: " + students.size() + " student(s).");
    }

    private void printHeader() {
        System.out.println(String.format("%-6s | %-20s | %-15s | %5s",
                "ID", "Name", "Department", "GPA"));
        System.out.println("-------------------------------------------------------");
    }

    // ----- Persistence: Text --------------------------------------------

    private void saveText() {
        try {
            textStorage.save(manager.getAllStudents(), FileManager.TEXT_FILE);
            System.out.println("Saved " + manager.size() + " record(s) to " + FileManager.TEXT_FILE);
        } catch (IOException e) {
            System.out.println("Error saving text file: " + e.getMessage());
        }
    }

    private void loadText() {
        try {
            List<Student> loaded = textStorage.load(FileManager.TEXT_FILE);
            manager.replaceAll(loaded);
            System.out.println("Loaded " + loaded.size() + " record(s) from text file.");
        } catch (IOException e) {
            System.out.println("Error loading text file: " + e.getMessage());
        }
    }

    // ----- Persistence: Binary ------------------------------------------

    private void saveBinary() {
        try {
            binaryStorage.save(manager.getAllStudents(), FileManager.BINARY_FILE);
            System.out.println("Saved " + manager.size() + " record(s) to " + FileManager.BINARY_FILE);
        } catch (IOException e) {
            System.out.println("Error saving binary file: " + e.getMessage());
        }
    }

    private void loadBinary() {
        try {
            List<Student> loaded = binaryStorage.load(FileManager.BINARY_FILE);
            manager.replaceAll(loaded);
            System.out.println("Loaded " + loaded.size() + " record(s) from binary file.");
        } catch (IOException e) {
            System.out.println("Error loading binary file: " + e.getMessage());
        }
    }

    // ----- Persistence: Serialization -----------------------------------

    private void saveSerialized() {
        try {
            serialStorage.save(manager.getAllStudents(), FileManager.SERIAL_FILE);
            System.out.println("Saved " + manager.size() + " record(s) to " + FileManager.SERIAL_FILE);
        } catch (IOException e) {
            System.out.println("Error during serialization: " + e.getMessage());
        }
    }

    private void loadSerialized() {
        try {
            List<Student> loaded = serialStorage.load(FileManager.SERIAL_FILE);
            manager.replaceAll(loaded);
            System.out.println("Loaded " + loaded.size() + " record(s) via deserialization.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error during deserialization: " + e.getMessage());
        }
    }

    // ----- Backup --------------------------------------------------------

    private void createBackup() {
        // Back up whichever data files currently exist.
        String[] sources = { FileManager.TEXT_FILE, FileManager.BINARY_FILE, FileManager.SERIAL_FILE };
        for (String source : sources) {
            try {
                File backup = fileManager.backup(source);
                System.out.println("Backup created: " + backup.getAbsolutePath());
            } catch (IOException e) {
                System.out.println("Skipped " + source + ": " + e.getMessage());
            }
        }
    }

    // ----- Input helpers (with validation) ------------------------------

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = in.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    private double readGpa(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = in.nextLine().trim();
            try {
                double value = Double.parseDouble(line);
                if (value < 0.0 || value > 4.0) {
                    System.out.println("GPA must be between 0.0 and 4.0.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number for GPA.");
            }
        }
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return in.nextLine().trim();
    }
}
