package analysis;

import data.EmissionRecord;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class DescriptiveStatistics {

    // Method to calculate the mean
    public static double mean(List<EmissionRecord> data) {
        double sum = 0;
        for (EmissionRecord record : data) {
            sum += record.getValue(); // Assuming getValue() returns the emission value
        }
        return sum / data.size();
    }

    // Method to calculate the median
    public static double median(List<EmissionRecord> data) {
        List<Double> values = new ArrayList<>();
        for (EmissionRecord record : data) {
            values.add(record.getValue());
        }
        Collections.sort(values);
        int size = values.size();
        if (size % 2 == 0) {
            return (values.get(size / 2 - 1) + values.get(size / 2)) / 2;
        } else {
            return values.get(size / 2);
        }
    }

    // Method to calculate the mode
    public static double mode(List<EmissionRecord> data) {
        Map<Double, Integer> frequencyMap = new HashMap<>();
        for (EmissionRecord record : data) {
            double value = record.getValue();
            frequencyMap.put(value, frequencyMap.getOrDefault(value, 0) + 1);
        }
        return frequencyMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .get().getKey();
    }

    // Method to calculate the standard deviation
    public static double standardDeviation(List<EmissionRecord> data) {
        double mean = mean(data);
        double sumOfSquares = 0;
        for (EmissionRecord record : data) {
            sumOfSquares += Math.pow(record.getValue() - mean, 2);
        }
        return Math.sqrt(sumOfSquares / data.size());
    }
}
