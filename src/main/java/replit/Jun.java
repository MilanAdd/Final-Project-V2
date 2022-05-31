package replit;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import replit.Ground;
import javafx.scene.paint.Color;
import replit.utility.Resource;

public class Jun {
  private static int junBaseY; // base of the character
  private static int junTopY; // y-coordinate of top of character
  private static int junStartX; // starting x-coordinate of character
  private static int junEndX; // ending x-coordinate of character
  private static int initjunStartXCnt; // counter of starting x-coordinate of character
  private static int initjunStartXCntMax; // max value of counter of starting x-coordinate of character
  
  private static int junBottom; // bottom of the character
  private static int junTop; // top of character
  private static int topPoint; // top point of character

  private static boolean topPointReached; // boolean that states if top point is reached
  private static int jumpFactor = 20; // jump factor of character, is set to 20

  private static final int STAND_STILL = 1; // value of stand still is 1, use as one of the cases
  private static final int JUMPING = 2; // value of jumping is 2, use as one of the cases
  private static final int DEBT = 3; // value of debt is 3, use as one of the cases

  private static int state; // state of character, changes depending of what type of case character is at

  static BufferedImage junhyoungImage; // image of character
  static BufferedImage debtJunhyoungImage; // image of character when they're in debt

  public Jun () {
    junhyoungImage = new Resource().getResourceImage("src/main/java/replit/images/jundan_transparent.png"); // character image set to specified file path
    debtJunhyoungImage = new Resource().getResourceImage("src/main/java/replit/images/jundan_in_debt.png"); // debt character image set to specified file path

    junStartX = 100; // initial x-coordinate for character is set to 100
    junBaseY = Ground.GROUND_Y + 5; // y-coordinate of base of character is set to y-coordinate of ground plus 5
    junTopY = Ground.GROUND_Y - junhyoungImage.getHeight() + 5; // y-coordinate of top of character is set to y-coordinate of ground minus the height of the character plus 5

    junEndX = junStartX + junhyoungImage.getWidth(); // end x-coordinate of character is starting x-coordinate plus width of character image
    initjunStartXCnt = 0; // initial counter value for x-coordinate of character is 0
    initjunStartXCntMax = 75; // max counter value for x-coordinate of character is 75
    topPoint = junTopY - 120; // top point of character is y-coordinate of character minus 120

    state = 1; // state value is initially set to 1
  }

  /** Returns starting position of character, used in Panel class */
  public int getPos () {
    return junStartX;
  }
  
  /** Generates random number between inputted min, max; used in checkjunStartXCnt() method */
  public int getRandomNumber (int min, int max) {
    return (int) ((Math.random() * (max - min)) + min);
  }


  /** Counters junStartXCnt incrementally starting from 0
   * When that value equals the max value of the counter (75), then the starting x-pos of the character changes to a random number between 100 and 600 
   * Resets counter, repeats process, changes x-pos of character randomly again
   */
  private void checkjunStartXCnt () {
    initjunStartXCnt++;
    if (initjunStartXCnt == initjunStartXCntMax) {
      junStartX = getRandomNumber(100, 600);
      initjunStartXCnt = 0;
    }
  }

  /** Draws character
   * Changes boolean statements, y-positions are altered based on state of character 
   */
  public void create (Graphics g) {
    junBottom = junTop + junhyoungImage.getHeight();
      checkjunStartXCnt(); // randomly changes position of character

      /** Switch statement based on state of character */
      switch (state) {

        /** When character is not jumping, the character is being created on alternating x-pos, default y-pos */
        case STAND_STILL:
          System.out.println("stand");
          g.drawImage(junhyoungImage, junStartX, junTopY, null);
        
        /** When character is jumping, character is being drawn on different y-pos 
         * Differs based on pos while jumping 
         */
        case JUMPING:
          /** Y-pos becomes top of character being substracted by the jump factor (20) when the top of the character is greater than the top point and the top point isn't reached yet */
          if (junTop > topPoint && !topPointReached) {
            g.drawImage(junhyoungImage, junStartX, junTop -= jumpFactor, null);
            break;
          }
          /** When top of character is greater than or equal to top point and top point isn't reached, the state switches to when character is still and the top point reached boolean becomes false */
          if (junTop >= topPoint && !topPointReached) {
            state = STAND_STILL;
            topPointReached = false;
            break;
          }

          /** When top of character is greater than top point and top point is reached, the y-pos of character being drawn becomes top of character being incrementally added by jump factor (20) */
          if (junTop > topPoint && topPointReached) {
            /** When y-coor of top of character is equal to top of character and top point is reached, state switches to when character is still, top point reached boolean becomes false */
            if (junTopY == junTop && topPointReached) {
              state = STAND_STILL;
              topPointReached = false;
              break;
            }
            g.drawImage(junhyoungImage, junStartX, junTop += jumpFactor, null);
            break;
          }

        /** When character goes into debt b/c it collides w/ obstacle, new image of character is drawn, y-pos becomes top of character */
        case DEBT:
          g.drawImage(debtJunhyoungImage, junStartX, junTop, null);
          break;
        
      }
  }

  /** Sets state to debt */
  public void debt () {
    state = DEBT;
  }

  /** Creates rectangle surrounding character, used to detect if it collides w/ obstacle */
  public static Rectangle getJun () {
    Rectangle jun = new Rectangle(); // New rectangle is being created
    jun.x = junStartX; // X-coor of character becomes starting x-pos

    /** If the character is jumping and the top point isn't reached, the y-coor of the rectangle is the top of the character minus the jump factor */
    if (state == JUMPING && !topPointReached) {
      jun.y = junTop - jumpFactor;
    }
    /** If the character is jumping and the top point is reached, the y-coor of the rectangle is the top of the character plus the jump factor */
    else if (state == JUMPING && topPointReached) {
      jun.y = junTop + jumpFactor;
    }
    /** If the character isn't jumping, the y-coor of the rectangle is the y-coor of the base of the character minus 41 */
    else if (state != JUMPING) {
      jun.y = junBaseY - 41;
    }

    jun.width = junhyoungImage.getWidth(); // rect's width is the width of the image
    jun.height = junhyoungImage.getHeight(); // rect's height is the height of the image

    return jun;
  }

  /** Makes character/game start running
   * Top of character is set equal to y-value of character's top
   * Sets character's state to still
   */
  public void startRunning () {
    junTop = junTopY;
    state = STAND_STILL;
  }

  /** Makes character start jumping
   * Top of character is set equal to y-value of character's top
   * Top point isn't reached, done by stating boolean condition as false
   * Sets character's state to jumping
   */
  public void jump () {
    junTop = junTopY;
    topPointReached = false;
    state = JUMPING;
  }

  /** If character is jumping, then boolean returns true, false otherwise
   * Used in panel class so that if character is jumping, audio can be played
   */
  public boolean isJump() {
    if (state == JUMPING && (junTopY == junTop && !topPointReached)) {
      return true;
    }
    return false;
  }
}