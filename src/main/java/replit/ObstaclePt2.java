/**
package replit;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import replit.utility.Resource;

public class ObstaclePt2 {

  public final BufferedImage blueEthereumCoin;
  public final BufferedImage redEthereumCoin;
  public final BufferedImage blueEthDefault;
  public final Random rand;

  public final List<Obstacle> obstacles;
  public final Jun jun;

  public ObstaclePt2(Jun jun) {
    rand = new Random();
    blueEthereumCoin = new Resource().getResourceImage("src/main/java/replit/images/coin1.png");
    redEthereumCoin = new Resource().getResourceImage("src/main/java/replit/images/coin2.png");
    blueEthDefault = new Resource().getResourceImage("src/main/java/replit/images/blue_ethereum_transparent(1).png");
    obstacles = new ArrayList<Obstacle>();
    this.jun = jun;
    obstacles.add(obstaclesCreator());
  }

  public void updateObstLoc() {
    for(Obstacle obst : obstacles) {
      obst.updateObstaclesLoc();
    }
    Obstacle obstac = obstacles.get(0);
    if(obstac.offScreen()) {
      obstacles.clear();
      obstacles.add(obstaclesCreator());
    }
  }

  public void draw(Graphics g) {
    for(Obstacle obst : obstacles) {
      obst.drawLocation(g);
    }
  }

  private Obstacle obstaclesCreator() {
    Eth obst = new Eth(jun, 1000, blueEthDefault.getWidth() - 10, blueEthDefault.getHeight() - 10, blueEthDefault);
    int type = rand.nextInt(2);
    switch(type) {
      case 0:
        return new Eth(jun, 1000, blueEthereumCoin.getWidth() - 10, blueEthereumCoin.getHeight() - 10, blueEthereumCoin);
      case 1:
        return new Eth(jun, 1000, redEthereumCoin.getWidth() - 10, redEthereumCoin.getHeight() - 10, redEthereumCoin);
      default:
        return obst;
    }
  }

  public boolean isCollided() {
    for(Obstacle obst : obstacles) {
      if(jun.getJun().intersects(obst.getHitBox())) {
        return true;
      }
    }
    return false;
  }

  public void resume() {
    obstacles.clear();
    obstacles.add(obstaclesCreator());
  }
}
*/

/**
package replit;

import replit.Panel;

import static replit.Ground.GROUND_Y;
import static replit.Panel.WIDTH;
import replit.utility.Resource;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import replit.ObstacleManager;

public class ObstaclePt2 {
  private class Obstacle {
    private BufferedImage img;
    private double x;
    private int y;

    private Obstacle(BufferedImage img, double x, int y) {
      this.img = img;
      this.x = x;
      this.y = y;
    }
  }

  private static final double HITBOX_X = 33;
  private static final int HITBOX_Y = 33;
  private static final int ETH_AMOUNT = 2;
  private static final int MAX_ETH_GROUP = 1;

  private ObstacleManager obstMngr;
  private Ground gr;
  private Panel gamePanel;
  private Jun jun;
  private List<Obstacle> obstacles;

  public ObstaclePt2(ObstacleManager obstMnger, Ground gr, Jun jun, Panel gamePanel) {
    this.obstMngr = obstMngr;
    this.gr = gr;
    this.jun = jun;
    this.gamePanel = gamePanel;
    obstacles = new ArrayList<Obstacle>();
  }

  public void updateLoc() {
    for(Iterator<Obstacle> i = obstacles.iterator(); i.hasNext();) {
      Obstacle obst = i.next();
      obst.x += Math.round(jun.getSpeedX() * 100d) / 100d;
      if((int)obst.x + obst.img.getWidth() < 0) {
        i.remove();
      }
    }
  }

  public boolean spaceAvail() {
    for(Iterator<Obstacle> i = obstacles.iterator(); i.hasNext();) {
      Obstacle obst = i.next();
      if(WIDTH - (obst.x + obst.img.getWidth()) < obstMngr.getDistanceBetweenObstacles()) {
        return false;
      }
    }
    return true;
  }

  public boolean createObst() {
    if(Math.random() * 100 < obstMngr.getObstaclePct()) {
      for(int i = 0, obstNum = (int)(Math.random() * MAX_ETH_GROUP + 1); i < obstNum; i++) {
        BufferedImage ethImg = new Resource().getResourceImage("src/main/java/replit/images-" + (int)(Math.random() * ETH_AMOUNT + 1) + ".png");
        int x = WIDTH;
        int y = GROUND_Y - ethImg.getHeight();

        if(i > 0) {
          x = (int)obstacles.get(obstacles.size() - 1).x + obstacles.get(obstacles.size() - 1).img.getWidth();
        }
        obstacles.add(new Obstacle(ethImg, x, y));
      }
      return true;
    }
    return false;
  }

  public boolean isCollided() {
    for(Iterator<Obstacle> i = obstacles.iterator(); i.hasNext();) {
      Obstacle obst = i.next();
      Rectangle obstHitBox = getHitBox(obst);
      if(obstHitBox.intersects(jun.getJun())) {
        return true;
      }
    }
    return false;
  }

  private Rectangle getHitBox(Obstacle obst) {
    return new Rectangle((int)obst.x + (int)(obst.img.getWidth() / HITBOX_X),
                    obst.y + obst.img.getHeight() / HITBOX_Y,
                    obst.img.getWidth() - (int)(obst.img.getWidth() / HITBOX_X) * 2,
                    obst.img.getHeight() - obst.img.getHeight() / HITBOX_Y);
  }

  public void clearObst() {
    obstacles.clear();
  }

  public void draw(Graphics g) {
    for(Iterator<Obstacle> i = obstacles.iterator(); i.hasNext();) {
      Obstacle obst = i.next();
      g.drawImage(obst.img, (int)(obst.x), obst.y, null);
    }
  }

  public void drawHitBox(Graphics g) {
    g.setColor(Color.RED);
    for(Iterator<Obstacle> i = obstacles.iterator(); i.hasNext();) {
      Obstacle obst = i.next();
      Rectangle obstHitBox = getHitBox(obst);
      g.drawRect(obstHitBox.x, obstHitBox.y, (int)obstHitBox.getWidth(), (int)obstHitBox.getHeight());
    }
  }
 }
*/