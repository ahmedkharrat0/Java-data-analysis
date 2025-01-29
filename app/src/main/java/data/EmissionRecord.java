package data;

public class EmissionRecord {
    private String domain;
    private String area;
    private String emissions;
    private String source;
    private int year;
    private String unit;
    private double value;

    public EmissionRecord(String domain, String area, String emissions, String source, int year, String unit, double value) {
        this.domain = domain;
        this.area = area;
        this.emissions = emissions;
        this.source = source;
        this.year = year;
        this.unit = unit;
        this.value = value;
    }

    public String getDomain() {
        return domain;
    }

    public String getArea() {
        return area;
    }

    public String getEmissions() {
        return emissions;
    }

    public String getSource() {
        return source;
    }

    public int getYear() {
        return year;
    }

    public String getUnit() {
        return unit;
    }

    public double getValue() {
        return value;
    }
}
