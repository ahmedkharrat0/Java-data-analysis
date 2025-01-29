package analysis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import data.EmissionRecord;

public class RegressionAnalysis implements Analysis {
    private double slope; // Instance variable to store slope
    private double intercept; // Instance variable to store intercept

    @Override
    public void analyze(List<EmissionRecord> data1, List<EmissionRecord> data2) {
        // Transform and filter data for CO2 and CH4 emissions, only for "World"
        Map<Integer, Double> co2Data = filterAndTransform(data1, "CO2", "World");
        Map<Integer, Double> ch4Data = filterAndTransform(data2, "CH4", "World");

        // Perform regression analysis for CO2 and CH4 emissions
        System.out.println("Performing regression analysis for CO2 emissions:");
        performRegression(co2Data); // Regression for CO2

        System.out.println("Testing CO2 predictions:");
        testPredictions(co2Data);

        System.out.println("Performing regression analysis for CH4 emissions:");
        performRegression(ch4Data); // Regression for CH4

        System.out.println("Testing CH4 predictions:");
        testPredictions(ch4Data);
    }

    // Method to filter and transform data
    private Map<Integer, Double> filterAndTransform(List<EmissionRecord> data, String emissionType, String areaFilter) {
        Map<Integer, Double> transformedData = new HashMap<>();
        for (EmissionRecord record : data) {
            if (emissionType.equalsIgnoreCase(record.getEmissions()) &&
                    areaFilter.equalsIgnoreCase(record.getArea())) {
                transformedData.put(record.getYear(), record.getValue()); // Use getValue()
            }
        }
        return transformedData;
    }

    // Method to perform simple linear regression
    public void performRegression(Map<Integer, Double> data) {
        // Perform linear regression to find the relationship between years and
        // emissions
        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;
        int n = data.size(); // The number of data points

        // Calculate the necessary summations
        for (Map.Entry<Integer, Double> entry : data.entrySet()) {
            int year = entry.getKey();
            double emission = entry.getValue();

            sumX += year;
            sumY += emission;
            sumXY += year * emission;
            sumX2 += year * year;
        }

        // Calculate the slope (m) and intercept (b) of the regression line (y = mx + b)
        double denominator = n * sumX2 - sumX * sumX;
        if (denominator == 0) {
            System.out.println("Unable to perform regression: Insufficient data.");
            return;
        }

        slope = (n * sumXY - sumX * sumY) / denominator;
        intercept = (sumY * sumX2 - sumX * sumXY) / denominator;

        // Output the regression equation
        System.out.println("Regression Equation: y = " + slope + "x + " + intercept);
    }

    // Method to predict emission value (y) for a given year (x)
    public double predict(int year) {
        return slope * year + intercept;
    }

    // Method to test predictions against actual values
    private void testPredictions(Map<Integer, Double> emissionData) {
        for (Map.Entry<Integer, Double> entry : emissionData.entrySet()) {
            int year = entry.getKey();
            double actualValue = entry.getValue();
            double predictedValue = predict(year);
            System.out.println("Year: " + year);
            System.out.println("Actual Value: " + actualValue);
            System.out.println("Predicted Value: " + predictedValue);
            System.out.println("Difference: " + Math.abs(actualValue - predictedValue));
            System.out.println();
        }
    }
}
