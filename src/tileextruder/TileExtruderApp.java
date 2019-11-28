package tileextruder;

import tileextruder.model.TileExtruder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TileExtruderApp {

    public static void main(String[] args) throws IOException {
        BufferedImage image = TileExtruder.extrudeTiles(ImageIO.read(new File("in.png")));
        ImageIO.write(image, "png", new File("out.png"));
    }

}
