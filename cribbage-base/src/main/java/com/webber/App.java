package com.webber;

import static com.webber.cribbage.model.Suit.*;
import static com.webber.cribbage.model.Rank.*;

import com.webber.cribbage.model.Card;
import com.webber.cribbage.model.CardDeck;

public class App {

  public static void main(String[] args) {
    CardDeck cardDeck = new CardDeck();
    cardDeck.shuffle();
    for (Card card : cardDeck.getDeck()) {
      System.out.print(card + ", ");
    }
  
  }
}
