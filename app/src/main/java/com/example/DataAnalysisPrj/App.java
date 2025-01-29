package com.example.DataAnalysisPrj;

import data.DataLoader;
import org.apache.spark.sql.SparkSession;
import data.DatabaseManager;

public class App {
    public static void main(String[] args) {
        System.setProperty("spark.security.manager", "false");
        SparkSession spark = SparkSession.builder()
                .appName("DataAnalysisPrj")
                .master("local[1]")
                .getOrCreate();
        spark.sparkContext().setLogLevel("OFF");

        DatabaseManager dbManager = new DatabaseManager("jdbc:postgresql://localhost:5432/postgres", "postgres", "123");
        DataLoader loader = new DataLoader(spark, dbManager);

        new UserInterface(loader, dbManager);
    }
}
