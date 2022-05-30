/**
package replit;

import replit.ObstaclePt2;
import replit.Panel;
import replit.Ground;
import replit.Jun;

import java.awt.Graphics;
import java.awt.Rectangle;

public class ObstacleManager {
    private static final double PERCENTAGE_INC = 0.0001;
    private static final double DISTANCE_DEC = -0.005;
    private static final int MINIMUM_DIST = 250;

    private double distanceBetweenObstacles = 750;
    private double obstaclePct = 2;

    private ObstaclePt2 obst;
    private Ground gr;
    private Jun jun;

    public ObstacleManager(Panel gamePanel) {
        obst = new ObstaclePt2(this, gr, jun, gamePanel);
    }

    public double getDistanceBetweenObstacles() {
        return distanceBetweenObstacles;
    }

    public double getObstaclePct() {
        return obstaclePct;
    }

    public void updateLoc() {
        obstaclePct += PERCENTAGE_INC;
        if(distanceBetweenObstacles > MINIMUM_DIST) {
            distanceBetweenObstacles += DISTANCE_DEC;
        }
        obst.updateLoc();
    }

    public boolean isCollided(Rectangle hitBox) {
        if(obst.isCollided()) {
            return true;
        }
        return false;
    }

    public void clearObst() {
        obst.clearObst();
    }

    public void draw(Graphics g) {
        obst.draw(g);
    }

    public void drawHitBox(Graphics g) {
        obst.drawHitBox(g);
    }
}
 */