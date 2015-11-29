package hallo.testproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.InputStream;
import android.view.View.OnClickListener;

public class beginning extends AppCompatActivity implements OnClickListener { // This is the main game interface

    private SharedPreferences preferenceSettings;
    private SharedPreferences.Editor preferenceEditor;
    private static final int PREFERENCE_MODE_PRIVATE = 0;

    public Context c;
    TextView texter;
    Button left, up, down, right, takeItem;

    Button buttons[];

    InputStream rooms;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beginning);

        preferenceSettings = getSharedPreferences("prefs", PREFERENCE_MODE_PRIVATE);
        preferenceEditor = preferenceSettings.edit();
        preferenceSettings.getAll();

        defaultPrefs(); // Set all shared preferences to default

        texter = (TextView)findViewById(R.id.textView4);
        left = (Button)findViewById(R.id.left_button);
        up = (Button)findViewById(R.id.up_button);
        down = (Button)findViewById(R.id.down_button);
        right = (Button)findViewById(R.id.right_button);
        takeItem = (Button)findViewById(R.id.take_item_button);

        left.setOnClickListener(this);
        up.setOnClickListener(this);
        down.setOnClickListener(this);
        right.setOnClickListener(this);
        takeItem.setOnClickListener(this);

        Button buttons2[] = {left, up, down, right};
        buttons = buttons2;

        c = beginning.this; // creates a context/reference
        InputStream testRoom = getResources().openRawResource(R.raw.rockypath);
        Gf.createRoom(testRoom);




        updateButtons();

        Gf.updateFlags();
        Gf.checkInfo();




    }

    @Override
    public void onClick(View v) { // generic on click for all buttons


        int resId;


        switch(v.getId()){

            case R.id.left_button:
                resId = getResources().getIdentifier("raw/" + Gf.buttons[0][0], null, this.getPackageName());
                rooms = getResources().openRawResource(resId);
                Log.d("Room",String.valueOf(resId));
                Gf.createRoom(rooms);
                Gf.updateFlags();
                Gf.checkInfo();
                updateButtons();
                break;

            case R.id.up_button:
                resId = getResources().getIdentifier("raw/" + Gf.buttons[0][1], null, this.getPackageName());
                rooms = getResources().openRawResource(resId);
                Log.d("Room",String.valueOf(resId));
                Gf.createRoom(rooms);
                Gf.updateFlags();
                Gf.checkInfo();
                updateButtons();
                break;


            case R.id.down_button:
                resId = getResources().getIdentifier("raw/" + Gf.buttons[0][2], null, this.getPackageName());
                rooms = getResources().openRawResource(resId);
                Log.d("Room",String.valueOf(resId));
                Gf.createRoom(rooms);
                Gf.updateFlags();
                Gf.checkInfo();
                updateButtons();
                break;


            case R.id.right_button:
                resId = getResources().getIdentifier("raw/" + Gf.buttons[0][3], null, this.getPackageName());
                rooms = getResources().openRawResource(resId);
                Log.d("Room",String.valueOf(resId));
                Gf.createRoom(rooms);
                Gf.updateFlags();
                Gf.checkInfo();
                updateButtons();
                break;

            case R.id.take_item_button:
                takeItem();
                break;

            default:
                break;


        }

    }

    public void updateButtons() { // refreshes buttons after room is created

        takeItem.setVisibility(View.GONE);

        for (int c = 0; c < Gf.buttons[0].length; c++) // removes all
                buttons[c].setVisibility(View.GONE);


        for (int c = 0; c < Gf.buttons[0].length; c++) // proper button txt
            try {
                //
                buttons[c].setText(Gf.buttons[1][c]);
            }
            catch (NullPointerException n) {
                Log.d("error","You got a null point fool");

            }

        for (int c = 0; c < Gf.buttons[0].length; c++) // adds buttons needed
            if (buttons[c].getText() != "")
                buttons[c].setVisibility(View.VISIBLE);


        if (Gf.item.length() > 0) {
            takeItem.setVisibility(View.VISIBLE);
            takeItem.setText("Take " + Gf.item);
        }


        texter.setText(Gf.firstLine + "\n" + Gf.secondLine + "\n" + Gf.thirdLine);


    }

    public void takeItem(){ // when you take an item

        preferenceEditor.putBoolean(Gf.item, true);

    }

    public void defaultPrefs(){ // populates buttons

        preferenceEditor.putBoolean("unlitTorch", false);
        preferenceEditor.putBoolean("flint", false);
        preferenceEditor.putBoolean("litTorch", false);

    }


}
