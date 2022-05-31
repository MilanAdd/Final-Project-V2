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
  
  public static int WIDTH; // Panel width
  public static int HEIGHT; // Panel height
  private Thread animator; // thread of execution of game
  
  private boolean running = false; // boolean that determines if game is running
  private boolean gameOver = false; // boolean that determines if game is over
  
  Ground ground; // ground object
  Jun jun; // character object
  EthereumCoinObstacles obst; // obstacle object
  BufferedImage nycImg; // image object

  private int score; // score that is displayed on screen
  private String gameDone; // game over message
  private String startOverMessage; // message that pops up after the game is over, states instructions
  
  public Panel () {
    WIDTH = Main.WIDTH; // sets width as GUI/panel width
    HEIGHT = Main.HEIGHT; // sets height as GUI/panel height
    
    ground = new Ground(HEIGHT); // sets ground object as new ground object with height of panel
    jun = new Jun(); // sets character object as new character object
    obst = new EthereumCoinObstacles((int)(WIDTH * 1.5)); // sets obstacle object as new obstacle object with position of width of panel times 1.5
    nycImg = new Resource().getResourceImage("src/main/java/replit/images/wall_street(1).jpg"); // sets image object as resource object that gets image from specified file path

    score = 0; // score is initially set as 0
    gameDone = "Jun's in debt :("; // sets gameDone as specific game over message
    startOverMessage = "Press the spacebar to start a new game"; // set as instructions when starting a new game after colliding with an obstacle
    
    setSize(WIDTH, HEIGHT); // sets panel size as window width and height
    setVisible(true); // frame becomes visible on screen
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

    /** If the character is jumping, a sound effect is played */
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
