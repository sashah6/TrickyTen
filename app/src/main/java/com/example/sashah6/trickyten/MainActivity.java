package com.example.sashah6.trickyten;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //Global variables
    //Most recent user guess
    int myRecentGuess = 0;
    //Most recent computer generated guess
    int computerRecentGuess = 0;
    //How many turns in?
    int turnsIn = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This method simply get's the most recent guess (from P1 or P2) and displays it to the last guess text view
     */
    private void updateDisplay() {
        TextView temp = (TextView) this.findViewById(R.id.my_guess_text_view);
        temp.setText("" + myRecentGuess);
        TextView temp2 = (TextView) this.findViewById(R.id.computer_guess_text_view);
        temp2.setText("" + computerRecentGuess);
        //If game is over, disable any new userinput
        if (gameOver()){
            TextView end = (TextView) findViewById(R.id.game_over_text_view);
            end.setText("Game Over. You "+winner());
            end.setVisibility(View.VISIBLE);
        }
    }

    /**
     * This method gets the input of the user and stores it in the global variable
     * @param
     */
    public void userInput(View view) {
        //check if the user entered something...
        EditText input = (EditText) this.findViewById(R.id.guess_edit_text);
        if (input.getText().toString().isEmpty()){
            Context context = getApplicationContext();
            CharSequence text = "Please enter something!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else {
            //need to convert from Editable -> String -> int
            int userGuess = Integer.parseInt(input.getText().toString());
            //turnsIn + 1 now that the user has gone
            turnsIn++;
            if (isValidInput(userGuess)) {
                //update the globalLastGuess
                myRecentGuess = userGuess;
                //now display it on the screen
                updateDisplay();
                //generate a new guess only if the greatestNumber isn't 100
                if (!gameOver())
                    computerRecentGuess = generateGuess();
                updateDisplay();
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
            //clear the most recent input from the edit text so the user can enter the next guess
            input.setText("");
        }
    }
    /**
     * Generate a random guess (int from 0 to 9) and add 1 to eliminate 0.
     * @param
     * @return
     */
    public int generateGuess() {
        //generate a random int from 1 to 10
        Random temp = new Random();
        int randomNum = temp.nextInt(9) + 1;
        if ((perfectPath() - myRecentGuess == 12)){
            return myRecentGuess+1;
        }
        else if ((perfectPath()- myRecentGuess < 11) && turnsIn > 5)
            return perfectPath();
        else
            return myRecentGuess+randomNum;
    }

    /**
     * Generates the perfect computer output before correction
     * @return
     */
    private int perfectPath () {
        //divide the user's current guess by 10 and round up
        int y =(11*((int) Math.ceil(myRecentGuess/10.0))) + 1;
        return y;
    }

    /**
     * Checks if the guess that the user entered is valid. Within 10, not negative, not greater than 100
     * @param guess
     * @return
     */
    private boolean isValidInput (int guess) {
        if (guess - computerRecentGuess > 10 || guess - computerRecentGuess < 1) {
            //ask the user to enter another input
            Context context = getApplicationContext();
            int greaterGuess = myRecentGuess;
            if (computerRecentGuess > myRecentGuess)
                greaterGuess = computerRecentGuess;
            if (greaterGuess < 90) {
                CharSequence text = "Enter an integer from " + (greaterGuess + 1) + " to " + (greaterGuess + 10);
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
            else {
                CharSequence text = "You're closer than you think";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
            return false;
        }
        return true;
    }
    /**
     * Determines when the game is over;
     * @return boolean
     */
    private boolean gameOver (){
        if ((myRecentGuess == 100) || (computerRecentGuess == 100)) {
            EditText input = (EditText) this.findViewById(R.id.guess_edit_text);
            input.setKeyListener(null);
            return true;
        }
        return false;
    }

    /**
     * Return a string with weather the user won or lost.
     * @return String
     */
    private String winner () {
        if (gameOver() && myRecentGuess == 100) {
            return "Win!";
        }
        else if (gameOver() && computerRecentGuess == 100)
            return "Lose.";

        return "";
    }

}
