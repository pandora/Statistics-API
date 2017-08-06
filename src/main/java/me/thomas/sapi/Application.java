package me.thomas.sapi;

import static spark.Spark.get;
import static spark.Spark.post;


import me.thomas.sapi.Transaction.TransactionController;
import me.thomas.sapi.statistics.StatisticsController;

public class Application {

    public static void main(String[] args) {    
        post("/transactions", TransactionController.insertTransaction);

        get("/statistics", StatisticsController.getStatistics);
    }

}
