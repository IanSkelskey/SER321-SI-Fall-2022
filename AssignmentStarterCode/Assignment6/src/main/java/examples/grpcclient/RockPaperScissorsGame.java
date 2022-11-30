package examples.grpcclient;

import service.PlayReq;
import service.Played;

import java.util.Random;

public class RockPaperScissorsGame {

    private final String playerName;
    private final Played playerPlay;
    private final Played serverPlay;
    private boolean playerWins = false;
    private boolean draw = false;

    public RockPaperScissorsGame(PlayReq request) {
        this.playerName = request.getName();
        this.playerPlay = request.getPlay();
        this.serverPlay = getRandomPlay();
    }

    public void playGame() {
        playerWins = false;
        draw = playerPlay == serverPlay;
        if (playerPlay == Played.ROCK && serverPlay == Played.SCISSORS) playerWins = true; // Rock beats scissors
        if (playerPlay == Played.SCISSORS && serverPlay == Played.PAPER) playerWins = true; // Scissors beats paper
        if (playerPlay == Played.PAPER && serverPlay == Played.ROCK) playerWins = true; // Paper beats rock

        RockPaperScissorsLeaderboard.updatePlayerScore(playerName, playerWins);

    }

    public String getPlayMessage() {
        if (draw) {
            return playerName + " and the server both played " + playerPlay + ".\n" +
                    "It's a draw!\n";
        }
        return playerName + " played " + playerPlay + ".\n" +
                "The server played " + serverPlay + "\n" +
                ((playerWins) ? playerName + " wins!\n" : playerName + " loses...\n");
    }

    public Played getRandomPlay() {
        Random random = new Random();
        return Played.forNumber(random.nextInt(3));
    }

    public boolean getPlayerWins() {
        return this.playerWins;
    }

}
