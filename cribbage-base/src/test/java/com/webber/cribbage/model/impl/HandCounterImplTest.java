package com.webber.cribbage.model.impl;

import static com.webber.cribbage.model.Rank.*;
import static com.webber.cribbage.model.Suit.HEARTS;
import static com.webber.cribbage.model.Suit.SPADES;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.webber.cribbage.model.Card;
import com.webber.cribbage.model.Hand;

/**
 * Created: 31.12.2014 15:00:22
 * 
 * @author John
 *
 */
public class HandCounterImplTest {

  private Hand hand;
  private HandCounterImpl handCounter;

  @Before
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
    assertEquals(6, handCounter.getTwoCardPermutations(hand).size());
  }

  @Test
  public void testGetTwoCardPermutationsWithCutCard() {
    hand = new Hand(5);
    hand.addCard(new Card(SPADES, THREE));
    hand.addCard(new Card(SPADES, FOUR));
    hand.addCard(new Card(SPADES, SIX));
    hand.addCard(new Card(HEARTS, ACE));
    hand.addCard(new Card(HEARTS, NINE));
    assertEquals(10, handCounter.getTwoCardPermutations(hand).size());
  }

  @Test
  public void testGetThreeCardPermutations() {
    hand.addCard(new Card(SPADES, THREE));
    hand.addCard(new Card(SPADES, FOUR));
    hand.addCard(new Card(SPADES, SIX));
    hand.addCard(new Card(HEARTS, ACE));
    assertEquals(4, handCounter.getThreeCardPermutations(hand).size());
  }
  
  @Test
  public void testGetThreeCardPermutationsWithCutCard() {
    hand = new Hand(5);
    hand.addCard(new Card(SPADES, THREE));
    hand.addCard(new Card(SPADES, FOUR));
    hand.addCard(new Card(SPADES, SIX));
    hand.addCard(new Card(HEARTS, ACE));
    hand.addCard(new Card(HEARTS, NINE));
    assertEquals(10, handCounter.getThreeCardPermutations(hand).size());
  }
  
  @Test
  public void testGetFourCardPermutations() {
    hand.addCard(new Card(SPADES, THREE));
    hand.addCard(new Card(SPADES, FOUR));
    hand.addCard(new Card(SPADES, SIX));
    hand.addCard(new Card(HEARTS, ACE));
    assertEquals(1, handCounter.getFourCardPermutations(hand).size());
  }
  
  @Test
  public void testGetFourCardPermutationsWithCutCard() {
    hand = new Hand(5);
    hand.addCard(new Card(SPADES, THREE));
    hand.addCard(new Card(SPADES, FOUR));
    hand.addCard(new Card(SPADES, SIX));
    hand.addCard(new Card(HEARTS, ACE));
    hand.addCard(new Card(HEARTS, NINE));
    assertEquals(5, handCounter.getFourCardPermutations(hand).size());
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

  //FIXME - after pairs are implemented this will have to be updated.
  @Test
  public void testHandTwoThreeCardFifteens() {
    hand.addCard(new Card(SPADES, THREE));
    hand.addCard(new Card(SPADES, FOUR));
    hand.addCard(new Card(SPADES, EIGHT));
    hand.addCard(new Card(HEARTS, THREE));
    assertCount(4);
  }

  @Test
  public void testHandFourCardFifteen() {
    hand.addCard(new Card(SPADES, ACE));
    hand.addCard(new Card(SPADES, THREE));
    hand.addCard(new Card(SPADES, SIX));
    hand.addCard(new Card(HEARTS, FIVE));
    assertCount(2);
  }
  
  //FIXME - only correct as long as just fifteens are counted.
  @Test
  public void testHandFiveCardFifteen() {
    hand = new Hand(5);
    hand.addCard(new Card(SPADES, TWO));
    hand.addCard(new Card(SPADES, FOUR));
    hand.addCard(new Card(SPADES, SIX));
    hand.addCard(new Card(HEARTS, ACE));
    hand.addCard(new Card(HEARTS, TWO));
    assertCount(2);

  }

  private void assertCount(int expect) {
    assertEquals(expect, handCounter.getHandCount(hand));
  }

}
