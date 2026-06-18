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

