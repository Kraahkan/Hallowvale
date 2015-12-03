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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.plattysoft.leonids.ParticleSystem;

import java.io.InputStream;
import java.util.Objects;
import java.util.Stack;

public class beginning extends AppCompatActivity implements OnClickListener { // This is the main game interface

    private SharedPreferences preferenceSettings;
    private SharedPreferences.Editor preferenceEditor;
    private static final int PREFERENCE_MODE_PRIVATE = 0;

    public Context c;

    String savedRoom;
    String currentRoom;
    String[] savedTemp;
    public boolean soundCheck = false;

    TextView texter, riddle, answer;
    EditText riddleAnswer;

    Button left, up, down, right, takeItem, inventory;
    ImageButton sound;
    LinearLayout master;

    public int backCount = 0;

    TextView[] items = new TextView[10];

    Button buttons[];

    public Stack<String> heldItems;
    public int count = -1;

    InputStream rooms; // for reading rooms
    String[] temp = new String[100];

    public boolean instFlag = true;

    boolean played = false;
    MediaPlayer ambiance;
    MediaPlayer item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beginning);

        heldItems = new Stack();

        preferenceSettings = getSharedPreferences("prefs", PREFERENCE_MODE_PRIVATE);
        preferenceEditor = preferenceSettings.edit();
        preferenceSettings.getAll();

        musicOn();

        //defaultPrefs(); // Set all shared preferences to default, including items
        //DEV TOOL

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
        sound = (ImageButton)findViewById(R.id.imageButton);


        left.setOnClickListener(this);
        up.setOnClickListener(this);
        down.setOnClickListener(this);
        right.setOnClickListener(this);
        takeItem.setOnClickListener(this);
        inventory.setOnClickListener(this);
        sound.setOnClickListener(this);

        Button buttons2[] = {left, up, down, right};
        buttons = buttons2;

        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/LibreBaskerville-Regular.ttf");
        texter.setTypeface(myTypeface);

        // ----------------------------------------------------

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        for (int c = 0; c < width; c = c + 140)
            snow(c);

        Gf.updateFlags(); // populates with txt file paths and sets flags all to 0

        int resId = getResources().getIdentifier("raw/" + preferenceSettings.getString("currentRoom", "start"), null, this.getPackageName());
        rooms = getResources().openRawResource(resId);
        Gf.createRoom(rooms, preferenceSettings.getString("currentRoom", "start"));

        initialItemCheck(); // gets called everytime there is a click

        updateButtons();
        preferenceEditor.putString("currentRoom", Gf.currentPath);

    }

    @Override
    public void onPause(){
        super.onPause();

        ambiance.stop();
        finish();

    }

    public void mainMenuClick(View v){

        Intent i = new Intent(beginning.this, MainMenu.class);
        startActivity(i);

    }

    public void muteSound(){

        if(!soundCheck) {
            ambiance.pause();
            sound.setBackgroundResource(R.drawable.soundmute);
        }else
        {
            ambiance.start();
            sound.setBackgroundResource(R.drawable.sound);
        }

        soundCheck = !soundCheck;


    }

    public void musicOn(){

        ambiance = MediaPlayer.create(this, R.raw.wind);

            ambiance.setVolume(1, 1);
            ambiance.start();
            ambiance.setLooping(true);


    }

    public void item() {
        item = MediaPlayer.create(this, R.raw.drum);
        item.start();

        item.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
            public void onCompletion(MediaPlayer player) {
                player.release();
            }
        });
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

        //final MediaPlayer m = MediaPlayer.create(this, R.raw.drum);

        //m.start();

        float alpha = v.getAlpha();

        ObjectAnimator scaleAnim = ObjectAnimator.ofFloat(v, "scaleX", 1.0f, 0.5f);
        scaleAnim.setDuration(100);
        scaleAnim.setRepeatCount(1);
        scaleAnim.setRepeatMode(ValueAnimator.REVERSE);
        scaleAnim.start();

        /*ObjectAnimator alphaFlash = ObjectAnimator.ofFloat(v, "alpha", 1.0f, 0.5f);
        alphaFlash.setDuration(100);
        alphaFlash.setRepeatCount(1);
        alphaFlash.setRepeatMode(ValueAnimator.REVERSE);
        alphaFlash.start();*/


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
                item();

                break;

            case R.id.inventory:
                callInventory();
                break;

            case R.id.imageButton:
                muteSound();
                break;

            default:
                break;


        }


        initialItemCheck();
        preferenceEditor.putString("currentRoom", Gf.currentPath);

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


        fillBackpack(); //Fill inventory even after exiting app


        dialogBuilder.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        AlertDialog alertDialog = dialogBuilder.create();

        alertDialog.show();


    }

    public void fillBackpack(){

        if(preferenceSettings.getBoolean("torch", false)){
            items[backCount].setText("TORCH");
            backCount++;
        }
        if(preferenceSettings.getBoolean("rope", false)){
            items[backCount].setText("ROPE");
            backCount++;
        }
        if(preferenceSettings.getBoolean("book", false)){
            items[backCount].setText("BOOK");
            backCount++;
        }
        if(preferenceSettings.getBoolean("watch", false)){
            items[backCount].setText("WATCH");
            backCount++;
        }

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

        texter.append(Gf.currentPath + "\n\n");


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
        preferenceEditor.putBoolean("watch", false);
        preferenceEditor.commit();// Always commit

    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putString("room", Gf.currentPath);
        savedInstanceState.putBoolean("torch", preferenceSettings.getBoolean("torch", false));
        savedInstanceState.putBoolean("flint", preferenceSettings.getBoolean("flint", false));
        savedInstanceState.putBoolean("book", preferenceSettings.getBoolean("book", false));
        savedInstanceState.putBoolean("rope", preferenceSettings.getBoolean("rope", false));
        savedInstanceState.putBoolean("watch", preferenceSettings.getBoolean("watch", false));
        savedInstanceState.putStringArray("temp", temp);
        // etc.
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        savedRoom = savedInstanceState.getString("room");
        boolean torch = savedInstanceState.getBoolean("torch");
        boolean flint = savedInstanceState.getBoolean("flint");
        boolean book = savedInstanceState.getBoolean("book");
        boolean rope = savedInstanceState.getBoolean("rope");
        boolean watch = savedInstanceState.getBoolean("watch");
        savedTemp = savedInstanceState.getStringArray("temp");
        Log.d("savedTemp", savedTemp.toString());

        preferenceEditor.putBoolean("torch", torch);
        preferenceEditor.putBoolean("flint", flint);
        preferenceEditor.putBoolean("book", book);
        preferenceEditor.putBoolean("rope", rope);
        preferenceEditor.putBoolean("watch", watch);

        preferenceEditor.commit();


        //InputStream restoreRoom = getResources().openRawResource(R.raw.start); // first room
        //Gf.createRoom(restoreRoom, savedRoom);

        int resId = getResources().getIdentifier("raw/" + savedRoom, null, this.getPackageName());
        rooms = getResources().openRawResource(resId);
        Gf.createRoom(rooms, savedRoom);
    }


}