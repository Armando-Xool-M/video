/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Cliente;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import javax.imageio.ImageIO;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class WebcamClient extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Socket socket = new Socket("localhost", 9999);
        InputStream in = socket.getInputStream();
        DataInputStream dis = new DataInputStream(in);

        Canvas canvas = new Canvas(640, 480);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, 640, 480);
        primaryStage.setScene(scene);
        primaryStage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                try {
                    byte[] data = new byte[dis.available()];
                    dis.readFully(data);
                    Image img = new Image(new ByteArrayInputStream(data));
                    gc.drawImage(img, 0, 0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        timer.start();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
