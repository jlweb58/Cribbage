package com.webber.cribbage.model;

import com.webber.cribbage.GameManager;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Game {
    private final UUID id;
    private final GameManager gameManager;

    public Game(GameManager gameManager) {
        this.id = UUID.randomUUID();
        this.gameManager = gameManager;
    }

    public UUID getId() {
        return id;
    }

    public GameManager getGameManager() {
        return gameManager;
    }
}
