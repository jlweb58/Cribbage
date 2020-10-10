package com.webber.cribbage.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created: 31.12.2014 14:33:47
 * 
 * @author John
 *
 */
public class Hand {
  private List<Card> unplayedCards = new ArrayList<Card>();
  
  private List<Card> playedCards = new ArrayList<Card>();
  
  private int handSize;
  
  
  public Hand(int handSize) {
    this.handSize = handSize;
  }
  
  
  public void reset() {
    unplayedCards = playedCards;
    playedCards = new ArrayList<Card>();
  }
//  public void setCards(List<Card> cards) {
//    unplayedCards = cards;
//  }
  
  public void addCard(Card card) {
    if (unplayedCards.size() >= handSize) {
      throw new IllegalArgumentException("Hand has already been dealt");
    }
    unplayedCards.add(card);
  }
  
  public List<Card> getUnplayedCards() {
    return Collections.unmodifiableList(unplayedCards);
  }
  
  public void playCard(Card card) {
    int index = unplayedCards.indexOf(card);
    if (index < 0) {
      throw new IllegalArgumentException(card + " was already played, or is not in this hand");
    }
    unplayedCards.remove(card);
    playedCards.add(card);
  }
  
  
}
