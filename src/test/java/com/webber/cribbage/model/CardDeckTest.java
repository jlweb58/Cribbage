package com.webber.cribbage.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CardDeckTest {

    private CardDeck cardDeck;

    @BeforeEach
    public void setUp() {
        cardDeck = new CardDeck();
    }

    @Test
    public void itShouldHave52CardsWhenNew() {
        assertEquals(52, cardDeck.size());
    }

    @Test
    public void deckShouldReduceInSizeAfterDealingCard() {
        cardDeck.dealCard();
        assertEquals(51, cardDeck.size());
    }

    @Test
    public void cutCardShouldReturnACard() {
        Card card = cardDeck.cutCard();
        assertNotNull(card, "cut card was null");
    }

    @Test
    public void cutShouldNeverReturnsDealtCards() {
        CardDeck deck = new CardDeck();

        // deal some cards
        Set<Card> dealt = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            dealt.add(deck.dealCard());
        }

        // Now repeatedly cut until deck empty — each cut removes one card.
        while (deck.size() > 0) {
            Card cut = deck.cutCard();
            assertFalse(dealt.contains(cut), "Cut card was already dealt: " + cut);
        }
    }

    @Test
    void cutCardIsNeverAlreadyDealt() {
        CardDeck deck = new CardDeck(12345L);

        // Deal 10 cards
        Set<Card> dealt = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            dealt.add(deck.dealCard());
        }

        Card cut = deck.cutCard();

        assertFalse(dealt.contains(cut));
    }
}
