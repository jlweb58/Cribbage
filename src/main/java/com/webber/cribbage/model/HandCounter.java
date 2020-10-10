package com.webber.cribbage.model;

/**
 * Created: 31.12.2014 14:33:36
 * 
 * @author John
 *
 */
public interface HandCounter {

  /**
   * Gets the total count for a particular hand including the cut card
   * @param hand      The hand (which must have four cards)
   * @param cutCard   The cut card
   * @return   The point count for that hand and cut card
   */
  int getHandCount(Hand hand, Card cutCard);
  
  /**
   * Gets the total count for a particular hand without the cut card
   * @param hand      The hand (which must have four cards)
   * @return    The point count for that hand
   */
  int getHandCount(Hand hand);
}
