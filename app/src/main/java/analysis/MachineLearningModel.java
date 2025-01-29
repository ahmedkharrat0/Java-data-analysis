package analysis;

import data.DatabaseManager;
import data.EmissionRecord;
import weka.classifiers.trees.J48;
import weka.core.*;
import java.sql.*;
import java.util.*;

public class MachineLearningModel {
    private DatabaseManager dbManager;

    public MachineLearningModel(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public void runClassification() {
        // Fetch data from co2 and ch4 tables
        List<EmissionRecord> co2Records = fetchDataFromDatabase("co2");
        List<EmissionRecord> ch4Records = fetchDataFromDatabase("ch4");

        // Process the data for both CO2 and CH4
        classifyEmissions(co2Records, "co2");
        classifyEmissions(ch4Records, "ch4");
    }

    private List<EmissionRecord> fetchDataFromDatabase(String table) {
        List<EmissionRecord> records = new ArrayList<>();
        String query = "SELECT * FROM " + table;
        try (ResultSet rs = dbManager.getData(query)) {
            while (rs.next()) {
                String domain = rs.getString("domain");
                String area = rs.getString("area");
                String emissions = table; // CO2 or CH4
                String source = rs.getString("source");
                int year = rs.getInt("year");
                String unit = rs.getString("unit");
                double value = rs.getDouble("value");

                records.add(new EmissionRecord(domain, area, emissions, source, year, unit, value));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    private void classifyEmissions(List<EmissionRecord> records, String tableName) {
        // Calculate mean and standard deviation
        double mean = calculateMean(records);
        double stdDev = calculateStandardDeviation(records, mean);

        // Create training data for Weka
        ArrayList<Attribute> attributes = createAttributes();
        Instances data = new Instances("EmissionData", attributes, records.size());
        data.setClassIndex(data.numAttributes() - 1);

        // Populate the Instances object with data
        for (EmissionRecord record : records) {
            double[] values = new double[data.numAttributes()];
            values[0] = record.getValue(); // Emission value
            values[1] = classifyEmission(record.getValue(), mean, stdDev); // Classify into Low, Moderate, High
            data.add(new DenseInstance(1.0, values));
        }

        // Train the classifier using Weka's J48 Decision Tree
        try {
            J48 tree = new J48();  // J48 is Weka's implementation of a decision tree
            tree.buildClassifier(data);

            // Output the classifier's decision tree
            System.out.println("Decision Tree for " + tableName + " emissions:");
            System.out.println(tree);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double calculateMean(List<EmissionRecord> records) {
        double sum = 0;
        for (EmissionRecord record : records) {
            sum += record.getValue();
        }
        return sum / records.size();
    }

    private double calculateStandardDeviation(List<EmissionRecord> records, double mean) {
        double sum = 0;
        for (EmissionRecord record : records) {
            sum += Math.pow(record.getValue() - mean, 2);
        }
        return Math.sqrt(sum / records.size());
    }

    private int classifyEmission(double value, double mean, double stdDev) {
        if (value <= mean - stdDev) {
            return 0; // Low
        } else if (value > mean - stdDev && value <= mean + stdDev) {
            return 1; // Moderate
        } else {
            return 2; // High
        }
    }

    private ArrayList<Attribute> createAttributes() {
        // Define attributes for Weka
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("value"));  // Emission value
        ArrayList<String> classValues = new ArrayList<>(Arrays.asList("Low", "Moderate", "High"));
        attributes.add(new Attribute("class", classValues));  // Class attribute
        return attributes;
    }
}
