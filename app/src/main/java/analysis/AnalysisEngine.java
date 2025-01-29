package analysis;

import java.util.List;
import data.EmissionRecord;

public class AnalysisEngine {
    private Analysis analysis;

    // Constructor to inject specific analysis type (e.g., CorrelationAnalysis)
    public AnalysisEngine(Analysis analysis) {
        this.analysis = analysis;
    }

    // Updated method to handle analysis requiring two datasets
    public void runAnalysis(List<EmissionRecord> data1, List<EmissionRecord> data2) {
        analysis.analyze(data1, data2);  // Polymorphic call with two datasets
    }
}
