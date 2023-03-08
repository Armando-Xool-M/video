/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Cliente;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class WebcamClient extends Application {

    private static final String HOSTNAME = "localhost";
    private static final int PORT = 8080;
    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;

    private Socket socket;
    private InputStream inputStream;
    private Mat mat = new Mat();

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane root = new BorderPane();
        ImageView imageView = new ImageView();
        root.setCenter(imageView);
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        stage.setScene(scene);
        stage.setTitle("Webcam");
        stage.show();

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        socket = new Socket(HOSTNAME, PORT);
        inputStream = socket.getInputStream();

        while (true) {
            try {
                byte[] buffer = new byte[WIDTH * HEIGHT * 3];
                inputStream.read(buffer);
                mat = Imgcodecs.imdecode(new MatOfByte(buffer), Imgcodecs.IMREAD_UNCHANGED);

                // Resize the image to fit the ImageView
                Imgproc.resize(mat, mat, new org.opencv.core.Size(WIDTH, HEIGHT));

                // Convert the OpenCV Mat to a JavaFX Image
                MatOfByte byteMat = new MatOfByte();
                Imgcodecs.imencode(".jpg", mat, byteMat);
                byte[] byteArray = byteMat.toArray();
                InputStream stream = new ByteArrayInputStream(byteArray);
                Image image = new Image(stream);

                imageView.setImage(image);
            } catch (IOException e) {
                System.err.println("Cannot receive data from server.");
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}