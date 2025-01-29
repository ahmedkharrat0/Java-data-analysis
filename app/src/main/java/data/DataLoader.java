package data;

import org.apache.spark.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {
    private final SparkSession spark;
    private DatabaseManager dbManager;


    public DataLoader(SparkSession spark, DatabaseManager dbManager) {
        this.spark = spark;
        this.dbManager = dbManager;

    }

    public List<EmissionRecord> loadCsv(String[] filePaths) {
        List<EmissionRecord> records = new ArrayList<>();
        for (String filePath : filePaths) {
            try {
                Dataset<Row> df = spark.read()
                    .option("header", "true")
                    .option("inferSchema", "true")
                    .csv(filePath);

                // Debugging: Print the schema and first rows of the dataset
                df.printSchema();
                df.show(5);

                df.collectAsList().forEach(row -> {
                    String domain = row.getString(row.fieldIndex("Domain"));
                    String area = row.getString(row.fieldIndex("Area"));
                    String emissions = row.getString(row.fieldIndex("Emissions")).trim(); // Ensure no whitespace issues
                    String source = row.getString(row.fieldIndex("Source"));
                    int year = row.getInt(row.fieldIndex("Year"));
                    String unit = row.getString(row.fieldIndex("Unit"));
                    double value = row.getDouble(row.fieldIndex("Value"));

                    // Create and add an EmissionRecord
                    records.add(new EmissionRecord(domain, area, emissions, source, year, unit, value));
                });
            } catch (Exception e) {
                System.err.println("Error loading file: " + filePath);
                e.printStackTrace();
            }
        }
        return records;
    }
}