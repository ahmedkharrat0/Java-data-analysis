package analysis;

import data.EmissionRecord;
import java.util.List;

public interface Analysis {
    void analyze(List<EmissionRecord> data1, List<EmissionRecord> data2);
}
