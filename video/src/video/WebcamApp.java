/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package video;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class WebcamApp extends Application {

    private VideoCapture capture;
    private Mat frame;
    private Image image;
    private ImageView imageView;

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        imageView = new ImageView();
        root.getChildren().add(imageView);
        Scene scene = new Scene(root, 640, 480);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Inicializar OpenCV
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Crear objeto de captura de video
        capture = new VideoCapture(0);

        // Crear objeto de imagen y visualización
        frame = new Mat();
        image = new Image("file:image.png");
        imageView.setImage(image);

        // Iniciar bucle de captura de video
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Capturar imagen desde la webcam
                capture.read(frame);

                // Convertir imagen de OpenCV a JavaFx
                image = Utils.mat2Image(frame);

                // Actualizar visualización
                imageView.setImage(image);
            }
        };
        timer.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}