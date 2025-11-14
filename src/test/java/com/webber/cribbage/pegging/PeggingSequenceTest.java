package com.webber.cribbage.pegging;

import com.webber.cribbage.model.Card;
import com.webber.cribbage.model.Hand;
import com.webber.cribbage.model.Player;
import com.webber.cribbage.model.Rank;
import com.webber.cribbage.model.Suit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PeggingSequenceTest {


    private Player player1;
    private Player player2;
    private PeggingSequence peggingSequence;

    @BeforeEach
    public void setUp() {
        player1 = new Player("p1");
        player2 = new Player("p2");
        peggingSequence = new PeggingSequence();
    }

    @Test
    public void itShouldCountCorrectlyAndAward1PointGoIfBelow31() {
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
        Card card1 = new Card(Suit.CLUBS, Rank.TEN);
        Card card2 = new Card(Suit.DIAMONDS, Rank.FIVE);
        assertEquals(0, peggingSequence.playCard(card1, player1));
        assertEquals(2, peggingSequence.playCard(card2, player2));
    }

    @Test
    public void itShouldCountOrderedRunsCorrectly() {
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
        Card card1 = new Card(Suit.CLUBS, Rank.FOUR);
        Card card2 = new Card(Suit.DIAMONDS, Rank.FIVE);
        Card card3 = new Card(Suit.HEARTS, Rank.SIX);
        assertEquals(0, peggingSequence.playCard(card1, player1));
        assertEquals(0, peggingSequence.playCard(card2, player2));
        assertEquals(5, peggingSequence.playCard(card3, player1));
    }

    @Test
    public void itCanHandleA7CardRun() {
        Card card1 = new Card(Suit.CLUBS, Rank.ACE);
        Card card2 = new Card(Suit.DIAMONDS, Rank.TWO);
        Card card3 = new Card(Suit.HEARTS, Rank.THREE);
        Card card4 = new Card(Suit.SPADES, Rank.FOUR);
        Card card5 = new Card(Suit.CLUBS, Rank.SIX);
        Card card6 = new Card(Suit.HEARTS, Rank.FIVE);
        Card card7 = new Card(Suit.CLUBS, Rank.SEVEN);

        assertEquals(0, peggingSequence.playCard(card1, player1));
        assertEquals(0, peggingSequence.playCard(card2, player2));
        assertEquals(3, peggingSequence.playCard(card3, player1));
        assertEquals(4, peggingSequence.playCard(card4, player2));
        assertEquals(0, peggingSequence.playCard(card5, player1));
        assertEquals(6, peggingSequence.playCard(card6, player2));
        assertEquals(7, peggingSequence.playCard(card7, player1));
    }

    @Test
    public void itCanHandleRunAnd15AtSameTime() {
        Card card1 = new Card(Suit.SPADES, Rank.FOUR);
        Card card2 = new Card(Suit.CLUBS, Rank.SIX);
        Card card3 = new Card(Suit.HEARTS, Rank.FIVE);
        assertEquals(0, peggingSequence.playCard(card1, player1));
        assertEquals(0, peggingSequence.playCard(card2, player2));
        assertEquals(5, peggingSequence.playCard(card3, player1));
    }

    @Test
    public void itShouldThrowExceptionWhenCardWouldExceed31() {
        Card card1 = new Card(Suit.CLUBS, Rank.KING);
        Card card2 = new Card(Suit.DIAMONDS, Rank.QUEEN);
        Card card3 = new Card(Suit.HEARTS, Rank.JACK);
        Card card4 = new Card(Suit.SPADES, Rank.FOUR);
        peggingSequence.playCard(card1, player1);
        peggingSequence.playCard(card2, player2);
        peggingSequence.playCard(card3, player1);

        assertThrows(IllegalArgumentException.class, () -> {
            peggingSequence.playCard(card4, player1);
        });
    }

    @Test
    public void itShouldTrackAllPlays() {
        Card card1 = new Card(Suit.CLUBS, Rank.TWO);
        Card card2 = new Card(Suit.DIAMONDS, Rank.THREE);

        peggingSequence.playCard(card1, player1);
        peggingSequence.playCard(card2, player2);

        List<PeggingSequence.CardPlay> plays = peggingSequence.getPlays();
        assertEquals(2, plays.size());
        assertEquals(card1, plays.get(0).getCard());
        assertEquals(player1, plays.get(0).getPlayer());
        assertEquals(card2, plays.get(1).getCard());
        assertEquals(player2, plays.get(1).getPlayer());
    }

    @Test
    public void itShouldMarkSequenceCompleteAt31() {
        assertFalse(peggingSequence.isComplete());

        peggingSequence.playCard(new Card(Suit.CLUBS, Rank.KING), player1);
        assertFalse(peggingSequence.isComplete());

        peggingSequence.playCard(new Card(Suit.DIAMONDS, Rank.KING), player2);
        assertFalse(peggingSequence.isComplete());

        peggingSequence.playCard(new Card(Suit.HEARTS, Rank.KING), player1);
        assertFalse(peggingSequence.isComplete());

        peggingSequence.playCard(new Card(Suit.SPADES, Rank.ACE), player2);
        assertTrue(peggingSequence.isComplete());
    }
}
