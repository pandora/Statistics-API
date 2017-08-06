package me.thomas.sapi.statistics;

import org.eclipse.jetty.http.HttpStatus;
import com.fasterxml.jackson.databind.ObjectMapper;

import spark.Request;
import spark.Response;
import spark.Route;

public class StatisticsController {

    public static Route getStatistics = (Request request, Response response) -> {

        Statistics stats = new Statistics();
        response.status(HttpStatus.OK_200);
        
        return new ObjectMapper().writeValueAsString(stats);
    };

}
