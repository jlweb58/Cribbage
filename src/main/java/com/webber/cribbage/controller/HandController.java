package com.webber.cribbage.controller;

import com.webber.cribbage.model.Card;
import com.webber.cribbage.model.CardDeck;
import com.webber.cribbage.model.Hand;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Transactional
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/hands")
public class HandController {

    @RequestMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

@GetMapping(path = "/")
public ResponseEntity<Hand> getHand() {
        CardDeck cardDeck = new CardDeck();
        Hand hand = new Hand(6);
        for (int i = 0; i < 6; i++) {
            hand.addCard(cardDeck.dealCard());
        }
        return ResponseEntity.ok(hand);
    }

}
