package com.example.sudokuapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Activity: EndGame
 * Description: Activity that is displayed when the user ends their game.
 * It displays their score and a button that allows the user
 * to play again.
 */
public class EndGame extends AppCompatActivity
{
    private TextView tvScore;
    private int finalScore;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        tvScore = findViewById(R.id.tvScore);

        Intent gameActivity = getIntent();

        // Retrieve the user's score from the game that just finished
        finalScore = gameActivity.getExtras().getInt("score");

        // Display the user's score
        tvScore.setText(Integer.toString(finalScore));


    }

    /**
     * Method that is called when the user clicks the "Play Again" button.
     * It takes them back to the main page where they can choose
     * to play another game.
     * @param view
     */
    public void onBackToMainPage(View view)
    {
        Intent backToMainIntent = new Intent(this, MainActivity.class);

        // Send the users score to the main page, so high score can be update, if needed
        backToMainIntent.putExtra("score", finalScore);
        startActivity(backToMainIntent);
    }
}
