package server;
import java.util.Scanner;
import java.util.*; 
import java.io.*;

/**
 * Class: Game 
 * Description: Game class that can load an ascii image
 * Class can be used to hold the persistent state for a game for different threads
 * synchronization is not taken care of .
 * You can change this Class in any way you like or decide to not use it at all
 * I used this class in my SockBaseServer to create a new game and keep track of the current image evenon differnt threads. 
 * My threads each get a reference to this Game
 */

public class Game {
    private char[][] original; // the original image
    private char[][] hidden; // the hidden image
    private int col; // columns in original, approx
    private int row; // rows in original and hidden
    private boolean won; // if the game is won or not
    private List<String> files = new ArrayList<String>(); // list of files, each file has one image


    public Game(){
        // you can of course add more or change this setup completely. You are totally free to also use just Strings in your Server class instead of this class
        won = true; // setting it to true, since then in newGame() a new image will be created
        files.add("board1.txt");
        files.add("board2.txt");
        files.add("board3.txt");
        files.add("board4.txt");
        files.add("board5.txt");
        files.add("board6.txt");
    }

    /**
     * Sets the won flag to true
     * @param args Unused.
     * @return Nothing.
     */
    public void setWon(){
        won = true;
    }

    public boolean getWon() {
        return won;
    }

    /**
     * Method loads in a new image from the specified files and creates the hidden image for it. 
     * @return Nothing.
     */
    public void newGame(){
        if (won) {
            won = false; 
            List<String> rows = new ArrayList<String>();

            try{
                // loads one random image from list
                Random rand = new Random(); 
                col = 0;
                int randInt = rand.nextInt(files.size());
                File file = new File(
                        Game.class.getResource("/"+files.get(randInt)).getFile()
                        );
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    if (col < line.length()) {
                        col = line.length();
                    }
                    rows.add(line);
                }
            }
            catch (Exception e){
                System.out.println("File load error"); // extremely simple error handling, you can do better if you like. 
            }

            // this handles creating the orinal array and the hidden array in the correct size
            String[] rowsASCII = rows.toArray(new String[0]);

            row = rowsASCII.length;

            // Generate original array by splitting each row in the original array.
            original = new char[row][col];
            for(int i = 0; i < row; i++) {
                char[] splitRow = rowsASCII[i].toCharArray();
                for (int j = 0; j < splitRow.length; j++) {
                    original[i][j] = splitRow[j];
                }
            }

            // Generate Hidden array with X's (this is the minimal size for columns)
            hidden = new char[row][col];
            for(int i = 0; i < row; i++){
                for(int j = 0; j < col; j++){
                    if(i == 0 || j < 2)
                        hidden[i][j] = original[i][j];
                    else if(original[i][j] != '|')
                        hidden[i][j] = '?';
                    else
                        hidden[i][j] = '|';
                }
            }
        }
        else {
        }
    }

    /**
     * Method returns original board as String
     * @return String of original board
     */
    public String showBoard() {
        StringBuilder sb = new StringBuilder();
        for (char[] subArray : original) {
            sb.append(subArray);
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Method returns the String of the current hidden image
     * @return String of the current hidden board
     */
    public String getBoard(){
        StringBuilder sb = new StringBuilder();
        for (char[] subArray : hidden) {
            sb.append(subArray);
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Shows the two chosen tiles and then hides them again. Returns the board with one showing.
     * @param tile1row
     * @param tile1col
     * @param tile2row
     * @param tile2col
     * @return String of board with the 1 tile showing
     */
    public String tempFlipWrongTiles(int tile1row, int tile1col) {
        hidden[tile1row][tile1col] = original[tile1row][tile1col];
        StringBuilder sb = new StringBuilder();
        for (char[] subArray : hidden) {
            sb.append(subArray);
            sb.append("\n");
        }
        hidden[tile1row][tile1col] = '?';
        return sb.toString();
    }

        /**
     * Shows the two chosen tiles and then hides them again. Returns the board with them showing.
     * @param tile1row
     * @param tile1col
     * @param tile2row
     * @param tile2col
     * @return String of board with the 2 tile showing
     */
    public String tempFlipWrongTiles(int tile1row, int tile1col, int tile2row, int tile2col) {
        hidden[tile1row][tile1col] = original[tile1row][tile1col];
        hidden[tile2row][tile2col] = original[tile2row][tile2col];
        StringBuilder sb = new StringBuilder();
        for (char[] subArray : hidden) {
            sb.append(subArray);
            sb.append("\n");
        }
        hidden[tile1row][tile1col] = '?';
        hidden[tile2row][tile2col] = '?';
        return sb.toString();
    }

    /**
     *
     * @param row
     * @param col
     * @return character at selected row and column
     */
    public synchronized char getTile(int row, int col) {
        if (row < original.length && col < original[0].length)
            return original[row][col];
        else
            return '?';
    }

    /**
     * Method that replaces a character, needed when a match was found, would need to be called twice
     * You can of course also change it to get to rows and two cols if you like
     * @return String of the current hidden board
     */
    public synchronized String replaceOneCharacter(int rowNumber, int colNumber) {
        hidden[rowNumber][colNumber] = original[rowNumber][colNumber];
        return(getBoard());
    }

    public void checkWin() {
        boolean equal = true;
        for (int r = 0; r < row; r++) {
            for (int c = 0; c < col; c++) {
                if (hidden[r][c] != original[r][c]) {
                    equal = false;
                }
            }
        }
        if (equal) {
            setWon();
        }
    }
}
