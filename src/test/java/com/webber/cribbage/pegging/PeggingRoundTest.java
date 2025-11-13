package com.webber.cribbage.pegging;

import com.webber.cribbage.model.Card;
import com.webber.cribbage.model.Hand;
import com.webber.cribbage.model.Player;
import com.webber.cribbage.model.Rank;
import com.webber.cribbage.model.Suit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the PeggingRound class, specifically testing the playCard method.
 */
public class PeggingRoundTest {

    @Test
    public void playCard_updatesCurrentCountAndScoresCorrectly() {
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");

        Hand player1Hand = new Hand(4);
        player1Hand.addCard(new Card(Suit.CLUBS, Rank.ACE));
        player1Hand.addCard(new Card(Suit.CLUBS, Rank.FOUR));
        player1Hand.addCard(new Card(Suit.CLUBS, Rank.SIX));

        Hand player2Hand = new Hand(4);
        player2Hand.addCard(new Card(Suit.DIAMONDS, Rank.THREE));
        player2Hand.addCard(new Card(Suit.DIAMONDS, Rank.FIVE));
        player2Hand.addCard(new Card(Suit.DIAMONDS, Rank.SEVEN));

        PeggingRound peggingRound = new PeggingRound(player1, player1Hand, player2, player2Hand, player1);

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
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");

        Hand player1Hand = new Hand(1);
        player1Hand.addCard(new Card(Suit.HEARTS, Rank.TWO));

        Hand player2Hand = new Hand(1);
        player2Hand.addCard(new Card(Suit.SPADES, Rank.FIVE));

        PeggingRound peggingRound = new PeggingRound(player1, player1Hand, player2, player2Hand, player1);

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
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");

        Hand player1Hand = new Hand(2);
        player1Hand.addCard(new Card(Suit.HEARTS, Rank.KING));
        player1Hand.addCard(new Card(Suit.SPADES, Rank.NINE));

        Hand player2Hand = new Hand(2);
        player2Hand.addCard(new Card(Suit.SPADES, Rank.QUEEN));
        player2Hand.addCard(new Card(Suit.CLUBS, Rank.EIGHT));

        PeggingRound peggingRound = new PeggingRound(player1, player1Hand, player2, player2Hand, player1);

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
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");

        Hand player1Hand = new Hand(4);
        player1Hand.addCard(new Card(Suit.CLUBS, Rank.TWO));
        player1Hand.addCard(new Card(Suit.HEARTS, Rank.THREE));

        Hand player2Hand = new Hand(4);
        player2Hand.addCard(new Card(Suit.DIAMONDS, Rank.THREE));
        player2Hand.addCard(new Card(Suit.SPADES, Rank.FOUR));

        PeggingRound peggingRound = new PeggingRound(player1, player1Hand, player2, player2Hand, player1);

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
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");

        Hand player1Hand = new Hand(1);
        player1Hand.addCard(new Card(Suit.HEARTS, Rank.KING));

        Hand player2Hand = new Hand(1);
        player2Hand.addCard(new Card(Suit.SPADES, Rank.ACE));

        PeggingRound peggingRound = new PeggingRound(player1, player1Hand, player2, player2Hand, player1);

        // Player1 plays KING
        PeggingRound.PeggingResult result = peggingRound.playCard(new Card(Suit.HEARTS, Rank.KING));
        assertEquals(10, result.getNewCount());
        assertFalse(peggingRound.isComplete());

        // Player2 plays ACE
        result = peggingRound.playCard(new Card(Suit.SPADES, Rank.ACE));
        assertEquals(31, result.getNewCount());

        // Verify new sequence started
        assertEquals(0, peggingRound.getCurrentCount());
    }

    @Test
    public void playCard_throwsExceptionIfInvalidCardIsPlayed() {
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");

        Hand player1Hand = new Hand(1);
        player1Hand.addCard(new Card(Suit.CLUBS, Rank.TWO));

        Hand player2Hand = new Hand(1);
        player2Hand.addCard(new Card(Suit.SPADES, Rank.FIVE));

        PeggingRound peggingRound = new PeggingRound(player1, player1Hand, player2, player2Hand, player1);

        // Player1 attempts to play a card not in their hand
        assertThrows(IllegalArgumentException.class, () -> {
            peggingRound.playCard(new Card(Suit.HEARTS, Rank.TWO));
        });
    }
}
