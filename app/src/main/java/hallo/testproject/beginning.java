package hallo.testproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.InputStream;

public class beginning extends AppCompatActivity { // This is the main game interface

    private SharedPreferences preferenceSettings;
    private SharedPreferences.Editor preferenceEditor;
    private static final int PREFERENCE_MODE_PRIVATE = 0;

    public Context c;
    TextView texter;
    Button left, up, down, right;

    Button buttons[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beginning);

        preferenceSettings = getSharedPreferences("prefs", PREFERENCE_MODE_PRIVATE);
        preferenceEditor = preferenceSettings.edit();
        preferenceSettings.getAll();

        texter = (TextView)findViewById(R.id.textView4);
        left = (Button)findViewById(R.id.left_button);
        up = (Button)findViewById(R.id.button3);
        down = (Button)findViewById(R.id.button7);
        right = (Button)findViewById(R.id.button8);

        Button buttons2[] = {left, up, down, right};
        buttons = buttons2;




        c = beginning.this; // creates a context/reference
        InputStream testRoom = getResources().openRawResource(R.raw.room1);
        Gf.createRoom(testRoom);

        Gf.updateFlags();

        Gf.checkInfo();
        updateButtons();

        texter.setText(Gf.firstLine + "\n" + Gf.secondLine + "\n" + Gf.thirdLine);


    }

    public void updateButtons() {
        for (int c = 0; c < Gf.buttons[0].length; c++)
            try {
                buttons[c].setText(Gf.buttons[1][c]);
            }
            catch (NullPointerException n) {
                Log.d("error","You got a null point fool");

            }
        for (int c = 0; c < Gf.buttons[0].length; c++)
            if (buttons[c].getText() == "")
                buttons[c].setVisibility(View.GONE);


    }


}
