import java.util.DoubleSummaryStatistics;
import java.util.List;

/**
 * Produces a statistical report over the current student records using the
 * Java Streams API:
 *
 * <ul>
 *   <li>Total number of students</li>
 *   <li>Highest GPA</li>
 *   <li>Lowest GPA</li>
 *   <li>Average GPA</li>
 * </ul>
 */
public class ReportGenerator {

    /** @return a formatted, multi-line report string. */
    public String generate(List<Student> students) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n========== STUDENT REPORT ==========\n");

        if (students == null || students.isEmpty()) {
            sb.append("No student records available.\n");
            sb.append("====================================\n");
            return sb.toString();
        }

        // A single stream pass collects count, min, max, sum and average.
        DoubleSummaryStatistics stats = students.stream()
                .mapToDouble(Student::getGpa)
                .summaryStatistics();

        sb.append(String.format("Total Students : %d%n", stats.getCount()));
        sb.append(String.format("Highest GPA    : %.2f%n", stats.getMax()));
        sb.append(String.format("Lowest GPA     : %.2f%n", stats.getMin()));
        sb.append(String.format("Average GPA    : %.2f%n", stats.getAverage()));

        // Identify which student(s) hold the top GPA, again via streams.
        students.stream()
                .filter(s -> s.getGpa() == stats.getMax())
                .findFirst()
                .ifPresent(top -> sb.append(String.format(
                        "Top Student    : %s (ID %d)%n", top.getName(), top.getId())));

        sb.append("====================================\n");
        return sb.toString();
    }
}
