package video;

import java.io.ByteArrayInputStream;
import java.net.*;
import java.util.zip.Inflater;
import org.opencv.core.Core;
import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.videoio.VideoCapture;
import org.opencv.imgcodecs.*;

public class VideoChatServer {

    // Parámetros de red
    private static final int PORT = 8000;
    private static final int PACKET_SIZE = 65535;

    // Parámetros de video
    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;

    public static void main(String[] args) throws Exception {
        // Inicializar OpenCV
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Inicializar la cámara
        VideoCapture capture = new VideoCapture(0);
        capture.set(3, WIDTH);
        capture.set(4, HEIGHT);

        // Inicializar el socket
        DatagramSocket socket = new DatagramSocket(PORT);

        // Inicializar el buffer de recepción
        byte[] buffer = new byte[PACKET_SIZE];

        // Inicializar el cuadro de video
        Mat mat = new Mat();

        while (true) {
// Recibir el paquete
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);

            // Decodificar el paquete en una imagen
            byte[] data = packet.getData();
            byte[] header = new byte[3];
            System.arraycopy(data, 0, header, 0, 3);
            byte[] compressedBytes = new byte[data.length - 3];
            System.arraycopy(data, 3, compressedBytes, 0, data.length - 3);

            Inflater inflater = new Inflater();
            inflater.setInput(compressedBytes);
            byte[] imageBytes = new byte[WIDTH * HEIGHT * 3];
            inflater.inflate(imageBytes);
            inflater.end();

            mat = new Mat(HEIGHT, WIDTH, Imgcodecs.IMREAD_COLOR);
            mat.put(0, 0, imageBytes);

            // Procesar el cuadro de video
            Core.flip(mat, mat, Core.ROTATE_180);
            Imgcodecs.imwrite("frame.jpg", mat);

            // Mostrar el cuadro de video
            // (opcional, solo para fines de depuración)
            HighGui.imshow("Server", mat);
            HighGui.waitKey(1);
        }
    }
}
