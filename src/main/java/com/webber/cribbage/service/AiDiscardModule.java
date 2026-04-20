package com.webber.cribbage.service;

import com.webber.cribbage.HandCounter;
import com.webber.cribbage.model.Card;
import com.webber.cribbage.model.CardDeck;
import com.webber.cribbage.model.Hand;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AiDiscardModule {

    private final HandCounter handCounter = new HandCounter();

    public List<Card> chooseDiscard(Hand hand) {
        List<Card> handCards = hand.getUnplayedCards();
        if (handCards.size() != 6) {
            throw new IllegalArgumentException("Discard selection requires a 6-card hand");
        }

        List<Card> bestDiscard = new ArrayList<>(2);
        double bestScore = Integer.MIN_VALUE;

        for (int first = 0; first < handCards.size() - 1; first++) {
            for (int second = first + 1; second < handCards.size(); second++) {
                List<Card> discard = List.of(handCards.get(first), handCards.get(second));
                List<Card> keptHand = getKeptCards(handCards, first, second);

                double score = evaluateHandCombination(keptHand, discard);

                if (score > bestScore) {
                    bestScore = score;
                    bestDiscard = new ArrayList<>(discard);
                }
            }
        }

        return bestDiscard;
    }

    private List<Card> getKeptCards(List<Card> handCards, int discardIndexA, int discardIndexB) {
        List<Card> keptCards = new ArrayList<>(4);
        for (int i = 0; i < handCards.size(); i++) {
            if (i != discardIndexA && i != discardIndexB) {
                keptCards.add(handCards.get(i));
            }
        }
        return keptCards;
    }

    double evaluateHandCombination(List<Card> hand, List<Card> cardsToCrib) {
        if (hand.size() != 4) {
            throw new IllegalArgumentException("Invalid hand size");
        }
        log.info("Starting hand evaluation for hand: " + hand);
        double total = 0.0;
        CardDeck deck = new CardDeck();
        hand.forEach(deck::removeCard);
        cardsToCrib.forEach(deck::removeCard);
        int remainingCuts = deck.size();

        while (deck.size() > 0) {
            Card cut = deck.cutCard();
            Hand handToTest = new Hand(5);
            hand.forEach(handToTest::addCard);
            handToTest.addCard(cut);
            total += handCounter.getHandCount(handToTest);
            log.info("Evaluated hand with cut card: " + cut + ", total points: " + total);
        }

        return total / remainingCuts;
    }


}
