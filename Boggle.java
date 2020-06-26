package boggle;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Stack;

/**
 * This is the main game class Boggle. It handles all of the game code besides the solving algorithm. The arrays and
 * gridpane are kept and updates here along with the timehandler and all of the other eventhandlers. All game logic
 * is here.
 */
public class Boggle {

    private GridPane _gridPane;
    private char[][] _charArray;
    private boolean[][] _isClicked;
    private ArrayList<String> _userFoundWords;
    private ArrayList<Rectangle> _rectangles;
    private Stack<Integer> _iStack;
    private Stack<Integer> _jStack;
    private Label _scoreLabel;
    private Label _timeLabel;
    private Label _statusLabel;
    private Label _currentWordLabel;
    private boolean _gameOver;
    private int _size;
    private int _time;
    private int _score;
    private String _currentWord;
    private BoggleSolver _solver;
    private Timeline _timeline;

    /*
     * Constructor for the boggle game. It sets up all of the game elements including the event handlers, logical
     * representation of the board using arrays, and the graphical representation using a gridpane
     */
    public Boggle(int size) throws IOException {
        _score = 0;
        _size = size;
        _gameOver = false;
        _charArray = new char[_size][_size];
        _isClicked = new boolean[_size][_size];
        _userFoundWords = new ArrayList<>();
        _gridPane = new GridPane();
        _iStack = new Stack<>();
        _jStack = new Stack<>();
        _gridPane.setFocusTraversable(true);

        this.clearBoolArray();
        this.setUpBoard();

        //This is the key handler.
        _gridPane.setOnKeyPressed(e -> {
            //keyhandler does not respond if the game is over
            if (!_gameOver) {
                switch (e.getCode()) {
                    case SPACE:
                        //rotates the whole board
                        this.rotateGrid();
                        break;
                    case Q:
                        //Q serves same functionality as the "clear" button
                        this.clearLetters();
                        this.clearWord();
                        break;
                    case ESCAPE:
                        //the esc key ends the game early without waiting for 2 minutes to be up
                        this.endGame();
                        break;
                    default:
                        break;
                }
            }
            e.consume();
        });

        _gridPane.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->{
            //event handler does not respond if the game is over
            if (!_gameOver) {
                //gets the location of the rectangle that was clicked
                Node clicked = e.getPickResult().getIntersectedNode();
                int row = GridPane.getRowIndex(clicked);
                int col = GridPane.getColumnIndex(clicked);

                //checks if the clicked rectangle is already clicked and if its adjacent to an already clicked rectangle
                if (!_isClicked[row][col] && this.canAdd(row, col)) {
                    //sets color to red to indicate to the player that they have selected it
                    ((Rectangle) clicked).setStroke(Color.RED);
                    _isClicked[row][col] = true;
                    //adds the location to the two Stacks so the game knows what the last selected rectangle is
                    _iStack.push(row);
                    _jStack.push(col);
                    //updates the label and string with the new letter
                    this.addLetterToLabel(row, col);

                }
                //if an already clicked rectangle is clicked
                else if (_isClicked[row][col] && this.canRemove(row, col)) {
                    //resets the color
                    ((Rectangle) clicked).setStroke(Color.WHITE);
                    //removes its location for the stack
                    _iStack.pop();
                    _jStack.pop();
                    _isClicked[row][col] = false;
                    //removes the letter from the current word
                    this.removeLetterFromLabel();
                }
                else {
                    return;
                }
            }
            e.consume();
        });

    }

    /*
     * This method sets up the board. It randomly generates characters for use in the game
     */
    private void setUpBoard() throws IOException {
        SecureRandom r = new SecureRandom();

        /*
         * These "dice" contain the actual letters that are used in a real boggle game. If the user selects to play
         * the standard 4x4 game, the actual characters of the dice will be used.
         */
        ArrayList<String> dice = new ArrayList<>();
        dice.add("AEANEG");
        dice.add("AHSPCO");
        dice.add("ASPFFK");
        dice.add("OBJOAB");
        dice.add("IOTMUC");
        dice.add("RYVDEL");
        dice.add("LREIXD");
        dice.add("EIUNES");
        dice.add("WNGEEH");
        dice.add("LNHNRZ");
        dice.add("TSTIYD");
        dice.add("OWTOAT");
        dice.add("ERTTYL");
        dice.add("TOESSI");
        dice.add("TERWHV");
        dice.add("NUIHMQ");

        /*
         * If the player is not playing 4x4, the 16 dice in the original boggle game cannot be used. In substitution,
         * this string contains a very rough approximation of the distribution of letters based on how common they are
         * in the english language.
         */
        String alphabet = "AAAABBCCDDDEEEEEEFFGGHHHIIIJJKKLLLMMNNNOOOOPPQRRRSSSSTTTTTUUUVVWWXYYZ";

        char letter;
        //sets up the gridpane
        _gridPane.setStyle("-fx-background-color: black");
        _gridPane.setHgap(0);
        _gridPane.setVgap(0);
        _rectangles = new ArrayList<>();
        //loops though the entire board, adding a label and a rectangle for each location based on the boggle game size
        for(int i = 0; i < _size; i++) {
            for(int j = 0; j < _size; j++) {
                Label label = new Label();

                //uses the "dice" method for letter selection of playing 4x4
                if (_size == 4) {
                    int n = r.nextInt(dice.size());
                    letter = dice.get(n).charAt(r.nextInt(dice.get(n).length()));
                    dice.remove(n);
                }
                //otherwise uses alphabet string
                else {
                    letter = alphabet.charAt(r.nextInt(alphabet.length()));
                }

                //letter is added to the label
                label.setText(String.valueOf(letter));
                label.setStyle("-fx-font: 100 arial;");
                label.setTextFill(Color.WHITE);

                //a rectangle is created ontop of the label with a transparent color. User can see the label underneath
                Rectangle rect = new Rectangle(Constants.RECTANGLE_SIZE, Constants.RECTANGLE_SIZE, Color.TRANSPARENT);
                rect.setStroke(Color.WHITESMOKE);
                rect.setStrokeWidth(Constants.STROKE_SIZE);
                _gridPane.add(label, i, j);
                _gridPane.add(rect, i, j);

                //adds the rectangle to an arraylist. this is used later to change the color of the rectangle's border
                _rectangles.add(rect);

                //allignments of label and rectangle are set
                GridPane.setHalignment(label, HPos.CENTER);
                GridPane.setValignment(label, VPos.CENTER);
                GridPane.setHalignment(rect, HPos.CENTER);
                GridPane.setValignment(rect, VPos.CENTER);

                //finally the character is added to the array of characters in the correct location
                _charArray[i][j] = letter;

            }
        }
        _gridPane.requestFocus();
        //The boggle solver is created which will solve the current board
        _solver = new BoggleSolver(_charArray);
        //sets up and starts the timehandler
        this.setUpTimeline();

    }

    /*
     * This method is called any time the user clicks the "Check Word" button. It asks the solver if the current word
     * can be found in this boggle board.
     * Scoring is also handled here.
     */
    private void checkCurrentWord() {
        //If the word entered is good, then word will be added to a list for later and points will be awarded
        if (_solver.validWord(_currentWord) && !_userFoundWords.contains(_currentWord)) {
            _userFoundWords.add(_currentWord);
            //status label is updated to let the player know they found a word
            _statusLabel.setText("You found the word " + "''" + _currentWord + "''");
            //switch statement awards points based on how long the word the player found is
            switch (_currentWord.length()) {
                case 1:
                case 2:
                case 3:
                case 4:
                    _score = _score + 1;
                    break;
                case 5:
                    _score = _score + 2;
                    break;
                case 6:
                    _score = _score + 3;
                    break;
                case 7:
                    _score = _score + 5;
                    break;
                default:
                    _score = _score + 11;
                    break;
            }
            _scoreLabel.setText("Score: " + _score);
        }
        //if the player enters a word they already found, status label tells the,
        else if (_userFoundWords.contains(_currentWord)) {
            _statusLabel.setText("You have already found the word " + "''" + _currentWord + "''");
        }
        //if word is not a valid word
        else {
            _statusLabel.setText("Sorry, " + "''" + _currentWord + "''" + " is not a valid word!");
        }
    }

    /*
     * This method sets up all the elements below the game board. The borderpane from PaneOrganizer is pass in to it
     */
    public void setUpLabel(BorderPane pane) {
        //the current word the user has selected is this label at the center of the borderpane
        _currentWord = "";
        _currentWordLabel = new Label("Current Word: ");
        _currentWordLabel.setStyle("-fx-font: 20 arial;");
        pane.setCenter(_currentWordLabel);

        //vbox is created to hold the time remaining label and the status label
        VBox vbox = new VBox(1);
        _timeLabel = new Label("Time remaining: 120 seconds");
        _statusLabel = new Label("You have 2 minutes to find as many words as you can! Start NOW!");
        vbox.getChildren().addAll(_statusLabel, _timeLabel);
        vbox.setAlignment(Pos.CENTER);
        pane.setTop(vbox);

        //button for checking the current word
        Button checkWordButton = new Button("Check Word");
        //event handler only responds if the game is currently in progress
        checkWordButton.setOnAction(e -> {
           if (!_gameOver) {
               this.checkCurrentWord();
               checkWordButton.setFocusTraversable(false);
               _gridPane.requestFocus();
           }
           e.consume();
        });
        pane.setRight(checkWordButton);

        //hbox contains the score label and the clear button
        HBox hbox = new HBox(Constants.HBOX_SPACING);
        _scoreLabel = new Label("Score: 0");
        _scoreLabel.setStyle("-fx-font: 16 arial;");
        Button clearButton = new Button("Clear"); //button that clears the board
        clearButton.setOnAction(e -> {
            if (!_gameOver) {
                this.clearLetters();
                _statusLabel.setText("Board cleared");
                clearButton.setFocusTraversable(false);
                _gridPane.requestFocus();
            }
            e.consume();
        });
        hbox.getChildren().addAll(clearButton, _scoreLabel);
        pane.setLeft(hbox);
    }

    /*
     * This method adds a new character to the current string of the currently selected word
     */
    private void addLetterToLabel(int i, int j) {
        _currentWord = _currentWord + _charArray[j][i];
        _currentWordLabel.setText("Current Word: " + _currentWord);
    }

    /*
     * This method removes the last character from the currently selected word
     */
    private void removeLetterFromLabel() {
        _currentWordLabel.setText("Current Word: " + _currentWord.substring(0, _currentWord.length() - 1));
        _currentWord = _currentWord.substring(0, _currentWord.length() - 1);
    }

    /*
     * This method simply clears the Currentword label
     */
    private void clearWord() {
        _currentWord = "";
        _currentWordLabel.setText("Current Word: ");
    }

    /*
     * This method determines if the letter that the user clicked on can be added to the word.
     * The letter can only be added if it is adjacent to the last letter that was added.
     */
    private boolean canAdd(int row, int col) {
        //handles the case there is nothing currently selected
        if (_iStack.empty() && _jStack.empty()) {
            return true;
        }
        //Checks if the row and col of the clicked letter is within 1 row/column of the last selected letter
        else if (!(_iStack.peek()-row == 0 && _jStack.peek()-col == 0) && (((_iStack.peek()-row == -1) || (_iStack.peek()-row == 0) || (_iStack.peek()-row == 1)) && ((_jStack.peek()-col == -1) || (_jStack.peek()-col == 0) || (_jStack.peek()-col == 1)))) {
            return true;
        }
        else {
            return false;
        }

    }

    /*
     * This method determines if a clicked letter can be removed. It can only be removed if it was also the last letter
     * added to the string.
     */
    private boolean canRemove (int row, int col) {
        if ((_iStack.peek() == row) && (_jStack.peek() == col)) {
            return true;
        }
        else {
            return false;
        }
    }

    /*
     * This method simply clears all of the selected spaces and clears the stored selected letters
     */
    private void clearLetters() {
        _iStack.clear();
        _jStack.clear();
        this.clearWord();
        this.clearBoolArray();
        //loops through all the rectangles to reset their color to white
        for (Rectangle rectangle : _rectangles) {
            rectangle.setStroke(Color.WHITE);
        }
    }

    /*
     * This method loops through and clears the isClicked boolean array
     */
    private void clearBoolArray() {
        for(int i = 0; i < _size; i++) {
            for (int j = 0; j < _size; j++) {
                _isClicked[i][j] = false;
            }
        }
    }

    /*
     * This method is a getter method that returns the gridPane to the paneOrganizer so it can be added to a root pane
     */
    public GridPane getGrid() {
        return _gridPane;
    }

    /*
     * This method is called when the player wants to rotate the board
     */
    private void rotateGrid() {
        //The entire gridPane can be rotated
        _gridPane.setRotate(_gridPane.getRotate() + Constants.ROTATION_ANGLE);
        //each of the labels within the gridpane must be rotated back 90 degrees so the player can still read them
         for(int i = 0; i < _size*2; i++) {
             for(int j = 0; j < _size; j++) {
                 _gridPane.getChildren().get(i*_size + j).setRotate(_gridPane.getChildren().get(i*_size + j).getRotate() - Constants.ROTATION_ANGLE);
             }
         }
    }

    /*
     * This method is called when the game ends, either from the timer running out or the player manually ending
     * the game
     */
    private void endGame() {
        _statusLabel.setText("Game ending...");
        //Timeline is stopped
        _timeline.stop();
        //boolean is set to true to the key and click handlers and buttons know to not respond to inputs
        _gameOver = true;
        //the postpane is created which displays all the found words
        new PostPane(_userFoundWords, _solver.getWords());
    }

    /*
     * This method sets up the timeline that is used for the timer.
     */
    private void setUpTimeline() {
        //Official boggle game time is set at 2 minutes
        _time = Constants.GAME_TIME_LENGTH;
        KeyFrame kf = new KeyFrame(Duration.seconds(1), new Boggle.TimeHandler());
        _timeline = new Timeline(kf);
        _timeline.setCycleCount(Animation.INDEFINITE);
        _timeline.play();
    }

    /*
     * This is the private inner class that is the timehandler.
     */
    private class TimeHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            //When the timer reaches zero, the game will stop and the endGame method is called.
            if (_time == 0) {
                Boggle.this.endGame();
            }
            else {
                _time = _time - 1;
                _timeLabel.setText("Time remaining: " + _time + " seconds");
            }
            event.consume();
        }
    }
}



