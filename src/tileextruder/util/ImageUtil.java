package tileextruder.util;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;

public class ImageUtil {

    public static void copyRect(final BufferedImage src, final int srcRectX, final int srcRectY,
                                final int srcRectWidth, final int srcRectHeight,
                                final BufferedImage dest, final int destPosX, final int destPosY) {
        switch (src.getRaster().getDataBuffer().getDataType()) {
            case DataBuffer.TYPE_BYTE:
                if (src.getType() == BufferedImage.TYPE_BYTE_BINARY) {  // byte-packed 1, 2, or 4 bit image
                    // TODO
                } else {
                    // bpc = bits per component (channel)
                    // TYPE_BYTE_INDEXED - indexed byte image (8 bit)
                    // TYPE_3BYTE_BGR    - 8 bpc RGB
                    // TYPE_4BYTE_ABGR   - 8 bpc RGBA
                    // TYPE_BYTE_GRAY    - 8 bpc GRAY
                    // TYPE_CUSTOM       - 8 bpc GRAYA (grey + alpha)
                    copyRectDataBufferByte(src, srcRectX, srcRectY, srcRectWidth, srcRectHeight, dest, destPosX, destPosY);
                }
                break;
            case DataBuffer.TYPE_USHORT:
            case DataBuffer.TYPE_SHORT:
                // TODO
        }
    }

    private static void copyRectDataBufferByte(final BufferedImage src, final int srcRectX, final int srcRectY,
                                               final int srcRectWidth, final int srcRectHeight,
                                               final BufferedImage dest, final int destPosX, final int destPosY) {
        byte[] srcBuf = ((DataBufferByte) src.getRaster().getDataBuffer()).getData();
        byte[] destBuf = ((DataBufferByte) dest.getRaster().getDataBuffer()).getData();

        final int n;  // the number of components, including alpha
        if (src.getType() == BufferedImage.TYPE_BYTE_INDEXED)
            n = 1;
        else
            n = src.getColorModel().getNumComponents();
        int srcOffset = n * (srcRectY * src.getWidth() + srcRectX);
        int destOffset = n * (destPosY * dest.getWidth() + destPosX);

        for (int y = srcRectY;
                 y < srcRectY + srcRectHeight;
                 y++, srcOffset += n * src.getWidth(), destOffset += n * dest.getWidth()) {
            System.arraycopy(srcBuf, srcOffset, destBuf, destOffset, n * srcRectWidth);
        }
    }

}
