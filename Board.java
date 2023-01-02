/**
 * Execution: n/a
 * Description: Defines a board for the game to be played on, and functions for 
 *              updating it
 * Dependencies: Button.java, Space.java
 */

import java.util.*;

public class Board {
    
    private Space[][] board; // game board
    private int[][] minePositions;
    private int mineAmount;
    private Button button; // button for resetting (also a visual for win/loss state)
    
    // booleans regarding the state of the board
    private boolean isWon;
    private boolean isLost;
    private boolean isStarted;
    
    // dimensions
    private int xLength; // total board length in the x direction in pixels
    private int yLength; // total board length in the y direction in pixels
    private int rows; // amount of rows in the board (amount of spaces vertically)
    private int cols; // amount of columns (amount of spaces horizontally)
    
    // constants to help draw the board
    private static int BORDER = 12;
    private static int BUFFER = 4;
    
    /* constructor: initializes size, mine amounts, and dependent attributes based
     *              on the difficulty (input as an int array). also sets the 
     *              positions (in pixels) of the spaces on the canvas
     */
    public Board(int[] diff) {
        this.rows = diff[0];
        this.cols = diff[1];
        xLength = cols * Space.SIZE + BORDER * 2;
        yLength = rows * Space.SIZE + BORDER * 3 + Button.SIZE;
        board = new Space[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = new Space(BORDER + Space.SIZE / 2 + i * Space.SIZE, 
                                       BORDER + Space.SIZE / 2 + j * Space.SIZE);
            }
        }
        minePositions = new int[diff[2]][2];
        mineAmount = diff[2];
        button = new Button(xLength / 2, yLength - BORDER - Button.SIZE / 2);
        isWon = false;
        isLost = false;
        isStarted = false;
    }
    
    /**
     * Input: n/a
     * Output: void
     * Description: To be run when a new board is started: Waits for the first click,
     *              then generates mine locations accordingly and updates the states
     *              of spaces (i.e. if they become numbered). Also reveals spaces
     *              as a result of the first click.
     */
    public void start() {
        drawStarterBoard();
        PennDraw.advance();
        
        // waits for a first click in the valid area and then stores locations
        double[] firstClickPos = new double[2]; // stores PennDraw.mouseX,Y() values
        boolean hasFirstValidClicked = false;
        int[] firstClickLocation = new int[2]; // stores which space was clicked on
        while (!hasFirstValidClicked) {
            if (PennDraw.mousePressed() &&
                PennDraw.mouseY() < yLength - BUFFER * 2 - Button.SIZE) {
                firstClickPos[1] = PennDraw.mouseX();
                firstClickPos[0] = PennDraw.mouseY();
            }
            if (clickLocation(firstClickPos[0], firstClickPos[1]) != null) {
                firstClickLocation[0] = clickLocation(firstClickPos[0], 
                                                      firstClickPos[1])[0];
                firstClickLocation[1] = clickLocation(firstClickPos[0], 
                                                      firstClickPos[1])[1];
                if (firstClickLocation[0] != -1) {
                    hasFirstValidClicked = true;
                }
            }
        }
        
        // generate mine positions, avoiding area around first click
        ArrayList<int[]> invalidMineSpaces = adjacents(firstClickLocation[0], 
                                                       firstClickLocation[1]);
        invalidMineSpaces.add(firstClickLocation);
        int minesAdded = 0;
        while (minesAdded < mineAmount) {
            int[] minePosAttempt = {(int) (Math.random() * rows), 
                                    (int) (Math.random() * cols)};
            boolean isPosValid = true;
            for (int j = 0; j < invalidMineSpaces.size() && isPosValid; j++) {
                if (minePosAttempt[0] == invalidMineSpaces.get(j)[0] && 
                    minePosAttempt[1] == invalidMineSpaces.get(j)[1]) {
                    isPosValid = false;
                }
            }
            if (isPosValid) {
                minePositions[minesAdded] = minePosAttempt;
                invalidMineSpaces.add(minePosAttempt);
                minesAdded++;
            }
        }
        
        // set mines
        for (int i = 0; i < mineAmount; i++) {
            board[minePositions[i][0]][minePositions[i][1]].setMine();
        }
        
        // update numbered spaces
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!board[i][j].getState().equals("mine")) {
                    int mineCount = 0;
                    ArrayList<int[]> adj = adjacents(i, j);
                    for (int k = 0; k < adj.size(); k++) {
                        int[] curr = adj.get(k);
                        if (board[curr[0]][curr[1]].getState().equals("mine")) {
                            mineCount++;
                        }
                    }
                    if (mineCount != 0) {
                        board[i][j].setState("" + mineCount);
                    }
                }
            }
        }
        
        // reveal adjacent spaces around the first click
        propagateBlank(firstClickLocation[0], firstClickLocation[1]);
        
        isStarted = true;
    }

    /**
     * Input: positions of a click location on the board, as returned by the 
     *        clickLocation function
     * Output: void
     * Description: Updates the board according to a click on the board:
     *              - if a mine is clicked on, reveals it, sets the state to loss,
     *                and updates the reset button
     *              - if a numbered space is clicked on, reveal it
     *              - if a blank space is clicked on, reveal it as well as 
     *                adjacent spaces according to the propagateBlank function
     *              - finally, if the win conditions are met, update the board as 
     *                won and update the reset button accordingly
     */
    public void onClick(int y, int x) {
        if (isLost || isWon) { // do nothing if the game's already been won
            return;
        } if (board[y][x].getIsFlagged()) { // do nothing if the space is flagged
            return;
        } if (board[y][x].getState().equals("mine")) { // you lose if you hit a mine
            board[y][x].reveal();
            isLost = true;
            // reveals all mine locations upon defeat
            for (int i = 0; i < mineAmount; i++) {
                board[minePositions[i][0]][minePositions[i][1]].reveal();
            }
            button.drawLoseButton();
            return;
        } if (board[y][x].getIsRevealed()) { // do nothing if it's already revealed
            return;
        } if (!(board[y][x].getState().equals("blank"))) { // just reveal if numbered
            board[y][x].reveal();
            isWon();
            return;
        } else { // reveal itself and adjacent spaces if it's blank
            propagateBlank(y, x); 
            isWon();
            return;
        }
    }
    
    /**
     * Input: n/a
     * Output: void
     * Description: Checks if the game win condition is met, updates isWon 
     *              accordingly and draws the button in its "won" state if needed
     */
    private void isWon() {
        boolean win = true;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!(board[i][j].getIsRevealed() || 
                      board[i][j].getState().equals("mine"))) {
                    win = false;
                }
            }
        }
        isWon = win;
        if (win) {
            button.drawWinButton();
        }
    }
    
    /**
     * Input: Click position as measured by PennDraw.mouseX() and mouseY()
     * Output: Click location as an int array denoting position on the board OR 
     *         {-1, -1}, denoting a click on the button OR NULL, denoting a click
     *         not in the valid area 
     * Description: Converts click position on the canvas to a more usable form
     */
    public int[] clickLocation(double yPos, double xPos) {
        int[] clickConvert = new int[2];
        int x = (int) xPos;
        int y = (int) yPos;
        if (isOnField(x, y)) {
            clickConvert[1] = (x - BORDER) / Space.SIZE;
            clickConvert[0] = (y - BORDER) / Space.SIZE;
        } else if (isOnButton(x, y)) {
            clickConvert[1] = -1;
            clickConvert[0] = -1;
        } else {
            clickConvert = null;
        }
        return clickConvert;
    }
    
    /**
     * Input: Position as integers
     * Output: Whether the position is on the playing board
     * Description: Checks whether a position is on the playing board
     */
    private boolean isOnField(int x, int y) {
        return x > BORDER && x < xLength - BORDER && y > BORDER && 
                y < yLength - 2 * BORDER - Button.SIZE;
    }
    
    /**
     * Input: Position as integers
     * Output: Whether the position is on the reset button
     * Description: Checks whether a position is on the reset button
     */
    private boolean isOnButton(int x, int y) {
        return x > xLength / 2 - Button.SIZE / 2 && 
                x < xLength / 2 + Button.SIZE / 2 && 
                y > yLength - BORDER - Button.SIZE && 
                y < yLength - BORDER;
    }
    
    /**
     * Input: n/a
     * Output: void
     * Description: Draws the starter board, including all the unrevealed spaces
     */
    private void drawStarterBoard() {
        PennDraw.setCanvasSize(xLength, yLength);
        PennDraw.setXscale(0, xLength);
        PennDraw.setYscale(0, yLength);
        PennDraw.setPenRadius(0.002);
        PennDraw.setPenColor(PennDraw.WHITE);
        PennDraw.filledPolygon(0, 0, 0, yLength, xLength, yLength);
        PennDraw.setPenColor(PennDraw.DARK_GRAY);
        PennDraw.filledPolygon(0, 0, xLength, 0, xLength, yLength);
        PennDraw.setPenColor(PennDraw.LIGHT_GRAY);
        PennDraw.filledRectangle(xLength / 2, yLength / 2, xLength / 2 - BUFFER, 
                                yLength / 2 - BUFFER);
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j].drawUnrevealed();
            }
        }
        button.drawButton();
        PennDraw.setFontSize(10);
        PennDraw.text(xLength / 4, yLength - BUFFER - Button.SIZE / 2, 
                      "Hover + space");
        PennDraw.text(xLength / 4, yLength - BUFFER * 4 - Button.SIZE / 2, 
                      " = toggle flag!");
        PennDraw.text(xLength / 4, yLength - BUFFER * 7 - Button.SIZE / 2, 
                      "Click to reset ->");
        PennDraw.text(xLength * 3 / 4, yLength - BUFFER - Button.SIZE / 2, 
                      "Press 1, 2, or 3");
        PennDraw.text(xLength * 3 / 4, yLength - BUFFER * 4 - Button.SIZE / 2, 
                      "to select");
        PennDraw.text(xLength * 3 / 4, yLength - BUFFER * 7 - Button.SIZE / 2, 
                      "difficulty!");
    }
    
    /**
     * Input: Position of a blank space as integers
     * Output: void
     * Description: Reveals a contiguous area of blank spaces, containing the input 
     *              space, bordered by numbered spaces 
     */
    private void propagateBlank(int y, int x) {
        int[] original = {y, x};
        
        // stores all spaces that have been affected
        ArrayList<int[]> affected = new ArrayList<>();
        
        // stores blank spaces that have been affected
        ArrayList<int[]> affectedBlank = new ArrayList<>();
        
        /* checks spaces adjacent to every affected blank space, appending them to
         * the necessary lists, until all of the affected blank spaces have been 
         * checked
        */
        boolean allChecked = false;
        int blankIdx = 0;
        affected.add(original);
        affectedBlank.add(original);
        while (!allChecked) {
            ArrayList<int[]> adj = adjacents(affectedBlank.get(blankIdx)[0], 
                                             affectedBlank.get(blankIdx)[1]);
            for (int i = 0; i < adj.size(); i++) {
                boolean inAlready = false;
                for (int j = 0; j < affected.size() && !inAlready; j++) {
                    if (affected.get(j)[0] == adj.get(i)[0] && 
                        affected.get(j)[1] == adj.get(i)[1]) {
                        inAlready = true;
                    }
                }
                if (!inAlready) {
                    int[] toAdd = new int[2];
                    toAdd[0] = adj.get(i)[0];
                    toAdd[1] = adj.get(i)[1];
                    affected.add(toAdd);
                    if (board[adj.get(i)[0]][adj.get(i)[1]].getState()
                        .equals("blank")) {
                        affectedBlank.add(toAdd);
                    }
                }
            }
            blankIdx++;
            if (blankIdx == affectedBlank.size()) {
                allChecked = true;
            }
        }
        
        // reveal all affected spaces
        for (int i = 0; i < affected.size(); i++) {
            board[affected.get(i)[0]][affected.get(i)[1]].reveal();
        }
    }
    
    /**
     * Input: Position of a space as integers
     * Output: ArrayList of positions of adjacent spaces to the input space
     * Description: Returns the positions of spaces that are both on the game board
     *              and adjacent to the input space
     */
    private ArrayList<int[]> adjacents(int yIn, int xIn) {
        ArrayList<int[]> output = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (xIn + i >= 0 && xIn + i < cols && yIn + j >= 0 && 
                    yIn + j < rows && !((i == 0) && (j == 0))) {
                    
                    int[] adj = new int[2];
                    adj[0] = yIn + j;
                    adj[1] = xIn + i;
                    output.add(adj);
                }
            }
        }
        return output; 
    }
    
    // getters
    public boolean getIsWon() {
        return isWon;
    }
    
    public boolean getIsLost() {
        return isLost;
    }
    
    public boolean getIsStarted() {
        return isStarted;
    }
    
    public Space getSpace(int y, int x) {
        return board[y][x];
    }
    
    // ** Everything below is for a little easter egg **
    
    // overloaded constructor: initializes the easter egg board
    public Board() {
        this.rows = 9;
        this.cols = 9;
        xLength = cols * Space.SIZE + BORDER * 2;
        yLength = rows * Space.SIZE + BORDER * 3 + Button.SIZE;
        board = new Space[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = new Space(BORDER + Space.SIZE / 2 + i * Space.SIZE, 
                                       BORDER + Space.SIZE / 2 + j * Space.SIZE);
            }
        }
        int[][] shape = {{4, 1}, {5, 1}, {6, 2}, {6, 3}, {5, 4}, {6, 5}, {6, 6}, 
                         {5, 7}, {4, 7}, {3, 2}, {2, 3}, {1, 4}, {2, 5}, {3, 6}};
        minePositions = new int[14][2];
        for (int i = 0; i < shape.length; i++) {
            minePositions[i] = shape[i];
        }
        mineAmount = 14;
        button = new Button(xLength / 2, yLength - BORDER - Button.SIZE / 2);
        isWon = false;
        isLost = false;
        isStarted = false;
    }
    
    /**
     * Input: n/a
     * Output: void
     * Description: Starter function for the easter egg: draws the board and sets
     *              the mines
     */
    public void specialStart() {
        drawStarterBoard();
        PennDraw.advance();
        
        for (int i = 0; i < mineAmount; i++) {
            board[minePositions[i][0]][minePositions[i][1]].setMine();
        }
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!board[i][j].getState().equals("mine")) {
                    int mineCount = 0;
                    ArrayList<int[]> adj = adjacents(i, j);
                    for (int k = 0; k < adj.size(); k++) {
                        int[] curr = adj.get(k);
                        if (board[curr[0]][curr[1]].getState().equals("mine")) {
                            mineCount++;
                        }
                    }
                    if (mineCount != 0) {
                        board[i][j].setState("" + mineCount);
                    }
                }
            }
        }
        isStarted = true;
    }
}