package me.thomas.sapi.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import spark.Request;
import spark.Response;
import spark.Route;

import me.thomas.sapi.Transaction.TransactionController;
import me.thomas.sapi.statistics.Statistics;
import me.thomas.sapi.statistics.StatisticsController;

import static me.thomas.sapi.lib.Time.*;

/**
 * Test the API handlers that expose the /transactions and /statistics endpoints.
 */
public class ApiRouteTest {
        
    @Before
    public void setUp() {
        Statistics stats = new Statistics();
        stats.clear();
    }

    /**
     * Tests the TransactionController. Ensures that the correct HTTP statuses are returned depending
     * on whether or not transaction timestamps are deemed stale.
     */
    @Test
    public void transactionControllerTest() {
        Route insertTransaction = TransactionController.insertTransaction;
        
        Request  request  = new RequestTest(nowMillis());
        Response response = new ResponseTest();
        
        // Test response after posting valid transactions for the current sliding window
        int status = handleAndReturnStatus(insertTransaction, request, response);
        assertEquals(201, status);
        
        // Test response after posting stale transactions
        request  = new RequestTest(nowMillis() - 61000);
        status = handleAndReturnStatus(insertTransaction, request, response);
        assertEquals(204, status);
    }
    
    /**
     * Tests the StatisticsController. Ensures the correct output and HTTP status
     * are returned after none or more transactions have been added into the system.
     */
    @Test
    public void statisticsControllerTest() {
        Route getStatistics = StatisticsController.getStatistics;
        
        Request  request  = new RequestTest();
        Response response = new ResponseTest();
        
        // Test response and HTTP code after no transactions have been submitted
        int status = handleAndReturnStatus(getStatistics, request, response);
        assertEquals(200, status);
        assertEquals("{\"sum\":0.0,\"avg\":0.0,\"max\":0.0,\"min\":0.0,\"count\":0}", handleAndReturnContent(getStatistics, request, response));
        
        // Test response and HTTP code after one transaction has been submitted
        transactionControllerTest();
        
        status = handleAndReturnStatus(getStatistics, request, response);
        assertEquals(200, status);
        assertEquals("{\"sum\":10.0,\"avg\":10.0,\"max\":10.0,\"min\":10.0,\"count\":1}", handleAndReturnContent(getStatistics, request, response));
    }
   
    /**
     * Override Spark's Request class whose constructor, and other methods, are protected.
     */
    class RequestTest extends Request {    
       
        long timestamp;
       
        public RequestTest() {
            super();
        }
       
        public RequestTest(long t) {
            timestamp = t;
        }
       
        public String body() {
            return "{\"amount\": 10.0, \"timestamp\": "+ timestamp + "}";
        }
    }
   
    /**
    * Override Spark's Response class whose constructor, and other methods, are protected.
    */
    class ResponseTest extends Response {   
        private int status;
        private String body;
       
        public ResponseTest() {
            super();
        }
       
        public int status() {
            return status;
        }
       
        public String body() {
            return body;
        }
       
        public void status(int statusCode) {
            status = statusCode;
        }
       
        public void body(String b) {
            body = b;
        }
       
    }

    private int handleAndReturnStatus(Route route, Request request, Response response) {
        Map<Integer, String> result = invokeHandler(route, request, response);
        return (int) result.keySet().toArray()[0];
    }
    
    private String handleAndReturnContent(Route route, Request request, Response response) {
        Map<Integer, String> result = invokeHandler(route, request, response);
        return (String) result.values().toArray()[0];
    }
    
    private Map<Integer, String> invokeHandler(Route route, Request request, Response response) {
        String content;
        Map<Integer, String> result = new HashMap<>();
        
        try {
            content = (String) route.handle(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        result.put(response.status(), content);
        return result;
    }

}
