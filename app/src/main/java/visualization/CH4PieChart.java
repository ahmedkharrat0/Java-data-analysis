package visualization;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import data.EmissionRecord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CH4PieChart {

    public static JFreeChart createCH4Chart(List<EmissionRecord> data) {
        // Filter and aggregate data by area, excluding "World"
        Map<String, Double> areaEmissions = new HashMap<>();
        for (EmissionRecord record : data) {
            if ("CH4".equalsIgnoreCase(record.getEmissions()) && !"World".equalsIgnoreCase(record.getArea())) {
                areaEmissions.merge(record.getArea(), record.getValue(), Double::sum);
            }
        }

        // Create a dataset for the pie chart
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Map.Entry<String, Double> entry : areaEmissions.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        // Create the pie chart
        JFreeChart chart = ChartFactory.createPieChart(
            "CH4 Emissions by Area",  // Chart Title
            dataset,                 // Dataset
            true,                     // Include legend
            true,
            java.util.Locale.getDefault()// Tooltips
        );

        // Customize the plot (no depth method available in PiePlot)
        PiePlot plot = (PiePlot) chart.getPlot();
        // Customize other aspects like section colors or label formatting
        // plot.setSectionDepth(0.30);  // This line is causing the issue, remove it or use another effect

        return chart;
    }

    public static ChartPanel createChartPanel(List<EmissionRecord> data) {
        JFreeChart chart = createCH4Chart(data);
        return new ChartPanel(chart);
    }
}
