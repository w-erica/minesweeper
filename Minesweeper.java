/**
 * Execution: java Minesweeper
 * Description: A game of Minesweeper! Reveal everything but the mines. Click the 
 *              top button to reset the game, but only after you've clicked on the 
 *              board at least once. Choose difficulty by hitting a number on your
 *              keyboard. Difficulty changes take effect upon the next reset. Toggle
 *              a flag on a space by hovering and hitting spacebar.
 * Dependencies: Button.java, Space.java, Board.java
 */

import java.util.*;

public class Minesweeper {
    public static void main(String[] args) {
        /* difficulty arrays (first element is amount of rows, second is amount of
         * columns, last is number of mines)
         */
        int[][] difficulties = {{9, 9, 10}, {16, 16, 40}, {16, 30, 99}};
        
        // define a current difficulty and set it to easiest for default
        int[] currDifficulty = new int[3];
        for (int i = 0; i < 3; i++) {
            currDifficulty[i] = difficulties[0][i];
        }
        
        PennDraw.enableAnimation(30);
        
        // draw and start the first board
        Board curr = new Board(currDifficulty);
        curr.start();
        
        while (true) {
            
            // to execute on click:
            if (PennDraw.mousePressed()) {
                int[] location = curr.clickLocation(PennDraw.mouseY(), 
                                                    PennDraw.mouseX());
                if (location != null) {
                    // if the button has been clicked on, make a new board ("reset")
                    if (location[0] == -1) {
                        curr = new Board(currDifficulty);
                        curr.start();
                    }
                    
                    /* if the board has been clicked, carry out the onClick function 
                     * from the Board class
                     */
                     else if (location != null) {
                        curr.onClick(location[0], location[1]);
                    }
                }
            } 
            
            // execute keyboard commands:
            if (PennDraw.hasNextKeyTyped() && !curr.getIsWon() && 
                !curr.getIsLost() && curr.getIsStarted()) {
                char nextKey = PennDraw.nextKeyTyped();
                
                // update difficulties
                if (nextKey == '1') {
                    for (int i = 0; i < 3; i++) {
                        currDifficulty[i] = difficulties[0][i];
                    }
                } else if (nextKey == '2') {
                    for (int i = 0; i < 3; i++) {
                        currDifficulty[i] = difficulties[1][i];
                    }
                } else if (nextKey == '3') {
                    for (int i = 0; i < 3; i++) {
                        currDifficulty[i] = difficulties[2][i];
                    }
                } else if (nextKey == '0') { // easter egg :)
                    curr = new Board();
                    curr.specialStart();
                }
                
                // toggle flagged state
                else if (nextKey == ' ') {
                    int[] location = curr.clickLocation(PennDraw.mouseY(), 
                                                       PennDraw.mouseX());
                    if (location != null) {
                        if (location[0] != -1) {
                            if (!curr.getSpace(location[0], 
                                              location[1]).getIsFlagged()) {
                                curr.getSpace(location[0], location[1]).setFlag();
                            } else if (curr.getSpace(location[0], 
                                              location[1]).getIsFlagged()) {
                                curr.getSpace(location[0], location[1]).removeFlag();
                            }
                        }
                    }
                }
            }
            PennDraw.advance();
        }
    }
}