package me.thomas.sapi.lib;

/**
 * A time utility class
 */
public class Time {

    /**
     * A wrapper around currentTimeMillis() that returns UTC epoch time in seconds
     * 
     * @return UTC epoch time in seconds
     */
    public static long now() {
        return System.currentTimeMillis() / 1000;
    }
    
    /**
     * A convenience wrapper around currentTimeMillis() that returns UTC epoch time in milliseconds
     * 
     * @return UTC epoch time in milliseconds
     */
    public static long nowMillis() {
        return System.currentTimeMillis();
    }

}
