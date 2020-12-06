package com.webber.cribbage.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;


/**
 * Created: 01.01.2015 16:25:19
 * 
 * @author John
 *
 */
public class CardTest {

  @Test
  public void testCompare() {
    Card c1 = new Card(Suit.SPADES, Rank.FIVE);
    Card c2 = new Card(Suit.HEARTS, Rank.FIVE);
    Card c3 = new Card(Suit.SPADES, Rank.EIGHT);
    Card c4 = new Card(Suit.SPADES, Rank.THREE);
    assertEquals(0, c1.compareTo(c2));
    assertTrue(c1.compareTo(c3) < 0);
    assertTrue(c1.compareTo(c4) > 0);
    
  }
}
