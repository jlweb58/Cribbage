package com.webber.cribbage.model;

import lombok.Getter;

@Getter
public class PlayedCard {

    private final Card card;

    private final Player player;

    private final int peggingPoints;


    public PlayedCard(Card card, Player player, int peggingPoints) {
        this.card = card;
        this.player = player;
        this.peggingPoints = peggingPoints;
    }
}
