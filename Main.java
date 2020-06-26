package boggle;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This is the App class which sets up the stage and uses the pane from paneorganizer to display all the elements
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setScene(new Scene(new PrePane(primaryStage).getRoot(), Constants.PREPANE_WIDTH, Constants.PREPANE_HEIGHT));
        primaryStage.setTitle("Select your Boggle size");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
