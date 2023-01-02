/**
 * Execution: n/a
 * Description: Defines each cell/space on the board - includes functions for 
 *              updating states and for visualization
 */

public class Space {
    
    private boolean isRevealed;
    private boolean isFlagged;
    private String state; // mine, blank, or a the number of adjacent mines
    private int[] pos; // position on the canvas, in pixels (x is in the first spot)
    
    // constants to help with drawing
    public static int SIZE = 24;
    private static int BUFFER_1 = 4;
    private static int BUFFER_2 = 6;
    private static double RADIUS_1 = 0.004;
    private static double RADIUS_2 = 0.002;
    
    // constructor: initializes everything to default values
    public Space(int y, int x) {
        this.pos = new int[2];
        this.pos[0] = x;
        this.pos[1] = y;
        this.state = "blank";
        this.isRevealed = false;
        this.isFlagged = false;
    }
    
    // getters:
    public boolean getIsRevealed() {
        return this.isRevealed;
    }
    
    public boolean getIsFlagged() {
        return this.isFlagged;
    }
    
    public String getState() {
        return this.state;
    }
    
    public int[] getPos() {
        return this.pos;
    }
    
    // state setters:
    public void setMine() {
        state = "mine";
    }
    
    public void setState(String state) {
        this.state = state;
    }
    
    // flagged setters:
    /**
     * Input: n/a
     * Output: void
     * Description: Sets the space to flagged and draws a flag on it
     */
    public void setFlag() {
        isFlagged = true;
        isRevealed = false;
        drawFlag();
    }
    
    /**
     * Input: n/a
     * Output: void
     * Description: Sets the space to unflagged and removes the flag on it
     */
    public void removeFlag() {
        isFlagged = false;
        isRevealed = false;
        drawUnrevealed();
    }
    
    /**
     * Input: n/a
     * Output: void
     * Description: Sets a space to revealed and draws its state
     */
    public void reveal() {
        if (state.equals("mine")) {
            drawMine();
        } else if (state.equals("blank")) {
            drawBlank();
        } else {
            drawNumber();
        }
        isRevealed = true;
    }
    
    
    // below are all the drawing functions:
    
    /**
     * Input: n/a
     * Output: void
     * Description: Draws a space in its unrevealed state
     */
    public void drawUnrevealed() {
        PennDraw.setPenRadius(RADIUS_2);
        PennDraw.setPenColor(PennDraw.LIGHT_GRAY);
        PennDraw.filledSquare(pos[0], pos[1], SIZE / 2);
        PennDraw.setPenColor(PennDraw.WHITE);
        PennDraw.filledPolygon(pos[0] - SIZE / 2, pos[1] - SIZE / 2, 
                               pos[0] - SIZE / 2, pos[1] + SIZE / 2,
                               pos[0] + SIZE / 2, pos[1] + SIZE / 2);
        PennDraw.setPenColor(PennDraw.DARK_GRAY);
        PennDraw.filledPolygon(pos[0] - SIZE / 2, pos[1] - SIZE / 2, 
                               pos[0] + SIZE / 2, pos[1] - SIZE / 2,
                               pos[0] + SIZE / 2, pos[1] + SIZE / 2);
        PennDraw.setPenColor(PennDraw.LIGHT_GRAY);
        PennDraw.filledSquare(pos[0], pos[1], SIZE / 2 - BUFFER_1);
    }
    
    /**
     * Input: n/a
     * Output: void
     * Description: Draws a flag on an unrevealed space
     */
    private void drawFlag() {
        drawUnrevealed();
        PennDraw.setPenRadius(RADIUS_2);
        PennDraw.setPenColor(PennDraw.BLACK);
        PennDraw.filledRectangle(pos[0], pos[1], SIZE / 16, SIZE / 4);
        PennDraw.filledRectangle(pos[0], pos[1] - SIZE / 4, SIZE / 5, SIZE / 16);
        PennDraw.setPenColor(PennDraw.RED);
        PennDraw.filledPolygon(pos[0], pos[1] - SIZE / 8, pos[0], pos[1] + SIZE / 4, 
                               pos[0] - SIZE / 4, pos[1] + SIZE / 16);
    }
    
    /**
     * Input: n/a
     * Output: void
     * Description: Draws a revealed mine
     */
    private void drawMine() {
        drawBlank();
        PennDraw.setPenColor(PennDraw.BLACK);
        PennDraw.filledCircle(pos[0], pos[1], SIZE / 2 - BUFFER_2);
        PennDraw.setPenRadius(RADIUS_1);
        PennDraw.line(pos[0] - SIZE / 2 + BUFFER_1, pos[1], 
                      pos[0] + SIZE / 2 - BUFFER_1, pos[1]);
        PennDraw.line(pos[0], pos[1] + SIZE / 2 - BUFFER_1, 
                      pos[0], pos[1] - SIZE / 2 + BUFFER_1);
    }
    
    /**
     * Input: n/a
     * Output: void
     * Description: Draws a revealed numbered space
     */
    private void drawNumber() {
        drawBlank();
        PennDraw.setPenRadius(RADIUS_1);
        PennDraw.setPenColor(PennDraw.BLUE);
        PennDraw.setFontBold();
        PennDraw.setFontSize(SIZE - 2 * BUFFER_1);
        PennDraw.text(pos[0], pos[1] - BUFFER_1, state);
    }
    
    /**
     * Input: n/a
     * Output: void
     * Description: Draws a revealed blank space
     */
    private void drawBlank() {
        PennDraw.setPenRadius(RADIUS_2);
        PennDraw.setPenColor(PennDraw.LIGHT_GRAY);
        PennDraw.filledSquare(pos[0], pos[1], SIZE / 2);
        PennDraw.setPenColor(PennDraw.GRAY);
        PennDraw.square(pos[0], pos[1], SIZE / 2);
    }
}