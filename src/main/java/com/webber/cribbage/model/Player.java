package com.webber.cribbage.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
public class Player {
    private final String name;

    @Setter
    private Hand dealtHand;

    public Player(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
