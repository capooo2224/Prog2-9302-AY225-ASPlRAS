import java.io.*;
import java.text.*;
import java.util.*;

// ─── Separate class for each CSV row ───
class DataRecord {
    private String[] headers;
    private String[] values;

    public DataRecord(String[] headers, String[] values) {
        this.headers = headers;
        this.values = values;
    }

    public String get(String columnName) {
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].equalsIgnoreCase(columnName)) {
                return (i < values.length) ? values[i] : "";
            }
        }
        return null;
    }

    public String[] getHeaders() { return headers; }
    public String[] getValues()  { return values; }

    public double getDouble(String columnName) {
        String val = get(columnName);
        if (val == null || val.trim().isEmpty()) return 0.0;
        try {
            return Double.parseDouble(val.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < headers.length; i++) {
            sb.append(headers[i]).append(": ").append(i < values.length ? values[i] : "").append("  |  ");
        }
        return sb.toString();
    }
}

// ─── Main application ───
public class app {

    // Prompt user for CSV path, validate, and return the File
    private static File getValidFile(Scanner input) {
        File file;
        while (true) {
            System.out.print("Enter dataset file path: ");
            String path = input.nextLine().trim();

            file = new File(path);

            if (!file.exists() || !file.isFile()) {
                System.out.println("Error: File not found. Please try again.");
                continue;
            }
            if (!file.canRead()) {
                System.out.println("Error: File is not readable. Please try again.");
                continue;
            }
            if (!path.toLowerCase().endsWith(".csv")) {
                System.out.println("Error: File is not a CSV file. Please try again.");
                continue;
            }
            break;
        }
        return file;
    }

    // Read CSV into a list of DataRecord objects using BufferedReader
    private static List<DataRecord> readCSV(File file) throws IOException {
        List<DataRecord> records = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(file));

        String headerLine = br.readLine();
        if (headerLine == null || headerLine.trim().isEmpty()) {
            br.close();
            throw new IOException("CSV file is empty or has no header row.");
        }

        String[] headers = headerLine.split(",", -1);
        for (int i = 0; i < headers.length; i++) {
            headers[i] = headers[i].trim();
        }

        String line;
        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            String[] values = line.split(",", -1);
            for (int i = 0; i < values.length; i++) {
                values[i] = values[i].trim();
            }
            records.add(new DataRecord(headers, values));
        }
        br.close();
        return records;
    }

    // Auto-detect which column holds dates and which holds sales amounts
    private static String[] detectColumns(String[] headers) {
        String[] dateKeywords  = {"date", "month", "order_date", "transaction_date", "sold_date"};
        String[] salesKeywords = {"sales", "amount", "revenue", "total", "price", "income"};

        String dateCol  = null;
        String salesCol = null;

        for (String h : headers) {
            String lower = h.toLowerCase();
            if (dateCol == null) {
                for (String k : dateKeywords) {
                    if (lower.contains(k)) { dateCol = h; break; }
                }
            }
            if (salesCol == null) {
                for (String k : salesKeywords) {
                    if (lower.contains(k)) { salesCol = h; break; }
                }
            }
        }
        return new String[]{dateCol, salesCol};
    }

    // Extract "YYYY-MM" month key from a date string
    private static String extractMonthKey(String dateStr) {
        // Try common date formats
        String[] formats = {
            "MM/dd/yyyy", "M/d/yyyy", "yyyy-MM-dd", "dd-MM-yyyy",
            "MM-dd-yyyy", "yyyy/MM/dd", "dd/MM/yyyy", "M/d/yy"
        };

        for (String fmt : formats) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(fmt, Locale.ENGLISH);
                sdf.setLenient(false);
                Date d = sdf.parse(dateStr.trim());
                SimpleDateFormat out = new SimpleDateFormat("yyyy-MM");
                return out.format(d);
            } catch (ParseException e) {
                // try next format
            }
        }
        return null;
    }

    // Convert "YYYY-MM" key to a readable month name like "January 2024"
    private static String getMonthName(String monthKey) {
        String[] parts = monthKey.split("-");
        int year  = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);

        String[] names = {"", "January", "February", "March", "April", "May", "June",
                          "July", "August", "September", "October", "November", "December"};
        return names[month] + " " + year;
    }

    // Group records by month and compute total sales per month
    private static TreeMap<String, double[]> groupByMonth(List<DataRecord> records, String dateCol, String salesCol) {
        // TreeMap keeps keys sorted (ascending by YYYY-MM)
        TreeMap<String, double[]> monthlyData = new TreeMap<>();

        for (DataRecord r : records) {
            String dateVal  = r.get(dateCol);
            double salesVal = r.getDouble(salesCol);

            if (dateVal == null || dateVal.trim().isEmpty()) continue;

            String monthKey = extractMonthKey(dateVal);
            if (monthKey == null) continue;

            // double[0] = totalSales, double[1] = transactionCount
            if (!monthlyData.containsKey(monthKey)) {
                monthlyData.put(monthKey, new double[]{0.0, 0});
            }
            monthlyData.get(monthKey)[0] += salesVal;
            monthlyData.get(monthKey)[1] += 1;
        }

        return monthlyData;
    }

    // Display the sorted monthly summary table and identify the best month
    private static void displayMonthlySummary(TreeMap<String, double[]> monthlyData) {
        if (monthlyData.isEmpty()) {
            System.out.println("No valid monthly data found.");
            return;
        }

        // Find best-performing month
        String bestMonthKey = null;
        double bestSales = Double.MIN_VALUE;
        double grandTotal = 0;

        for (Map.Entry<String, double[]> entry : monthlyData.entrySet()) {
            double sales = entry.getValue()[0];
            grandTotal += sales;
            if (sales > bestSales) {
                bestSales = sales;
                bestMonthKey = entry.getKey();
            }
        }

        // Column widths
        int monthWidth = 20;
        int salesWidth = 18;
        int countWidth = 14;

        String border = "+" + "-".repeat(monthWidth + 2) + "+"
                       + "-".repeat(salesWidth + 2) + "+"
                       + "-".repeat(countWidth + 2) + "+";

        System.out.println("\n===== MONTHLY PERFORMANCE SUMMARY =====\n");
        System.out.println(border);
        System.out.printf("| %-" + monthWidth + "s | %-" + salesWidth + "s | %-" + countWidth + "s |%n",
                          "Month", "Total Sales", "Transactions");
        System.out.println(border);

        for (Map.Entry<String, double[]> entry : monthlyData.entrySet()) {
            String name  = getMonthName(entry.getKey());
            double sales = entry.getValue()[0];
            int count    = (int) entry.getValue()[1];
            System.out.printf("| %-" + monthWidth + "s | %" + salesWidth + ".2f | %" + countWidth + "d |%n",
                              name, sales, count);
        }

        System.out.println(border);

        System.out.printf("%nTotal Months Analyzed: %d%n", monthlyData.size());
        System.out.printf("Grand Total Sales   : %.2f%n", grandTotal);
        System.out.printf("%n*** Best-Performing Month: %s with total sales of %.2f ***%n%n",
                          getMonthName(bestMonthKey), bestSales);
    }

    // ─── Entry point ───
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("========================================");
        System.out.println("    Monthly Performance Analyzer");
        System.out.println("========================================\n");

        // 1. Get a valid CSV file from the user
        File csvFile = getValidFile(input);

        // 2. Read and parse the CSV
        List<DataRecord> records;
        try {
            records = readCSV(csvFile);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            input.close();
            return;
        }

        System.out.printf("%nDataset loaded: %d records%n", records.size());
        System.out.printf("Columns: %s%n", String.join(", ", records.get(0).getHeaders()));

        // 3. Detect date and sales columns
        String[] detected = detectColumns(records.get(0).getHeaders());
        String dateCol  = detected[0];
        String salesCol = detected[1];

        if (dateCol == null || salesCol == null) {
            System.out.println("\nError: Could not auto-detect date or sales columns.");
            System.out.println("Available columns: " + String.join(", ", records.get(0).getHeaders()));
            System.out.println("Please ensure your CSV has a date column and a sales/amount column.");
            input.close();
            return;
        }

        System.out.printf("%nDetected date column  : %s%n", dateCol);
        System.out.printf("Detected sales column : %s%n", salesCol);

        // 4. Group by month, compute totals, sort ascending
        TreeMap<String, double[]> monthlyData = groupByMonth(records, dateCol, salesCol);

        // 5. Display sorted monthly summary and best month
        displayMonthlySummary(monthlyData);

        System.out.println("========================================");
        System.out.println("         Analysis Complete");
        System.out.println("========================================");

        input.close();
    }
}
