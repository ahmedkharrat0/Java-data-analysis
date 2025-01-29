package visualization;

import org.jfree.data.category.DefaultCategoryDataset;
import data.EmissionRecord;

import java.util.List;

public class CH4Chart extends EmissionChart {

    public CH4Chart(List<EmissionRecord> data) {
        super(data);
    }

    @Override
    public DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (EmissionRecord record : data) {
            if ("CH4".equalsIgnoreCase(record.getEmissions())) {
                dataset.addValue(record.getValue(), record.getArea(), String.valueOf(record.getYear()));
            }
        }
        return dataset;
    }

    @Override
    protected String getChartTitle() {
        return "CH4 Emissions by Year and Area";
    }
}
