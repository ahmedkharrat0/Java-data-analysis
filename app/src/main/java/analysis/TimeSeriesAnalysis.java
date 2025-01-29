package analysis;

import data.EmissionRecord;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import java.util.List;

public class TimeSeriesAnalysis {

    private List<EmissionRecord> data;

    public TimeSeriesAnalysis(List<EmissionRecord> data) {
        this.data = data;
    }

    // Method to create a time series line chart for emissions data
    public static ChartPanel createTimeSeriesChart(List<EmissionRecord> data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Adding data to dataset (Year vs Emission Value)
        for (EmissionRecord record : data) {
            dataset.addValue(record.getValue(), record.getEmissions(), String.valueOf(record.getYear()));
        }

        // Creating the line chart
        JFreeChart chart = ChartFactory.createLineChart(
                "Emissions Over Time", // Chart title
                "Year", // X-axis label
                "Emission Value", // Y-axis label
                dataset // Dataset containing the data points
        );

        return new ChartPanel(chart); // Return the chart panel to be displayed
    }

    // Method for linear regression on the time series data
    public void performLinearRegression() {
        SimpleRegression regression = new SimpleRegression();

        // Adding data points to the regression model (Year vs Emission Value)
        for (EmissionRecord record : data) {
            regression.addData(record.getYear(), record.getValue());
        }

        // Get the regression results
        double slope = regression.getSlope();
        double intercept = regression.getIntercept();
        double rSquare = regression.getRSquare();

        // Print out the results
        System.out.println("Linear Regression Model for " + data.get(0).getEmissions() + ":");
        System.out.println("Slope: " + slope);
        System.out.println("Intercept: " + intercept);
        System.out.println("R-Square: " + rSquare);

        // Evaluate the model by comparing predictions with actual values
        evaluateModel(regression);
    }

    // Optional: Method to perform ARIMA (AutoRegressive Integrated Moving Average)
    // ARIMA is a time series forecasting method that can be added if you want to
    // implement it or use an external library like Deeplearning4j or Smile
    public static void performARIMA(List<EmissionRecord> data) {
        // Implement ARIMA model here or use an external library for advanced
        // forecasting
        // This method can be developed based on your future project needs.
    }

    // Method to evaluate the predictions made by the regression model
    private void evaluateModel(SimpleRegression regression) {
        for (EmissionRecord record : data) {
            double predictedValue = regression.predict(record.getYear()); // Predicted emission value
            double actualValue = record.getValue(); // Actual emission value
            double difference = Math.abs(predictedValue - actualValue); // Difference between actual and predicted

            // The results are no longer printed out
        }
    }

    // Optional: Method to generate a trendline for the data to visualize the
    // general direction
    public static ChartPanel createTrendlineChart(List<EmissionRecord> data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Adding data to dataset (Year vs Emission Value)
        for (EmissionRecord record : data) {
            dataset.addValue(record.getValue(), record.getEmissions(), String.valueOf(record.getYear()));
        }

        // Creating the trendline chart (adding the regression line)
        JFreeChart chart = ChartFactory.createLineChart(
                "Emissions Trend with Regression Line", // Chart title
                "Year", // X-axis label
                "Emission Value", // Y-axis label
                dataset // Dataset containing the data points
        );

        // You could add a regression line to the chart using the regression slope and
        // intercept
        // chart.addXYPlot(...);

        return new ChartPanel(chart); // Return the trendline chart panel
    }
}
