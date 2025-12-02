package com.webber.cribbage.service;

import com.webber.cribbage.GameManager;
import com.webber.cribbage.model.Game;
import com.webber.cribbage.model.Player;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService {
    // In-memory storage for active games
    private final Map<UUID, Game> activeGames = new ConcurrentHashMap<>();

    public Game createGame(String player1Name, String player2Name) {
        Player p1 = new Player(player1Name);
        Player p2 = new Player(player2Name);

        // Randomly assign dealer
        Player dealer = Math.random() > 0.5 ? p1 : p2;

        GameManager manager = new GameManager(p1, p2, dealer);
        // Automatically start the first hand so the frontend receives dealt cards immediately
        manager.startHand();

        Game game = new Game();
        activeGames.put(game.getId(), game);
        return game;
    }

    public Game getGame(UUID gameId) {
        return activeGames.get(gameId);
    }

    public Game getGameOrThrow(UUID gameId) {
        Game game = activeGames.get(gameId);
        if (game == null) {
            throw new IllegalArgumentException("Game with ID " + gameId + " not found");
        }
        return game;
    }
}
