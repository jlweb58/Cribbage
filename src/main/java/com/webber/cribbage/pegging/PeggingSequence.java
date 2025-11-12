package com.webber.cribbage.pegging;

import com.webber.cribbage.model.Card;
import com.webber.cribbage.model.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single pegging sequence that continues until the count reaches 31
 * or both players cannot play (Go).
 */
public class PeggingSequence {
    private final List<CardPlay> plays = new ArrayList<>();
    private int currentCount = 0;
    private Player lastPlayerToPlay;

    /**
     * Attempt to play a card in this sequence.
     * @return Points scored by playing this card
     * @throws IllegalArgumentException if card would exceed 31
     */
    public int playCard(Card card, Player player) {
        if (currentCount + card.getRank().getCount() > 31) {
            throw new IllegalArgumentException("Card would exceed 31");
        }

        currentCount += card.getRank().getCount();
        lastPlayerToPlay = player;
        plays.add(new CardPlay(card, player));

        return calculatePoints();
    }

    /**
     * Award "Go" point - called when opponent cannot play
     */
    public int awardGo(Player player) {
        if (currentCount == 31) {
            return 0; // Already scored 2 for 31
        }
        return 1;
    }

    public boolean canPlayCard(Card card) {
        return currentCount + card.getRank().getCount() <= 31;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public List<CardPlay> getPlays() {
        return new ArrayList<>(plays);
    }

    public Player getLastPlayerToPlay() {
        return lastPlayerToPlay;
    }

    public boolean isComplete() {
        return currentCount == 31;
    }

    private int calculatePoints() {
        int points = 0;

        // Check for 15 or 31
        if (currentCount == 15 || currentCount == 31) {
            points += 2;
        }

        // Check for pairs (last 2, 3, or 4 cards)
        points += countPairs();

        // Check for runs (last 3, 4, 5, 6, or 7 cards)
        points += countRuns();

        return points;
    }

    private int countPairs() {
        if (plays.size() < 2) return 0;

        int pairCount = 0;
        Card lastCard = plays.get(plays.size() - 1).getCard();

        for (int i = plays.size() - 2; i >= 0; i--) {
            if (plays.get(i).getCard().getRank().equals(lastCard.getRank())) {
                pairCount++;
            } else {
                break;
            }
        }

        // 1 match = pair (2 points), 2 matches = triple (6 points), 3 matches = quad (12 points)
        return pairCount > 0 ? (pairCount + 1) * pairCount : 0;
    }

    private int countRuns() {
        // Check for runs of length 7 down to 3
        for (int runLength = Math.min(7, plays.size()); runLength >= 3; runLength--) {
            if (isRun(runLength)) {
                return runLength;
            }
        }
        return 0;
    }

    private boolean isRun(int length) {
        List<Card> recentCards = new ArrayList<>();
        for (int i = plays.size() - length; i < plays.size(); i++) {
            recentCards.add(plays.get(i).getCard());
        }

        // Sort by rank
        recentCards.sort(Card::compareTo);

        // Check if consecutive
        for (int i = 1; i < recentCards.size(); i++) {
            if (recentCards.get(i).getRank().ordinal() - recentCards.get(i - 1).getRank().ordinal() != 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Inner class to track each card play
     */
    public static class CardPlay {
        private final Card card;
        private final Player player;

        public CardPlay(Card card, Player player) {
            this.card = card;
            this.player = player;
        }

        public Card getCard() {
            return card;
        }

        public Player getPlayer() {
            return player;
        }
    }
}
