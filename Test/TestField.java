import static org.junit.Assert.*;
import minesweeper.core.Clue;
import minesweeper.core.Field;
import minesweeper.core.GameState;
import minesweeper.core.Mine;

import org.junit.Test;


public class TestField {

	static final int ROWS = 9;
	static final int COLUMNS = 9;
	static final int MINES = 10;
	
	
	@Test
	public void testField() {
		int n = 0;
		String message = "n is not 10";
		Field field = new Field(8,8,10);
		for(int i=0; i<field.getRowCount(); i++){
			for(int j = 0; j<field.getColumnCount(); j++){
				if(field.getTile(i, j)instanceof Mine){
					n++;
				}
			}
		}
		
		assertTrue(message, n==10);
		
	}
	
	  @Test                
	    public void isSolved() {
	        Field field = new Field(ROWS, COLUMNS, MINES);
	        
	        assertEquals(GameState.PLAYING, field.getState());
	        
	        int open = 0;
	        for(int row = 0; row < field.getRowCount(); row++) {
	            for(int column = 0; column < field.getColumnCount(); column++) {
	                if(field.getTile(row, column) instanceof Clue) {
	                    field.openTile(row, column);
	                    open++;
	                }
	                if(field.getRowCount() * field.getColumnCount() - open == field.getMineCount()) {
	                    assertEquals(GameState.SOLVED, field.getState());
	                } else {
	                    assertNotSame(GameState.FAILED, field.getState());
	                }
	            }
	        }
	        
	        assertEquals(GameState.SOLVED, field.getState());
	    } 

}
