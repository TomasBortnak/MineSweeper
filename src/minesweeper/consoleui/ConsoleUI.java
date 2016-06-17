package minesweeper.consoleui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import minesweeper.UserInterface;
import minesweeper.core.Clue;
import minesweeper.core.Field;
import minesweeper.core.Mine;
import minesweeper.core.Tile;

/**
 * Console user interface.
 */
public class ConsoleUI implements UserInterface {
    /** Playing field. */
    private Field field;
    
    /** Input reader. */
    private BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    
    /**
     * Reads line of text from the reader.
     * @return line as a string
     */
    private String readLine() {
        try {
            return input.readLine();
        } catch (IOException e) {
            return null;
        }
    }
    
    /* (non-Javadoc)
	 * @see minesweeper.consoleui.UserInterface#newGameStarted(minesweeper.core.Field)
	 */
    @Override
	public void newGameStarted(Field field) {
        this.field = field;
        do {
            update();
            processInput();
            switch (field.getState()) {
			case SOLVED:
				System.out.println("Game solved");
				update();
				System.exit(0);
				break;
			case FAILED:
				System.out.println("Game OVER");
				update();
				System.exit(0);
				break;
			default:
				break;

			}
        } while(true);
    }
    
    /* (non-Javadoc)
	 * @see minesweeper.consoleui.UserInterface#update()
	 */
    @Override
    public void update() {
    	System.out.println("Pocet min: " + field.getRemainingMineCount());
    	System.out.println();
    	int row = 1; 		
    	char column = 'A'; 
        for(int i = -1; i < field.getColumnCount();i++){
        	for(int j =- 1; j < field.getRowCount(); j++){
        		if(i == -1 && j == -1){
        			System.out.print("  ");
        		}else if(i == -1 && j > -1){
        			System.out.print(row + " ");
        			row++;
        		}else if(j == -1 && i > -1){
        			System.out.print(column + " ");
        			column++;
        		}else if(i > -1 && j > -1){
        			Tile tempTile = field.getTile(i, j);
        			switch(tempTile.getState()){
        				case CLOSED :  
        					System.out.print("- ");
        					break;
        				case MARKED : 
        					System.out.print("M ");
        					break;
        				case OPEN : 
        					if(tempTile instanceof Mine){
        						System.out.print("X ");
        					}
        					if(tempTile instanceof Clue){
        						System.out.print(((Clue) tempTile).getValue() + " ");
        					}
        					break;
        			}
        		}
        		
        	}System.out.println();
        			}
        
        		}
        	
    
    /**
     * Processes user input.
     * Reads line from console and does the action on a playing field according to input string.
     */
	private void processInput() {

		try {
	        System.out.println();
	        System.out.println("Pre ukoncenie hry stlac X");
	        System.out.println("Pre oznaèenie dlaždice v riadku A a ståpci 1 stlac MA1");
	        System.out.println("Pre odkrytie dlaždice v riadku B a ståpci 4 stlac OB4");
	        System.out.println("Zadaj vstup: ");
			handleInput(readLine());
		} catch (WrongFormatException ex) {
			ex.printStackTrace();
		}

	}
    
    
	private void handleInput(String input) throws WrongFormatException  {
		

        
     
        
        Pattern pattern = Pattern
				.compile("([oOmMxX]{1})([a-zA-Z]{1})?(\\d{1,2})?");

		Matcher matcher = pattern.matcher(input);
		
		if (matcher.matches()) {

			String command = matcher.group(1).toUpperCase();
			int row, col;

			switch (command) {
			case "X":
				// System.out.println(command);
				System.exit(0);
				break;
			case "O":
				row = matcher.group(2).toLowerCase().toCharArray()[0] - 'a';
				col = Integer.parseInt(matcher.group(3)) - 1;

				// System.out.println(command + " " + row + " " + col);
				field.openTile(row, col);
				break;
			case "M":
				row = matcher.group(2).toLowerCase().toCharArray()[0] - 'a';
				col = Integer.parseInt(matcher.group(3)) - 1;

				// System.out.println(command + " " + row + " " + col);
				field.markTile(row, col);
				break;

			}
    }
    }}
