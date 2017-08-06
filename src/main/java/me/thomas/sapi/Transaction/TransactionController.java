package me.thomas.sapi.Transaction;

import spark.Route;
import spark.Request;
import spark.Response;

import org.eclipse.jetty.http.HttpStatus;
import com.fasterxml.jackson.databind.ObjectMapper;

import me.thomas.sapi.statistics.Statistics;

public class TransactionController {

    private static Statistics stats = new Statistics();

    public static Route insertTransaction = (Request request, Response response) -> {
        ObjectMapper jsonMapper = new ObjectMapper();
        Transaction transaction = jsonMapper.readValue(request.body(), Transaction.class);

        int status = stats.addStatistic(transaction) ? HttpStatus.CREATED_201 : HttpStatus.NO_CONTENT_204;
        response.status(status);

        return new String();
    };

}
