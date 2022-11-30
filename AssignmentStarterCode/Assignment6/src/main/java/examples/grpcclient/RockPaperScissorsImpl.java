package examples.grpcclient;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import service.*;

import java.util.*;

public class RockPaperScissorsImpl extends RockPaperScissorsGrpc.RockPaperScissorsImplBase {

    HashMap<String, Score> leaderboardEntries = new HashMap<>();

    @Override
    public void play(PlayReq request, StreamObserver<PlayRes> responseObserver) {
        String name = request.getName();
        Played played = request.getPlay();

        System.out.println("Client " + name + " wants to play rock paper scissors.\n" +
                "They have chosen " + played);

        Played serverPlay = getServerPlay();

        boolean win = playGame(played, serverPlay);

        String message = "You played " + played + ". The server played " + serverPlay +
                ((win) ? ". You win!" : ". You lose...");

        PlayRes response = PlayRes.newBuilder()
                .setIsSuccess(true)
                .setWin(win)
                .setMessage(message)
                .setError("No error.")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();

        Score score = (leaderboardEntries.containsKey(name)) ? leaderboardEntries.get(name) : new Score();
        if(win) {
            score.wins += 1;
        } else {
            score.losses += 1;
        }
        leaderboardEntries.put(name, score);
    }

    @Override
    public void leaderboard(Empty request, StreamObserver<LeaderboardRes> responseObserver) {
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
        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }

    public Played getServerPlay() {
        Random random = new Random();
        return Played.forNumber(random.nextInt(3));
    }

    public boolean playGame(Played player, Played server) {
        boolean win = false;

        if (player == Played.ROCK && server == Played.SCISSORS) win = true; // Rock beats scissors
        if (player == Played.SCISSORS && server == Played.PAPER) win = true; // Scissors beats paper
        if (player == Played.PAPER && server == Played.ROCK) win = true; // Paper beats rock

        return win;
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
