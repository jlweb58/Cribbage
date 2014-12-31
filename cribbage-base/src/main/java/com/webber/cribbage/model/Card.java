package com.webber.cribbage.model;

/**
 * 
 * The abstraction of a playing card for Cribbage, encapsulating suit and rank
 * Created: 31.12.2014 14:24:43
 * 
 * @author John
 *
 */
public class Card {

  private final Suit suit;

  private final Rank rank;

  public Card(Suit suit, Rank rank) {
    this.suit = suit;
    this.rank = rank;
  }

  public Suit getSuit() {
    return suit;
  }

  public Rank getRank() {
    return rank;
  }

  @Override
  public String toString() {
    return "[" + rank + " of " + suit + "]";
  }

}
