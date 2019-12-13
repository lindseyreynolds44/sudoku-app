package com.example.sudokuapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

/**
 * Activity: MainActivity
 * Description: Activity to start off the application. Allows user
 * to choose the level of difficulty and start a new game of Sudoku.
 */
public class MainActivity extends AppCompatActivity
{
    private static int highScore = 0;
    private TextView tvHighScore;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvHighScore = findViewById(R.id.tvHighScore);
        Intent fromEndGameActivityIntent = getIntent();

        // If this activity is being called from the EndGame screen, get the score
        if(fromEndGameActivityIntent.getExtras() != null)
        {
            int tempScore = fromEndGameActivityIntent.getExtras().getInt("score");
            if (tempScore > highScore)
            {
                highScore = tempScore;
            }
        }

        // Set the high score text
        tvHighScore.setText(Integer.toString(highScore));
    }

    /**
     * Start a new game of Sudoku of the specified difficulty level
     * @param difficulty
     */
    public void startNewGame(int difficulty)
    {
        Intent startGameIntent = new Intent(this, GamePlay.class);
        startGameIntent.putExtra("difficulty", difficulty);
        startActivity(startGameIntent);
    }

    /**
     * Called when the easy game button is pressed
     * @param view
     */
    public void onStartEasyGame(View view)
    {
        startNewGame(SudokuGrid.DIFF_EASY);
    }

    /**
     * Called when the medium game button is pressed
     * @param view
     */
    public void onStartMediumGame(View view)
    {
        startNewGame(SudokuGrid.DIFF_MED);
    }

    /**
     * Called when the hard game button is pressed
     * @param view
     */
    public void onStartHardGame(View view)
    {
        startNewGame(SudokuGrid.DIFF_HARD);
    }
}
