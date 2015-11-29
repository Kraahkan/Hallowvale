package hallo.testproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;

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
        texter.setMovementMethod(new ScrollingMovementMethod()); // scrolled txtview
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

        Gf.updateFlags();
        //Gf.checkInfo();
        // = beginning.this; // creates a context/reference

        InputStream testRoom = getResources().openRawResource(R.raw.start);

        Gf.createRoom(testRoom, "start");
        updateButtons();






    }

    @Override
    public void onClick(View v) { // generic on click for all buttons


        int resId;

        String name = "";
        switch(v.getId()){


            case R.id.left_button:
                name = Gf.buttons[0][0];
                resId = getResources().getIdentifier("raw/" + name, null, this.getPackageName());
                rooms = getResources().openRawResource(resId);
                Log.d("Room",String.valueOf(resId));
                Gf.createRoom(rooms, name);
               // Gf.updateFlags();
               // Gf.checkInfo();
                updateButtons();
                break;

            case R.id.up_button:
                name = Gf.buttons[0][1];
                resId = getResources().getIdentifier("raw/" + name, null, this.getPackageName());
                rooms = getResources().openRawResource(resId);
                Log.d("Room",String.valueOf(resId));
                Gf.createRoom(rooms, name );
                //Gf.updateFlags();
                //Gf.checkInfo();
                updateButtons();
                break;


            case R.id.down_button:
                name = Gf.buttons[0][2];
                resId = getResources().getIdentifier("raw/" + name, null, this.getPackageName());
                rooms = getResources().openRawResource(resId);
                Log.d("Room",String.valueOf(resId));
                Gf.createRoom(rooms, name );
                //Gf.updateFlags();
               // Gf.checkInfo();
                updateButtons();
                break;


            case R.id.right_button:
                name = Gf.buttons[0][3];
                resId = getResources().getIdentifier("raw/" + name, null, this.getPackageName());
                rooms = getResources().openRawResource(resId);
                Log.d("Room",String.valueOf(resId));
                Gf.createRoom(rooms, name);
               // Gf.updateFlags();
                //Gf.checkInfo();
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


        for (int c = 0; c < Gf.buttons[0].length; c++) { // proper button txt
            try {
                //
                buttons[c].setText(Gf.buttons[1][c]);
            } catch (NullPointerException n) {
                Log.d("error", "You got a null point fool");

            }
        }

        for (int c = 0; c < Gf.buttons[0].length; c++) // adds buttons needed
            if (buttons[c].getText() != "")
                buttons[c].setVisibility(View.VISIBLE);


        if (Gf.item.length() > 0) {
            takeItem.setVisibility(View.VISIBLE);
            takeItem.setText("Take " + Gf.item);
        }

        Log.d("current", Gf.currentPath);
        Gf.updateFlags();
        setText();



    }

    public void setText() {

        for (int c = 0; c < Gf.roomFlags[0].length; c++) {
            if ( Gf.roomFlags[0][c] != null) {
            }
            Log.d("path","searched path" + Gf.roomFlags[0][c] + "currennt path!" + Gf.currentPath);
            if (Gf.roomFlags[0][c] == Gf.currentPath ) {
                Log.d("bla","INTO LOOP");

                if (Integer.parseInt(Gf.roomFlags[2][c]) == 0) { // if first time
                    texter.setText(Gf.thirdLine);
                    Gf.roomFlags[2][c] = "1";
                }
                if (Integer.parseInt(Gf.roomFlags[1][c]) == 0) { // if odd time
                    texter.append(Gf.firstLine);
                    Gf.roomFlags[1][c] = "1";
                }
                if (Integer.parseInt(Gf.roomFlags[1][c]) == 1) {// if even time
                    texter.append(Gf.secondLine);
                    Gf.roomFlags[1][c] = "0";
                }
            }

            }







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
