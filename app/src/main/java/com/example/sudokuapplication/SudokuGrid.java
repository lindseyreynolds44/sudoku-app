package com.example.sudokuapplication;

import java.util.Random;

public class SudokuGrid
{
    public static final int GRID_SIZE = 9;
    public static final int BOX_SIZE = 3;
    public static final int EMPTY = 0;
    public static final int DIFF_EASY = 1;
    public static final int DIFF_MED = 2;
    public static final int DIFF_HARD = 4;

    private int[][] masterGrid = new int[GRID_SIZE][GRID_SIZE];
    private int[][] playingGrid = new int[GRID_SIZE][GRID_SIZE];
    private int difficultyLevel;
    private Random randomGenerator = new Random();

    /**
     * Constructor that initializes the masterGrid and creates the
     * playingGrid with a certain number of cells missing, depending
     * on the difficultly level of the game
     * @param difficulty - 1 = easy, 2 = medium, 3 = hard
     */
    public SudokuGrid(int difficulty)
    {
        // Difficulty level must be between 1 and 3
        if(difficulty < DIFF_EASY || difficulty > DIFF_HARD)
            difficultyLevel = DIFF_MED;
        else
            difficultyLevel = difficulty;

        // Initialize the grid objects
        initMasterGrid();
        initPlayingGrid();
    }

    /**
     * Default constructor that sets the difficulty of the game to medium
     */
    public SudokuGrid()
    {
        this(DIFF_MED);
    }

    /**
     * Generates the random masterGrid object that will be referenced
     * during game play
     */
    private void initMasterGrid()
    {
        // Generate the top left 3x3 box
        fillRandomSquare(0, 0, BOX_SIZE, BOX_SIZE);

        // Generate the center 3x3 box
        fillRandomSquare(BOX_SIZE, BOX_SIZE, BOX_SIZE * 2, BOX_SIZE * 2);

        // Generate the bottom right 3x3 box
        fillRandomSquare(BOX_SIZE * 2, BOX_SIZE * 2, GRID_SIZE, GRID_SIZE);

        // Fill in the rest of the grid
        fillRemainingGrid();

    }


    /**
     * Fills a 3x3 square with random numbers, from 1 to 9, without repeating
     * numbers.
     * @param startRow
     * @param startCol
     * @param endRow
     * @param endCol
     */
    private void fillRandomSquare(int startRow, int startCol,
                                  int endRow, int endCol)
    {
        // Get an array of numbers 1-9, in a random order
        int[] randomNine = generateRandomNine();

        // Loop through the specified 3x3 square, filling in the random numbers
        for(int row = startRow, index = 0; row < endRow; row++)
        {
            for(int col = startCol; col < endCol; col++, index++)
            {
                masterGrid[row][col] = randomNine[index];
            }
        }
    }

    /**
     * Generate an array of the numbers 1-9, in random order, without repeating
     * any numbers
     * @return the array of numbers
     */
    private int[] generateRandomNine()
    {
        String randomNumString = "";

        do
        {
            int randInt = randomGenerator.nextInt(GRID_SIZE) + 1; // number between 1-9

            // Check that this number is not a duplicate
            if(randomNumString.indexOf((char)(randInt + '0')) < 0)
            {
                randomNumString += randInt;
            }
        }
        // Continue generating random numbers until the string is GRID_SIZE long
        while (randomNumString.length() < GRID_SIZE);

        // Convert the string into an array
        String[] stringArray = randomNumString.split("");

        // Create an array of ints to return
        int[] randomInts = new int[GRID_SIZE];

        // Convert the string array into an int array
        for(int i = 0; i < GRID_SIZE; i++)
            randomInts[i] = Integer.parseInt(stringArray[i]);

        return randomInts;
    }

    /**
     * Fills the remaining cells in the grid, following the Sudoku guidelines,
     * after the random squares are filled in
     */
    private boolean fillRemainingGrid()
    {
        // Loop through the whole grid recursively until every space is filled
        for(int row = 0; row < GRID_SIZE; row++)
        {
            for(int col = 0; col < GRID_SIZE; col++)
            {
                if(masterGrid[row][col] == 0)
                {
                    // check every possible number
                    for(int num = 1; num <= GRID_SIZE; num++)
                    {
                        if(canPlaceHere(num, row, col))
                        {
                            masterGrid[row][col] = num;
                            if(fillRemainingGrid())
                                return true;
                            masterGrid[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        // if we have looped over the whole grid, return true
        return true;
    }

    /**
     * Check if the given number can be placed in the specified cell,
     * according to the rules of Sudoku. There can only be one of each number
     * in every row, column and 3x3 square.
     * @param number
     * @param thisRow
     * @param thisCol
     * @return boolean if the number is allowed to be placed in the cell
     */
    private boolean canPlaceHere(int number, int thisRow, int thisCol)
    {
        // check if the number is already in this row
        for(int col = 0; col < GRID_SIZE; col++)
        {
            if(masterGrid[thisRow][col] == number)
                return false;
        }

        // check if the number is already in this column
        for(int row = 0; row < GRID_SIZE; row++)
        {
            if(masterGrid[row][thisCol] == number)
                return false;
        }

        // check if the number is already in this box
        if(!isNumInSquare(number, thisRow, thisCol))
            return false;

        return true;
    }

    /**
     * Checks if the given number can be placed in the specified 3x3 square.
     * If this number already exists in the square, it cannot be placed there.
     * @param number
     * @param thisRow
     * @param thisCol
     * @return boolean if this number can be place in the square
     */
    private boolean isNumInSquare(int number, int thisRow, int thisCol)
    {
        int startRow, startCol;

        // Get the starting row of the square
        if(thisRow < BOX_SIZE)
            startRow = 0;
        else if(thisRow >= BOX_SIZE && thisRow < BOX_SIZE * 2)
            startRow = BOX_SIZE;
        else
            startRow = BOX_SIZE * 2;

        // Get the starting column of the square
        if(thisCol < BOX_SIZE)
            startCol = 0;
        else if(thisCol >= BOX_SIZE && thisCol < BOX_SIZE * 2)
            startCol = BOX_SIZE;
        else
            startCol = BOX_SIZE * 2;

        // Check if this number is already in this square
        for(int row = startRow; row < startRow + BOX_SIZE; row++)
        {
            for(int col = startCol; col < startCol + BOX_SIZE; col++)
            {
                if(masterGrid[row][col] == number)
                    return false;
            }
        }
        return true;
    }


    /**
     * Create the playingGrid, which will have a certain number of cells
     * removed, in order to be played on
     */
    private void initPlayingGrid()
    {
        // Copy all cells from the master grid over to the playing grid
        copyMasterGrid();
        // Delete cells to create the final playing grid
        deleteCells();
    }

    /**
     * Deletes random cells in each 3x3 square. The number of cells to be
     * deleted depends on the difficulty level.
     */
    private void deleteCells()
    {
        // Loop through each 3x3 square in the grid
        for(int row = 0; row <= BOX_SIZE * 2; row+=3)
        {
            for(int col = 0; col <= BOX_SIZE * 2; col+=3)
            {
                // Delete one cell from the current box
                int[] rowAndCol = getRandomCellToDelete(row, col);
                deleteThisCell(rowAndCol[0], rowAndCol[1]);

                // Delete more cells, depending on the difficulty level
                for(int i = 1; i < difficultyLevel; i++)
                {
                    do {
                        rowAndCol = getRandomCellToDelete(row, col);
                    }
                    // If this cell has already been deleted, try again
                    while (isCellEmpty(rowAndCol[0], rowAndCol[1]));

                    deleteThisCell(rowAndCol[0], rowAndCol[1]);
                }
            }
        }
    }

    /**
     * Gets a random cell in the specified 3x3 square to delete (the passed in row and column
     * are always the top left cell of the specific square)
     * @param row the top row of the 3x3 square
     * @param col the left most column of the 3x3 square
     * @return an int array containing the row and column of the cell
     * to be deleted
     */
    private int[] getRandomCellToDelete(int row, int col)
    {
        // Generate a random row and column in the square
        int randRow = randomGenerator.nextInt(BOX_SIZE); // number between 0-2
        int randCol = randomGenerator.nextInt(BOX_SIZE); // number between 0-2

        // get the random cell's row and column indexes
        int[] rowAndCol = {row + randRow, col + randCol};

        return rowAndCol;
    }

    /**
     * Delete the cell at the specified row and column
     * @param row
     * @param col
     */
    private void deleteThisCell(int row, int col)
    {
        playingGrid[row][col] = EMPTY;
    }

    /**
     * Check if the cell at the specified index is empty
     * @param row
     * @param col
     * @return boolean if it is empty
     */
    private boolean isCellEmpty(int row, int col)
    {
        if(playingGrid[row][col] == 0)
            return true;
        return false;
    }

    /**
     * Copy all the cells from masterGrid into playingGrid
     */
    private void copyMasterGrid()
    {
        for(int row = 0; row < GRID_SIZE; row++)
        {
            for(int col = 0; col < GRID_SIZE; col++)
            {
                playingGrid[row][col] = masterGrid[row][col];
            }
        }
    }

    /**
     * Get the value of the cell at the given index in masterGrid
     * @param row
     * @param col
     * @return the number in the cell
     */
    public int getMasterCellValue(int row, int col) {
        return masterGrid[row][col];

    }

    /**
     * Get the value of the cell at the given index in playingGrid
     * @param row
     * @param col
     * @return the number in the cell
     */
    public int getPlayingCellValue(int row, int col)
    {
        return playingGrid[row][col];
    }

    /**
     * Sets the cell at the specified index to the given number
     * @param number
     * @param row
     * @param col
     */
    public void setPlayingCell(int number, int row, int col)
    {
        playingGrid[row][col] = number;
    }


    /**
     * Displays the playingGrid
     */
    public void displayGrid()
    {
        for(int row = 0; row < GRID_SIZE; row++) {
            for(int col = 0; col < GRID_SIZE; col++)
            {
                System.out.print(playingGrid[row][col] + " | ");
            }
            System.out.println();
        }
    }

    /**
     * Getter method for this games difficulty level
     * @return the difficulty level
     */
    public int getDifficulty()
    {
        return difficultyLevel;
    }

}