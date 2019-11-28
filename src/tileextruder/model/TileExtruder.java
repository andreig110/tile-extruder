package tileextruder.model;

import java.awt.image.BufferedImage;

public class TileExtruder {

    private static final int TILE_SIZE = 32;

    public static BufferedImage extrudeTiles(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        final int columns = width / TILE_SIZE;  // The number of columns of tiles in the tileset
        final int rows = height / TILE_SIZE;  // The number of rows of tiles in the tileset

        BufferedImage outImage = new BufferedImage(columns * (TILE_SIZE + 2),
                rows * (TILE_SIZE + 2), image.getType());  // BufferedImage.TYPE_INT_ARGB

        for (int tx = 0; tx < columns; tx++)
            for (int ty = 0; ty < rows; ty++) {
                for (int x = 0; x < TILE_SIZE; x++)
                    for (int y = 0; y < TILE_SIZE; y++) {
                        final int color = image.getRGB(tx * TILE_SIZE + x, ty * TILE_SIZE + y);
                        outImage.setRGB(tx * (TILE_SIZE + 2) + 1 + x, ty * (TILE_SIZE + 2) + 1 + y, color);
                    }
            }

        return outImage;
    }

}
