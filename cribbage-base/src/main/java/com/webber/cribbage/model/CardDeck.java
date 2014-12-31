package com.webber.cribbage.model;

import java.security.SecureRandom;


/**
 * Created: 31.12.2014 14:45:42
 * 
 * @author John
 *
 */
public class CardDeck {
  
  private Card[] deck;
  
  private static final int DECK_SIZE = 52;
  
  public CardDeck() {
    deck = new Card[DECK_SIZE];
    fillDeck();
  }
  
  private void fillDeck() {
    int i = 0;
    for (Suit suit : Suit.values()) {
      for (Rank rank : Rank.values()) {
        Card card = new Card(suit, rank);
        deck[i++] = card;
      }
    }
  }
  
  public void shuffle() {
    SecureRandom random = new SecureRandom();
    for (int i = deck.length - 1; i >= 1; i--) {
      int j = random.nextInt(i + 1);
      Card tmp = deck[j];
      deck[j] = deck[i];
      deck[i] = tmp;
    }
    
    
  }
  
  public Card[] getDeck() {
    return deck;
  }
  
}
