package boggle;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * This is the PrePane class. PrePane is repsonsible for the scene/stage where the user selects the size of the
 * boggle game they wish to play. PrePane is called by App class when the applcatiuon is opened. PrePane calls the
 * normal paneorganizer after a size is selected.
 */
public class PrePane {

    private int _size;
    private VBox _root;
    private Stage _mainStage;

    /*
     * Constructor for PrePane.
     */
    public PrePane(Stage stage) {
        _mainStage = stage;
        _root = new VBox(Constants.VBOX_SPACING);
        _root.setAlignment(Pos.CENTER);

        //text labels
        Label label = new Label("Welcome to Boggle!");
        Label label2 = new Label("Please choose the size of the game you would like to play!");
        Label label3 = new Label("4x4 is the standard for a Boggle game");
        _root.getChildren().addAll(label, label2, label3); //labels are added to the pane

        //all of the buttons for size selection are added. Any new size can be added by just passing in the desired size into newButton method
        this.newButton(3);
        this.newButton(4);
        this.newButton(5);
        this.newButton(6);

        //Quit button code
        Button button = new Button("Quit");
        button.setOnAction(e -> Platform.exit());
        button.setFocusTraversable(false);
        _root.getChildren().add(button);
    }

    /*
     * This method sets up the button that allow the user to select the game size.
     */
    private void newButton(int gameSize) {
        Button button = new Button();
        button.setFocusTraversable(false);
        button.setText(gameSize + " x " + gameSize);
        //event handler for the button, it sets the size variable then calls the startGame method and also closes the currentStage
        button.setOnAction(e -> {
            _size = gameSize;
            try {
                this.startGame();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            //closes the window where user selects game size
            _mainStage.close();
        });
        //adds the button to the pane
        _root.getChildren().add(button);
    }

    /*
     * This is a getter method that return the root pane to the App class.
     */
    public VBox getRoot() {
        return _root;
    }

    /*
     * This method acts like the start method in App class. It creates and sets the stage/scene for the boggle game
     */
    private void startGame() throws IOException {
        Stage stage = new Stage();
        PaneOrganizer paneOrganizer = new PaneOrganizer(_size); //Pane organizer is created based on user selected size
        stage.setScene(new Scene(paneOrganizer.getRoot(), Constants.SCENE_WIDTH + (Constants.RECTANGLE_SIZE+Constants.STROKE_SIZE)*(_size-3), Constants.SCENE_HEIGHT + (Constants.RECTANGLE_SIZE+Constants.STROKE_SIZE)*(_size-3)));
        stage.setTitle("Boggle");
        stage.show();
    }

}
