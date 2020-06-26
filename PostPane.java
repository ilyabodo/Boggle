package boggle;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * This is the PostPane class. This class is responsible for the window that comes up after the Boggle game is over.
 * It displays all of the words the user found and all of the possible words.
 */
public class PostPane {

    /*
     * Constructor for PostPane. It uses the list of all words and list of words the user found
     */
    public PostPane(ArrayList userFoundWords, ArrayList words) {
        Stage newStage = new Stage();
        newStage.setTitle("Boggle Results");

        //This vbox holds all of the labels with the words the player found
        VBox userWords = new VBox(1);
        userWords.getChildren().add(new Label("Words you found:"));
        //loops through all strings in the arraylist and makes a new label for it
        for (Object userFoundWord : userFoundWords) {
            userWords.getChildren().add(new Label((String) userFoundWord));
        }

        VBox allWords = new VBox(1);
        allWords.getChildren().add(new Label("All possible words:"));
        //loops through all possible words in this boggle game and makes a label for each
        for (Object word : words) {
            allWords.getChildren().add(new Label((String) word));
        }

        //ScroolPane is the main pane of the PostPane class. It holds the other panes while allowing scrolling for long lists
        ScrollPane scrollpane = new ScrollPane();
        scrollpane.setPrefSize(Constants.POSTPANE_WIDTH, Constants.POSTPANE_HEIGHT);
        //HBox holds both Vboxes next to eachother
        HBox pane = new HBox(Constants.HBOX_SPACING);
        pane.getChildren().addAll(userWords, allWords);
        //scrollpane contents are set to the hbox
        scrollpane.setContent(pane);

        //creates a new scene using the scroll pane
        Scene scene = new Scene(scrollpane);
        newStage.setScene(scene);
        newStage.show(); //opens new scene in a new window
    }
}
