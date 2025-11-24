package com.webber.cribbage;

import com.webber.cribbage.model.Card;
import com.webber.cribbage.model.Hand;
import com.webber.cribbage.model.Player;
import com.webber.cribbage.pegging.PeggingRound;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleGameRunner {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Player human = new Player("You");
        Player computer = new Player("Computer");

        // Let human be non-dealer for first hand
        GameManager controller = new GameManager(human, computer, computer);

        System.out.println("Starting a single test hand of Cribbage...\n");

        controller.startHand();

        // Show human hand
        Hand humanHand = human.getDealtHand();
        Hand computerHand = computer.getDealtHand();

        System.out.println("Your dealt hand:");
        printNumberedCards(humanHand.getUnplayedCards());

        System.out.println("Computer's hand:");
        printNumberedCards(computerHand.getUnplayedCards());


        // Human chooses two cards to discard to crib
        List<Card> humanDiscards = readCardsToDiscard(scanner, humanHand);
        controller.discardToCrib(human, humanDiscards);

        // Very simple AI: discard the first two cards
        List<Card> computerDiscards = new ArrayList<>();
        List<Card> compCards = new ArrayList<>(computerHand.getUnplayedCards());
        computerDiscards.add(compCards.get(0));
        computerDiscards.add(compCards.get(1));
        controller.discardToCrib(computer, computerDiscards);

        controller.cutDeck();
        System.out.println("\nCut card: " + controller.getCutCard());

        // Start pegging
        PeggingRound peggingRound = controller.startPegging();
        System.out.println("\nStarting pegging. Non-dealer plays first: " +
                peggingRound.getCurrentPlayer().getName());

        // Pegging loop
        while (!peggingRound.isComplete()) {
            Player current = peggingRound.getCurrentPlayer();

            if (current.equals(human)) {
                handleHumanPeg(scanner, peggingRound);
            } else {
                handleComputerPeg(peggingRound);
            }
        }

        System.out.println("\nPegging complete.");
        System.out.println("Pegging scores:");
        System.out.println("You: " + peggingRound.getScore(human));
        System.out.println("Computer: " + peggingRound.getScore(computer));

        HandCounter handCounter = new HandCounter();
        int computerCount = handCounter.getHandCount(controller.getPlayer2().getDealtHand(), controller.getCutCard());
        int humanCount = handCounter.getHandCount(controller.getPlayer1().getDealtHand(), controller.getCutCard());

        System.out.println("\nComputer's hand count: " + computerCount);
        System.out.println("\nHuman's hand count: " + humanCount);
        System.out.println("\nComputer total points: " + (peggingRound.getScore(computer) + computerCount));
        System.out.println("\nHuman total points: " + (peggingRound.getScore(human) + humanCount));
    }

    private static void printNumberedCards(List<Card> cards) {
        int i = 1;
        for (Card card : cards) {
            System.out.printf("  %d) - %s%n", i++, card);
        }
    }

    private static List<Card> readCardsToDiscard(Scanner scanner, Hand hand) {
        List<Card> cards = new ArrayList<>(hand.getUnplayedCards());
        List<Card> discards = new ArrayList<>();

        while (discards.size() < 2) {
            System.out.print("Enter card number to discard to crib (" +
                    (discards.size() + 1) + "/2): ");
            String line = scanner.nextLine().trim();
            int index;
            try {
                index = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number between 1 and " + cards.size());
                continue;
            }
            if (index < 1 || index > cards.size()) {
                System.out.println("Invalid choice. Try again.");
                continue;
            }
            Card chosen = cards.get(index - 1);
            if (discards.contains(chosen)) {
                System.out.println("You already chose that card. Pick another.");
                continue;
            }
            discards.add(chosen);
            System.out.println("Selected: " + chosen);
        }

        return discards;
    }

    private static void handleHumanPeg(Scanner scanner, PeggingRound peggingRound) {
        List<Card> playable = peggingRound.getPlayableCards();

        if (playable.isEmpty()) {
            System.out.println("\nYour turn, but you cannot play. Enter 'g' for Go.");
            while (true) {
                System.out.print("> ");
                String input = scanner.nextLine().trim().toLowerCase();
                if ("g".equals(input)) {
                    PeggingRound.PeggingResult result = peggingRound.declareGo();
                    System.out.println("You said Go. Count remains " + result.getNewCount());
                    System.out.println("\nComputer points on go: " + result.getPoints());
                    break;
                } else {
                    System.out.println("Type 'g' to declare Go.");
                }
            }
            return;
        }

        System.out.println("\nYour turn. Current count: " + peggingRound.getCurrentCount());
        System.out.println("Playable cards:");
        printNumberedCards(playable);
        System.out.println("Enter card number to play, or 'g' for Go (if no legal play):");

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim().toLowerCase();
            if ("g".equals(input)) {
                if (!playable.isEmpty()) {
                    System.out.println("You have playable cards; you cannot say Go.");
                    continue;
                }
                PeggingRound.PeggingResult result = peggingRound.declareGo();
                System.out.println("You said Go. Count remains " + result.getNewCount());
                System.out.println("\nComputer points on go: " + result.getPoints());
                break;
            }
            int index;
            try {
                index = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number or 'g'.");
                continue;
            }
            if (index < 1 || index > playable.size()) {
                System.out.println("Invalid choice. Try again.");
                continue;
            }
            Card chosen = playable.get(index - 1);
            PeggingRound.PeggingResult result = peggingRound.playCard(chosen);
            System.out.println("You played: " + chosen +
                    "  | Count: " + result.getNewCount() +
                    "  | Points scored: " + result.getPoints());
            break;
        }
    }

    private static void handleComputerPeg(PeggingRound peggingRound) {
        List<Card> playable = peggingRound.getPlayableCards();
        Player current = peggingRound.getCurrentPlayer();

        if (playable.isEmpty()) {
            PeggingRound.PeggingResult result = peggingRound.declareGo();
            System.out.println("\nComputer (" + current.getName() + ") says Go. " +
                    "Count remains " + result.getNewCount());
            System.out.println("\nPlayer points on go: " + result.getPoints());
            return;
        }

        // Very simple AI: play the first available card
        Card chosen = playable.getFirst();
        PeggingRound.PeggingResult result = peggingRound.playCard(chosen);
        System.out.println("\nComputer played: " + chosen +
                "  | Count: " + result.getNewCount() +
                "  | Points scored: " + result.getPoints());
    }
}
