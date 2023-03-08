package video;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

public class WebcamServer {

    private static final int PORT = 8080;
    private static List<Socket> clients = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server started at port " + PORT);

        VideoCapture camera = new VideoCapture(0);
        if (!camera.isOpened()) {
            System.err.println("Cannot open camera.");
            return;
        }

        while (true) {
            Mat frame = new Mat();
            camera.read(frame);
            MatOfByte buffer = new MatOfByte();
            Imgcodecs.imencode(".jpg", frame, buffer);
            byte[] data = buffer.toArray();

            for (Socket client : clients) {
                try {
                    client.getOutputStream().write(data);
                } catch (IOException e) {
                    System.err.println("Cannot send data to client.");
                }
            }

            // Resize the frame to a specific size
            Size size = new Size(640, 480);
            Imgproc.resize(frame, frame, size);

            HighGui.imshow("Webcam", frame);
            if (HighGui.waitKey(1) == 27) {
                break;
            }
        }

        camera.release();
        HighGui.destroyAllWindows();
        serverSocket.close();
    }
}

