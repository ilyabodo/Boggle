# boggle
JavaFX boggle game with solver included

Rules for boggle can be found here:
https://www.fgbradleys.com/rules/Boggle.pdf

Controls:
  - Click on a letter to select it (can only select a letter that is adjacent or diagonally adjacent to last letter selected)
  - Click on the last letter selected to un-select that letter
  - Click "Check Word" to check if currently selected letters create a valid word

Key controls:
 - ESC - Stops the current game and displays results screen
 - SPACE - Rotates the game board 90 degrees
 - Q - Clears all of the currently selected letters

Boggle solver info:
  - Uses a dictionary file as a word list
  - Uses a trie data structure to efficiently find all of the possible words in the game board
