package tileextruder.util;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class ImageUtil {

    public static void copyRect(final BufferedImage src, final int srcRectX, final int srcRectY, final int srcRectWidth, final int srcRectHeight,
                                final BufferedImage dest, final int destPosX, final int destPosY) {
        byte[] srcBuf = ((DataBufferByte) src.getRaster().getDataBuffer()).getData();
        byte[] destBuf = ((DataBufferByte) dest.getRaster().getDataBuffer()).getData();
        int srcOffset = 4 * (srcRectY * src.getWidth() + srcRectX);
        int destOffset = 4 * (destPosY * dest.getWidth() + destPosX);
        for (int y = srcRectY; y < srcRectY + srcRectHeight; y++, srcOffset += 4 * src.getWidth(), destOffset += 4 * dest.getWidth()) {
            System.arraycopy(srcBuf, srcOffset, destBuf, destOffset, 4 * srcRectWidth);
        }
    }

}
