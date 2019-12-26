package tileextruder.util;

import java.awt.image.*;

public class ImageUtil {

    public static void copyRect(final BufferedImage src, final int srcRectX, final int srcRectY,
                                final int srcRectWidth, final int srcRectHeight,
                                final BufferedImage dest, final int destPosX, final int destPosY) {
        switch (src.getRaster().getDataBuffer().getDataType()) {
            case DataBuffer.TYPE_BYTE:
                if (    src.getType() == BufferedImage.TYPE_BYTE_BINARY ||   // byte-packed 1, 2, or 4 bit image
                        src.getType() == BufferedImage.TYPE_BYTE_INDEXED) {  // indexed byte image (8 bit)
                    copyRectRGB(src, srcRectX, srcRectY, srcRectWidth, srcRectHeight, dest, destPosX, destPosY);
                } else {
                    /* else if src.getType() is one of the following image type:
                       TYPE_3BYTE_BGR    - 8 bpc RGB
                       TYPE_4BYTE_ABGR   - 8 bpc RGBA
                       TYPE_BYTE_GRAY    - 8 bpc GRAY
                       TYPE_CUSTOM       - 8 bpc GRAYA (grey + alpha)
                       bpc = bits per component (channel)
                     */
                    copyRectDataBufferByte(src, srcRectX, srcRectY, srcRectWidth, srcRectHeight, dest, destPosX, destPosY);
                }
                break;
            case DataBuffer.TYPE_SHORT:
            case DataBuffer.TYPE_USHORT:
                /* BufferedImage image type for 16 bpc PNG formats:
                   TYPE_CUSTOM      - 16 bpc RGB
                   TYPE_CUSTOM      - 16 bpc RGBA
                   TYPE_USHORT_GRAY - 16 bpc GREY
                   TYPE_CUSTOM      - 16 bpc GREYA
                 */
                copyRectDataBufferShort(src, srcRectX, srcRectY, srcRectWidth, srcRectHeight, dest, destPosX, destPosY);
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

    private static void copyRectDataBufferShort(final BufferedImage src, final int srcRectX, final int srcRectY,
                                                final int srcRectWidth, final int srcRectHeight,
                                                final BufferedImage dest, final int destPosX, final int destPosY) {
        short[] srcBuf;
        short[] destBuf;
        if (src.getRaster().getDataBuffer() instanceof DataBufferUShort) {
            srcBuf = ((DataBufferUShort) src.getRaster().getDataBuffer()).getData();
            destBuf = ((DataBufferUShort) dest.getRaster().getDataBuffer()).getData();
        } else {
            srcBuf = ((DataBufferShort) src.getRaster().getDataBuffer()).getData();
            destBuf = ((DataBufferShort) dest.getRaster().getDataBuffer()).getData();
        }

        final int n = src.getColorModel().getNumComponents();  // the number of components, including alpha
        int srcOffset = n * (srcRectY * src.getWidth() + srcRectX);
        int destOffset = n * (destPosY * dest.getWidth() + destPosX);

        for (int y = srcRectY;
                 y < srcRectY + srcRectHeight;
                 y++, srcOffset += n * src.getWidth(), destOffset += n * dest.getWidth()) {
            System.arraycopy(srcBuf, srcOffset, destBuf, destOffset, n * srcRectWidth);
        }
    }

    private static void copyRectRGB(final BufferedImage src, final int srcRectX, final int srcRectY,
                                    final int srcRectWidth, final int srcRectHeight,
                                    final BufferedImage dest, final int destPosX, final int destPosY) {
        for (int y = srcRectY; y < srcRectY + srcRectHeight; y++)
            for (int x = srcRectX; x < srcRectX + srcRectWidth; x++) {
                final int argb = src.getRGB(x, y);
                dest.setRGB(destPosX + (x - srcRectX), destPosY + (y - srcRectY), argb);
            }
    }

}
