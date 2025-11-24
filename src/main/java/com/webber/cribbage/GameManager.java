
package com.webber.cribbage;

import com.webber.cribbage.model.Card;
import com.webber.cribbage.model.CardDeck;
import com.webber.cribbage.model.Hand;
import com.webber.cribbage.model.Player;
import com.webber.cribbage.model.Rank;
import com.webber.cribbage.pegging.PeggingRound;
import lombok.Getter;

import java.util.List;

@Getter
public class GameManager {
    private static final int WINNING_SCORE = 121;
    private static final int CARDS_PER_HAND = 6;
    private static final int CARDS_TO_CRIB = 2;
    private static final int CARDS_AFTER_DISCARD = 4;

    private final Player player1;
    private final Player player2;

    private Player dealer;
    private Player nonDealer;

    private final GameScore gameScore;
    private CardDeck deck;
    private Hand crib;
    private Card cutCard;

    private GameState state;

    public enum GameState {
        INITIAL,
        DEALING,
        DISCARDING_TO_CRIB,
        CUTTING,
        PEGGING,
        COUNTING_HANDS,
        GAME_OVER
    }

    public GameManager(Player player1, Player player2, Player initialDealer) {
        this.player1 = player1;
        this.player2 = player2;
        this.dealer = initialDealer;
        this.nonDealer = (dealer == player1) ? player2 : player1;
        this.gameScore = new GameScore(player1, player2);
        this.state = GameState.INITIAL;
    }

    /**
     * Start a new hand
     */
    public void startHand() {
        if (state == GameState.GAME_OVER) {
            throw new IllegalStateException("Game is over");
        }

        // Reset for new hand
        deck = new CardDeck();
        cutCard = null;
        crib = new Hand(CARDS_TO_CRIB * 2);

        // Deal 6 cards to each player
        dealCards();
        state = GameState.DISCARDING_TO_CRIB;
    }

    private void dealCards() {
        state = GameState.DEALING;

        Hand player1Hand = new Hand(CARDS_PER_HAND);
        Hand player2Hand = new Hand(CARDS_PER_HAND);

        // Alternate dealing to each player
        for (int i = 0; i < CARDS_PER_HAND; i++) {
            player1Hand.addCard(deck.dealCard());
            player2Hand.addCard(deck.dealCard());
        }

        player1.setDealtHand(player1Hand);
        player2.setDealtHand(player2Hand);

    }

    /**
     * Player discards cards to the crib
     */
    public void discardToCrib(Player player, List<Card> cards) {
        if (state != GameState.DISCARDING_TO_CRIB) {
            throw new IllegalStateException("Not in discarding phase");
        }
        if (cards.size() != CARDS_TO_CRIB) {
            throw new IllegalArgumentException("Must discard exactly " + CARDS_TO_CRIB + " cards");
        }

        Hand hand = player.getDealtHand();
        for (Card card : cards) {
            if (!hand.getUnplayedCards().contains(card)) {
                throw new IllegalArgumentException("Card not in player's hand");
            }
            hand.playCard(card); // Remove from hand
            crib.addCard(card);
        }

        // Check if both players have discarded
        if (player1.getDealtHand().getUnplayedCards().size() == CARDS_AFTER_DISCARD &&
                player2.getDealtHand().getUnplayedCards().size() == CARDS_AFTER_DISCARD) {
            state = GameState.CUTTING;
        }
    }

    /**
     * Cut the deck to reveal the cut card
     */
    public void cutDeck() {
        if (state != GameState.CUTTING) {
            throw new IllegalStateException("Not in cutting phase");
        }

        cutCard = deck.cutCard();

        // Check for "his nibs" - if cut card is a jack, dealer scores 2
        if (cutCard.getRank() == Rank.JACK) {
            gameScore.addScore(dealer, 2);
        }

        state = GameState.PEGGING;
    }

    /**
     * Play the pegging round
     */
    public PeggingRound startPegging() {
        if (state != GameState.PEGGING) {
            throw new IllegalStateException("Not in pegging phase");
        }

        // Non-dealer plays first
        Hand player1Hand = new Hand(CARDS_AFTER_DISCARD);
        Hand player2Hand = new Hand(CARDS_AFTER_DISCARD);

        // Copy cards to new hands for pegging
        for (Card card : player1.getDealtHand().getUnplayedCards()) {
            player1Hand.addCard(card);
        }
        for (Card card : player2.getDealtHand().getUnplayedCards()) {
            player2Hand.addCard(card);
        }

        return new PeggingRound(player1, player1Hand, player2, player2Hand, nonDealer);
    }

    public int scoreHand(Hand hand, Card cutCard) {
        return new HandCounter().getHandCount(hand, cutCard);
    }

    /**
     * Complete the hand by counting and scoring
     */
    public void countHands() {
        if (state != GameState.PEGGING) {
            throw new IllegalStateException("Must complete pegging first");
        }

        state = GameState.COUNTING_HANDS;

        // Non-dealer counts first
        int nonDealerScore = scoreHand(nonDealer.getDealtHand(), cutCard);
        gameScore.addScore(nonDealer, nonDealerScore);

        if (gameScore.getScore(nonDealer) >= WINNING_SCORE) {
            state = GameState.GAME_OVER;
            return;
        }

        // Dealer counts hand
        int dealerHandScore = scoreHand(dealer.getDealtHand(), cutCard);
        gameScore.addScore(dealer, dealerHandScore);

        if (gameScore.getScore(dealer) >= WINNING_SCORE) {
            state = GameState.GAME_OVER;
            return;
        }

        // Dealer counts crib
        int cribScore = scoreHand(crib, cutCard);
        gameScore.addScore(dealer, cribScore);

        if (gameScore.getScore(dealer) >= WINNING_SCORE) {
            state = GameState.GAME_OVER;
            return;
        }

        // Switch dealer for next hand
        Player temp = dealer;
        dealer = nonDealer;
        nonDealer = temp;

        state = GameState.INITIAL;
    }

    /**
     * Add pegging points during the pegging round
     */
    public void addPeggingScore(Player player, int points) {
        gameScore.addScore(player, points);

        // Check for win
        if (gameScore.getScore(player) >= WINNING_SCORE) {
            state = GameState.GAME_OVER;
        }
    }

    public Player getWinner() {
        if (state != GameState.GAME_OVER) {
            return null;
        }

        if (gameScore.getScore(player1) >= WINNING_SCORE) {
            return player1;
        } else if (gameScore.getScore(player2) >= WINNING_SCORE) {
            return player2;
        }

        return null;
    }

    public boolean isGameOver() {
        return state == GameState.GAME_OVER;
    }

    /**
     * Inner class to track game scores
     */
    public static class GameScore {
        private final Player player1;
        private final Player player2;
        private int player1Score = 0;
        private int player2Score = 0;

        public GameScore(Player player1, Player player2) {
            this.player1 = player1;
            this.player2 = player2;
        }

        public void addScore(Player player, int points) {
            if (player.equals(player1)) {
                player1Score += points;
            } else if (player.equals(player2)) {
                player2Score += points;
            } else {
                throw new IllegalArgumentException("Unknown player");
            }
        }

        public int getScore(Player player) {
            if (player.equals(player1)) {
                return player1Score;
            } else if (player.equals(player2)) {
                return player2Score;
            } else {
                throw new IllegalArgumentException("Unknown player");
            }
        }
    }
}
