package replit;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import replit.Ground;
import javafx.scene.paint.Color;
import replit.utility.Resource;

public class Jun {
  private static int junBaseY; 
  private static int junTopY;
  private static int junStartX;
  private static int junEndX;
  private static int initjunStartXCnt;
  private static int initjunStartXCntMax;
  
  private static int junTop;
  private static int junBottom;
  private static int topPoint;

  private static boolean topPointReached;
  private static int jumpFactor = 20;

  private static final int STAND_STILL = 1;
  private static final int JUMPING = 2;
  private static final int DEBT = 3;

  private static int state;

  private float speedX;

  static BufferedImage junhyoungImage;
  static BufferedImage debtJunhyoungImage;

  public Jun () {
    junhyoungImage = new Resource().getResourceImage("src/main/java/replit/images/jun.jpg");
    debtJunhyoungImage = new Resource().getResourceImage("src/main/java/replit/images/jun_in_debt.jpg");

    junStartX = 100;
    junBaseY = Ground.GROUND_Y + 5;
    junTopY = Ground.GROUND_Y - junhyoungImage.getHeight() + 5;

    junEndX = junStartX + junhyoungImage.getWidth();
    initjunStartXCnt = 0;
    initjunStartXCntMax = 75;
    topPoint = junTopY - 120;

    state = 1;
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
    /** If the character isn't jumping, the y-coor of the rectangle is the y-coor of the base of the character minus 27 */
    else if (state != JUMPING) {
      jun.y = junBaseY - 27;
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
}