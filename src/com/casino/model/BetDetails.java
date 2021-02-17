package com.casino.model;

public class BetDetails {
    private String betOn;
    private double betAmount;

    public BetDetails(String betOn, double betAmount) {
        this.betOn = betOn;
        this.betAmount = betAmount;
    }

    public String getBetOn() {
        return betOn;
    }

    public double getBetAmount() {
        return betAmount;
    }
}
