package replit.utility;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Resource {
    /** Method that gets image from specified file path */
    public BufferedImage getResourceImage(String path) {
        BufferedImage img = null;
        
        /** Catches any IOExceptions whenever file/image is trying to be found */
        try {
            img = ImageIO.read(new File(path));
        } catch(IOException e) {
            e.printStackTrace();
        }
        return img;
    }
}
