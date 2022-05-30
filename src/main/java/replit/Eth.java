/**
package replit;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Eth extends Obstacle {
  public final int basePos = 125;

  private int xPosition;
  public final int ethWidth;
  public final int ethHeight;

  private final BufferedImage img;
  private final Jun jun;

  private Rectangle rectBound;

  public Eth(Jun jun, int xPos, int width, int height, BufferedImage img) {
    this.xPosition = xPos;
    this.ethWidth = width;
    this.ethHeight = height;
    this.img = img;
    this.jun = jun;
    rectBound = new Rectangle();
  }

  @Override
  public void updateObstaclesLoc() {
    xPosition -= jun.getSpeedX();
  }

  @Override
  public void drawLocation(Graphics g) {
    g.drawImage(img, xPosition, basePos - img.getHeight(), null);
  }

  @Override
  public Rectangle getHitBox() {
    rectBound = new Rectangle();
    rectBound.x = (int) xPosition + (img.getWidth() - ethWidth) / 2;
    rectBound.y = basePos - img.getHeight() + (img.getHeight() - ethHeight) / 2;
    rectBound.width = ethWidth;
    rectBound.height = ethHeight;
    return rectBound;
  }

  @Override
  public boolean offScreen() {
    return xPosition < -img.getWidth();
  }
}
*/