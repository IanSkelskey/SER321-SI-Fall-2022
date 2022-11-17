package utils;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class BallotBox {
    private final CountDownLatch countDownLatch;
    private final ArrayList<Boolean> votes = new ArrayList<>();

    public BallotBox(int size) {
       countDownLatch = new CountDownLatch(size);
    }

    public void receiveVote(boolean vote) {
        votes.add(vote);
        countDownLatch.countDown();
    }

    public boolean getConsensus() {
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int yesVotes = 0;
        for(boolean b : votes) {
            if (b) yesVotes += 1;
        }
        return yesVotes > votes.size()/2 + 1;
    }
}
