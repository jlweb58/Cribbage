package com.webber.cribbage.strategy;

import com.webber.cribbage.model.Card;
import com.webber.cribbage.pegging.PeggingRound;

import java.util.List;

/**
 * Human player strategy - card selection is provided externally
 * This is just a placeholder; actual card selection comes from UI/API
 */
public class HumanPlayerStrategy implements PlayerStrategy {

    private Card selectedCard;

    @Override
    public Card chooseCardToPlay(List<Card> availableCards, PeggingRound peggingRound) {
        // This will be called by the game controller
        // The actual selection is set via setSelectedCard() from UI
        return selectedCard;
    }

    public void setSelectedCard(Card card) {
        this.selectedCard = card;
    }
}
