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
        sendPlayResponse(true, game.getPlayerWins(), game.getPlayMessage(), Error.NO_ERROR.toString(), responseObserver);
    }

    @Override
    public void leaderboard(Empty request, StreamObserver<LeaderboardRes> responseObserver) {
        sendLeaderBoardResponse(responseObserver);
    }

    private enum Error {
        NO_ERROR("Execution successful."),
        BAD_PLAY("Error processing move. Please enter:\n" +
                "\tRock (0), Paper(1), or Scissors(2)");

        private final String text;

        Error(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }

    }

    private void sendPlayResponse(boolean isSuccess, boolean win, String message, String error, StreamObserver<PlayRes> responseObserver) {
        PlayRes response = buildPlayResponse(isSuccess, win, message, error);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
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
                    .setError("No entries in the leaderboard yet.");
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
                    .setError("Leaderboard successfully populated.");
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

    private PlayRes buildPlayResponse(boolean isSuccess, boolean win, String message, String error) {
        return PlayRes.newBuilder()
                .setIsSuccess(isSuccess)
                .setWin(win)
                .setMessage(message)
                .setError(error)
                .build();
    }

}
