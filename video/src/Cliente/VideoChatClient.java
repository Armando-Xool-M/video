/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Cliente;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import video.Utils;

public class VideoChatClient extends Application {

    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;
    private ImageView imageView;

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        StackPane root = new StackPane();

        imageView = new ImageView();
        root.getChildren().add(imageView);

        Scene scene = new Scene(root, WIDTH, HEIGHT);

        primaryStage.setTitle("Video Chat Client");
        primaryStage.setScene(scene);
        primaryStage.show();

        Thread receiverThread = new Thread(new Receiver());
        receiverThread.start();
    }

    private class Receiver implements Runnable {
        private Socket socket;
        private InputStream inputStream;
        private Mat mat;

        public Receiver() {
            try {
                socket = new Socket("localhost", 8000);
                inputStream = socket.getInputStream();
                mat = new Mat(HEIGHT, WIDTH, CvType.CV_8UC3);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            byte[] data = new byte[WIDTH * HEIGHT * 3];

            while (true) {
                try {
                    inputStream.read(data);

                    int imageSize = (data.length - 3) / 3;
                    int width = WIDTH;
                    int height = HEIGHT;

                    byte[] header = new byte[3];
                    System.arraycopy(data, 0, header, 0, 3);

                    mat = new Mat(height, width, CvType.CV_8UC3);

                    byte[] compressedBytes = new byte[imageSize];
                    System.arraycopy(data, 3, compressedBytes, 0, imageSize);

                    Inflater inflater = new Inflater();
                    inflater.setInput(compressedBytes);
                    byte[] imageBytes = new byte[width * height * 3];
                    inflater.inflate(imageBytes);
                    inflater.end();

                    mat = Imgcodecs.imdecode(new MatOfByte(imageBytes), Imgcodecs.IMREAD_COLOR);

                    Image image = Utils.mat2Image(mat);
                    imageView.setImage(image);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (DataFormatException ex) {
                    Logger.getLogger(VideoChatClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
