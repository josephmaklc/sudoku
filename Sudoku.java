/**
 * Sudoku
 * Joseph Mak
 * 11/18/2005
 */

class SudokuCell {
    int value;
    int tryingValue;
    boolean possible[] = new boolean[9];
}


public class Sudoku {


    String boarddata[]={
            "060040003",
            "700803000",
            "200000780",
            "043070009",
            "070304050",
            "800050370",
            "086000007",
            "000701005",
            "400060030"};

    SudokuCell grid[][] = new SudokuCell[9][9];

    public static void main(String args[]) {

        Sudoku me = new Sudoku();

        me.readData();
        boolean result = me.solveSudoku(0);

        if (!result) {
            System.out.println("\nFailed");
        }
        else {
            me.showData();
        }

    }

    /**
     * This method shows the grid
     */
    void showData() {

        for(int i=0; i < 9; i++) {
            for(int j=0; j < 9; j++) {
                int c= grid[i][j].value;
                int t = grid[i][j].tryingValue;

                if (t > 0)
                    System.out.print( "("+t+")");
                else
                    System.out.print( " "+c+" ");
                if ((j % 3)== 2) System.out.print(" | ");
            }
            System.out.println();
            if ((i % 3)== 2) System.out.println("-----------------------------------");
        }
    }

    /**
     * reads data from the boarddata array into the SudokuCell array
     */

    void readData() {

        for(int i=0; i < 9; i++) {
            for(int j=0; j < 9; j++) {
                String line = boarddata[i];
                char ch = line.charAt(j);
                SudokuCell c = new SudokuCell();
                c.value = new Integer(ch+"").intValue();
                c.tryingValue=0;

                for (int k=0; k<9;k++) {
                    c.possible[k] = true;
                }
                if (c.value > 0)
                    c.possible[c.value-1] = false;

                grid[i][j] = c;
            }
        }

    }

    /**
     * This method determines which smallgrid a row,col belong to, and mark
     * cells already defined in the smallgrid as impossible
     */
    void markSmallGridImpossibles(int row, int col) {
        int smallgrid=0;
        if ((row>=0) && (row<=2) && (col>=0) && (col<=2)) smallgrid=1;
        if ((row>=0) && (row<=2) && (col>=3) && (col<=5)) smallgrid=2;
        if ((row>=0) && (row<=2) && (col>=6) && (col<=8)) smallgrid=3;
        if ((row>=3) && (row<=5) && (col>=0) && (col<=2)) smallgrid=4;
        if ((row>=3) && (row<=5) && (col>=3) && (col<=5)) smallgrid=5;
        if ((row>=3) && (row<=5) && (col>=6) && (col<=8)) smallgrid=6;
        if ((row>=6) && (row<=8) && (col>=0) && (col<=2)) smallgrid=7;
        if ((row>=6) && (row<=8) && (col>=3) && (col<=5)) smallgrid=8;
        if ((row>=6) && (row<=8) && (col>=6) && (col<=8)) smallgrid=9;

        int coor_row[] = new int[3];
        int coor_col[] = new int[3];

        switch (smallgrid) {
            case 1:
            case 2:
            case 3: coor_row[0] = 0;  coor_row[1] = 1;  coor_row[2] = 2;
                break;
            case 4:
            case 5:
            case 6: coor_row[0] = 3;  coor_row[1] = 4;  coor_row[2] = 5;
                break;
            case 7:
            case 8:
            case 9: coor_row[0] = 6;  coor_row[1] = 7;  coor_row[2] = 8;
                break;
        }

        switch (smallgrid) {
            case 1:
            case 4:
            case 7: coor_col[0] = 0;  coor_col[1] = 1;  coor_col[2] = 2;
                break;
            case 2:
            case 5:
            case 8: coor_col[0] = 3;  coor_col[1] = 4;  coor_col[2] = 5;
                break;
            case 3:
            case 6:
            case 9: coor_col[0] = 6;  coor_col[1] = 7;  coor_col[2] = 8;
                break;
        }

        // marking small grid defined boxes impossible
        for (int i=0; i < 3; i++) {
            for (int j=0; j < 3; j++) {
                int r = coor_row[i];
                int c = coor_col[j];

                int valueAtSmallGrid = grid[r][c].value;
                if (valueAtSmallGrid==0) valueAtSmallGrid = grid[r][c].tryingValue;
                if (valueAtSmallGrid > 0) {
                    grid[row][col].possible[valueAtSmallGrid-1] = false;
                }

            }

        }

    }

    /**
     * This method returns a string of possible values for the position
     * blank means no possible values.
     */
    String getPossible(int row, int col) {
        int i;

        // for each values in row known, mark impossible
        for (i=0; i < 9; i++) {
            int n = grid[row][i].value;
            if (n > 0) {
                grid[row][col].possible[n-1] = false;
            }

            n = grid[row][i].tryingValue;
            if (n > 0) {
                grid[row][col].possible[n-1] = false;
            }

        }

        // for each values in col known, mark impossible
        for (i=0; i < 9; i++) {
            int n = grid[i][col].value;
            if (n > 0) {
                grid[row][col].possible[n-1] = false;
            }

            n = grid[i][col].tryingValue;
            if (n > 0) {
                grid[row][col].possible[n-1] = false;
            }
        }
        // for each values in the small grid, mark impossible
        markSmallGridImpossibles(row,col);

        // collect results
        String result="";
        for (i=0; i < 9; i++) {
            boolean possible = grid[row][col].possible[i];
            if (possible) {
                int n = i+1;
                result = result+n;
            }
        }
        return result;

    }

    /**
     * Reset the data starting at a particular point because the previous guess was wrong
     */
    void resetAllTryingValuesBeyondThis(int start) {

        for (int i=start; i < 81; i++)	{
            int row = i / 9;
            int col = i % 9;
            grid[row][col].tryingValue=0;
            grid[row][col].value = new Integer(boarddata[row].charAt(col) + "");

            for (int j=0; j < 9; j++) {
                boolean boolValue = true;
                grid[row][col].possible[j] = boolValue;
            }

        }
    }

    boolean allValuesDefinedAfter(int start) {

        for (int i=start; i < 81; i++)	{
            int row = i / 9;
            int col = i % 9;
            if (grid[row][col]==null) return false;

            if (grid[row][col].value == 0) return false;

        }
        return true;

    }

    /**
     * main solve routine. This is done recursively.
     * 3 possible choices for the current grid:
     * 	1. there is exactly one possible, set the value, move on.
     *  2. no number possible at the grid, return false
     *  3. there are multiple possible values, set trying field, move on.
     */
    boolean solveSudoku(int start) {
        if (start<0) return false;
        if (start==81) return true; // should be 81 for full puzzle
        if (allValuesDefinedAfter(start)) return true;

        boolean result = false;
        int row = start / 9;
        int col = start % 9;

        //System.out.println("solveSudoku, at ["+row+","+col+"]");

        SudokuCell c = grid[row][col];
        if (c.value != 0) {
            // already defined
            //System.out.println("already defined at ["+row+","+col+"], bye");
            return solveSudoku(start+1);
        }
        //showData();

        String possibleValues = getPossible( row,col );
        int possibleValue = 0;

        if (possibleValues.equals("")) {
            // impossible
            if (c.tryingValue > 0)
                grid[row][col].possible[c.tryingValue-1] = false; // mark the trial wrong
            return false;
        }

        if (possibleValues.length()==1) {
            // must be this value
            possibleValue = new Integer( possibleValues ).intValue();
            result = true;
        }
        else
        {
            // try possible values
            for (int i=0; i < possibleValues.length(); i++) {
                possibleValue = new Integer( possibleValues.charAt(i)+"").intValue();

                resetAllTryingValuesBeyondThis(start);
                grid[row][col].tryingValue = possibleValue;

                boolean tryresult = solveSudoku(start+1);
                if (tryresult) {
                    result = true;
                    break;
                }
            }
        }


        if (result) {
            grid[row][col].value = possibleValue;
            //System.out.println("setting ["+row+","+col+"] trying to 0");
            grid[row][col].tryingValue=0; // no longer trying
            for (int i=0; i < 9; i++) {
                grid[row][col].possible[i] = false;
            }
            // move on
            return solveSudoku(start+1);
        }
        return false;
    }

}

