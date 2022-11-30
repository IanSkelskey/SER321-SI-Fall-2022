package examples.grpcclient;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import service.*;

import java.util.*;

public class RockPaperScissorsImpl extends RockPaperScissorsGrpc.RockPaperScissorsImplBase {

    @Override
    public void play(PlayReq request, StreamObserver<PlayRes> responseObserver) {
        RockPaperScissorsGame game = new RockPaperScissorsGame(request);
        game.playGame();
        sendPlayResponse(true, game.getPlayerWins(), game.getPlayMessage(), Error.GAME_SUCCESS.toString(), responseObserver);
    }

    private void sendPlayResponse(boolean isSuccess, boolean win, String message, String error, StreamObserver<PlayRes> responseObserver) {
        PlayRes response = buildPlayResponse(isSuccess, win, message, error);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private PlayRes buildPlayResponse(boolean isSuccess, boolean win, String message, String error) {
        return PlayRes.newBuilder()
                .setIsSuccess(isSuccess)
                .setWin(win)
                .setMessage(message)
                .setError(error)
                .build();
    }

    @Override
    public void leaderboard(Empty request, StreamObserver<LeaderboardRes> responseObserver) {
        sendLeaderBoardResponse(responseObserver);
    }

    private void sendLeaderBoardResponse(StreamObserver<LeaderboardRes> responseObserver) {
        LeaderboardRes response = buildLeaderBoardResponse();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private LeaderboardRes buildLeaderBoardResponse() {
        LeaderboardRes.Builder responseBuilder = LeaderboardRes.newBuilder();
        if (RockPaperScissorsLeaderboard.isEmpty()) {
            responseBuilder.setIsSuccess(false)
                    .setError(Error.NO_ENTRIES.toString());
        } else {
            ArrayList<String> sortedList = RockPaperScissorsLeaderboard.getNamesSortedByRank();
            int i = 1;
            for (String name : sortedList) {
                int wins = RockPaperScissorsLeaderboard.getPlayerWins(name);
                int losses = RockPaperScissorsLeaderboard.getPlayerLosses(name);

                LeaderboardEntry entry = buildLeaderBoardEntry(name, i, wins, losses);

                responseBuilder.addLeaderboard(entry);
                i++;
            }

            responseBuilder.setIsSuccess(true)
                    .setError(Error.LEADERBOARD_SUCCESS.toString());
        }
        return responseBuilder.build();
    }

    private LeaderboardEntry buildLeaderBoardEntry(String name, int rank, int wins, int losses) {
        return LeaderboardEntry.newBuilder()
                .setName(name)
                .setRank(rank)
                .setWins(wins)
                .setLost(losses)
                .build();
    }

    private enum Error {
        GAME_SUCCESS("Game execution successful."),
        LEADERBOARD_SUCCESS("Leaderboard successfully populated."),
        BAD_PLAY("Error processing move. Please enter:\n" +
                "\tRock (0), Paper(1), or Scissors(2)"),
        NO_ENTRIES("No entries in the leaderboard yet.");

        private final String text;

        Error(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }

    }

}
