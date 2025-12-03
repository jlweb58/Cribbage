package com.webber.cribbage.controller;

import com.webber.cribbage.model.Game;
import com.webber.cribbage.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/games")
// Allow requests from your frontend (adjust origin as needed)
@CrossOrigin(origins = "http://localhost:4200")
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    public ResponseEntity<Game> createGame(@RequestParam(defaultValue = "Player 1") String player1, 
                                           @RequestParam(defaultValue = "Player 2") String player2) {
        Game game = gameService.createGame(player1, player2);
        return ResponseEntity.ok(game);
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<Game> getGame(@PathVariable UUID gameId) {
        Game game = gameService.getGame(gameId);
        if (game == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(game);
    }

    @GetMapping("/")
    public ResponseEntity<Game> startGame() {
        Game game = gameService.createGame("Human", "Computer");
        return ResponseEntity.ok(game);
    }


}
