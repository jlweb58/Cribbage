package com.webber.cribbage.model;

import lombok.Getter;

import java.util.Objects;

/**
 * 
 * The abstraction of a playing card for Cribbage, encapsulating suit and rank
 * Created: 31.12.2014 14:24:43
 * 
 * @author John
 *
 */
@Getter
public class Card implements Comparable<Card>{

  private final Suit suit;

  private final Rank rank;

  public Card(Suit suit, Rank rank) {
    this.suit = suit;
    this.rank = rank;
  }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return suit == card.suit && rank == card.rank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(suit, rank);
    }

    @Override
  public String toString() {
    return "[" + rank + " of " + suit + "]";
  }

  @Override
  public int compareTo(Card o) {
    return this.getRank().ordinal() - o.getRank().ordinal();
  }

}
