import controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {


    private static final int WIDTH = 800;
    private static final int HEIGHT = 500;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        loader.setController(new MainController());
        Parent root = loader.load();

        Scene scene = new Scene(root, WIDTH, HEIGHT);

        primaryStage.setTitle("Lab 4 - Interpolation");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
