package cs1302.game;

import java.util.Scanner;

/**The purpose of this class is to catch errors with the seed file 
and initiate the MinesweeperGame object which executes the game loop.
*/

public class MinesweeperDriver {

/** The main method exists to execute the MinesweeperGame 
* object which is storied inside the MinesweeperGame class.
* @param args stores the arguments.
*/
    
    public static void main (String[] args) {
    //try {
        if (args.length != 1 ) {
            System.err.println("Usage: MinesweeperDriver SEED_FILE_PATH");
            System.exit(1);
        } // if
        Scanner stdIn = new Scanner(System.in);
        String seedPath = args[0];
        MinesweeperGame game = new MinesweeperGame(seedPath, stdIn);
    } // main
} // MinesweeperDriver
