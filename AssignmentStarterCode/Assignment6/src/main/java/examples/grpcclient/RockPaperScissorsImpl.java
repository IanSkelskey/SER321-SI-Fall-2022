package examples.grpcclient;

import io.grpc.stub.StreamObserver;
import service.*;

import java.util.*;

public class RockPaperScissorsImpl extends RockPaperScissorsGrpc.RockPaperScissorsImplBase {

    HashMap<String, Score> leaderboardEntries = new HashMap<>();

    PlayReq.Played ROCK = PlayReq.Played.ROCK;
    PlayReq.Played PAPER = PlayReq.Played.PAPER;
    PlayReq.Played SCISSORS = PlayReq.Played.SCISSORS;

    @Override
    public void play(PlayReq req, StreamObserver<PlayRes> responseObserver) {
        String name = req.getName();
        Random rand = new Random();
        PlayReq.Played playerChoice = req.getPlay();
        PlayReq.Played serverChoice = PlayReq.Played.forNumber(rand.nextInt(3));
        boolean win = true;
        StringBuilder message = new StringBuilder();
        StringBuilder error = new StringBuilder();

        message.append(name).append(" played ").append(playerChoice);
        message.append(" Server played ").append(serverChoice);

        if (playerChoice == ROCK && serverChoice == SCISSORS) {
            // You win.
            message.append(" You win");
        } else if (playerChoice == PAPER && serverChoice == ROCK) {
            // You win.
            message.append(" You win");
        } else if (playerChoice == SCISSORS && serverChoice == PAPER) {
            // You win.
            message.append(" You win");
        } else {
            // You lose or tie...
            message.append(" You lose");
            win = false;
        }

        PlayRes response = PlayRes.newBuilder()
                .setWin(win)
                .setIsSuccess(true)
                .setMessage(String.valueOf(message))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

        // Populate leaderboard entries map

        Score score = (leaderboardEntries.containsKey(name)) ? leaderboardEntries.get(name) : new Score();
        if(win) {
            score.wins += 1;
        } else {
            score.losses += 1;
        }
        leaderboardEntries.put(name, score);
    }

    @Override
    public void leaderboard(com.google.protobuf.Empty req, StreamObserver<LeaderboardRes> resStreamObserver) {
        LeaderboardRes.Builder response = LeaderboardRes.newBuilder();
        if (leaderboardEntries.isEmpty()) {
            response.setIsSuccess(false);
            response.setError("No entries in the leaderboard yet.");
        } else {
            ArrayList<String> sortedList = new ArrayList<>();
            leaderboardEntries.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(e -> {
                sortedList.add(e.getKey());
            });
            Collections.reverse(sortedList);
            int i = 1;
            for(String name: sortedList) {
                Score s = leaderboardEntries.get(name);
                LeaderboardEntry.Builder entryBuilder = LeaderboardEntry.newBuilder()
                        .setName(name)
                        .setRank(i)
                        .setWins(s.wins)
                        .setLost(s.losses);
                LeaderboardEntry entry = entryBuilder.build();
                response.addLeaderboard(entry);
                i++;
            }

            response.setIsSuccess(true);
            response.setError("Leaderboard successfully populated.");
        }
        LeaderboardRes resp = response.build();
        resStreamObserver.onNext(resp);
        resStreamObserver.onCompleted();
    }

    private static class Score implements Comparable<Score>{
        int wins;
        int losses;
        Score() {
            wins = 0;
            losses = 0;
        }

        @Override
        public int compareTo(Score o) {
            return (this.wins - o.wins);
        }
    }

}
