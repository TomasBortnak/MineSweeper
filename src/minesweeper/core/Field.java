package minesweeper.core;

import java.lang.Thread.State;
import java.util.Random;

import javax.swing.SwingWorker.StateValue;

/**
 * Field represents playing field and game logic.
 */
public class Field {
	
	
	
    /**
     * Playing field tiles.
     */
    private final Tile[][] tiles;

    /**
     * Field row count. Rows are indexed from 0 to (rowCount - 1).
     */
    private final int rowCount;
    
    

    /**
     * Column count. Columns are indexed from 0 to (columnCount - 1).
     */
    private final int columnCount;

    public GameState getState() {
		return state;
	}

	public void setState(GameState state) {
		this.state = state;
	}

	public int getColumnCount() {
		return columnCount;
	}

	public int getMineCount() {
		return mineCount;
	}
	
	public Tile[][] getTile() {
		return tiles;
	}

	public int getRowCount() {
		return rowCount;
	}
	
	public Tile getTile(int row, int column){
		return tiles[row][column];
	}
	
	

	/**
     * Mine count.
     */
    private final int mineCount;

    /**
     * Game state.
     */
    private GameState state = GameState.PLAYING;

    /**
     * Constructor.
     *
     * @param rowCount    row count
     * @param columnCount column count
     * @param mineCount   mine count
     */
    public Field(int rowCount, int columnCount, int mineCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.mineCount = mineCount;
        tiles = new Tile[rowCount][columnCount];

        //generate the field content
        generate();
    }

    /**
     * Opens tile at specified indeces.
     *
     * @param row    row number
     * @param column column number
     */
	public void openTile(int row, int column) {

		if (checkInRange(row, column)) {
			Tile tile = tiles[row][column];
			if (tile.getState() == Tile.State.CLOSED) {
				tile.setState(Tile.State.OPEN);
				if (tile instanceof Mine) {
					state = GameState.FAILED;
					System.out.println("Mine found on position:" + row + " "
							+ column);
					return;
				}
				if (((Clue) tiles[row][column]).getValue() == 0) {
					openAdjacentTiles(row, column);
				}

				if (isSolved()) {
					state = GameState.SOLVED;
					return;
				}
			}
		} else {
			System.out.println("Tile not in field");
		}
	}
    
	private void openAdjacentTiles(int row, int col) {
		for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
			int actRow = row + rowOffset;
			if (actRow >= 0 && actRow < rowCount) {
				for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
					int actColumn = col + columnOffset;
					if (actColumn >= 0 && actColumn < columnCount) {
						if (tiles[actRow][actColumn] instanceof Clue) {
							openTile(actRow, actColumn);

						}
					}
				}
			}
		}
	}

	
	private boolean checkInRange(int a, int b) {
		if (a < getRowCount() && b < getColumnCount() && a >= 0 && b >= 0) {
			return true;
		} else {
			return false;
		}

	}
    /**
     * Marks tile at specified indeces.
     *
     * @param row    row number
     * @param column column number
     */
    public void markTile(int row, int column) {
    	Tile tile = getTile(row, column);
    	if (tile.getState() == Tile.State.CLOSED){
    		tile.setState(Tile.State.MARKED);
    	}
    	else if(tile.getState() == Tile.State.MARKED){
    		tile.setState(Tile.State.CLOSED);
    	}
    }

    /**
     * Generates playing field.
     */
    private void generate() {
    	for(int i =0; i<getMineCount();i++){
    	Random rnd = new Random();
    	int randomColl = rnd.nextInt(getColumnCount());
    	int randomRow = rnd.nextInt(getRowCount());
    	
    	if(getTile(randomRow, randomColl)==null){
    		tiles[randomRow][randomColl] = new Mine();
    	}
    }
    for(int row=0;row<getRowCount();row++ ){
    	for(int coll = 0; coll<getColumnCount();coll++){
    		if(getTile(row, coll)==null){
    			tiles[row][coll] = new Clue(countAdjacentMines(row, coll));
    		}
    	}
    } 
    }
    /**
     * Returns true if game is solved, false otherwise.
     *
     * @return true if game is solved, false otherwise
     */
    private boolean isSolved() {
		int colom = getColumnCount();
		int row = getRowCount();
		int all = colom * row;
		int mine = getMineCount();
		
		if ((all - getNumberOf(Tile.State.OPEN)) == mine){
			return true;
			
		}
		else {
			return false;
		}
		
		
		
		

	}

	private int getNumberOf(Tile.State state) {
		int number = 0;
		for (int i = 0; i < getColumnCount(); i++) {
			for (int j = 0; j < getRowCount(); j++) {
				if (tiles[i][j].getState() == state) {
					number++;
				}

			}

		}
		return number;

	}

    /**
     * Returns number of adjacent mines for a tile at specified position in the field.
     *
     * @param row    row number.
     * @param column column number.
     * @return number of adjacent mines.
     */
    private int countAdjacentMines(int row, int column) {
        int count = 0;
        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            int actRow = row + rowOffset;
            if (actRow >= 0 && actRow < getRowCount()) {
                for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
                    int actColumn = column + columnOffset;
                    if (actColumn >= 0 && actColumn < getColumnCount()) {
                        if (tiles[actRow][actColumn] instanceof Mine) {
                            count++;
                        }
                    }
                }
            }
        }

        return count;
    }


	private int getTilesWithState(minesweeper.core.Tile.State marked) {
		int numberOfTiles = 0;
		for (int i = 0; i < getRowCount(); i++) {
			for (int j = 0; j < getColumnCount(); j++) {
				if (tiles[i][j].getState() == marked) {
					numberOfTiles++;
				}
			}
		}
		return numberOfTiles;
	}
    
	public int getRemainingMineCount() {
		
		int numberOfMines = 0;
		
		numberOfMines = getMineCount() - getTilesWithState(Tile.State.MARKED);

		return numberOfMines;
	}
	
}
