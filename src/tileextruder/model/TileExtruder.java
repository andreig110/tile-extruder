package tileextruder.model;

import tileextruder.util.ImageUtil;

import java.awt.image.BufferedImage;

public class TileExtruder {

    public static BufferedImage extrudeTiles(BufferedImage image, int tileWidth, int tileHeight) {
        final int columns = image.getWidth() / tileWidth;  // The number of columns of tiles in the tileset
        final int rows = image.getHeight() / tileHeight;  // The number of rows of tiles in the tileset

        final int extrudedTileWidth = tileWidth + 2;
        final int extrudedTileHeight = tileHeight + 2;
        BufferedImage extrudedImage = new BufferedImage(columns * extrudedTileWidth, rows * extrudedTileHeight, image.getType());

        for (int tx = 0; tx < columns; tx++)
            for (int ty = 0; ty < rows; ty++) {
                int tilePosX = tx * tileWidth;
                int tilePosY = ty * tileHeight;
                int extrudedTilePosX = tx * extrudedTileWidth;
                int extrudedTilePosY = ty * extrudedTileHeight;

                // Copy the tile
                ImageUtil.copyRect(image, tilePosX, tilePosY, tileWidth, tileHeight,
                        extrudedImage, extrudedTilePosX + 1, extrudedTilePosY + 1);

                // Extrude the border pixels
                ImageUtil.copyRect(image, tilePosX, tilePosY, tileWidth, 1,
                        extrudedImage, extrudedTilePosX + 1, extrudedTilePosY);  // Top border
                ImageUtil.copyRect(image, tilePosX, tilePosY + tileHeight - 1, tileWidth, 1,
                        extrudedImage, extrudedTilePosX + 1, extrudedTilePosY + extrudedTileHeight - 1);  // Bottom border
                ImageUtil.copyRect(image, tilePosX, tilePosY, 1, tileHeight,
                        extrudedImage, extrudedTilePosX, extrudedTilePosY + 1);  // Left border
                ImageUtil.copyRect(image, tilePosX + tileWidth - 1, tilePosY, 1, tileHeight,
                        extrudedImage, extrudedTilePosX + extrudedTileWidth - 1, extrudedTilePosY + 1);  // Right border

                // Extrude the corner pixels
                // Top-left corner
                int color = image.getRGB(tilePosX, tilePosY);
                extrudedImage.setRGB(extrudedTilePosX, extrudedTilePosY, color);
                // Top-right corner
                color = image.getRGB(tilePosX + tileWidth - 1, tilePosY);
                extrudedImage.setRGB(extrudedTilePosX + extrudedTileWidth - 1, extrudedTilePosY, color);
                // Bottom-left corner
                color = image.getRGB(tilePosX, tilePosY + tileHeight - 1);
                extrudedImage.setRGB(extrudedTilePosX, extrudedTilePosY + extrudedTileHeight - 1, color);
                // Bottom-right corner
                color = image.getRGB(tilePosX + tileWidth - 1, tilePosY + tileHeight - 1);
                extrudedImage.setRGB(extrudedTilePosX + extrudedTileWidth - 1, extrudedTilePosY + extrudedTileHeight - 1, color);
            }

        return extrudedImage;
    }

}
