package com.webber.cribbage.model;

import com.webber.cribbage.GameManager;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Game {
    private final UUID id;

    public Game() {
        this.id = UUID.randomUUID();
    }

}
