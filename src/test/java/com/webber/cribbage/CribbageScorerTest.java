package com.webber.cribbage;

import com.webber.cribbage.model.Card;
import com.webber.cribbage.model.Player;
import com.webber.cribbage.model.Rank;
import com.webber.cribbage.model.Suit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CribbageScorerTest {

    private CribbageScorer scorer;
    private Player player;

    @BeforeEach
    public void setUp() throws Exception {
        player = new Player("Test Player");
        scorer = new CribbageScorer();
    }

    @Test
    public void testExceeding31ThrowsException() throws Exception {

        scorer.playCard(new Card(Suit.SPADES, Rank.TEN), player);
        scorer.playCard(new Card(Suit.HEARTS, Rank.TEN), player);
        scorer.playCard(new Card(Suit.DIAMONDS, Rank.TEN), player);
        scorer.playCard(new Card(Suit.CLUBS, Rank.ACE), player);
        assertThrows(WouldExceed31Exception.class, () -> {
            scorer.playCard(new Card(Suit.CLUBS, Rank.TWO), player);
        });
    }

    @Test
    public void testReaching15ScoresTwoPoints() throws WouldExceed31Exception {
        assertEquals(0, scorer.playCard(new Card(Suit.SPADES, Rank.TEN), player));
        assertEquals(2, scorer.playCard(new Card(Suit.HEARTS, Rank.FIVE), player));
    }


    @Test
    public void testPlayingPairScoresTwoPoints() throws WouldExceed31Exception {
        assertEquals(0, scorer.playCard(new Card(Suit.SPADES, Rank.TEN), player));
        assertEquals(2, scorer.playCard(new Card(Suit.HEARTS, Rank.TEN), player));
    }

    @Test
    public void testPlayingSecondPairScoresSixPoints() throws WouldExceed31Exception {
        assertEquals(0, scorer.playCard(new Card(Suit.SPADES, Rank.TEN), player));
        assertEquals(2, scorer.playCard(new Card(Suit.HEARTS, Rank.TEN), player));
        assertEquals(6, scorer.playCard(new Card(Suit.CLUBS, Rank.TEN), player));
    }

    @Test
    public void testPlayingThreeCardConsecutiveRunScoresThreePoints() throws WouldExceed31Exception {
        assertEquals(0, scorer.playCard(new Card(Suit.SPADES, Rank.TEN), player));
        assertEquals(0, scorer.playCard(new Card(Suit.HEARTS, Rank.JACK), player));
        assertEquals(3, scorer.playCard(new Card(Suit.CLUBS, Rank.QUEEN), player));
    }

    @Test
    public void testPlayingThreeCardNonConsecutiveRunScoresThreePoints() throws WouldExceed31Exception {
        assertEquals(0, scorer.playCard(new Card(Suit.CLUBS, Rank.QUEEN), player));
        assertEquals(0, scorer.playCard(new Card(Suit.HEARTS, Rank.JACK), player));
        assertEquals(3, scorer.playCard(new Card(Suit.DIAMONDS, Rank.KING), player));
    }

    @Test
    public void testPlayingFourCardConsecutiveRunScoresFourPoints() throws WouldExceed31Exception {
        assertEquals(0, scorer.playCard(new Card(Suit.SPADES, Rank.THREE), player));
        assertEquals(0, scorer.playCard(new Card(Suit.HEARTS, Rank.FOUR), player));
        assertEquals(3, scorer.playCard(new Card(Suit.CLUBS, Rank.FIVE), player));
        assertEquals(4, scorer.playCard(new Card(Suit.DIAMONDS, Rank.SIX), player));
    }

    @Test
    public void testPlayingFourCardNonConsecutiveRunScoresFourPoints() throws WouldExceed31Exception {
        assertEquals(0, scorer.playCard(new Card(Suit.SPADES, Rank.THREE), player));
        assertEquals(0, scorer.playCard(new Card(Suit.HEARTS, Rank.FOUR), player));
        assertEquals(0, scorer.playCard(new Card(Suit.CLUBS, Rank.SIX), player));
        assertEquals(4, scorer.playCard(new Card(Suit.DIAMONDS, Rank.FIVE), player));
    }

}
