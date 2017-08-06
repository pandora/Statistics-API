package me.thomas.sapi.Transaction;

public class Transaction {

    private double amount;
    private long timestamp;

    public Transaction() {

    }

    public double getAmount() {
        return amount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setAmount(double amt) {
        amount = amt;
    }

    public void setTimestamp(long time) {
        timestamp = time;
    }

}
