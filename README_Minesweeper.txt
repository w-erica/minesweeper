====================

Welcome to my Minesweeper game! I hope you like it :)

====================

How to run (after downloading files): 
	    compile with "javac -cp .:junit-platform-console-standalone-1.3.2.jar:cis110.jar *.java"
            type “java Minesweeper” (no quotes) into the command line

How to play: click on all the spaces except for the mines. reset the board by 
             clicking on the button at the top (but you can only do this after 
             clicking on the board at least once).

====================

Additional features:
- Flagging toggle: hover over a space and hit spacebar to flag it if it’s unflagged 
  and unflag it if it’s flagged (flagged spaces cannot be revealed)
- Difficulty select: hit 1, 2, or 3 for beginner, intermediate, and expert 
  respectively. difficulty changes take effect upon the next reset
- An easter egg: hit 0 after your first click on the board

====================

Files and purposes:

Button.java: defines the reset button (which also functions as a visual indicator of 
             win/loss state

Space.java: defines the space object (which holds information about a certain cell 
            on the board)

Board.java: defines the game, including whether it’s been won or lost, each space on 
            the board and its location, etc.

Minesweeper.java: the main method that the game is run from

====================

Written by w-erica on GitHub. Created using the PennDraw GUI interface (see PennDraw.java for details).

====================