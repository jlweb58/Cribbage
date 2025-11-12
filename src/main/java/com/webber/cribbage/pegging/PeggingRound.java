package com.webber.cribbage.pegging;

import com.webber.cribbage.model.Card;
import com.webber.cribbage.model.Hand;
import com.webber.cribbage.model.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages a complete pegging round where players alternate playing cards
 * until all cards have been played.
 */
public class PeggingRound {
    private final Player player1;
    private final Player player2;
    private final Hand player1Hand;
    private final Hand player2Hand;

    private final Map<Player, Integer> scores = new HashMap<>();
    private final List<PeggingSequence> sequences = new ArrayList<>();

    private PeggingSequence currentSequence;
    private Player currentPlayer;
    private boolean player1CanPlay = true;
    private boolean player2CanPlay = true;

    public PeggingRound(Player player1, Hand player1Hand, Player player2, Hand player2Hand, Player starterPlayer) {
        this.player1 = player1;
        this.player2 = player2;
        this.player1Hand = player1Hand;
        this.player2Hand = player2Hand;
        this.currentPlayer = starterPlayer;

        scores.put(player1, 0);
        scores.put(player2, 0);

        startNewSequence();
    }

    /**
     * Play a card for the current player.
     * @return Points scored by this play
     */
    public PeggingResult playCard(Card card) {
        validateCardPlay(card);

        Hand currentHand = getCurrentHand();
        currentHand.playCard(card);

        int points = currentSequence.playCard(card, currentPlayer);
        scores.put(currentPlayer, scores.get(currentPlayer) + points);

        PeggingResult result = new PeggingResult(currentPlayer, card, points, currentSequence.getCurrentCount());

        // Check if sequence is complete (reached 31)
        if (currentSequence.isComplete()) {
            startNewSequence();
            player1CanPlay = hasPlayableCards(player1Hand);
            player2CanPlay = hasPlayableCards(player2Hand);
        } else {
            // Switch to other player
            switchPlayer();
            updatePlayability();
        }

        return result;
    }

    /**
     * Declare "Go" - current player cannot play
     * @return Points awarded for the Go
     */
    public PeggingResult declareGo() {
        if (canCurrentPlayerPlay()) {
            throw new IllegalStateException("Current player has playable cards");
        }

        Player otherPlayer = getOtherPlayer();
        int points = 0;

        // If other player also can't play, award Go to last player who played
        if (!canOtherPlayerPlay()) {
            Player lastPlayer = currentSequence.getLastPlayerToPlay();
            points = currentSequence.awardGo(lastPlayer);
            scores.put(lastPlayer, scores.get(lastPlayer) + points);

            // Start new sequence
            startNewSequence();
            player1CanPlay = hasPlayableCards(player1Hand);
            player2CanPlay = hasPlayableCards(player2Hand);

            // The player who got the Go plays first in the new sequence
            currentPlayer = lastPlayer;

            return new PeggingResult(lastPlayer, null, points, 0);
        }

        // Other player continues and will get Go point after their play(s)
        switchPlayer();
        return new PeggingResult(null, null, 0, currentSequence.getCurrentCount());
    }

    public boolean isComplete() {
        return player1Hand.getUnplayedCards().isEmpty() && player2Hand.getUnplayedCards().isEmpty();
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean canCurrentPlayerPlay() {
        Hand hand = getCurrentHand();
        return canPlay(hand);
    }

    private boolean canOtherPlayerPlay() {
        Hand hand = getOtherHand();
        return canPlay(hand);
    }

    private boolean canPlay(Hand hand) {
        for (Card card : hand.getUnplayedCards()) {
            if (currentSequence.canPlayCard(card)) {
                return true;
            }
        }
        return false;
    }

    public List<Card> getPlayableCards() {
        Hand hand = getCurrentHand();
        List<Card> playable = new ArrayList<>();
        for (Card card : hand.getUnplayedCards()) {
            if (currentSequence.canPlayCard(card)) {
                playable.add(card);
            }
        }
        return playable;
    }

    public int getCurrentCount() {
        return currentSequence.getCurrentCount();
    }

    public int getScore(Player player) {
        return scores.get(player);
    }

    public Map<Player, Integer> getScores() {
        return new HashMap<>(scores);
    }

    private void startNewSequence() {
        currentSequence = new PeggingSequence();
        sequences.add(currentSequence);
    }

    private void switchPlayer() {
        currentPlayer = getOtherPlayer();
    }

    private Player getOtherPlayer() {
        return currentPlayer == player1 ? player2 : player1;
    }

    private Hand getCurrentHand() {
        return currentPlayer == player1 ? player1Hand : player2Hand;
    }

    private Hand getOtherHand() {
        return currentPlayer == player1 ? player2Hand : player1Hand;
    }

    private void updatePlayability() {
        player1CanPlay = hasPlayableCards(player1Hand);
        player2CanPlay = hasPlayableCards(player2Hand);
    }

    private boolean hasPlayableCards(Hand hand) {
        if (hand.getUnplayedCards().isEmpty()) {
            return false;
        }
        return canPlay(hand);
    }

    private void validateCardPlay(Card card) {
        Hand currentHand = getCurrentHand();
        if (!currentHand.getUnplayedCards().contains(card)) {
            throw new IllegalArgumentException("Card not in current player's hand");
        }
        if (!currentSequence.canPlayCard(card)) {
            throw new IllegalArgumentException("Card would exceed 31");
        }
    }

    /**
     * Result of a pegging action
     */
    public static class PeggingResult {
        private final Player player;
        private final Card card;
        private final int points;
        private final int newCount;

        public PeggingResult(Player player, Card card, int points, int newCount) {
            this.player = player;
            this.card = card;
            this.points = points;
            this.newCount = newCount;
        }

        public Player getPlayer() {
            return player;
        }

        public Card getCard() {
            return card;
        }

        public int getPoints() {
            return points;
        }

        public int getNewCount() {
            return newCount;
        }
    }
}
