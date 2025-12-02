package com.webber.cribbage.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Represents a single deal/round within a Cribbage game.
 * Stores the state of cards and points specific to this round.
 */
@Getter
@Setter
public class Deal {
    private final UUID id;
    private final Player dealer;

    // N:1 relationship to Game (stored as ID for loose coupling or persistence)
    private UUID gameId;

    // The cards involved in this deal
    private Hand player1Hand;
    private Hand player2Hand;
    private Hand crib;
    private Card cutCard;

    // Points accrued specifically during this deal
    private int player1PeggingScore;
    private int player2PeggingScore;
    private int player1HandScore;
    private int player2HandScore;
    private int cribScore;

    public Deal(Player dealer) {
        this.id = UUID.randomUUID();
        this.dealer = dealer;
        this.player1PeggingScore = 0;
        this.player2PeggingScore = 0;
        this.player1HandScore = 0;
        this.player2HandScore = 0;
        this.cribScore = 0;
        this.crib = new Hand(4);
    }

    /**
     * Helper to determine total points generated in this deal
     */
    public int getTotalPointsForPlayer(boolean isPlayer1) {
        if (isPlayer1) {
            // Player 1 gets their pegging + hand points + crib (if they are dealer)
            // Assuming external logic determines who owns the crib,
            // but typically crib points belong to dealer.
            return player1PeggingScore + player1HandScore;
        } else {
            return player2PeggingScore + player2HandScore;
        }
    }
}
