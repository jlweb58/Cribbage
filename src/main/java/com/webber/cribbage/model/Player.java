package com.webber.cribbage.model;

import java.util.Objects;

public class Player {
    private final String name;

    private Hand dealtHand;

    private Hand crib;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Hand getDealtHand() {
        return dealtHand;
    }

    public void setDealtHand(Hand dealtHand) {
        this.dealtHand = dealtHand;
    }

    public Hand getCrib() {
        return crib;
    }

    public void setCrib(Hand crib) {
        this.crib = crib;
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
