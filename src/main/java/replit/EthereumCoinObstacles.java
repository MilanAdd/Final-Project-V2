package replit;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

import replit.utility.Resource;

public class EthereumCoinObstacles {
    
    /** Used for obstacle arraylist */
    private class EthereumCoinObstacle {
        BufferedImage image;
        int x;
        int y;

        /** Creates rectangle around obstacle */
        Rectangle getObstacle () {
            Rectangle obstacle = new Rectangle();
            obstacle.x = x;
            obstacle.y = y;
            obstacle.width = image.getWidth();
            obstacle.height = image.getHeight();

            return obstacle;
        }
    }

    private int firstX;
    private int obstacleInterval;
    private int movementSpeed;

    private ArrayList<BufferedImage> imgList;
    private ArrayList<EthereumCoinObstacle> ethObstList;

    private EthereumCoinObstacle blockedAt;

    public EthereumCoinObstacles (int firstPos) {
        ethObstList = new ArrayList<EthereumCoinObstacle>();
        imgList = new ArrayList<BufferedImage>();

        firstX = firstPos;
        obstacleInterval = 200;
        movementSpeed = 17;

        imgList.add(new Resource().getResourceImage("src/main/java/replit/images/coin1.png"));
        imgList.add(new Resource().getResourceImage("src/main/java/replit/images/coin2.png"));

        int x = firstX;

        /** Goes through imgList, creates new EthereumCoinObstacle object
         * Image of obstacle is whatever current object's image is
         * Obstacle's x-value is set as current x-value, y-value is ground's y-value minus the image's height plus five
         * The x-value is itself being incrementally added by the obstacle interval
         * The new EthereumCoinObstacle object gets added into the obstacle arraylist
         */
        for (BufferedImage img : imgList) {
            EthereumCoinObstacle obst = new EthereumCoinObstacle();
            obst.image = img;
            obst.x = x;
            obst.y = Ground.GROUND_Y - img.getHeight() + 5;
            x += obstacleInterval;

            ethObstList.add(obst);
        }
    }

    
    /** Updates ostacle position using iterator that traversely loops through obstacle arraylist */
    public void update () {
        Iterator<EthereumCoinObstacle> looper = ethObstList.iterator(); // iterator that loops through obstacle array

        EthereumCoinObstacle firstObst = looper.next(); // first obstacle of iteration
        firstObst.x -= movementSpeed; // first obstacle's x-pos is original x-pos subtracted incrementely by movement speed, moves obstacles from right to left

        /** While obstacles keep getting iterated, the x-pos of any obstacle is original x-pos subtracted incrementely by movement speed plus a random number
         * Adds challenge to game as one obstacle moves faster than another, requires quicker reaction time at certain points to successfully jump over the obstacle(s)
         */
        while (looper.hasNext()) {
            int rand = (int)(Math.random() * 81);
            EthereumCoinObstacle obst = looper.next();
            obst.x -= movementSpeed + rand;
        }

        EthereumCoinObstacle lastObst = ethObstList.get(ethObstList.size() - 1);

        /** If the obstacle reaches the left side of the screen, it is removed from the arraylist and ultimately the screen
         * A new obstacle is placed at the right side of the screen and is added to the arraylist
         */
        if (firstObst.x <= -firstObst.image.getWidth()) {
            ethObstList.remove(firstObst);
            firstObst.x = 1000 + obstacleInterval;
            ethObstList.add(firstObst);
        }
    }

    /** Creates a new drawing of an obstacle in the obstacle list */
    public void create (Graphics g) {
        for (EthereumCoinObstacle obst : ethObstList) {
            g.setColor(Color.black);
            g.drawImage(obst.image, obst.x, obst.y, null);
        }
    }

    /** Boolean check to see if the character's rectangle intersects/hits the obstacle's surrounding rectangle
     * States that the obstacle object blockedAt is that obstacle that was hit by the character
     * Returns true if any of the mentioned conditions fit, returns false otherwise
     */
    public boolean hasHitObst () {
        for (EthereumCoinObstacle obst : ethObstList) {
            if (Jun.getJun().intersects(obst.getObstacle())) {
                System.out.println("Jun = " + Jun.getJun() + "\nObstacle = " + obst.getObstacle() + "\n\n");
                blockedAt = obst;
                return true;
            }
        }
        return false;
    }

    /** Method that adds obstacle whenever a game is resumed
     * The x-value of the obstacle becomes it's original x-value divided by 2
     * A new obstacle arraylist is created
     * The enhanced for loop has already been explained in the constructor
     */
    public void resume () {
        int x = firstX / 2;
        ethObstList = new ArrayList<EthereumCoinObstacle>();

        for (BufferedImage img : imgList) {
            EthereumCoinObstacle obst = new EthereumCoinObstacle();
            obst.image = img;
            obst.x = x;
            obst.y = Ground.GROUND_Y - img.getHeight() + 5;
            x += obstacleInterval;

            ethObstList.add(obst);
        }
    }
}
