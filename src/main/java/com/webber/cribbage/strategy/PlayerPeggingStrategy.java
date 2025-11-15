package com.webber.cribbage.strategy;

import com.webber.cribbage.model.Card;
import com.webber.cribbage.pegging.PeggingRound;

import java.util.List;

/**
 * Strategy interface for player decisions during pegging
 */
public interface PlayerPeggingStrategy {

    /**
     * Choose which card to play during pegging
     * @param availableCards Cards that can legally be played
     * @param peggingRound Current state of the pegging round
     * @return The card to play, or null to declare "Go"
     */
    Card chooseCardToPlay(List<Card> availableCards, PeggingRound peggingRound);
}
