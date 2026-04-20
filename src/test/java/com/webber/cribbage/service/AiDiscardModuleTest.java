package com.webber.cribbage.service;

import com.webber.cribbage.model.Card;
import com.webber.cribbage.model.Hand;
import static com.webber.cribbage.model.Rank.*;

import static com.webber.cribbage.model.Suit.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.List;

public class AiDiscardModuleTest {

    private final AiDiscardModule aiDiscardModule = new AiDiscardModule();


    @Test
    public void testChooseDiscardHand1() {
        Hand hand = new Hand(6);
        hand.addCard(new Card(SPADES, KING));
        hand.addCard(new Card(HEARTS, KING));
        hand.addCard(new Card(CLUBS, SEVEN));
        hand.addCard(new Card(DIAMONDS, EIGHT));
        hand.addCard(new Card(SPADES, FIVE));
        hand.addCard(new Card(HEARTS, SIX));

        List<Card> discard = aiDiscardModule.chooseDiscard(hand);

        assertEquals(2, discard.size());
        assertTrue(discard.contains(new Card(SPADES, KING)));
        assertTrue(discard.contains(new Card(HEARTS, KING)));
    }

    @Test
    public void testChooseDiscardHand2() {
        Hand hand = new Hand(6);
        hand.addCard(new Card(SPADES, FIVE));
        hand.addCard(new Card(HEARTS, SEVEN));
        hand.addCard(new Card(CLUBS, EIGHT));
        hand.addCard(new Card(DIAMONDS, NINE));
        hand.addCard(new Card(SPADES, TEN));
        hand.addCard(new Card(HEARTS, JACK));

        List<Card> discard = aiDiscardModule.chooseDiscard(hand);

        assertEquals(2, discard.size());
        assertTrue(discard.contains(new Card(CLUBS, EIGHT)));
        assertTrue(discard.contains(new Card(HEARTS, SEVEN)));

    }





}
