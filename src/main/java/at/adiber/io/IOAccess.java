package at.adiber.io;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IOAccess {

    public static List<BufferedImage> readImages(String folder) {

        List<BufferedImage> images = new ArrayList<>();

        int i = 1;
        while(true) {
            try {
                images.add(ImageIO.read(new File(folder, (i++) + ".png")));
            } catch (IOException e) {
                break;
            }
        }

        return images;
    }

}
