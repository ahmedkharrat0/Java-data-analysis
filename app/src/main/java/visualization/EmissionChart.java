package visualization;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import data.EmissionRecord;
import java.util.List;

public abstract class EmissionChart {
    protected List<EmissionRecord> data;

    public EmissionChart(List<EmissionRecord> data) {
        this.data = data;
    }

    public abstract DefaultCategoryDataset createDataset();

    public JFreeChart createChart() {
        DefaultCategoryDataset dataset = createDataset();
        return ChartFactory.createLineChart(
            getChartTitle(),
            "Year",
            "Value",
            dataset
        );
    }

    public ChartPanel createChartPanel() {
        JFreeChart chart = createChart();
        return new ChartPanel(chart);
    }

    // Abstract method to get the title of the chart
    protected abstract String getChartTitle();
}
