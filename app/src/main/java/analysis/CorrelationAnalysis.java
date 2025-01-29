package analysis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import data.EmissionRecord;

public class CorrelationAnalysis implements Analysis {

    @Override
    public void analyze(List<EmissionRecord> data1, List<EmissionRecord> data2) {
        // Filter data for CO2 and CH4 emissions, only for the "World"
        Map<Integer, Double> co2Values = new HashMap<>();
        Map<Integer, Double> ch4Values = new HashMap<>();

        // Process only "World" data
        for (EmissionRecord record : data1) {
            if ("World".equalsIgnoreCase(record.getArea())) {  // Filter by "World"
                if ("CO2".equalsIgnoreCase(record.getEmissions())) {
                    co2Values.put(record.getYear(), record.getValue());
                }
            }
        }

        for (EmissionRecord record : data2) {
            if ("World".equalsIgnoreCase(record.getArea())) {  // Filter by "World"
                if ("CH4".equalsIgnoreCase(record.getEmissions())) {
                    ch4Values.put(record.getYear(), record.getValue());
                }
            }
        }

        // Calculate correlation between CO2 and CH4 values by matching years
        double correlation = calculateCorrelation(co2Values, ch4Values);
        System.out.println("Correlation between CO2 and CH4 emissions (World only): " + correlation);
    }

    // Method to calculate correlation (for simplicity, using Pearson's correlation)
    private double calculateCorrelation(Map<Integer, Double> co2Values, Map<Integer, Double> ch4Values) {
        // Ensure the data is aligned by year
        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0, sumY2 = 0;
        int n = co2Values.size();  // Assuming both maps have the same size

        for (Integer year : co2Values.keySet()) {
            if (ch4Values.containsKey(year)) {
                double x = co2Values.get(year);
                double y = ch4Values.get(year);
                sumX += x;
                sumY += y;
                sumXY += x * y;
                sumX2 += x * x;
                sumY2 += y * y;
            }
        }

        // Calculate Pearson's correlation coefficient
        double numerator = n * sumXY - sumX * sumY;
        double denominator = Math.sqrt((n * sumX2 - sumX * sumX) * (n * sumY2 - sumY * sumY));

        if (denominator == 0) {
            return 0;  // Avoid division by zero if no correlation
        }

        return numerator / denominator;
    }
}
