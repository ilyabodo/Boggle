package boggle;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * this is the paneorganizer class which sets up graphically the pane that is used my App class to display the stage.
 * It also contains the event handlers that trigger from button presses, key inputs, or the timeline.
 */
public class PaneOrganizer{

    private BorderPane _root;
    private Boggle _boggle;

    public PaneOrganizer(int size) throws IOException {
        //all the initial variables and objects are instanciated here
        _root = new BorderPane();
        _boggle = new Boggle(size);
        _root.setCenter(_boggle.getGrid());

        //Quit button and label are set up
        this.setUpButtonsLabels();
    }


    /*
     * This method is a getter method so the root pane can be passed to the App class.
     */
    public Pane getRoot() {
        return _root;
    }

    /*
     * This method is only used internally and is responsible for setting up the quit button and the label at the
     * bottom of the screen
     */
    private void setUpButtonsLabels() {
        BorderPane buttonlabelPane = new BorderPane();
        //the new pane is set to the bottom of the root borderpane
        _root.setBottom(buttonlabelPane);
        //The labels and buttons from Boggle class are set up on the borderPane
        _boggle.setUpLabel(buttonlabelPane);
        //button is created and added to the pane
        Button button = new Button("Quit");
        buttonlabelPane.setBottom(button);
        button.setOnAction(e -> Platform.exit());//event handler for quit button
        button.setFocusTraversable(false); //This removes focus so that the keyboard inputs work correctly.
        BorderPane.setAlignment(button, Pos.CENTER); //centers all of the elements
    }

}
