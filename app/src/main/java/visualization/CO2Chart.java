package visualization;

import org.jfree.data.category.DefaultCategoryDataset;
import data.EmissionRecord;

import java.util.List;

public class CO2Chart extends EmissionChart {

    public CO2Chart(List<EmissionRecord> data) {
        super(data);
    }

    @Override
    public DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (EmissionRecord record : data) {
            if ("CO2".equalsIgnoreCase(record.getEmissions())) {
                dataset.addValue(record.getValue(), record.getArea(), String.valueOf(record.getYear()));
            }
        }
        return dataset;
    }

    @Override
    protected String getChartTitle() {
        return "CO2 Emissions by Year and Area";
    }
}
