package com.webber.cribbage;

import com.webber.cribbage.model.Card;
import com.webber.cribbage.model.Hand;
import com.webber.cribbage.model.Player;
import com.webber.cribbage.model.impl.HandCounterImpl;

import java.util.ArrayList;
import java.util.List;

public class CribbageScorer {

    private List<Card> playedCards = new ArrayList<>();


    private int currentCount;

    /**
     * A card is played by a player, adjusting the current count and returning
     * pegging points if any are scored.
     */
    public int playCard(Card card, Player player) throws WouldExceed31Exception {
        if (currentCount + card.getRank().getCount() > 31) {
            throw new WouldExceed31Exception();
        }
        playedCards.add(card);
        currentCount += card.getRank().getCount();
        if (playedCards.size() > 1) {
            return calculatePeggingPoints();
        }
        else return 0;
    }

    private int calculatePeggingPoints() {
        int runningCount = 0;

        if (currentCount == 15 || currentCount == 31) {
            runningCount += 2;
        }
        runningCount += getPairsCount();
        runningCount += getRunsCount();
        return runningCount;
    }

    private int getPairsCount() {
        int runningCount = 0;
        for (int i = 0; i < playedCards.size() - 1; i++) {
            for (int j = i + 1; j < playedCards.size(); j++) {
                if (playedCards.get(i).getRank().equals(playedCards.get(j).getRank())) {
                    runningCount += 2;
                }
            }
        }
        return runningCount;
    }

    // Calculate whether the last three or four cards played are a run, no matter which
    // order they are in. If so, return one point for each card in the run.
    private int getRunsCount() {
        Hand hand = new Hand(playedCards.size());
        playedCards.forEach(hand::addCard);
        return new HandCounterImpl().countRuns(hand);
    }


    public int getCurrentCount() {
        return currentCount;
    }

}
