package replit;

import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.Frame;
import java.awt.Color;

class Main {
    // Creates window with title "Jun Failed Crypto Investments" and size 1200 x 800
    JFrame window = new JFrame("Jun's Failed Crypto Investments");

    public static int WIDTH = 1200;
    public static int HEIGHT = 800;

    /** Creates container (component that holds gathering of components) that's that content panel of the JFrame window
      * Panel is created with keyboard inputs, power to get focused, centered border layout, a set size, and ability to become resizable and visible
      */
    public void createAndShowGUI () {
      window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      Container container = window.getContentPane();

      Panel gamePanel = new Panel();
      gamePanel.addKeyListener(gamePanel);
      gamePanel.setFocusable(true);

      container.setLayout(new BorderLayout());

      container.add(gamePanel, BorderLayout.CENTER);

      window.setSize(WIDTH, HEIGHT);
      window.setResizable(false);
      window.setVisible(true);
    }
  /** Where the GUI of the game is actually executed and shown */
  public static void main (String[] args) {
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run () {
        new Main().createAndShowGUI();
      }
    });
  }
}