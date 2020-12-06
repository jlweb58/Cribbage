package com.webber.cribbage.model.impl;

import static com.webber.cribbage.model.Rank.*;
import static com.webber.cribbage.model.Suit.CLUBS;
import static com.webber.cribbage.model.Suit.DIAMONDS;
import static com.webber.cribbage.model.Suit.HEARTS;
import static com.webber.cribbage.model.Suit.SPADES;
import static com.webber.cribbage.model.impl.HandCounterImpl.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.webber.cribbage.model.Card;
import com.webber.cribbage.model.Hand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Created: 31.12.2014 15:00:22
 * 
 * @author John
 *
 */
public class HandCounterImplTest {

  private Hand hand;
  private HandCounterImpl handCounter;

  @BeforeEach
  public void setUp() {
    hand = new Hand(4);
    handCounter = new HandCounterImpl();
  }

  @Test
  public void testGetTwoCardPermutations() {
    hand.addCard(new Card(SPADES, THREE));
    hand.addCard(new Card(SPADES, FOUR));
    hand.addCard(new Card(SPADES, SIX));
    hand.addCard(new Card(HEARTS, ACE));
    assertEquals(6, getTwoCardPermutations(hand).size());
  }

  @Test
  public void testGetTwoCardPermutationsWithCutCard() {
    hand = new Hand(5);
    hand.addCard(new Card(SPADES, THREE));
    hand.addCard(new Card(SPADES, FOUR));
    hand.addCard(new Card(SPADES, SIX));
    hand.addCard(new Card(HEARTS, ACE));
    hand.addCard(new Card(HEARTS, NINE));
    assertEquals(10, getTwoCardPermutations(hand).size());
  }

  @Test
  public void testGetThreeCardPermutations() {
    hand.addCard(new Card(SPADES, THREE));
    hand.addCard(new Card(SPADES, FOUR));
    hand.addCard(new Card(SPADES, SIX));
    hand.addCard(new Card(HEARTS, ACE));
    assertEquals(4, getThreeCardPermutations(hand).size());
  }
  
  @Test
  public void testGetThreeCardPermutationsWithCutCard() {
    hand = new Hand(5);
    hand.addCard(new Card(SPADES, THREE));
    hand.addCard(new Card(SPADES, FOUR));
    hand.addCard(new Card(SPADES, SIX));
    hand.addCard(new Card(HEARTS, ACE));
    hand.addCard(new Card(HEARTS, NINE));
    assertEquals(10, getThreeCardPermutations(hand).size());
  }
  
  @Test
  public void testGetFourCardPermutations() {
    hand.addCard(new Card(SPADES, THREE));
    hand.addCard(new Card(SPADES, FOUR));
    hand.addCard(new Card(SPADES, SIX));
    hand.addCard(new Card(HEARTS, ACE));
    assertEquals(1, getFourCardPermutations(hand).size());
  }
  
  @Test
  public void testGetFourCardPermutationsWithCutCard() {
    hand = new Hand(5);
    hand.addCard(new Card(SPADES, THREE));
    hand.addCard(new Card(SPADES, FOUR));
    hand.addCard(new Card(SPADES, SIX));
    hand.addCard(new Card(HEARTS, ACE));
    hand.addCard(new Card(HEARTS, NINE));
    assertEquals(5, getFourCardPermutations(hand).size());
  }

  @Test
  public void testHandNoPoints() {
    hand.addCard(new Card(SPADES, THREE));
    hand.addCard(new Card(SPADES, FOUR));
    hand.addCard(new Card(SPADES, SIX));
    hand.addCard(new Card(HEARTS, ACE));
    assertCount(0);
  }

  @Test
  public void testHandOneTwoCardFifteen() {
    hand.addCard(new Card(SPADES, TWO));
    hand.addCard(new Card(SPADES, FOUR));
    hand.addCard(new Card(SPADES, FIVE));
    hand.addCard(new Card(HEARTS, KING));
    assertCount(2);
  }

  @Test
  public void testHandTwoTwoCardFifteens() {
    hand.addCard(new Card(SPADES, TEN));
    hand.addCard(new Card(SPADES, FOUR));
    hand.addCard(new Card(SPADES, FIVE));
    hand.addCard(new Card(HEARTS, KING));
    assertCount(4);
  }

  @Test
  public void testHandOneThreeCardFifteen() {
    hand.addCard(new Card(SPADES, THREE));
    hand.addCard(new Card(SPADES, FOUR));
    hand.addCard(new Card(SPADES, EIGHT));
    hand.addCard(new Card(HEARTS, ACE));
    assertCount(2);
  }

  @Test
  public void testHandTwoThreeCardFifteens() {
    hand.addCard(new Card(SPADES, THREE));
    hand.addCard(new Card(SPADES, FOUR));
    hand.addCard(new Card(SPADES, EIGHT));
    hand.addCard(new Card(HEARTS, THREE));
    assertCount(6);
  }

  @Test
  public void testHandFourCardFifteen() {
    hand.addCard(new Card(SPADES, ACE));
    hand.addCard(new Card(SPADES, THREE));
    hand.addCard(new Card(SPADES, SIX));
    hand.addCard(new Card(HEARTS, FIVE));
    assertCount(2);
  }
  
  @Test
  public void testHandFiveCardFifteenPlusPair() {
    hand.addCard(new Card(SPADES, TWO));
    hand.addCard(new Card(SPADES, FOUR));
    hand.addCard(new Card(SPADES, SIX));
    hand.addCard(new Card(HEARTS, ACE));
    Card cutCard = new Card(HEARTS, TWO);
    assertEquals(4, handCounter.getHandCount(hand, cutCard));
  }

  @Test
  public void testCountThreeCardRun() {
    hand.addCard(new Card(SPADES, TEN));
    hand.addCard(new Card(SPADES, JACK));
    hand.addCard(new Card(SPADES, QUEEN));
    hand.addCard(new Card(HEARTS, ACE));
    assertCount(3);
  }
  
  @Test
  public void testCountFourCardRun() {
    hand.addCard(new Card(SPADES, TEN));
    hand.addCard(new Card(SPADES, JACK));
    hand.addCard(new Card(SPADES, QUEEN));
    hand.addCard(new Card(HEARTS, KING));
    assertCount(4);
  }

  @Test
  public void testCountThreeCardDoubleRun() {
    hand.addCard(new Card(SPADES, TEN));
    hand.addCard(new Card(SPADES, JACK));
    hand.addCard(new Card(SPADES, QUEEN));
    hand.addCard(new Card(HEARTS, TEN));
    assertCount(8);
  }

  @Test
  public void testCountTwoPairs() {
    hand.addCard(new Card(SPADES, TEN));
    hand.addCard(new Card(SPADES, NINE));
    hand.addCard(new Card(HEARTS, NINE));
    hand.addCard(new Card(HEARTS, TEN));
    assertCount(4);
  }

  @Test
  public void testCountThreeOfAKind() {
    hand.addCard(new Card(DIAMONDS, NINE));
    hand.addCard(new Card(SPADES, NINE));
    hand.addCard(new Card(HEARTS, NINE));
    hand.addCard(new Card(HEARTS, TEN));
    assertCount(6);
  }

  @Test
  public void testCountFourOfAKind() {
    hand.addCard(new Card(DIAMONDS, NINE));
    hand.addCard(new Card(SPADES, NINE));
    hand.addCard(new Card(HEARTS, NINE));
    hand.addCard(new Card(CLUBS, NINE));
    assertCount(12);
  }

  @Test
  public void testCountFourFlush() {
    hand.addCard(new Card(SPADES, ACE));
    hand.addCard(new Card(SPADES, TWO));
    hand.addCard(new Card(SPADES, FOUR));
    hand.addCard(new Card(SPADES, SIX));
    assertCount(4);
  }

  @Test
  public void testCountThreeFlush() {
    hand.addCard(new Card(SPADES, ACE));
    hand.addCard(new Card(HEARTS, TWO));
    hand.addCard(new Card(SPADES, FOUR));
    hand.addCard(new Card(SPADES, SIX));
    assertCount(0);
  }
  
  @Test
  public void testCountFiveFlush() {
    hand.addCard(new Card(SPADES, ACE));
    hand.addCard(new Card(SPADES, TWO));
    hand.addCard(new Card(SPADES, FOUR));
    hand.addCard(new Card(SPADES, SIX));
    Card cutCard = new Card(SPADES, SEVEN);
    assertEquals(7, handCounter.getHandCount(hand, cutCard));
  }

  @Test
  public void testCountNibs() {
    Card cutCard = new Card(SPADES, EIGHT);
    hand.addCard(new Card(SPADES, JACK));
    hand.addCard(new Card(SPADES, TWO));
    hand.addCard(new Card(SPADES, FOUR));
    hand.addCard(new Card(CLUBS, SIX));
    assertEquals(1, handCounter.getHandCount(hand, cutCard));
  }
  
  private void assertCount(int expect) {
    assertEquals(expect, handCounter.getHandCount(hand));
  }

}
