package com.webber.cribbage.strategy;

import com.webber.cribbage.model.Card;
import com.webber.cribbage.pegging.PeggingRound;

import java.util.List;

/**
 * Simple AI strategy for pegging - plays lowest card that won't set up opponent
 */
public class SimpleAIStrategy implements PlayerStrategy {

    @Override
    public Card chooseCardToPlay(List<Card> availableCards, PeggingRound peggingRound) {
        if (availableCards.isEmpty()) {
            return null;
        }

        int currentCount = peggingRound.getCurrentCount();

        // Try to make 15 or 31
        for (Card card : availableCards) {
            int newCount = currentCount + card.getRank().getCount();
            if (newCount == 15 || newCount == 31) {
                return card;
            }
        }

        // Avoid leaving counts that are easy to score on (5, 10, 21)
        Card safestCard = null;
        int safestScore = -1;

        for (Card card : availableCards) {
            int newCount = currentCount + card.getRank().getCount();
            int dangerScore = calculateDangerScore(newCount);

            if (safestCard == null || dangerScore < safestScore) {
                safestCard = card;
                safestScore = dangerScore;
            }
        }

        return safestCard != null ? safestCard : availableCards.get(0);
    }

    private int calculateDangerScore(int count) {
        // Higher scores are more dangerous (easier for opponent to score)
        if (count == 5 || count == 21) return 3;  // Easy to make 15/31
        if (count == 10 || count == 20) return 2;  // Easy to make pairs with face cards
        return 0;
    }
}
