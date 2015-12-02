package hallo.testproject;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.plattysoft.leonids.ParticleSystem;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

public class beginning extends AppCompatActivity implements OnClickListener { // This is the main game interface

    private SharedPreferences preferenceSettings;
    private SharedPreferences.Editor preferenceEditor;
    private static final int PREFERENCE_MODE_PRIVATE = 0;

    public Context c;


    TextView texter, riddle, answer;
    EditText riddleAnswer;

    Button left, up, down, right, takeItem, inventory;
    LinearLayout master;

    TextView[] items = new TextView[10];

    Button buttons[];

    public Stack<String> heldItems;
    public int count = -1;

    InputStream rooms; // for reading rooms
    String[] temp = new String[100];

    public boolean instFlag = true;


    boolean played = false;
    MediaPlayer ambience;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ambience = MediaPlayer.create(this, R.raw.wind);
        ambience.setVolume(1, 1);
        ambience.start();
        ambience.setLooping(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beginning);

        heldItems = new Stack();

        preferenceSettings = getSharedPreferences("prefs", PREFERENCE_MODE_PRIVATE);
        preferenceEditor = preferenceSettings.edit();
        preferenceSettings.getAll();

        defaultPrefs(); // Set all shared preferences to default, including items

        // ----------------------------------------------------

        texter = (TextView)findViewById(R.id.textView4);
        texter.setMovementMethod(new ScrollingMovementMethod()); // scrolled txtview
        left = (Button)findViewById(R.id.left_button);
        up = (Button)findViewById(R.id.up_button);
        down = (Button)findViewById(R.id.down_button);
        right = (Button)findViewById(R.id.right_button);
        takeItem = (Button)findViewById(R.id.take_item_button);
        inventory = (Button)findViewById(R.id.inventory);
        master = (LinearLayout)findViewById(R.id.masterLayout);


        left.setOnClickListener(this);
        up.setOnClickListener(this);
        down.setOnClickListener(this);
        right.setOnClickListener(this);
        takeItem.setOnClickListener(this);
        inventory.setOnClickListener(this);

        Button buttons2[] = {left, up, down, right};
        buttons = buttons2;

        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/LibreBaskerville-Regular.ttf");
        texter.setTypeface(myTypeface);

        // ----------------------------------------------------

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        for (int c = 0; c < width; c = c + 100)
            snow(c);

        Gf.updateFlags(); // populates with txt file paths and sets flags all to 0

        InputStream testRoom = getResources().openRawResource(R.raw.start); // first room
        Gf.createRoom(testRoom, "start");

        initialItemCheck(); // gets called everytime there is a click

        updateButtons();

    }

    public void snow(int pos) {


        new ParticleSystem(this, 60, R.drawable.crappysnowflake, 10000) // first is max, second is spawn time
                .setSpeedModuleAndAngleRange(0.02f, 0.3f, 80, 90) // first two are acceleration, second are angles (min/max)
                .setRotationSpeed(144)
                .setFadeOut(1000)
                        //.setAcceleration(0.00005f, 90)
                .emit(pos, -20, 1); // x & y spawn pos, # per second


    }

    @Override
    public void onClick(View v) { // generic on click for all buttons

        final MediaPlayer m = MediaPlayer.create(this, R.raw.drum);

        m.start();


        ObjectAnimator scaleAnim = ObjectAnimator.ofFloat(v, "scaleX", 1.0f, 0.5f);
        scaleAnim.setDuration(100);
        scaleAnim.setRepeatCount(1);
        scaleAnim.setRepeatMode(ValueAnimator.REVERSE);
        scaleAnim.start();

        ObjectAnimator alphaFlash = ObjectAnimator.ofFloat(v, "alpha", 1.0f, 0.5f);
        alphaFlash.setDuration(100);
        alphaFlash.setRepeatCount(1);
        alphaFlash.setRepeatMode(ValueAnimator.REVERSE);
        alphaFlash.start();



        switch(v.getId()){

            case R.id.left_button:
                buttonMethod(0);
                break;

            case R.id.up_button:
                buttonMethod(1);
                break;

            case R.id.down_button:
                buttonMethod(2);
                break;

            case R.id.right_button:
                buttonMethod(3);
                break;

            case R.id.take_item_button:
                takeItem();
                takeItem.setVisibility(View.GONE);

                break;

            case R.id.inventory:
                callInventory();
                break;

            default:
                break;


        }

        initialItemCheck();

    }

    public void buttonMethod(int i) {

        int resId;
        String name = "";

        name = Gf.buttons[0][i];
        resId = getResources().getIdentifier("raw/" + name, null, this.getPackageName());
        rooms = getResources().openRawResource(resId);
        Gf.createRoom(rooms, name);
        updateButtons();

    }

    public void callRiddleView(){

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(beginning.this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.riddle, null);

        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Riddle Room");

        riddle = (TextView)dialogView.findViewById(R.id.view_riddle);
        answer = (TextView)dialogView.findViewById(R.id.answer);
        riddleAnswer = (EditText)dialogView.findViewById(R.id.enter_riddle);

        dialogBuilder.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        AlertDialog alertDialog = dialogBuilder.create();

        alertDialog.show();

    }

    public void callInventory(){


        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(beginning.this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.inventory, null);

        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Inventory");

            items[0] = (TextView) dialogView.findViewById(R.id.item1);
            items[1] = (TextView) dialogView.findViewById(R.id.item2);
            items[2] = (TextView) dialogView.findViewById(R.id.item3);
            items[3] = (TextView) dialogView.findViewById(R.id.item4);
            items[4] = (TextView) dialogView.findViewById(R.id.item5);
            items[5] = (TextView) dialogView.findViewById(R.id.item6);
            items[6] = (TextView) dialogView.findViewById(R.id.item7);
            items[7] = (TextView) dialogView.findViewById(R.id.item8);
            items[8] = (TextView) dialogView.findViewById(R.id.item9);
            items[9] = (TextView) dialogView.findViewById(R.id.item10);

            for(int i = 0; i < items.length; i++) {

                if(!Objects.equals(temp[i], null)) {

                    items[i].setText(temp[i].toUpperCase());

                }

            }


        dialogBuilder.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        AlertDialog alertDialog = dialogBuilder.create();

        alertDialog.show();


    }


    public void initialItemCheck() { // checks if needs item

        buttons[0].setAlpha(1f);
        buttons[0].setClickable(true);
        buttons[1].setAlpha(1f);
        buttons[1].setClickable(true);
        buttons[2].setAlpha(1f);
        buttons[2].setClickable(true);
        buttons[3].setAlpha(1f);
        buttons[3].setClickable(true);

        for (int c = 0; c < 4; c++) {

            if (!Objects.equals(Gf.buttons[2][c], "")) {
                if (preferenceSettings.getBoolean(Gf.buttons[2][c], false) == false) {
                    Log.d("ItemFalse", Gf.buttons[2][0]);
                    buttons[c].setAlpha(.5f);
                    buttons[c].setClickable(false);
                } else {
                    Log.d("ItemTrue", Gf.buttons[2][0]);
                    buttons[c].setAlpha(1f);
                    buttons[c].setClickable(true);
                }
            }
        }
    }

    @Override
    public void onBackPressed() { // no bac
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


        if (Gf.item.length() > 0 && preferenceSettings.getBoolean(Gf.item, false) == false) {
            takeItem.setVisibility(View.VISIBLE);
            takeItem.setAlpha(1f);
            takeItem.setClickable(true);
            takeItem.setText("Take " + Gf.item);
        }

        Log.d("current", Gf.currentPath);

        setText();

    }

    public void setText() {

        texter.setText("");

        for (int c = 0; c < Gf.roomFlags[0].length; c++) {
            if (Gf.roomFlags[0][c] != null) {
                Log.d("path", Gf.roomFlags[0][c]);
                Log.d("current", Gf.currentPath);
            }


            if (Objects.equals(Gf.roomFlags[0][c], Gf.currentPath)) { // find current room in array
                // checks flags

                if (Integer.parseInt(Gf.roomFlags[2][c]) == 0) { // if first time
                    texter.append(Gf.thirdLine + "\n\n");
                    Gf.roomFlags[2][c] = "1";
                }
                if (Integer.parseInt(Gf.roomFlags[1][c]) == 0) { // if odd time
                    texter.append(Gf.firstLine);
                    Gf.roomFlags[1][c] = "1";
                } else { //(Integer.parseInt(Gf.roomFlags[1][c]) == 1) // if even time
                    texter.append(Gf.secondLine);
                    Gf.roomFlags[1][c] = "0";
                }
            }

        }
    }



    public void takeItem(){ // when you take an item

        preferenceEditor.putBoolean(Gf.item, true);
        preferenceEditor.commit(); //Always commit

        heldItems.push(Gf.item);
        count++;
        temp[count] = Gf.item;

    }

    public void defaultPrefs(){ // default items

        preferenceEditor.putBoolean("torch", false);
        preferenceEditor.putBoolean("flint", false);
        preferenceEditor.putBoolean("littorch", false);
        preferenceEditor.putBoolean("book", false);
        preferenceEditor.putBoolean("rope", false);
        preferenceEditor.commit();// Always commit

    }

    @Override
    public void onPause(){
        super.onPause();

        ambience.stop();
        //this.finish();

    }


}