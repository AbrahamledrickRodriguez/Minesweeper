package cs1302.game;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;

/** The purpose of this class is to make different methods that are able to proform the 
 * fuctions needed to run minesweeperand put those methods into a loop that can be repeated 
 * and print out different results for a win and a loss and 
 * keep track of the score. Additionally this loop will be put into a constructor object 
 * which will be called in the main method of the driver file.
 */
 
public class MinesweeperGame {
    private int row;
    private int col;
    private int urow;
    private int ucol;
    private int numMines;
    private String[][] board;
    private boolean[][] mineSquare;
    private boolean gameInProgress;
    private double userScore;
    private int roundCount = 0;
    private int minesFlagged;
    private int spacesRevealed;
    private int mine;
    private String command;
    private final Scanner stdIn;
    private String seedPath;
    private String fullCommand;

    /** The purpose of the MinesweeperGame constructor method is to 
     * encapsilate the contents of gameLoop into one
     * onject so they can be easily called in the driver file and 
     * executed. We want to be able to seperate the user command 
     * switch statement from he end game if else statment. 
     * @param seedPath has the values need to generate the board.
     * @param stdIn takes in the standard input from the user and seed.in.
     */
     
    public MinesweeperGame(String seedPath, Scanner stdIn) {
        this.stdIn = stdIn;
        this.seedPath = seedPath;
        play();
    }
    
    /** The function of the gameLoop is to take the different user options 
    * supplied by the different methods and loop them until the game ends.
     */
     
    private void play() {
        // this method loops the relevant methods to make the game work
        fillSeed();
        fillBoard();
        printWelcome();
        printMineField(); 
        while (gameInProgress = true) {
            promptUser();
         
         
            if (this.command.equals ("r") || this.command.equals ("reveal")) {
                revealSquare();
                roundCount++;
                printMineField();
                isWon();
                // must increase round count becuase some methods dont take a round
            } else if (this.command.equals ("g") || this.command.equals ("guess")) {
                potentialMine();
                roundCount++;
                printMineField();
            } else if (this.command.equals ("m") || this.command.equals ("mark")) {
                markMine();
                roundCount++;
                isWon();
                printMineField();
            } else if (this.command.equals ("h") || this.command.equals ("help")) {
                helpUser();
                roundCount++;
                printMineField();
            } else if (this.command.equals ("q") || this.command.equals ("quit")) {
                quitGame();
            } else if (this.command.equals ("nofog")) {
                nofog();
            } else {
                System.err.println("Invalid Command: <The command you entered isn't an action>"); 
            } // if-else
        } // while
    } // play
    
    /** The point of this method is to put the contents of the seed file into 
    * another file and have them set equal to various varaibles that will be used throughout
    * the class.
    */
    
    public void fillSeed() { // might need to call this in CHECK
        try {
            File seedInput = new File(seedPath);
            Scanner seedScanner = new Scanner(seedInput);
            this.row = seedScanner.nextInt();
            this.col = seedScanner.nextInt(); 
            this.numMines = seedScanner.nextInt();
 
            if (numMines < 1 || numMines > (row * col) - 1) {
                throw new ArrayIndexOutOfBoundsException();
            }
            // this fills the seed up and makes sure the seedfile is in bounds
            if (row < 5 || col < 5 || row > 10 || col > 10) {
                throw new ArrayIndexOutOfBoundsException();
            } // if
            board = new String[row][col];
            mineSquare = new boolean[row][col];
            for (int i = 0; row > i ; i++) {      
                for (int j = 0; col > j; j++) {
                    this.mineSquare[i][j] = false;
                } // for   
            } // for
            for (mine = 0; mine < numMines; mine++) {
                int x = seedScanner.nextInt();
                int y = seedScanner.nextInt();
                mineSquare[x][y] = true;
            } // for
        } catch (FileNotFoundException fnfe) { 
            System.err.println("Seed File Not Found Error: " + fnfe.getMessage());
            System.exit(2);
        } catch (ArrayIndexOutOfBoundsException tbts) {
            System.err.println("Seed File Malformed Error: " + tbts.getMessage());
            System.exit(3);
        } // try
    } // fillSeed

/** The point of this method is to fill out the starting board and have it 
* ready to be printed.
*/

    public void fillBoard() { //CHECK
        for (int i = 0; row > i ; i++) {   
           
            for (int j = 0; col > j; j++) {
                board[i][j] = "|   ";
            } // for
            board[i][col - 1] = "|   |";    
        } // for
    } //fillBoard

/** The point of this method is to print out a new board everytime the method is called.
* The board will mostly be called at the start of a new round adn after a valid user command. 
*/

    public void printMineField() {
        System.out.println("Rounds Completed: " + roundCount + "\n");
        for (int i = 0; row > i ; i++) {
            System.out.print(i); 
            for (int j = 0; col > j; j++) {
                System.out.print(board[i][j]);
            } // for
            System.out.println("");     
        } // for
        System.out.print("  ");
        for (int n = 0; col > n; n++) {
            System.out.print(" " + n + "  ");
        } //for
          // it is important to call this method in seperatly becuase some commands are temporary
    } // printMineField

/** This method prompts users for input and takes in the whole line and parses it out
* so other methods can use it read in instructions.
*/

    public void promptUser() { // CHECK but may have to take stdIn out of while loop
        try {
            System.out.println ("");
            System.out.print ("minesweeper-alpha: ");
            this.fullCommand = stdIn.nextLine();
            if (this.fullCommand == null) {
                throw new NoSuchElementException();
            }
            Scanner commandScan = new Scanner(this.fullCommand);
            this.command = commandScan.next(); 
        } catch (NoSuchElementException pu) {
            System.err.println("Seed File Malformed Error: " + pu.getMessage());
            System.exit(3);
        }
    }

/** This method prints out the welcome screen before the while loop in the play method.
*/

    private void printWelcome() { 
        try {
            File welcome = new File("resources/welcome.txt");
            Scanner welcomeScan = new Scanner(welcome);
            while (welcomeScan.hasNextLine()) {
                System.out.println(welcomeScan.nextLine());
            }
        } catch (FileNotFoundException ws) {
            System.err.print("File Not Found Error: " + ws.getMessage());
        }
    } // printWelcome

/** this method keeps track of the users score thoughout the game and is
* called at the end of the game if they will. The score is printed with the win screen.
*/

    private void totalScore() {
        this.userScore = 100.00 * this.row * this.col / this.roundCount;
    } // totalScore

/** This method is called when all the conditions for a win have been met
* and it prints out the win screen along with the users score.
*/

    private void printWin() {
        String winning = "\n";
        try {
            System.out.println();
            File win = new File("resources/gamewon.txt");
            Scanner winScan = new Scanner(win);
            while (winScan.hasNextLine()) {
                winning += winScan.nextLine();
                if (winScan.hasNextLine()) {
                    winning += "\n";
                } else {
                    String score = String.format ("%.2f", this.userScore);
                    winning += " " + score;
                }
            } //while
        } catch (FileNotFoundException fnfe1) {
            System.err.print("File Not Found Error: " + fnfe1.getMessage());
        } //catch
        System.out.print(winning + "\n");
        System.exit(0);
    } // printWin

   /** This method is called at the end of each round in order to check if the user has won yet
   * and it wont be true until all the squares without mines are revealed and 
   * all the squares with a mine are flagged.
   * @return returns true if the game is won and false if it isnt.
   */
   
    private boolean isWon() {
        totalScore();
        if (minesFlagged == numMines && spacesRevealed == (row * col) - numMines) {
            printWin();
            return true;
        } else {
            return false;
        } // if 
    } // isWon

     /** This method is called by the revealSquare method when the user
     * reveals a mine and it prints out the loss screen and exits the game.
     */

    private void printLoss() {
        try {
            File loss = new File("resources/gameover.txt");
            Scanner lossScan = new Scanner(loss);
            while (lossScan.hasNextLine()) {
                System.out.println(lossScan.nextLine());
            }
        } catch (FileNotFoundException fnfe2) {
            System.err.print("File Not Found Error: " + fnfe2.getMessage());
        }
        System.exit(0);
    } // printLoss

    /** This method makes sure that the user input is within the dimentions of the array.
    * @return makes isInbounds true if the row and col's size is between 5 and 10.
    */

    private boolean isInBounds() {
        try {
            if (0 <= urow && urow < row && 0 <= ucol && ucol < col) {
                return true;
            } else { 
                throw new ArrayIndexOutOfBoundsException();
            }
        } catch (ArrayIndexOutOfBoundsException ioob) {
            System.err.print("Invalid Command:" + ioob.getMessage()); 
            return false;
        }
        
    } // isInBounds
    
    /** this method is called in the revealSquare method to scan the mines
    * that are within a one square radius of the mine being revealed in the
    * revealSquare method.
    * @return reports the total number of mines in proximity to the
    * stop that the user chose.
    */

    private int getNumAdjMines() {
        int getNumAdjMines = 0;
        if (urow - 1 < 0 || ucol - 1 < 0) {
            ;
        } else {
            if (mineSquare[urow - 1][ucol - 1] == true) {
                getNumAdjMines++;
            }
        }
        if (ucol - 1 < 0) {
            ;
        } else {
            if (mineSquare[urow][ucol - 1] == true) {
                getNumAdjMines++;
            } 
        }
        if (urow + 1 >= row || ucol - 1 < 0) {
            ;
        } else {
            if (mineSquare[urow + 1][ucol - 1] == true) {
                getNumAdjMines++;
            } 
        }
        if (urow - 1 < 0) {
            ;
        } else {
            if (mineSquare[urow - 1][ucol] == true) {
                getNumAdjMines++;
            } 
        }
        if (urow + 1 >= row) {
           ;
        } else {
            if (mineSquare[urow + 1][ucol] == true) {
                getNumAdjMines++;
            } 
        }
        if (urow - 1 < 0 || ucol + 1 >= col) {
           ;
        } else {
            if (mineSquare[urow - 1][ucol + 1] == true) {
                getNumAdjMines++;
            }
        } 
        if (ucol + 1 >= col) {
            ;
        } else {
            if (mineSquare[urow][ucol + 1] == true) {
                getNumAdjMines++;
            } 
        } 
        if (urow + 1 >= row || ucol + 1 >= col) {
            ;
        } else {
            if (mineSquare[urow + 1][ucol + 1] == true) {  
                getNumAdjMines++;
            } 
        }
        return getNumAdjMines++;
    } // getNumAdjMines
    
    /** the revealSquare method is called when the user inputs 'r' or 'reveal'
    * and it either ends the game if a mine is reveal or it stores the number 
    * of adjacent mines on the the square of the array that the user reveal. 
    * If the latter happened then it increases the number of spacesRevealed.
    */
    
    private void revealSquare() { //is in bounds do exceptions
        try {
            Scanner revealScan = new Scanner(this.fullCommand);
            revealScan.next();
            urow = revealScan.nextInt();
            ucol = revealScan.nextInt();
            if (!isInBounds()) {
                throw new ArrayIndexOutOfBoundsException();
            }
            if (mineSquare[urow][ucol] == true) {
                printLoss();
            } else { 
                board[urow][ucol] = "| " + getNumAdjMines() + " ";
            }
            spacesRevealed++;
        } catch (ArrayIndexOutOfBoundsException rs) {
            System.err.println("Invalid Command: " + rs.getMessage()); 
        } // catch
    } // revealSquare

    /** the potentialMine method or the guess command assings a '?' to the
    * space of a users choosing if they in put 'g' or 'guess' and it subtracks
    * from foundMines if the user guess a previous marked mine.
     */

    private void potentialMine() {
        try {
            Scanner potentialM = new Scanner(this.fullCommand);
            potentialM.next();
            urow = potentialM.nextInt();
            ucol = potentialM.nextInt();
            if (!isInBounds()) {
                throw new ArrayIndexOutOfBoundsException();
            }
            if (mineSquare[urow][ucol] == true && board[urow][ucol].equals("| F |")) {
                minesFlagged--;
                board [urow][ucol] = "| ? ";
            } else {
                board [urow][ucol] = "| ? ";
            } 
        } catch (ArrayIndexOutOfBoundsException pm) {
            System.err.println("Invalid Command: " + pm.getMessage()); 
        } // catch
    } // potentialMine

    /** the markMine method is called when the user inputs 'm' or 'mark' and it assign the 
    * character 'F' to a space of the users choice and if that space has a mine, the varaible
    * minesFlagged increses.
    */

    private void markMine() {
        try {
            Scanner markM = new Scanner(this.fullCommand);
            markM.next();
            urow = markM.nextInt();
            ucol = markM.nextInt();
            if (!isInBounds()) {
                throw new ArrayIndexOutOfBoundsException();
            }
            if (mineSquare[urow][ucol] = true) {
                board[urow][ucol] = "| F ";
                minesFlagged++;
            } else {
                board[urow][ucol]  = "| F ";
            }
        } catch (ArrayIndexOutOfBoundsException mm) {
            System.err.println("Invalid Command: " + mm.getMessage()); 
        } // catch
    } // markMine

    /** The nofog method acts as a cheat code for the user and it 
    * assigns '< >' to each square containing a mine. This method is 
   * called when the user inputs 'nofog' into the game prompt. 
     */

    private void nofog() {
        System.out.println("Rounds Completed: " + roundCount + "\n");
        for (int a = 0; a < col; a++) {
            System.out.print(a + "");
            for (int b = 0; b < row; b++) {
                if (mineSquare[a][b] == true) { 
                    System.out.print("|<" + board[a][b].substring(1, 2) + ">");
                } else { 
                    System.out.print(board[a][b]);
                } 
                if (b < row - 1) {
                    System.out.print("");
                }     
            }
            System.out.println("");
        } 
        System.out.print("   ");
        for (int c = 0; c < board[0].length; c++) {
            System.out.print(c + "   ");
        }    
        System.out.println();
        roundCount --;
    } // nofog

    /** The point of this method is to print the help screen for the user
    * if they input 'h' or 'help' and the help screen reminds the user
    * of the actions they can make. The roundCount increases if method is called.
      */

    private void helpUser() {
        System.out.println(
            "Commands Available...");
        System.out.println("- Reveal: r/reveal row col");
        System.out.println("-   Mark: m/mark   row col");
        System.out.println("-  Guess: g/guess  row col");
        System.out.println("-   Help: h/help");
        System.out.println("-   Quit: q/quit");
    } // helpUser

    /** The point of this method is to quit out of the current game
    * and have the program gracefully as per the users command.
    */ 

    private void quitGame() {
        gameInProgress = false;
        System.out.println("Quitting the game...");
        System.out.println("Bye!");
        System.exit(0);
    } // quitGame
} // MineSweeperGame