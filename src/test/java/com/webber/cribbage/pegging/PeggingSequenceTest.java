package com.webber.cribbage.pegging;

import com.webber.cribbage.model.Card;
import com.webber.cribbage.model.Hand;
import com.webber.cribbage.model.Player;
import com.webber.cribbage.model.Rank;
import com.webber.cribbage.model.Suit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PeggingSequenceTest {


    private Player player1;
    private Player player2;

    @BeforeEach
    public void setUp() {
        player1 = new Player("p1");
        player2 = new Player("p2");
    }

    @Test
    public void itShouldCountCorrectlyAndAward1PointGoIfBelow31() {
        PeggingSequence peggingSequence = new PeggingSequence();
        Card card1 = new Card(Suit.CLUBS, Rank.JACK);
        Card card2 = new Card(Suit.DIAMONDS, Rank.TWO);
        Card card3 = new Card(Suit.HEARTS, Rank.KING);
        Card card4 = new Card(Suit.SPADES, Rank.EIGHT);
        peggingSequence.playCard(card1, player1);
        assertEquals(10, peggingSequence.getCurrentCount());
        peggingSequence.playCard(card2, player2);
        assertEquals(12, peggingSequence.getCurrentCount());
        peggingSequence.playCard(card3, player1);
        assertEquals(22, peggingSequence.getCurrentCount());
        peggingSequence.playCard(card4, player2);
        assertEquals(30, peggingSequence.getCurrentCount());
        assertEquals(1, peggingSequence.awardGo(player2));
    }

    @Test
    public void itShouldCountCorrectlyAndAward2PointsGoIfExactly31() {
        PeggingSequence peggingSequence = new PeggingSequence();
        Card card1 = new Card(Suit.CLUBS, Rank.JACK);
        Card card2 = new Card(Suit.DIAMONDS, Rank.TWO);
        Card card3 = new Card(Suit.HEARTS, Rank.KING);
        Card card4 = new Card(Suit.SPADES, Rank.NINE);
        peggingSequence.playCard(card1, player1);
        assertEquals(10, peggingSequence.getCurrentCount());
        peggingSequence.playCard(card2, player2);
        assertEquals(12, peggingSequence.getCurrentCount());
        peggingSequence.playCard(card3, player1);
        assertEquals(22, peggingSequence.getCurrentCount());
        assertEquals(2, peggingSequence.playCard(card4, player2));
        assertEquals(31, peggingSequence.getCurrentCount());
        assertEquals(0, peggingSequence.awardGo(player2));
    }

    @Test
    public void itShouldAllowPlayingCardTotalBelow32()  {
        PeggingSequence peggingSequence = new PeggingSequence();
        Card card1 = new Card(Suit.CLUBS, Rank.JACK);
        Card card2 = new Card(Suit.DIAMONDS, Rank.TWO);
        Card card3 = new Card(Suit.HEARTS, Rank.KING);
        Card card4 = new Card(Suit.SPADES, Rank.NINE);
        assertTrue(peggingSequence.canPlayCard(card1));
        peggingSequence.playCard(card1, player1);
        assertTrue(peggingSequence.canPlayCard(card2));
        peggingSequence.playCard(card2, player2);
        assertTrue(peggingSequence.canPlayCard(card3));
        peggingSequence.playCard(card3, player1);
        assertTrue(peggingSequence.canPlayCard(card4));
        peggingSequence.playCard(card4, player2);
    }

    @Test
    public void itShouldPreventPlayingCardTotalAbove31()  {
        PeggingSequence peggingSequence = new PeggingSequence();
        Card card1 = new Card(Suit.CLUBS, Rank.JACK);
        Card card2 = new Card(Suit.DIAMONDS, Rank.TWO);
        Card card3 = new Card(Suit.HEARTS, Rank.KING);
        Card card4 = new Card(Suit.SPADES, Rank.TEN);
        assertTrue(peggingSequence.canPlayCard(card1));
        peggingSequence.playCard(card1, player1);
        assertTrue(peggingSequence.canPlayCard(card2));
        peggingSequence.playCard(card2, player2);
        assertTrue(peggingSequence.canPlayCard(card3));
        peggingSequence.playCard(card3, player1);
        assertFalse(peggingSequence.canPlayCard(card4));
    }

    @Test
    public void itShouldIdentifyLastPlayerToPlay() {
        PeggingSequence peggingSequence = new PeggingSequence();
        Card card1 = new Card(Suit.CLUBS, Rank.JACK);
        Card card2 = new Card(Suit.DIAMONDS, Rank.TWO);
        Card card3 = new Card(Suit.HEARTS, Rank.KING);
        peggingSequence.playCard(card1, player1);
        assertEquals(player1, peggingSequence.getLastPlayerToPlay());
        peggingSequence.playCard(card2, player2);
        assertEquals(player2, peggingSequence.getLastPlayerToPlay());
        peggingSequence.playCard(card3, player1);
        assertEquals(player1, peggingSequence.getLastPlayerToPlay());
    }

@Test
public void itShouldCountPairsCorrectly() {
        PeggingSequence peggingSequence = new PeggingSequence();
        Card card1 = new Card(Suit.CLUBS, Rank.TWO);
        Card card2 = new Card(Suit.DIAMONDS, Rank.TWO);
        Card card3 = new Card(Suit.HEARTS, Rank.TWO);
        Card card4 = new Card(Suit.SPADES, Rank.TWO);
        assertEquals(0, peggingSequence.playCard(card1, player1));
        assertEquals(2,  peggingSequence.playCard(card2, player2));
        assertEquals(6, peggingSequence.playCard(card3, player1));
        assertEquals(12, peggingSequence.playCard(card4, player2));
    }

    @Test
    public void itShouldCountFifteensCorrectly() {
        PeggingSequence peggingSequence = new PeggingSequence();
        Card card1 = new Card(Suit.CLUBS, Rank.TEN);
        Card card2 = new Card(Suit.DIAMONDS, Rank.FIVE);
        assertEquals(0, peggingSequence.playCard(card1, player1));
        assertEquals(2, peggingSequence.playCard(card2, player2));
    }

    @Test
    public void itShouldCountOrderedRunsCorrectly() {
        PeggingSequence peggingSequence = new PeggingSequence();
        Card card1 = new Card(Suit.CLUBS, Rank.TWO);
        Card card2 = new Card(Suit.DIAMONDS, Rank.THREE);
        Card card3 = new Card(Suit.HEARTS, Rank.FOUR);
        Card card4 = new Card(Suit.SPADES, Rank.FIVE);
        assertEquals(0, peggingSequence.playCard(card1, player1));
        assertEquals(0, peggingSequence.playCard(card2, player2));
        assertEquals(3, peggingSequence.playCard(card3, player1));
        assertEquals(4, peggingSequence.playCard(card4, player2));
    }

    @Test
    public void itShouldCountUnorderedRunsCorrectly() {
        PeggingSequence peggingSequence = new PeggingSequence();
        Card card1 = new Card(Suit.CLUBS, Rank.TWO);
        Card card2 = new Card(Suit.DIAMONDS, Rank.THREE);
        Card card3 = new Card(Suit.HEARTS, Rank.FOUR);
        Card card4 = new Card(Suit.SPADES, Rank.FIVE);
        assertEquals(0, peggingSequence.playCard(card1, player1));
        assertEquals(0, peggingSequence.playCard(card3, player2));
        assertEquals(0, peggingSequence.playCard(card4, player1));
        assertEquals(4, peggingSequence.playCard(card2, player2));


    }

    @Test
    public void itShouldHandleAMixedSequenceWithoutGoCorrectly1() {
        PeggingSequence peggingSequence = new PeggingSequence();
        Card card1 = new Card(Suit.CLUBS, Rank.TWO);
        Card card2 = new Card(Suit.DIAMONDS, Rank.THREE);
        Card card3 = new Card(Suit.HEARTS, Rank.FOUR);
        Card card4 = new Card(Suit.SPADES, Rank.FIVE);
        Card card5 = new Card(Suit.CLUBS, Rank.FIVE);
        assertEquals(0, peggingSequence.playCard(card1, player1));
        assertEquals(0, peggingSequence.playCard(card2, player2));
        assertEquals(3, peggingSequence.playCard(card3, player1));
        assertEquals(4, peggingSequence.playCard(card4, player2));
        assertEquals(2, peggingSequence.playCard(card5, player1));
    }

    @Test
    public void itShouldHandleAMixedSequenceWithoutGoCorrectly2() {
        PeggingSequence peggingSequence = new PeggingSequence();
        Card card1 = new Card(Suit.CLUBS, Rank.FOUR);
        Card card2 = new Card(Suit.DIAMONDS, Rank.FIVE);
        Card card3 = new Card(Suit.HEARTS, Rank.SIX);
        assertEquals(0, peggingSequence.playCard(card1, player1));
        assertEquals(0, peggingSequence.playCard(card2, player2));
        assertEquals(5, peggingSequence.playCard(card3, player1));
    }

}
