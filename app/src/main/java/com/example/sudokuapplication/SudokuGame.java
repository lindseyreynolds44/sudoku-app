package com.example.sudokuapplication;

public class SudokuGame {

    private SudokuGrid grid;
    private int score = 0;
    private int difficultyLevel;

    /**
     * Constructor that initializes the masterGrid and creates the
     * playingGrid object
     */
    public SudokuGame(int difficulty)
    {
        difficultyLevel = difficulty;
        grid = new SudokuGrid(difficulty);
        grid.displayGrid();
    }

    /**
     * Default constructor that sets the difficulty of the game to medium
     */
    public SudokuGame()
    {
        this(SudokuGrid.DIFF_MED);
    }

    /**
     * Plays the given number at the specified index. If this is the correct
     * number, score is incremented according to the difficulty level and the number is
     * placed onto the grid. If this is not the correct number, score is decremented by 10 points
     * and nothing is added to the grid.
     * @param number
     * @param row
     * @param col
     * @return boolean if the number was played
     */
    public boolean playNumber(int number, int row, int col)
    {
        // Check if this is the correct number according to the masterGrid
        if(grid.getMasterCellValue(row, col) == number)
        {
            // Increase the score according to the game's difficulty level
            score += (difficultyLevel * 10);
            grid.setPlayingCell(number, row, col);
            return true;
        }
        else
        {
            score -= 10;
            return false;
        }
    }

    /**
     * Check if the cell at the specified index is empty
     * @param row
     * @param col
     * @return boolean if it is empty
     */
    public boolean isCellEmpty(int row, int col)
    {
        if(grid.getPlayingCellValue(row, col) == 0)
            return true;
        return false;
    }

    /**
     * Get the value of the cell at the given index
     * @param row
     * @param col
     * @return the number in the cell
     */
    public int getCellValue(int row, int col)
    {
        return grid.getPlayingCellValue(row, col);
    }

    /**
     * Check if the game is over by checking if there are any empty cells
     * left in the grid
     * @return if the game is over
     */
    public boolean isGameOver()
    {
        // Check if there are any empchty cells left
        for(int row = 0; row < SudokuGrid.GRID_SIZE; row++) {
            for (int col = 0; col < SudokuGrid.GRID_SIZE; col++) {
                if (isCellEmpty(row, col)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Get the current score of the game
     * @return the score
     */
    public int getScore()
    {
        return score;
    }
}
