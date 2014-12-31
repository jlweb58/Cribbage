package com.webber.cribbage.model.impl;

import java.util.ArrayList;
import java.util.List;

import com.webber.cribbage.model.Card;
import com.webber.cribbage.model.Hand;
import com.webber.cribbage.model.HandCounter;

/**
 * Created: 31.12.2014 14:39:35
 * 
 * @author John
 *
 */
public class HandCounterImpl implements HandCounter {

  private static final int FIFTEEN = 15;
  private static final int TWO = 2;

  @Override
  public int getHandCount(Hand hand, Card cutCard) {
    Hand countingHand = new Hand(5);
    for (Card card : hand.getUnplayedCards()) {
      countingHand.addCard(card);
    }
    countingHand.addCard(cutCard);
    return getHandCount(countingHand);
  }

  @Override
  public int getHandCount(Hand hand) {
    int total = 0;
    total += countFifteens(hand);
    return total;
  }

  private int countFifteens(Hand hand) {
    int total = 0;
    List<Card[]> twoCards = getTwoCardPermutations(hand);
    for (Card[] pair : twoCards) {
      int fifteen = pair[0].getRank().getCount() + pair[1].getRank().getCount();
      if (fifteen == FIFTEEN) {
        total += TWO;
      }
    }
    List<Card[]> threeCardSets = getThreeCardPermutations(hand);
    for (Card[] cards : threeCardSets) {
      int fifteen = cards[0].getRank().getCount() + cards[1].getRank().getCount() + cards[2].getRank().getCount();
      if (fifteen == FIFTEEN) {
        total += TWO;
      }
    }
    List<Card[]> fourCardSets = getFourCardPermutations(hand);
    for (Card[] cards : fourCardSets) {
      int fifteen = cards[0].getRank().getCount() + cards[1].getRank().getCount() + cards[2].getRank().getCount()
          + cards[3].getRank().getCount();
      if (fifteen == FIFTEEN) {
        total += TWO;
      }
    }
    //five-card fifteen (including cut card)
    if (hand.getUnplayedCards().size() == 5) {
      int fifteen = 0;
      for (Card card : hand.getUnplayedCards()) {
        fifteen += card.getRank().getCount();
      }
      if (fifteen == FIFTEEN) {
        total += TWO;
      }
    }
    return total;
  }

  public List<Card[]> getFourCardPermutations(Hand hand) {
    List<Card[]> permutations = new ArrayList<Card[]>();
    List<Card> cards = hand.getUnplayedCards();
    int firstIndex = 0;
    int secondIndex = 1;
    int thirdIndex = 2;
    int fourthIndex = 3;
    int maxIndex = cards.size() - 1;
    while (firstIndex <= (cards.size() - 4)) {
      permutations.add(new Card[]{cards.get(firstIndex), cards.get(secondIndex), cards.get(thirdIndex), cards.get(fourthIndex)});
      if (fourthIndex < maxIndex) {
        fourthIndex++;
      } else {
        if (thirdIndex < (fourthIndex - 1)) {
          thirdIndex++;
          if (fourthIndex - 1 > thirdIndex) {
            fourthIndex--;
          }
        } else {
          if (secondIndex < (thirdIndex - 1)) {
            secondIndex++;
            if (thirdIndex - 1 > secondIndex) {
              thirdIndex--;
            }
          } else {
            firstIndex++;
            secondIndex = firstIndex + 1;
            thirdIndex = secondIndex + 1;
          }
        }
      }
    }
    return permutations;
  }

  public List<Card[]> getThreeCardPermutations(Hand hand) {
    List<Card[]> permutations = new ArrayList<Card[]>();
    List<Card> cards = hand.getUnplayedCards();
    int firstIndex = 0;
    int secondIndex = 1;
    int thirdIndex = 2;
    int maxIndex = cards.size() - 1;
    while (firstIndex <= (cards.size() - 3)) {
      permutations.add(new Card[]{cards.get(firstIndex), cards.get(secondIndex), cards.get(thirdIndex)});
      if (thirdIndex < maxIndex) {
        thirdIndex++;
      } else {
        if (secondIndex < (thirdIndex - 1)) {
          secondIndex++;
          if (thirdIndex - 1 > secondIndex) {
            thirdIndex--;
          }
        } else {
          firstIndex++;
          secondIndex = firstIndex + 1;
          thirdIndex = secondIndex + 1;
        }
      }

    }

    return permutations;
  }

  public List<Card[]> getTwoCardPermutations(Hand hand) {
    List<Card[]> permutations = new ArrayList<Card[]>();
    List<Card> cards = hand.getUnplayedCards();
    int firstIndex = 0;
    int secondIndex = 1;
    int maxIndex = cards.size() - 1;
    while (firstIndex <= (cards.size() - 2)) {
      permutations.add(new Card[]{cards.get(firstIndex), cards.get(secondIndex)});
      if (secondIndex < maxIndex) {
        secondIndex++;
      } else {
        firstIndex++;
        secondIndex = firstIndex + 1;
      }
    }
    return permutations;
  }

}
