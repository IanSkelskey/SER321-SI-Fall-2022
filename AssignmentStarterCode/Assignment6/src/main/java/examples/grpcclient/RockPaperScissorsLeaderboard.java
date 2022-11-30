package examples.grpcclient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RockPaperScissorsLeaderboard {

    private static final HashMap<String, Score> entries = new HashMap<>();

    public static void updatePlayerScore(String name, boolean win) {
        Score score = (entries.containsKey(name)) ? entries.get(name) : new Score();
        if (win) {
            score.wins += 1;
        } else {
            score.losses += 1;
        }
        entries.put(name, score);
    }

    public static ArrayList<String> getNamesSortedByRank() {
        ArrayList<String> sortedNamesByRank = new ArrayList<>();
        entries.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(e -> {
            sortedNamesByRank.add(e.getKey());
        });
        Collections.reverse(sortedNamesByRank);
        return sortedNamesByRank;
    }

    public static boolean isEmpty() {
        return entries.isEmpty();
    }

    public static int getPlayerWins(String name) {
        return entries.get(name).wins;
    }

    public static int getPlayerLosses(String name) {
        return entries.get(name).losses;
    }

    private static class Score implements Comparable<Score> {
        int wins;
        int losses;

        Score() {
            this.wins = 0;
            this.losses = 0;
        }

        @Override
        public int compareTo(Score o) {
            return (this.wins - o.wins) == 0 ? o.losses - this.losses : this.wins - o.wins;
        }
    }
}
