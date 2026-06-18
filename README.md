# Student Record Management System (Java File I/O & Streams)

A console-based OOP application for managing student records, demonstrating
Java **File I/O**, **byte/character/object streams**, the **Streams API**, the
**File class**, **buffered streams**, and **exception handling**.

## Features

| Requirement                  | Where it is implemented                                  |
|------------------------------|----------------------------------------------------------|
| `Student` class (ID, Name, Department, GPA) | `Student.java` (implements `Serializable`) |
| Add / Search / Update / Delete / Display | `StudentManager.java` + menu in `Main.java` |
| Text storage (`PrintWriter`, `Scanner`)   | `TextFileStorage.java`                     |
| Binary storage (`DataOutputStream`, `DataInputStream`) | `BinaryFileStorage.java`       |
| Object serialization (`ObjectOutputStream`, `ObjectInputStream`) | `SerializationStorage.java` |
| Report (Total / Highest / Lowest / Average GPA) via Streams | `ReportGenerator.java`     |
| `File` class: auto-create dirs/files, show properties | `FileManager.java`               |
| Backup using **buffered streams** | `FileManager.backup(...)`                           |
| Exception handling           | Throughout (custom `StudentNotFoundException`, try/with-resources, input validation) |

## Project layout

```
Bereket Project/
├── src/                 # Java source files
│   ├── Student.java
│   ├── StudentNotFoundException.java
│   ├── StudentManager.java
│   ├── TextFileStorage.java
│   ├── BinaryFileStorage.java
│   ├── SerializationStorage.java
│   ├── ReportGenerator.java
│   ├── FileManager.java
│   └── Main.java
├── out/                 # Compiled .class files (created by compile)
├── data/                # Auto-created at runtime
│   ├── students.txt     # text format
│   ├── students.dat     # binary format
│   └── students.ser     # serialized objects
├── backup/              # Auto-created; timestamped backups
├── compile.bat
├── run.bat
└── README.md
```

## How to compile and run

Requires JDK 8+ (developed/tested on JDK 21).

### Using the batch scripts (Windows)
```
compile.bat
run.bat
```

### Manually
```
javac -d out src\*.java
java -cp out Main
```

## Menu

```
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
```

## Notes

- Records are kept in memory during a session; use the **Save** options (7/9/11)
  to persist them and the **Load** options (8/10/12) to read them back.
- The text file uses a pipe (`|`) delimiter so records stay human-readable.
- Backups are written to `backup/` with a timestamped filename, e.g.
  `students_backup_20260618_090651.txt`.
- GPA input is validated to the range `0.0 – 4.0`; duplicate IDs are rejected.
