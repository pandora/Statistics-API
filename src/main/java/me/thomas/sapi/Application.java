package me.thomas.sapi;

import static spark.Spark.get;
import static spark.Spark.post;

import me.thomas.sapi.Transaction.TransactionController;
import me.thomas.sapi.statistics.StatisticsController;

/**
 * Main application entry point. API routes defined are (see README for further
 * details):
 * 
 * 1- POST /transactions 2- GET /statistics
 */
public class Application {

    public static void main(String[] args) {
        post("/transactions", TransactionController.insertTransaction);

        get("/statistics", StatisticsController.getStatistics);
    }

}
