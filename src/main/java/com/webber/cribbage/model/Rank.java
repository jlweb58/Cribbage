package com.webber.cribbage.model;


/**
 * Represents the count of a card for cribbage (Ace low, King high)
 * Created: 31.12.2014 14:20:46
 * 
 * @author John
 *
 */
public enum Rank {

  ACE(1),
  TWO(2),
  THREE(3),
  FOUR(4),
  FIVE(5),
  SIX(6),
  SEVEN(7),
  EIGHT(8),
  NINE(9),
  TEN(10),
  JACK(10),
  QUEEN(10),
  KING(10)
  ;
  
  private int count;
  
  private Rank(int count) {
    this.count = count;
  }
  
  public int getCount()  {
    return count;
  }
}
