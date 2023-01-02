/**
 * Execution: n/a
 * Description: Defines visuals and dimensions of the button to reset the board (so
 *              it also shows visually the win state)
 */

public class Button {
    
    private int[] pos; // real position on the canvas, in pixels
    public static int SIZE = 32; // side length of the button
    
    // constants to help with drawing:
    private static int BUFFER_1 = 3;
    private static int BUFFER_2 = 5;
    private static double RADIUS_1 = 0.004;
    private static double RADIUS_2 = 0.002;
    
    // constructor: sets a button at the location specified 
    public Button(int x, int y) {
        this.pos = new int[2];
        this.pos[0] = x;
        this.pos[1] = y;
    }
    
    /**
     * Input: n/a
     * Output: void 
     * Description: Draws the button with sunglasses, to be called upon win
     */
    public void drawWinButton() {
        drawBaseButton();
        PennDraw.setPenRadius(RADIUS_1);
        PennDraw.filledRectangle(pos[0] - SIZE / 8, pos[1] + SIZE / 12, SIZE / 10, 
                                 SIZE / 12);
        PennDraw.filledRectangle(pos[0] + SIZE / 8, pos[1] + SIZE / 12, SIZE / 10, 
                                 SIZE / 12);
        PennDraw.setPenRadius(RADIUS_2);
        PennDraw.line(pos[0] - SIZE / 8, pos[1] + SIZE / 12, pos[0] + SIZE / 8, 
                      pos[1] + SIZE / 12);
        PennDraw.arc(pos[0], pos[1] - SIZE / 12, SIZE / 8, 180, 350);
    }
    
    /**
     * Input: n/a
     * Output: void 
     * Description: Draws the button deceased, to be called upon a loss
     */
    public void drawLoseButton() {
        drawBaseButton();
        PennDraw.setPenRadius(RADIUS_2);
        PennDraw.setFontSize(SIZE / 4);
        PennDraw.text(pos[0] - SIZE / 6, pos[1], "X");
        PennDraw.text(pos[0] + SIZE / 6, pos[1], "X");
        PennDraw.arc(pos[0], pos[1] - SIZE / 4, SIZE / 8, 10, 170);
    }
    
    /**
     * Input: n/a
     * Output: void 
     * Description: Draws the button with a smiley, to be used when there is no 
     *              loss or win
     */
    public void drawButton() {
        drawBaseButton();
        PennDraw.setPenRadius(RADIUS_1);
        PennDraw.point(pos[0] - SIZE / 6, pos[1] + SIZE / 12);
        PennDraw.point(pos[0] + SIZE / 6, pos[1] + SIZE / 12);
        PennDraw.setPenRadius(RADIUS_2);
        PennDraw.arc(pos[0], pos[1] - SIZE / 12, SIZE / 8, 190, 350);
    }
    
    /**
     * Input: n/a
     * Output: void 
     * Description: Helper function that draws a base for the other drawbutton
     *              functions
     */
    private void drawBaseButton() {
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
        PennDraw.setPenColor(PennDraw.YELLOW);
        PennDraw.filledCircle(pos[0], pos[1], SIZE / 2 - BUFFER_2);
        PennDraw.setPenColor(PennDraw.BLACK);
        PennDraw.setPenRadius(RADIUS_2);
        PennDraw.circle(pos[0], pos[1], SIZE / 2 - BUFFER_2);
    }
    
}