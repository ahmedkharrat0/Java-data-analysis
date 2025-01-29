package com.example.DataAnalysisPrj;

import data.DataLoader;
import data.EmissionRecord;
import analysis.*;
import data.DatabaseManager;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartPanel;
import visualization.CO2Chart;
import visualization.CH4Chart;
import visualization.CO2PieChart;
import visualization.CH4PieChart;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class UserInterface {
    private JFrame frame;
    private JButton uploadButton;
    private JButton analyzeButton;
    private JButton displayChartsButton;
    private JButton regressionButton;
    private JTable resultTable;
    private DefaultTableModel tableModel;
    private DataLoader dataLoader;
    private DatabaseManager dbManager;
    private List<EmissionRecord> loadedData;
    private JPanel chartPanel;

    public UserInterface(DataLoader dataLoader, DatabaseManager dbManager) {
        this.dataLoader = dataLoader;
        this.dbManager = dbManager;
        this.loadedData = new ArrayList<>();
        frame = new JFrame("Data Analysis Tool");
        frame.setSize(1000, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        uploadButton = new JButton("Upload File");
        analyzeButton = new JButton("Run Analysis");
        displayChartsButton = new JButton("Display Charts");
        regressionButton = new JButton("Run Regression");
        
        tableModel = new DefaultTableModel();
        resultTable = new JTable(tableModel);
        tableModel.addColumn("Metric");
        tableModel.addColumn("Value");
        
        chartPanel = new JPanel();
        chartPanel.setLayout(new GridLayout(2, 2));
        
        panel.add(uploadButton);
        panel.add(analyzeButton);
        panel.add(displayChartsButton);
        panel.add(regressionButton);
        frame.add(panel, BorderLayout.NORTH);
        frame.add(new JScrollPane(resultTable), BorderLayout.WEST);
        frame.add(chartPanel, BorderLayout.CENTER);

        uploadButton.addActionListener(new UploadAction());
        analyzeButton.addActionListener(new AnalyzeAction());
        displayChartsButton.addActionListener(new DisplayChartsAction());
        regressionButton.addActionListener(new RegressionAction());

        frame.setVisible(true);
    }

    private class UploadAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                List<EmissionRecord> newData = dataLoader.loadCsv(new String[]{selectedFile.getAbsolutePath()});
                loadedData.addAll(newData);
                tableModel.addRow(new Object[]{"File Selected", selectedFile.getName()});
                tableModel.addRow(new Object[]{"Data Loaded", newData.size() + " records"});
            }
        }
    }

    private class AnalyzeAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (loadedData.isEmpty()) {
                tableModel.addRow(new Object[]{"Error", "No data loaded. Please upload a file first."});
                return;
            }
            tableModel.addRow(new Object[]{"Status", "Running analysis..."});
            
            tableModel.addRow(new Object[]{"Mean", DescriptiveStatistics.mean(loadedData)});
            tableModel.addRow(new Object[]{"Median", DescriptiveStatistics.median(loadedData)});
            tableModel.addRow(new Object[]{"Mode", DescriptiveStatistics.mode(loadedData)});
            tableModel.addRow(new Object[]{"Standard Deviation", DescriptiveStatistics.standardDeviation(loadedData)});
            
            new AnalysisEngine(new CorrelationAnalysis()).runAnalysis(loadedData, loadedData);
            new MachineLearningModel(dbManager).runClassification();
            tableModel.addRow(new Object[]{"Status", "Analysis completed."});
        }
    }

    private class RegressionAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (loadedData.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No data loaded. Please upload a file first.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            new AnalysisEngine(new RegressionAnalysis()).runAnalysis(loadedData, loadedData);
            tableModel.addRow(new Object[]{"Regression", "Completed"});
        }
    }

    private class DisplayChartsAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (loadedData.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No data loaded. Please upload a file first.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            chartPanel.removeAll();
            chartPanel.add(new ChartPanel(CO2PieChart.createCO2Chart(loadedData)));
            chartPanel.add(new ChartPanel(CH4PieChart.createCH4Chart(loadedData)));
            chartPanel.add(new CO2Chart(loadedData).createChartPanel());
            chartPanel.add(new CH4Chart(loadedData).createChartPanel());
            chartPanel.revalidate();
            chartPanel.repaint();
        }
    }
}
