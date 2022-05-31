package replit;

import java.io.*;
import javax.imageio.*;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import replit.Ground;
import replit.Jun;
import replit.EthereumCoinObstacles;
import replit.utility.Resource;
import replit.utility.AudioResource;

class Panel extends JPanel implements KeyListener, Runnable {
  
  public static int WIDTH;
  public static int HEIGHT;
  private Thread animator;
  
  private boolean running = false;
  private boolean gameOver = false;
  
  Ground ground;
  Jun jun;
  EthereumCoinObstacles obst;
  BufferedImage nycImg;

  private int score;
  private String gameDone;
  private String startOverMessage;
  
  public Panel () {
    WIDTH = Main.WIDTH;
    HEIGHT = Main.HEIGHT;
    
    ground = new Ground(HEIGHT);
    jun = new Jun();
    obst = new EthereumCoinObstacles((int)(WIDTH * 1.5));
    nycImg = new Resource().getResourceImage("src/main/java/replit/images/wall_street(1).jpg");

    score = 0;
    gameDone = "Jun's in debt :(";
    startOverMessage = "Press the spacebar to start a new game";
    
    setSize(WIDTH, HEIGHT);
    setVisible(true);
  }
  
  public void paint (Graphics g) {
    super.paint(g); // inherits paint method in Component class so that methods (e.g. repaint) can be used
    g.drawImage(nycImg, 0, 0, this); // Draws Wall Street as background image

    /** States game over message: "Jun's in debt" plus instructions on how to start another game */
    if (gameOver) {
      g.setFont(new Font("Gore Regular", Font.PLAIN, 75));
      g.setColor(Color.BLACK);
      g.drawString(gameDone, getWidth()/2 - 330, 250);
      g.setFont(new Font("Gore Regular", Font.PLAIN, 37));
      g.setColor(Color.BLACK);
      g.drawString(startOverMessage, getWidth()/2 - 500, 350);
    }
    
    /** Displays score, which is amount of money collected */
    g.setFont(new Font("Gore Regular", Font.PLAIN, 75));
    g.setColor(Color.BLACK);
    g.drawString("$", getWidth()/2 - 123, 100);
    g.drawString(Integer.toString(score), getWidth()/2 - 63, 100);
    
    /** Creates a text when game is first being started, displays an introductory message plus instructions on how to start the game */
    if (jun.getPos() == 100 && score == 0) {
      g.setFont(new Font("Gore Regular", Font.PLAIN, 32));
      g.setColor(Color.BLACK);
      g.drawString("Press the spacebar to start a journey on Wall Street", getWidth()/2 - 535, 250);
    }
    
    /** Ground, character, and obstacle objects are being created, displayed */
    ground.create(g); 
    jun.create(g);
    obst.create(g);
  }
  
  /** Void method while game is being run */
  public void run () {
    running = true;

    /** When game is true (or when condition running is true), the game updates using updateGame() method, repaints panel, handles potential InterruptedException errors */
    while (running) {
      updateGame();
      repaint();      
      try {
        Thread.sleep(50);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
  
  /** Updates game while it's being run */
  public void updateGame () {
    score += 100;

    /** Ground and obstacle objects are being updated */
    ground.update();
    obst.update();

    if(jun.isJump()) {
      new Thread(new AudioResource("src/main/java/replit/audio/jump.wav")).start();
    }

    /** If a character collides with an obstacle (checked with hasHitObst() boolean method), the character goes into debt state, the game resets, and the panel repaints */
    if (obst.hasHitObst()) {
      jun.debt();
      new Thread(new AudioResource("src/main/java/replit/audio/oh_no...anyway.wav")).start();
      reset();
      repaint();
      running = false;
      gameOver = true;
      System.out.println("You hit the obstacle!");
    }
  }

  /** Resets score to 0, obstacles are being put to position based on position values set for obstacle in resume method, obstacles are added */
  public void reset () {
    score = 0;
    System.out.println("reset");
    obst.resume();
    gameOver = false;
  }
  
  /** Several if conditions when spacebar is being pressed (i.e. when the game ends due to a collision, when a new game starts, and when the character jumps due to spacebar being pressed) */
  public void keyTyped (KeyEvent e) {
    if (e.getKeyChar() == ' ') {    
      /** Resets all commands to when game is at state before being started */
      if (gameOver) {
        reset();
      } 

      /** When the spacebar is first being clicked after new game, new thread starts, meaning game starts and character can be used to jump */
      if (animator == null || !running) {
        System.out.println("Game has started");        
        animator = new Thread(this);
        animator.start();     
        jun.startRunning();   
      }
      else {
        jun.jump(); // Calls jump method from Jun class, sets jun object values to what they should be when it's being used to jump
      }
    }
  }
  
  public void keyPressed (KeyEvent e) {
    // System.out.println("keyPressed: "+e);
  }
  
  public void keyReleased (KeyEvent e) {
    // System.out.println("keyReleased: "+e);
  }
}
