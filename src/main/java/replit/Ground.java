package replit;

import replit.utility.Resource;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

import javax.imageio.ImageIO;

public class Ground {
    /** Private class with image and x value */
    private class GroundImage {
        BufferedImage image; 
        int x;
    }

    public static int GROUND_Y; // y-value of ground

    private BufferedImage image; // BufferedImage object is image

    private ArrayList<GroundImage> groundImageSet; // arraylist of ground images

    public Ground (int panelHeight) {
        GROUND_Y = (int)(panelHeight - 0.1 * panelHeight); // y-value of ground is panel height minus 10% of the panel height

        /** Try and catch statement that detects if there is exception to Ground image */
        try {
            image = new Resource().getResourceImage("src/main/java/replit/images/ground.png");
        } catch (Exception e) {
            e.printStackTrace();
        }

        groundImageSet = new ArrayList<GroundImage>(); // arraylist of ground images is set to new GroundImage arraylist

        /** For loop that creates new GroundImage object, sets image of ground, x-pos of object, and adds object to ground image set arraylist
         * Loops and executes three times
         */
        for (int i = 0; i < 3; i++) {
            GroundImage obj = new GroundImage();
            obj.image = image;
            obj.x = 0;
            groundImageSet.add(obj);
        }
    }

    /** Updates ground using iterator that traversely loops through ground image set arraylist */
    public void update () {
        Iterator<GroundImage> looper = groundImageSet.iterator(); // iterator that loops through ground image set array
        GroundImage first = looper.next(); // first ground image of iteration

        first.x -= 10; // x-value of first ground image is itself incrementally subtracted by 10

        int previousX = first.x; // previous x value becomes x-value of first ground image
        
        /** As long as there is another ground image being iterated, another ground image will be createdI
         * Its x-value is the previous x-value plus the ground image's width
         * The previous x-value will the next ground image's x-value
         */
        while (looper.hasNext()) {
            GroundImage next = looper.next();
            next.x = previousX + image.getWidth();
            previousX = next.x;
        }

        /** If the first ground image's x-value is less than the negative value of the ground image's width, then that ground image is removed from the image arraylist
         * Its x-value is the previous x-value plus the ground image's width
         * That image becomes added to the image arraylist
         */
        if (first.x < -image.getWidth()) {
            groundImageSet.remove(first);
            first.x = previousX + image.getWidth();
            groundImageSet.add(first);
        }
    }

    /** Enhanced for loop that goes through ground image set arraylist
     * A ground image object's image is set as the image
     * It's integer x-value is the x-value
     * The ground y-value is the y-value
     * A new ground image is being created
     */
    public void create (Graphics g) {
        for (GroundImage img : groundImageSet) {
            g.drawImage(img.image, (int) img.x, GROUND_Y, null);
        }
    }
}