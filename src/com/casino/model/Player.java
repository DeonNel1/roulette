package com.casino.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class Player {
    private String name;
    private Double totalBetAmount;
    private Double totalEarnings;
    private List<BetDetails> bets;

    public Player(String name) {
        this.name = name;
        this.totalEarnings = 0.0;
        this.totalBetAmount = 0.0;
        this.bets = new ArrayList<>();
    }

    public Player(String name, Double totalBetAmount, Double totalEarnings) {
        this.name = name;
        this.totalBetAmount = totalBetAmount;
        this.totalEarnings = totalEarnings;
        this.bets = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTotalBetAmount() {
        return totalBetAmount;
    }

    public void setTotalBetAmount(Double totalBetAmount) {
        this.totalBetAmount = totalBetAmount;
    }

    public Double getTotalEarnings() {
        return totalEarnings;
    }

    public void setTotalEarnings(Double totalEarnings) {
        this.totalEarnings = totalEarnings;
    }

    public List<BetDetails> getBets() {
        return bets;
    }

    public void addToTotalBetAmount(double amount) {
        this.totalBetAmount += amount;
    }

    public void addToTotalEarnings(double amount) {
        this.totalEarnings += amount;
    }

    public void  addBet(BetDetails bet) {
        this.bets.add(bet);
    }

    public void clearBets() {
        this.bets.clear();
    }
}
