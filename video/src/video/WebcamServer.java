package video;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

public class WebcamServer {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) throws IOException {
        VideoCapture capture = new VideoCapture(0);
        capture.set(Videoio.CAP_PROP_FRAME_WIDTH, 640);
        capture.set(Videoio.CAP_PROP_FRAME_HEIGHT, 480);
        capture.set(Videoio.CAP_PROP_FPS, 30);

        ServerSocket serverSocket = new ServerSocket(9999);
        while (true) {
            Socket socket = serverSocket.accept();
            new Thread(() -> {
                try {
                    OutputStream out = socket.getOutputStream();
                    while (true) {
                        Mat frame = new Mat();
                        capture.read(frame);
                        BufferedImage img = Utils.matToBufferedImage(frame);
                        BufferedImage encodedImg = encodeImage(img);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ImageIO.write(encodedImg, "jpg", baos);
                        byte[] data = baos.toByteArray();
                        out.write(data);
                        out.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private static BufferedImage encodeImage(BufferedImage img) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "jpg", baos);
        byte[] data = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        BufferedImage encodedImg = ImageIO.read(bais);
        return encodedImg;
    }

}
