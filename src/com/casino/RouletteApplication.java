package com.casino;

import com.casino.model.BetDetails;
import com.casino.model.Player;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class RouletteApplication {
    private CopyOnWriteArrayList<Player> players = new CopyOnWriteArrayList<>();
    boolean playing = true;

    Runnable spinTable = new Runnable() {
        @Override
        public void run() {
             calculateEarnings(spin());
        }
    };

    public void  start() {
        ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
        exec.scheduleAtFixedRate(spinTable, 0, 30, TimeUnit.SECONDS);
        readPlayers();
        readBets();
    }

    public void readPlayers() {
        try(BufferedReader in = new BufferedReader(new FileReader("./players.txt"))) {
            String str;
            while ((str = in.readLine()) != null) {
                deSerializePlayer(str);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readBets() {
        while (playing) {
            Scanner input = new Scanner(System.in);
            for (Player player: players) {
                boolean validInput = false;
                while (validInput == false) {
                    System.out.println("\nPlayer " + player.getName() + " Enter your bet (possible values are 1-36, EVEN or ODD) followed by the amount you want to bet ");
                    String line = input.nextLine();
                    String[] props = line.split(" ");
                    if (props.length == 2) {
                        boolean isValid = validateBet(props[0].toUpperCase(), props[1]);
                        if (isValid) {
                            player.addBet(new BetDetails(props[0].toUpperCase(), Double.parseDouble(props[1])));
                            validInput = isValid;
                        }
                    } else {
                        System.out.println("Invalid input please enter bet followed by bet amount");
                    }
                }
            }
        }
    }

    private boolean validateBet(String bet, String amount) {
        try {
            Double.parseDouble(amount);
        } catch (NumberFormatException e) {

            System.out.println("Invalid input: Amount must be a number e.g. 2 or 5.5 please try again");
            return false;
        }

        String regex = "EVEN|ODD|[1-9]|1[0-9]|2[0-9]|3[0-6]";
        if (!Pattern.matches(regex, bet)) {
            System.out.println("Invalid input: possible bets are even odd and numbers 1 to 16 please try again");
            return false;
        }
        return true;
    }

    private void  deSerializePlayer(String player) {
        String[] props = player.split(",");
        if (props.length == 1) {
            players.add(new Player(props[0]));
        } else if (props.length == 3) {
            players.add(new Player(props[0], Double.parseDouble(props[1]), Double.parseDouble(props[2])));
        }
    }

    private int spin() {
        Random random = new Random();
        return random.nextInt(36 - 1) + 1;
    }

    private void calculateEarnings(int spinResult) {
        boolean newBets = false;
        StringBuilder sb = new StringBuilder();
        String formatString = "%-20s%-20s%-20s%-20s%n";
        sb.append("Number: " + spinResult + "\n");
        sb.append(String.format(formatString, "Player", "Bet", "Outcome", "Winnings"));
        sb.append("---\n");
        for (Player player: players) {
            for(BetDetails bet: player.getBets()) {
                newBets = true;
                switch (bet.getBetOn()) {
                    case "EVEN":
                        if (isEven(spinResult)) {
                            double earnings = bet.getBetAmount() * 2;
                            sb.append(String.format(formatString, player.getName(), bet.getBetOn(), "WIN", earnings));
                            player.addToTotalBetAmount(bet.getBetAmount());
                            player.addToTotalEarnings(earnings);
                        } else {
                            sb.append(String.format(formatString, player.getName(), bet.getBetOn(), "LOSE", 0.0));
                            player.addToTotalBetAmount(bet.getBetAmount());
                        }
                        break;
                    case "ODD":
                        if (!isEven(spinResult)) {
                            double earnings = bet.getBetAmount() * 2;
                            sb.append(String.format(formatString, player.getName(), bet.getBetOn(), "WIN", earnings));
                            player.addToTotalBetAmount(bet.getBetAmount());
                            player.addToTotalEarnings(earnings);
                        } else {
                            sb.append(String.format(formatString, player.getName(), bet.getBetOn(), "LOSE", 0.0));
                            player.addToTotalBetAmount(bet.getBetAmount());
                        }
                        break;
                    default:
                        if (spinResult == Double.parseDouble(bet.getBetOn())) {
                            double earnings = bet.getBetAmount() * 36;
                            sb.append(String.format(formatString, player.getName(), bet.getBetOn(), "WIN", earnings));
                            player.addToTotalBetAmount(bet.getBetAmount());
                            player.addToTotalEarnings(earnings);
                        } else {
                            sb.append(String.format(formatString, player.getName(), bet.getBetOn(), "LOSE", 0.0));
                            player.addToTotalBetAmount(bet.getBetAmount());
                        }
                    }
                }
            player.clearBets();
            }
        if (newBets) {
            System.out.print(sb.toString());
            outputTotals();
        }
    }

    private void outputTotals() {
        StringBuilder sb = new StringBuilder();
        String formatString = "%-20s%-20s%-20s%n";
        sb.append( "\n\n");
        sb.append(String.format(formatString, "Player", "Total Wins",  "Total Bet"));
        sb.append( "---\n");
        for (Player player: players) {
            sb.append(String.format(formatString, player.getName(), player.getTotalEarnings(), player.getTotalBetAmount()));
        }
        System.out.println(sb);
    }

    private boolean isEven(int num) {
        if (num % 2 == 0) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        RouletteApplication roulette = new RouletteApplication();
        System.out.println("Roulette app");
        roulette.start();

    }
}
