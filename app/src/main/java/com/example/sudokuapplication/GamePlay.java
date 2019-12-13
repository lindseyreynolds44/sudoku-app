package com.example.sudokuapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * Activity: GamePlay
 * Description: This activity displays the current Sudoku game and allows
 * the user to interact with the grid.
 */
public class GamePlay extends AppCompatActivity
{
    private EditText[][] etArray = new EditText[SudokuGrid.GRID_SIZE][SudokuGrid.GRID_SIZE];
    private TextView tvScore;
    private SudokuGame game;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);

        // Get the difficulty level that was chosen on the main page
        Intent startIntent = getIntent();
        int difficulty = startIntent.getExtras().getInt("difficulty");

        // Create a new game with the specified difficulty level
        game = new SudokuGame(difficulty);

        tvScore = findViewById(R.id.tvScore);

        createGrid();
    }

    /**
     * Link the EditText objects of the grid with the correct numbers from the Sudoku
     * game that was created.
     */
    private void createGrid()
    {
        // Loop through the grid, cell by cell
        for(int row = 0; row < SudokuGrid.GRID_SIZE; row++){
            for(int col = 0; col < SudokuGrid.GRID_SIZE; col++){

                // Get the name of the EditText object
                String btnName = "et" + row + col;
                int resID = getResources().getIdentifier(btnName, "id", getPackageName());

                // Link the EditText object to the cell at the appropriate index
                etArray[row][col] = findViewById(resID);

                // If this cell contains a number, set the value in its EditText object
                if(!game.isCellEmpty(row, col)){
                    String value = Integer.toString(game.getCellValue(row, col));
                    etArray[row][col].setText(value);

                    // Set the EditText object to be uneditable
                    etArray[row][col].setKeyListener(null);
                }
                // If this cell is empty
                else {
                    // Create temporary row and column indexes to be used in the listener event
                    final int tempRow = row;
                    final int tempCol = col;

                    // Create a listener for each empty cell on the playing grid
                    etArray[row][col].setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                                onEnterClick(v, tempRow, tempCol);
                                return true;
                            }
                            return false;
                        }
                    });
                }
            }
        }
    }

    /**
     * When the user enters a number into a cell and clicks the enter or done button,
     * this method is called. If the number is correct, it is added to the grid and
     * the score is increased. If it is wrong, nothing is added to the grid and the score
     * is decreased.
     * @param v
     * @param tempRow
     * @param tempCol
     */
    private void onEnterClick(View v, int tempRow, int tempCol)
    {
        try
        {
            // Get the number that the user just entered
            String value = etArray[tempRow][tempCol].getText().toString().trim();
            int number = Integer.parseInt(value);

            // Play the number and find out if it was correct
            boolean wasCorrect = game.playNumber(number, tempRow, tempCol);

            // If it was correct, update the game grid
            if(wasCorrect)
            {
                etArray[tempRow][tempCol].setText(value);
                etArray[tempRow][tempCol].setKeyListener(null);
            }
            // Otherwise, let the user know that the number was incorrect
            else {
                Toast.makeText(GamePlay.this, R.string.incorrect,
                        Toast.LENGTH_SHORT).show();
                etArray[tempRow][tempCol].setText("");
            }
        }
        // In case the user entered something other than a number
        catch(NumberFormatException e)
        {
            Toast.makeText(GamePlay.this,
                    "Please enter a number", Toast.LENGTH_LONG).show();
        }

        // Make the keyboard disappear after each play
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(v.getWindowToken(), 0);

        // Update the score
        tvScore.setText(Integer.toString(game.getScore()));

        // Check if the game should continue
        if(game.isGameOver())
            onEndGame(v);
    }

    /**
     * When the game is over, go to the EndGame activity
     * @param view
     */
    public void onEndGame(View view) {
        Intent endGameIntent = new Intent(this, EndGame.class);

        // Send the final score to the next activity
        endGameIntent.putExtra("score", game.getScore());

        startActivity(endGameIntent);
    }
}