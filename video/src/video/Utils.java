/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package video;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import javafx.embed.swing.SwingFXUtils;
import org.opencv.core.Mat;

import javafx.scene.image.Image;

public final class Utils {

    /**
     * Convert a Mat object (OpenCV) in the corresponding Image for JavaFX
     *
     * @param frame
     *            the {@link Mat} representing the current frame
     * @return the {@link Image} to show
     */
    public static Image mat2Image(Mat frame) {
        try {
            return SwingFXUtils.toFXImage(matToBufferedImage(frame), null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Convert a Mat object (OpenCV) to BufferedImage.
     *
     * @param frame
     *            the {@link Mat} representing the current frame
     * @return the {@link BufferedImage} to be displayed
     */
    public static BufferedImage matToBufferedImage(Mat frame) {
        // create a temporary buffer
        byte[] buffer = new byte[frame.width() * frame.height() * frame.channels()];
        // encode the frame in the buffer, according to the Mat format
        frame.get(0, 0, buffer);
        // create an image from the byte array
        BufferedImage image = new BufferedImage(frame.width(), frame.height(), BufferedImage.TYPE_3BYTE_BGR);
        WritableRaster raster = image.getRaster();
        DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
        System.arraycopy(buffer, 0, dataBuffer.getData(), 0, buffer.length);
        return image;
    }
}
