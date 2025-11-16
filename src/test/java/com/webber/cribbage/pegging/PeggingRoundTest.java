package com.webber.cribbage.pegging;

import com.webber.cribbage.model.Card;
import com.webber.cribbage.model.Hand;
import com.webber.cribbage.model.Player;
import com.webber.cribbage.model.Rank;
import com.webber.cribbage.model.Suit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the PeggingRound class, specifically testing the playCard method.
 */
public class PeggingRoundTest {

    private PeggingRound peggingRound;
    private Player player1;
    private Player player2;

    private Hand player1Hand;
    private Hand player2Hand;

    @BeforeEach
    public void setUp() {
        player1 = new Player("Player1");
        player2 = new Player("Player2");
        player1Hand = new Hand(4);
        player2Hand = new Hand(4);
        peggingRound = new PeggingRound(player1, player1Hand, player2, player2Hand, player1);

    }

    @Test
    public void playCard_updatesCurrentCountAndScoresCorrectly() {

        player1Hand.addCard(new Card(Suit.CLUBS, Rank.ACE));
        player1Hand.addCard(new Card(Suit.CLUBS, Rank.FOUR));
        player1Hand.addCard(new Card(Suit.CLUBS, Rank.SIX));

        player2Hand.addCard(new Card(Suit.DIAMONDS, Rank.THREE));
        player2Hand.addCard(new Card(Suit.DIAMONDS, Rank.FIVE));
        player2Hand.addCard(new Card(Suit.DIAMONDS, Rank.SEVEN));


        // Player1 plays ACE
        PeggingRound.PeggingResult result = peggingRound.playCard(new Card(Suit.CLUBS, Rank.ACE));
        assertEquals(player1, result.getPlayer());
        assertEquals(1, result.getNewCount());
        assertEquals(0, result.getPoints());

        // Player2 plays THREE
        result = peggingRound.playCard(new Card(Suit.DIAMONDS, Rank.THREE));
        assertEquals(player2, result.getPlayer());
        assertEquals(4, result.getNewCount());
        assertEquals(0, result.getPoints());

        // Player1 plays FOUR
        result = peggingRound.playCard(new Card(Suit.CLUBS, Rank.FOUR));
        assertEquals(player1, result.getPlayer());
        assertEquals(8, result.getNewCount());
        assertEquals(0, result.getPoints());
    }

    @Test
    public void playCard_switchesPlayerAfterPlay() {
        player1Hand.addCard(new Card(Suit.HEARTS, Rank.TWO));

        player2Hand.addCard(new Card(Suit.SPADES, Rank.FIVE));

        // Player1 begins
        assertEquals(player1, peggingRound.getCurrentPlayer());

        // Player1 plays TWO
        peggingRound.playCard(new Card(Suit.HEARTS, Rank.TWO));
        assertEquals(player2, peggingRound.getCurrentPlayer());

        // Player2 plays FIVE
        peggingRound.playCard(new Card(Suit.SPADES, Rank.FIVE));
        assertEquals(player1, peggingRound.getCurrentPlayer());
    }

    @Test
    public void playCard_throwsExceptionIfCardExceeds31() {
        player1Hand.addCard(new Card(Suit.HEARTS, Rank.KING));
        player1Hand.addCard(new Card(Suit.SPADES, Rank.NINE));

        player2Hand.addCard(new Card(Suit.SPADES, Rank.QUEEN));
        player2Hand.addCard(new Card(Suit.CLUBS, Rank.EIGHT));

        // Player1 plays KING
        peggingRound.playCard(new Card(Suit.HEARTS, Rank.KING));
        // Player 2 plays QUEEN
        peggingRound.playCard(new Card(Suit.SPADES, Rank.QUEEN));
        // Player 1 plays NINE
        peggingRound.playCard(new Card(Suit.SPADES, Rank.NINE));

        // Player2 attempts to play EIGHT (would exceed 31)
        assertThrows(IllegalArgumentException.class, () -> {
            peggingRound.playCard(new Card(Suit.CLUBS, Rank.EIGHT));
        });
    }

    @Test
    public void playCard_updatesScoresForScorablePlay() {
        player1Hand.addCard(new Card(Suit.CLUBS, Rank.TWO));
        player1Hand.addCard(new Card(Suit.HEARTS, Rank.THREE));

        player2Hand.addCard(new Card(Suit.DIAMONDS, Rank.THREE));
        player2Hand.addCard(new Card(Suit.SPADES, Rank.FOUR));

        // Player1 plays TWO
        PeggingRound.PeggingResult result = peggingRound.playCard(new Card(Suit.CLUBS, Rank.TWO));
        assertEquals(0, result.getPoints());
        assertEquals(2, peggingRound.getCurrentCount());
        assertEquals(0, peggingRound.getScore(player1));

        // Player2 plays THREE
        result = peggingRound.playCard(new Card(Suit.DIAMONDS, Rank.THREE));
        assertEquals(0, result.getPoints());
        assertEquals(5, peggingRound.getCurrentCount());
        assertEquals(0, peggingRound.getScore(player2));

        // Player1 plays THREE (pair scores 2 points)
        result = peggingRound.playCard(new Card(Suit.HEARTS, Rank.THREE));
        assertEquals(2, result.getPoints());
        assertEquals(8, peggingRound.getCurrentCount());
        assertEquals(2, peggingRound.getScore(player1));
    }

    @Test
    public void playCard_startsNewSequenceAfterReaching31() {
        player1Hand.addCard(new Card(Suit.HEARTS, Rank.KING));
        player1Hand.addCard(new Card(Suit.HEARTS, Rank.TEN));

        player2Hand.addCard(new Card(Suit.SPADES, Rank.JACK));
        player2Hand.addCard(new Card(Suit.SPADES, Rank.ACE));

        // Player1 plays KING
        PeggingRound.PeggingResult result = peggingRound.playCard(new Card(Suit.HEARTS, Rank.KING));
        assertEquals(10, result.getNewCount());
        assertFalse(peggingRound.isComplete());

        // Player 2 plays JACK
        result = peggingRound.playCard(new Card(Suit.SPADES, Rank.JACK));
        assertEquals(20, result.getNewCount());
        assertFalse(peggingRound.isComplete());

        // Player 1 plays TEN
        result = peggingRound.playCard(new Card(Suit.HEARTS, Rank.TEN));
        assertEquals(30, result.getNewCount());
        assertFalse(peggingRound.isComplete());

        // Player2 plays ACE
        result = peggingRound.playCard(new Card(Suit.SPADES, Rank.ACE));
        assertEquals(31, result.getNewCount());

        // Verify new sequence started
        assertEquals(0, peggingRound.getCurrentCount());
    }

    @Test
    public void playCard_throwsExceptionIfInvalidCardIsPlayed() {
        player1Hand.addCard(new Card(Suit.CLUBS, Rank.TWO));

        player2Hand.addCard(new Card(Suit.SPADES, Rank.FIVE));

        // Player1 attempts to play a card not in their hand
        assertThrows(IllegalArgumentException.class, () -> {
            peggingRound.playCard(new Card(Suit.HEARTS, Rank.TWO));
        });
    }

    @Test
    public void declareGo_throwsExceptionIfCurrentPlayerCanPlay() {
        player1Hand.addCard(new Card(Suit.SPADES, Rank.SIX));
        player1Hand.addCard(new Card(Suit.SPADES, Rank.EIGHT));
        player1Hand.addCard(new Card(Suit.SPADES, Rank.SEVEN));

        player2Hand.addCard(new Card(Suit.HEARTS, Rank.SIX));
        player2Hand.addCard(new Card(Suit.HEARTS, Rank.EIGHT));
        player2Hand.addCard(new Card(Suit.HEARTS, Rank.SEVEN));

        peggingRound.playCard(new Card(Suit.SPADES, Rank.SIX));
        assertThrows(IllegalStateException.class, () -> {
            peggingRound.declareGo();
        });
    }

    @Test
    public void declareGo_handlesOtherPlayerCantPlay() {
        player1Hand.addCard(new Card(Suit.SPADES, Rank.NINE));
        player1Hand.addCard(new Card(Suit.SPADES, Rank.EIGHT));
        player1Hand.addCard(new Card(Suit.SPADES, Rank.SEVEN));

        player2Hand.addCard(new Card(Suit.HEARTS, Rank.QUEEN));
        player2Hand.addCard(new Card(Suit.HEARTS, Rank.EIGHT));
        player2Hand.addCard(new Card(Suit.HEARTS, Rank.SEVEN));

        peggingRound.playCard(new Card(Suit.SPADES, Rank.NINE));
        peggingRound.playCard(new Card(Suit.HEARTS, Rank.QUEEN));
        peggingRound.playCard(new Card(Suit.SPADES, Rank.EIGHT));
        assertFalse(peggingRound.canCurrentPlayerPlay());
        PeggingRound.PeggingResult peggingResult = peggingRound.declareGo();
        // Player 1 got 1 point for the go < 31
        assertEquals(1, peggingResult.getPoints());
        // Player 2 starts the next sequence
        assertEquals(player2, peggingResult.getPlayer());
        // Count is back to 0
        assertEquals(0, peggingResult.getNewCount());
    }

    @Test
    public void declareGo_otherPlayerCanContinue() {
        player1Hand.addCard(new Card(Suit.SPADES, Rank.NINE));
        player1Hand.addCard(new Card(Suit.SPADES, Rank.EIGHT));
        player1Hand.addCard(new Card(Suit.SPADES, Rank.FOUR));

        player2Hand.addCard(new Card(Suit.HEARTS, Rank.QUEEN));
        player2Hand.addCard(new Card(Suit.HEARTS, Rank.EIGHT));
        player2Hand.addCard(new Card(Suit.HEARTS, Rank.SEVEN));

        peggingRound.playCard(new Card(Suit.SPADES, Rank.NINE));
        peggingRound.playCard(new Card(Suit.HEARTS, Rank.QUEEN));
        peggingRound.playCard(new Card(Suit.SPADES, Rank.EIGHT));
        assertFalse(peggingRound.canCurrentPlayerPlay());
        PeggingRound.PeggingResult peggingResult = peggingRound.declareGo();
        assertEquals(0, peggingResult.getPoints());
        assertEquals(27, peggingResult.getNewCount());
        assertFalse(peggingRound.isComplete());
        assertEquals(player1, peggingResult.getPlayer());
    }

    @Test
    public void simulatePlayOfCompleteHand() {
        player1Hand.addCard(new Card(Suit.SPADES, Rank.NINE));
        player1Hand.addCard(new Card(Suit.SPADES, Rank.EIGHT));
        player1Hand.addCard(new Card(Suit.SPADES, Rank.FOUR));
        player1Hand.addCard(new Card(Suit.SPADES, Rank.THREE));

        player2Hand.addCard(new Card(Suit.HEARTS, Rank.QUEEN));
        player2Hand.addCard(new Card(Suit.HEARTS, Rank.EIGHT));
        player2Hand.addCard(new Card(Suit.HEARTS, Rank.SEVEN));
        player2Hand.addCard(new Card(Suit.HEARTS, Rank.FIVE));

        peggingRound.playCard(new Card(Suit.SPADES, Rank.NINE));
        peggingRound.playCard(new Card(Suit.HEARTS, Rank.QUEEN));
        peggingRound.playCard(new Card(Suit.SPADES, Rank.EIGHT));
        assertFalse(peggingRound.canCurrentPlayerPlay());
        peggingRound.declareGo();
        PeggingRound.PeggingResult peggingResult = peggingRound.playCard(new Card(Suit.SPADES, Rank.FOUR));
        assertEquals(2, peggingResult.getPoints());
        assertEquals(31, peggingResult.getNewCount());
        assertEquals(0, peggingRound.getCurrentCount());
        assertEquals(player2, peggingRound.getCurrentPlayer());

        // Start the new sequence with player2
        peggingRound.playCard(new Card(Suit.HEARTS, Rank.EIGHT));
        peggingRound.playCard(new Card(Suit.SPADES, Rank.THREE));
        peggingRound.playCard(new Card(Suit.HEARTS, Rank.SEVEN));
        assertFalse(peggingRound.canCurrentPlayerPlay());
        peggingResult = peggingRound.declareGo();
        assertEquals(0, peggingResult.getPoints());
        peggingResult = peggingRound.playCard(new Card(Suit.HEARTS, Rank.FIVE));
        assertEquals(1, peggingResult.getPoints());
        assertEquals(23 , peggingResult.getNewCount());
    }

    @Test
    public void getPlayableCards_shouldReturnCorrectCardsInSimulatedHand() {
        player1Hand.addCard(new Card(Suit.SPADES, Rank.NINE));
        player1Hand.addCard(new Card(Suit.SPADES, Rank.EIGHT));
        player1Hand.addCard(new Card(Suit.SPADES, Rank.FOUR));
        player1Hand.addCard(new Card(Suit.SPADES, Rank.THREE));

        player2Hand.addCard(new Card(Suit.HEARTS, Rank.QUEEN));
        player2Hand.addCard(new Card(Suit.HEARTS, Rank.EIGHT));
        player2Hand.addCard(new Card(Suit.HEARTS, Rank.SEVEN));
        player2Hand.addCard(new Card(Suit.HEARTS, Rank.FIVE));

        List<Card> playableCards = peggingRound.getPlayableCards();
        // Before any card was played - they belong to player 1, all cards playable
        assertEquals(4, playableCards.size());
        assertTrue(playableCards.contains(new Card(Suit.SPADES, Rank.NINE)));
        assertTrue(playableCards.contains(new Card(Suit.SPADES, Rank.EIGHT)));
        assertTrue(playableCards.contains(new Card(Suit.SPADES, Rank.FOUR)));
        assertTrue(playableCards.contains(new Card(Suit.SPADES, Rank.THREE)));
        assertFalse(peggingRound.isComplete());

        // After player 1 has played a card, all player 2 cards are playable
        // Total 9
        peggingRound.playCard(new Card(Suit.SPADES, Rank.NINE));
        playableCards = peggingRound.getPlayableCards();
        assertEquals(4, playableCards.size());
        assertTrue(playableCards.contains(new Card(Suit.HEARTS, Rank.QUEEN)));
        assertTrue(playableCards.contains(new Card(Suit.HEARTS, Rank.EIGHT)));
        assertTrue(playableCards.contains(new Card(Suit.HEARTS, Rank.SEVEN)));
        assertTrue(playableCards.contains(new Card(Suit.HEARTS, Rank.FIVE)));
        assertFalse(peggingRound.isComplete());

        // After player 2 has played a card, player 1 has 3 cards left
        // Total 19
        peggingRound.playCard(new Card(Suit.HEARTS, Rank.QUEEN));
        playableCards = peggingRound.getPlayableCards();
        assertEquals(3, playableCards.size());
        assertTrue(playableCards.contains(new Card(Suit.SPADES, Rank.EIGHT)));
        assertTrue(playableCards.contains(new Card(Suit.SPADES, Rank.FOUR)));
        assertTrue(playableCards.contains(new Card(Suit.SPADES, Rank.THREE)));
        assertFalse(peggingRound.isComplete());

        // After player 1 plays the second card, player 2 has 3 cards left,
        // but none are playable
        // Total 27
        peggingRound.playCard(new Card(Suit.SPADES, Rank.EIGHT));
        playableCards = peggingRound.getPlayableCards();
        assertEquals(0, playableCards.size());
        assertFalse(peggingRound.isComplete());

        // Player 2 declares go. Player 1 plays card to reach 31.
        // Player 2 is to play, total 0, still 3 playable cards
        peggingRound.declareGo();
        peggingRound.playCard(new Card(Suit.SPADES, Rank.FOUR));
        playableCards = peggingRound.getPlayableCards();
        assertEquals(3, playableCards.size());
        assertTrue(playableCards.contains(new Card(Suit.HEARTS, Rank.EIGHT)));
        assertTrue(playableCards.contains(new Card(Suit.HEARTS, Rank.SEVEN)));
        assertTrue(playableCards.contains(new Card(Suit.HEARTS, Rank.FIVE)));
        assertFalse(peggingRound.isComplete());

        // Player 2 plays the second card, player 1 has 1 card left
        // Total 8
        peggingRound.playCard(new Card(Suit.HEARTS, Rank.EIGHT));
        playableCards = peggingRound.getPlayableCards();
        assertEquals(1, playableCards.size());
        assertTrue(playableCards.contains(new Card(Suit.SPADES, Rank.THREE)));
        assertFalse(peggingRound.isComplete());

        // Player 1 plays last card, player 2 has 2 cards left
        // Total 11
        peggingRound.playCard(new Card(Suit.SPADES, Rank.THREE));
        playableCards = peggingRound.getPlayableCards();
        assertEquals(2, playableCards.size());
        assertTrue(playableCards.contains(new Card(Suit.HEARTS, Rank.SEVEN)));
        assertTrue(playableCards.contains(new Card(Suit.HEARTS, Rank.FIVE)));
        assertFalse(peggingRound.isComplete());

        // Player 2 plays last two cards
        // Total 18, 23
        peggingRound.playCard(new Card(Suit.HEARTS, Rank.SEVEN));
        peggingRound.declareGo();
        playableCards = peggingRound.getPlayableCards();
        assertEquals(1, playableCards.size());
        assertTrue(playableCards.contains(new Card(Suit.HEARTS, Rank.FIVE)));
        PeggingRound.PeggingResult result = peggingRound.playCard(new Card(Suit.HEARTS, Rank.FIVE));
        playableCards = peggingRound.getPlayableCards();
        assertEquals(0, playableCards.size());
        assertTrue(peggingRound.isComplete());
        assertEquals(1, result.getPoints());
    }

    @Test
    public void getScores_shouldReturnBothPlayerScores() {
        player1Hand.addCard(new Card(Suit.CLUBS, Rank.TWO));
        player1Hand.addCard(new Card(Suit.HEARTS, Rank.THREE));

        player2Hand.addCard(new Card(Suit.DIAMONDS, Rank.THREE));
        player2Hand.addCard(new Card(Suit.SPADES, Rank.FOUR));

        peggingRound.playCard(new Card(Suit.CLUBS, Rank.TWO));
        peggingRound.playCard(new Card(Suit.DIAMONDS, Rank.THREE));
        peggingRound.playCard(new Card(Suit.HEARTS, Rank.THREE)); // Pair: 2 points

        Map<Player, Integer> scores = peggingRound.getScores();
        assertEquals(2, scores.get(player1).intValue());
        assertEquals(0, scores.get(player2).intValue());
    }


}
