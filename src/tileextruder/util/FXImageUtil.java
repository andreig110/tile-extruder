package tileextruder.util;

import javafx.scene.image.*;

import java.nio.IntBuffer;
import java.util.Arrays;

public class FXImageUtil {

    public static WritableImage scaleImage(Image image, int scaleFactor) {
        final int W = (int) image.getWidth();
        final int H = (int) image.getHeight();

        WritableImage scaledImage = new WritableImage(W * scaleFactor, H * scaleFactor);

        PixelReader reader = image.getPixelReader();
        int[] buffer = new int[scaleFactor * scaleFactor];
        PixelWriter writer = scaledImage.getPixelWriter();
        final PixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbInstance();

        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                final int argb = reader.getArgb(x, y);
                Arrays.fill(buffer, argb);
                writer.setPixels(x * scaleFactor, y * scaleFactor, scaleFactor, scaleFactor, pixelFormat, buffer, 0, 0);
            }
        }

        return scaledImage;
    }

}
