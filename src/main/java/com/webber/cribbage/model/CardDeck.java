package com.webber.cribbage.model;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class CardDeck {

    private final List<Card> deck = new ArrayList<>();
    private final Random random;

    public CardDeck() {
        this (new SecureRandom ());
    }

    // Useful for tests
    CardDeck(long seed) {
        this(new java.util.Random(seed));
    }

    CardDeck(Random random) {
        this.random = random;
        fillDeck();
        shuffle();
    }

    public Card dealCard() {
        if (deck.isEmpty()) {
            throw new IllegalStateException("No cards in this deck");
        }
        return deck.removeFirst();
    }

    public Card cutCard() {
        if (deck.isEmpty()) {
            throw new IllegalStateException("No cards in this deck");
        }
        int index = random.nextInt(deck.size());
        return deck.remove(index);
    }

    private void fillDeck() {
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                deck.add(new Card(suit, rank));
            }
        }
    }

    private void shuffle() {
        Collections.shuffle(deck, random);
    }

    public int size() {
        return deck.size();
    }


}
